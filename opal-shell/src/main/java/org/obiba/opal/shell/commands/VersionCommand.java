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

import org.obiba.opal.shell.commands.options.VersionCommandOptions;
import org.obiba.runtime.upgrade.VersionProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Version command.
 */
@CommandUsage(description = "Displays the current version of Opal.")
public class VersionCommand extends AbstractCommand<VersionCommandOptions> {
  //
  // Constants
  //

  @Autowired
  private VersionProvider opalVersionProvider;

  //
  // AbstractContextLoadingCommand Methods
  //

  @Override
  public int execute() {
    getShell().printf("Opal %s\n", opalVersionProvider.getVersion());
    return 0; // success!
  }
}
