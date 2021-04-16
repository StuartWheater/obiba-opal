/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.shell.commands;

import org.obiba.opal.shell.OpalShell;

import uk.co.flamingpenguin.jewel.cli.CommandLineInterface;

/**
 * Base class for commands.
 */
public abstract class AbstractCommand<T> implements Command<T> {
  //
  // Instance Variables
  //

  private String name;

  private OpalShell shell;

  private String owner;

  protected T options;

  //
  // Command Methods
  //

  @Override
  public String getName() {
    if(name == null) {
      CommandLineInterface annotation = getCommandLineInterfaceAnnotation();
      name = annotation.application();
    }
    return name;
  }

  @Override
  public void setOwner(String owner) {
    this.owner = owner;
  }

  @Override
  public String getOwner() {
    return owner;
  }

  @Override
  public T getOptions() {
    return options;
  }

  @Override
  public void setOptions(T options) {
    this.options = options;
  }

  @Override
  public void setShell(OpalShell shell) {
    this.shell = shell;
  }

  //
  // Methods
  //

  public OpalShell getShell() {
    return shell;
  }

  private CommandLineInterface getCommandLineInterfaceAnnotation() {
    CommandLineInterface annotation = options.getClass().getAnnotation(CommandLineInterface.class);
    if(annotation == null) {
      for(Class<?> optionsInterface : options.getClass().getInterfaces()) {
        annotation = optionsInterface.getAnnotation(CommandLineInterface.class);
        if(annotation != null) break;
      }
    }
    return annotation;
  }
}
