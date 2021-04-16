/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.shell;

/**
 * Holds the current {@code OpalShell}. Instances of this class are responsible for holding on to an instance of {@code
 * OpalShell}. Typically, implementations will bind the shell to a {@code ThreadLocal} variable.
 */
public interface OpalShellHolder {

  OpalShell getCurrentShell();

  void bind(OpalShell shell);

}
