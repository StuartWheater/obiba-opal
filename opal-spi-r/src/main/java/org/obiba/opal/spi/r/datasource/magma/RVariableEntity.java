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

import org.obiba.magma.support.VariableEntityBean;

import javax.validation.constraints.NotNull;
import java.text.NumberFormat;

public class RVariableEntity extends VariableEntityBean {

  private final String rEntityIdentifier;

  private final boolean numeric;

  RVariableEntity(@NotNull String entityType, @NotNull String entityIdentifier) {
    super(entityType, normalizeId(entityIdentifier));
    this.rEntityIdentifier = entityIdentifier;
    this.numeric = false;
  }

  RVariableEntity(@NotNull String entityType, @NotNull double entityIdentifier) {
    super(entityType, normalizeId(entityIdentifier));
    this.rEntityIdentifier = Double.toString(entityIdentifier);
    this.numeric = true;
  }

  public String getRIdentifier() {
    return rEntityIdentifier;
  }

  public boolean isNumeric() {
    return numeric;
  }

  private static String normalizeId(String id) {
    String nid = id.replaceAll(",",".").trim();
    return nid.endsWith(".0") ?
        nid.substring(0, nid.lastIndexOf(".0")) : id;
  }

  private static String normalizeId(double id) {
    NumberFormat fmt = NumberFormat.getInstance();
    fmt.setGroupingUsed(false);
    fmt.setMaximumIntegerDigits(999);
    fmt.setMaximumFractionDigits(999);
    return normalizeId(fmt.format(id));
  }
}
