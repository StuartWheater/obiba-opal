/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.gwt.app.client.validator;

import javax.annotation.Nullable;

import com.google.gwt.user.client.ui.ListBox;

public class ListBoxItemConditionalValidator implements FieldValidator {
  //
  // Instance Variables
  //

  private final ListBox listBox;

  private final String matchingItemValue;

  private final FieldValidator delegate;

  private String id;

  //
  // Constructors
  //

  public ListBoxItemConditionalValidator(ListBox listBox, String matchingItemValue, FieldValidator delegate) {
    this.listBox = listBox;
    this.matchingItemValue = matchingItemValue;
    this.delegate = delegate;
  }

  //
  // FieldValidator Methods
  //

  @Nullable
  @Override
  public String validate() {
    if(listBox.getValue(listBox.getSelectedIndex()).equals(matchingItemValue)) {
      return delegate.validate();
    }
    return null;
  }

  public ListBoxItemConditionalValidator setId(String value) {
    id = value;
    return this;
  }

  @Override
  public String getId() {
    return id;
  }
}
