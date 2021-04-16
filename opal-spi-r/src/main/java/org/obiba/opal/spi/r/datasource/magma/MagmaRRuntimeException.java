/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.spi.r.datasource.magma;

/**
 * Magma to R related runtime errors.
 */
public class MagmaRRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public MagmaRRuntimeException(String msg, Exception cause) {
    super(msg, cause);
  }

  public MagmaRRuntimeException(String msg) {
    super(msg);
  }

}
