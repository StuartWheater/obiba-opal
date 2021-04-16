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

import java.util.List;

import uk.co.flamingpenguin.jewel.cli.CommandLineInterface;
import uk.co.flamingpenguin.jewel.cli.Option;
import uk.co.flamingpenguin.jewel.cli.Unparsed;

/**
 * This interface declares the options that may be used with the decrypt command.
 * <p/>
 * Note that the <code>getFiles</code> method is used to access unparsed file arguments at the end of the command line.
 *
 * @author cag-dspathis
 */
@CommandLineInterface(application = "encrypt")
public interface EncryptCommandOptions extends HelpOption {

  @Option(shortName = "u", description = "The functional unit.")
  String getUnit();

  @Option(shortName = "a", description = "Alias of key to use for encrypting the destination datasource")
  String getAlias();

  @Option(shortName = "o", longName = "out",
      description = "The directory into which the decrypted files are written. Default is current directory.")
  String getOutput();

  boolean isOutput();

  @Unparsed(name = "FILE")
  List<String> getFiles();

}
