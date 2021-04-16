/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.opal.shell.commands.options;

import uk.co.flamingpenguin.jewel.cli.CommandLineInterface;
import uk.co.flamingpenguin.jewel.cli.Option;

/**
 * This interface declares the options that may be used with the report command.
 */
@CommandLineInterface(application = "report")
public interface ReportCommandOptions extends HelpOption {

  @Option(shortName = "n", description = "The report template name.")
  String getName();

  @Option(shortName = "p", description = "The report template project name.")
  String getProject();
}
