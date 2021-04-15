/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.gwt.app.client.magma.derive;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import org.obiba.opal.web.gwt.app.client.support.AttributeHelper;
import org.obiba.opal.web.model.client.magma.CategoryDto;

import com.google.common.base.Strings;
import com.google.common.collect.Range;

public class ValueMapEntry {

  public static final String EMPTY_VALUES_VALUE = "N/A";

  public static final String OTHER_VALUES_VALUE = "*";

  public enum ValueMapEntryType {
    CATEGORY_NAME, DISTINCT_VALUE, RANGE, EMPTY_VALUES, OTHER_VALUES
  }

  private final ValueMapEntryType type;

  private String value;

  private String label;

  private String newValue;

  private boolean missing;

  private double count;

  private ValueMapEntry(ValueMapEntryType type, String value, String label, String newValue, boolean missing,
      double count) {
    this.type = type;
    this.value = value;
    this.label = label;
    this.newValue = newValue;
    this.missing = missing;
    this.count = count;
  }

  public boolean isType(ValueMapEntryType... types) {
    for(ValueMapEntryType t : types) {
      if(type == t) return true;
    }
    return false;
  }

  public ValueMapEntryType getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  public String getLabel() {
    return label;
  }

  public String getNewValue() {
    return newValue;
  }

  public void setNewValue(String newValue) {
    this.newValue = newValue;
  }

  public boolean isMissing() {
    return missing;
  }

  public void setMissing(boolean missing) {
    this.missing = missing;
  }

  public double getCount() {
    return count;
  }

  public void setCount(double count) {
    this.count = count;
  }

  public static Builder fromCategory(CategoryDto cat) {
    return fromCategory(cat, 0);
  }

  public static Builder fromCategory(CategoryDto cat, double count) {
    String label = AttributeHelper.getLabelValue(cat.getAttributesArray());
    if(Strings.isNullOrEmpty(label)) {
      label = buildLabel(cat.getName());
    }
    return new Builder(ValueMapEntryType.CATEGORY_NAME).value(cat.getName()).label(label).count(count);
  }

  public static Builder fromDistinct(String value) {
    return fromDistinct(value, 0);
  }

  public static Builder fromDistinct(String value, double count) {
    return new Builder(ValueMapEntryType.DISTINCT_VALUE).value(value).label(buildLabel(value)).count(count);
  }

  public static Builder fromDistinct(Number value) {
    return fromRange(value, value);
  }

  public static Builder fromRange(Range<? extends Number> range) {
    return fromRange(range.hasLowerBound() ? range.lowerEndpoint() : null,
        range.hasUpperBound() ? range.upperEndpoint() : null).label(range.toString());
  }

  public static Builder fromRange(@Nullable Number lower, @Nullable Number upper) {
    String value;
    ValueMapEntryType type = ValueMapEntryType.RANGE;

    //noinspection IfStatementWithTooManyBranches
    if(lower == null) {
      value = "-" + formatNumber(upper);
    } else if(upper == null) {
      value = formatNumber(lower) + "+";
    } else if(lower.equals(upper)) {
      value = formatNumber(lower);
      type = ValueMapEntryType.DISTINCT_VALUE;
    } else {
      value = formatNumber(lower) + "-" + formatNumber(upper);
    }
    return new Builder(type).value(value).label(value);
  }

  @Nullable
  private static String formatNumber(@Nullable Number nb) {
    if(nb == null) return null;
    String str = nb.toString();
    // TODO use NumberFormat
    return str.endsWith(".0") ? str.substring(0, str.length() - 2) : str;
  }

  public static Builder createEmpties(String label) {
    return new Builder(ValueMapEntryType.EMPTY_VALUES).value(EMPTY_VALUES_VALUE).label(label).missing();
  }

  public static Builder createOthers(String label) {
    return new Builder(ValueMapEntryType.OTHER_VALUES).value(OTHER_VALUES_VALUE).label(label);
  }

  public static Builder create(ValueMapEntryType type) {
    return new Builder(type);
  }

  @NotNull
  private static String buildLabel(String text) {
    if(Strings.isNullOrEmpty(text)) return "";

    if(text.indexOf('_') == -1) {
      buildOpenTextLabel(text);
    }

    // Upper case for first letter of words following '_'
    StringBuilder b = new StringBuilder(text.toLowerCase());
    int i = 0;
    do {
      b.replace(i, i + 1, b.substring(i, i + 1).toUpperCase());
      i = b.indexOf("_", i) + 1;
    } while(i > 0 && i < b.length());

    return b.toString().replace('_', ' ');
  }

  private static String buildOpenTextLabel(String text) {
    // one word, normalize case
    if(text.indexOf(' ') == -1 && !text.isEmpty()) {
      StringBuilder b = new StringBuilder(text.toLowerCase());
      b.replace(0, 1, b.substring(0, 1).toUpperCase());
      return b.toString();
    }
    // multiple words: open text
    return text;
  }

  @SuppressWarnings("ParameterHidesMemberVariable")
  public static class Builder {
    private final ValueMapEntry entry;

    private Builder(ValueMapEntryType type) {
      entry = new ValueMapEntry(type, "", "", "", false, 0);
    }

    public Builder value(@Nullable String value) {
      entry.value = value;
      return this;
    }

    public Builder label(@Nullable String label) {
      entry.label = label;
      return this;
    }

    public Builder newValue(String newValue) {
      entry.newValue = newValue;
      return this;
    }

    public Builder missing() {
      entry.missing = true;
      return this;
    }

    public Builder count(double count) {
      entry.count = count;
      return this;
    }

    public ValueMapEntry build() {
      return entry;
    }
  }
}