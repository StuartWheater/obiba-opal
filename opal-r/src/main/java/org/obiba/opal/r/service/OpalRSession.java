/*
 * Copyright (c) 2020 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.r.service;

import com.google.common.base.Strings;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.obiba.opal.core.security.SessionDetachedSubject;
import org.obiba.opal.core.tx.TransactionalThreadFactory;
import org.obiba.opal.spi.r.*;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Reference to a R session.
 */
public class OpalRSession implements RASyncOperationTemplate {

  private static final Logger log = LoggerFactory.getLogger(OpalRSession.class);

  private static final String DEFAULT_CONTEXT = "default";

  private final TransactionalThreadFactory transactionalThreadFactory;

  private final String id;

  private final Lock lock = new ReentrantLock();

  private final RConnection rConnection;

  private final String user;

  private final Date created;

  private Date timestamp;

  private String originalWorkDir;

  private String originalTempDir;

  private boolean busy = false;

  private String executionContext = DEFAULT_CONTEXT;

  /**
   * R commands to be processed.
   */
  private final BlockingQueue<RCommand> rCommandQueue = new LinkedBlockingQueue<>();

  /**
   * All R commands.
   */
  private final List<RCommand> rCommandList = Collections.synchronizedList(new LinkedList<RCommand>());

  private RCommandsConsumer rCommandsConsumer;

  private Thread consumer;

  /**
   * R command identifier increment.
   */
  private int commandId = 1;

  /**
   * Build a R session reference from a R connection.
   *
   * @param connection
   */
  OpalRSession(RConnection connection, TransactionalThreadFactory transactionalThreadFactory, String user) {
    this.rConnection = connection;
    this.transactionalThreadFactory = transactionalThreadFactory;
    id = UUID.randomUUID().toString();
    this.user = user;
    created = new Date();
    timestamp = created;

    try {
      originalWorkDir = getRWorkDir();
      originalTempDir = updateRTempDir();
    } catch (Exception e) {
      // ignore
    }
  }

  /**
   * Get the unique identifier of the R session.
   *
   * @return
   */
  public String getId() {
    return id;
  }

  public void touch() {
    timestamp = new Date();
  }

  public String getUser() {
    return user;
  }

  public Date getCreated() {
    return created;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public boolean isBusy() {
    return busy;
  }

  public void setExecutionContext(String executionContext) {
    this.executionContext = executionContext;
  }

  public String getExecutionContext() {
    return Strings.isNullOrEmpty(executionContext) ? DEFAULT_CONTEXT : executionContext;
  }

  /**
   * Check if the R session is not busy and has expired.
   *
   * @param timeout in minutes
   * @return
   */
  public boolean hasExpired(long timeout) {
    Date now = new Date();
    return !busy && now.getTime() - timestamp.getTime() > timeout * 60 * 1000;
  }

  /**
   * Get the {@link File} directory specific to this user's R session. Create it if it does not exist.
   *
   * @param sessionId
   * @return
   */
  public File getWorkspace(String sessionId) {
    File ws = new File(getWorkspaces(), getUser() + File.separatorChar + (Strings.isNullOrEmpty(sessionId) ? getId() : sessionId));
    if (!ws.exists()) ws.mkdirs();
    return ws;
  }

  @Override
  public String toString() {
    return id;
  }

  //
  // ROperationTemplate methods
  //

  /**
   * Executes the R operation on the current R session of the invoking Opal user. If no current R session is defined, a
   * {@link NoSuchRSessionException} is thrown.
   */
  @Override
  public synchronized void execute(ROperation rop) {
    lock.lock();
    busy = true;
    touch();
    try {
      rop.doWithConnection(rConnection);
    } finally {
      busy = false;
      touch();
      lock.unlock();
    }
  }

  @Override
  public synchronized String executeAsync(ROperation rop) {
    touch();
    ensureRCommandsConsumer();
    String rCommandId = id + "-" + commandId++;
    RCommand cmd = new RCommand(rCommandId, rop);
    rCommandList.add(cmd);
    rCommandQueue.offer(cmd);
    return rCommandId;
  }

  @Override
  public Iterable<RCommand> getRCommands() {
    touch();
    return rCommandList;
  }

  @Override
  public boolean hasRCommand(String cmdId) {
    touch();
    for(RCommand rCommand : rCommandList) {
      if(rCommand.getId().equals(cmdId)) return true;
    }
    return false;
  }

  @Override
  public RCommand getRCommand(String cmdId) {
    touch();
    for(RCommand rCommand : rCommandList) {
      if(rCommand.getId().equals(cmdId)) return rCommand;
    }
    throw new NoSuchRCommandException(cmdId);
  }

  @Override
  public RCommand removeRCommand(String cmdId) {
    touch();
    RCommand rCommand = getRCommand(cmdId);
    synchronized(rCommand) {
      rCommand.notifyAll();
    }
    rCommandList.remove(rCommand);
    return rCommand;
  }

  /**
   * Close the R session.
   */
  public void close() {
    if(isClosed()) return;

    try {
      cleanRWorkDir();
      cleanRTempDir();
    } catch(Exception e) {
      // ignore
    }

    try {
      rConnection.close();
    } catch(Exception e) {
      // ignore
    }

    if(consumer == null) return;
    try {
      consumer.interrupt();
    } catch(Exception e) {
      // ignore
    } finally {
      consumer = null;
      rCommandList.clear();
      rCommandQueue.clear();
    }
  }

  public boolean isClosed() {
    return !rConnection.isConnected();
  }

  //
  // private methods
  //


  private String getRWorkDir() throws REXPMismatchException {
    RScriptROperation rop = new RScriptROperation("base::getwd()", false);
    execute(rop);
    return rop.getResult().asString();
  }

  private void cleanRWorkDir() {
    if (Strings.isNullOrEmpty(originalWorkDir)) return;
    RScriptROperation rop = new RScriptROperation(String.format("base::unlink('%s', recursive=TRUE)", originalWorkDir), false);
    execute(rop);
  }

  private String updateRTempDir() throws REXPMismatchException {
    RScriptROperation rop = new RScriptROperation("if (!require(unixtools)) { install.packages('unixtools', repos = 'http://www.rforge.net/') }", false);
    execute(rop);
    rop = new RScriptROperation("unixtools::set.tempdir(base::file.path(base::tempdir(), base::basename(base::getwd())))", false);
    execute(rop);
    rop = new RScriptROperation("base::dir.create(base::tempdir(), recursive = TRUE)", false);
    execute(rop);
    rop = new RScriptROperation("base::tempdir()", false);
    execute(rop);
    return rop.getResult().asString();
  }

  private void cleanRTempDir() {
    if (Strings.isNullOrEmpty(originalTempDir)) return;
    RScriptROperation rop = new RScriptROperation(String.format("base::unlink('%s', recursive=TRUE)", originalTempDir), false);
    execute(rop);
  }

  /**
   * Get the workspaces directory for the current execution context.
   *
   * @return
   */
  private File getWorkspaces() {
    return new File(String.format(String.format(OpalRSessionManager.WORKSPACES_FORMAT, getExecutionContext())));
  }

  /**
   * Detach the R connection and updates the R session.
   *
   * @param connection
   */
  private void close(RConnection connection) {
    if(!Strings.isNullOrEmpty(connection.getLastError()) && !connection.getLastError().toLowerCase().equals("ok")) {
      throw new RRuntimeException("Unexpected R server error: " + connection.getLastError());
    }
    try {
      connection.close();
    } catch(Exception e) {
      log.warn("Error while detaching R session.", e);
    }
  }

  private void ensureRCommandsConsumer() {
    if(rCommandsConsumer == null) {
      rCommandsConsumer = new RCommandsConsumer();
      startRCommandsConsumer();
    } else if(consumer == null || !consumer.isAlive()) {
      startRCommandsConsumer();
    }
  }

  private void startRCommandsConsumer() {
    Subject owner = SessionDetachedSubject.asSessionDetachedSubject(SecurityUtils.getSubject());
    consumer = transactionalThreadFactory.newThread(owner.associateWith(rCommandsConsumer));
    consumer.setName("R Operations Consumer " + rCommandsConsumer);
    consumer.setPriority(Thread.NORM_PRIORITY);
    consumer.start();
  }

  private class RCommandsConsumer implements Runnable {

    @Override
    public void run() {
      log.debug("Starting R operations consumer");
      try {
        //noinspection InfiniteLoopStatement
        while(true) {
          consume(rCommandQueue.take());
        }
      } catch(InterruptedException ignored) {
        log.debug("Stopping R operations consumer");
      } catch(Exception e) {
        log.error("Error in R command consumer", e);
      }
    }

    private void consume(RCommand rCommand) {
      try {
        rCommand.inProgress();
        execute(rCommand.getROperation());
        rCommand.completed();
      } catch(Exception e) {
        log.error("Error when consuming R command: {}", e.getMessage(), e);
        rCommand.failed(e.getMessage());
      }
      synchronized(rCommand) {
        rCommand.notifyAll();
      }
    }
  }

}
