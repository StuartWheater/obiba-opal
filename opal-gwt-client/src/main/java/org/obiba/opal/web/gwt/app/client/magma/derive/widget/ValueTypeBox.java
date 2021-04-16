/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.gwt.app.client.magma.derive.widget;

import org.obiba.opal.web.gwt.app.client.support.VariableDtos.ValueType;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

/**
 * ListBox of Value Type of Magma (Text, integer, ...)
 */
public class ValueTypeBox extends ListBox implements HasValue<String> {

  private ValueType valueType;

  public ValueTypeBox() {
    addChangeHandler(new ChangeHandler() {

      @Override
      public void onChange(ChangeEvent event) {
        valueType = ValueType.values()[getSelectedIndex()];
      }
    });
    populate();
  }

  private void populate() {
    for(ValueType type : ValueType.values()) {
      addItem(type.getLabel());
    }
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return addChangeHandler((ChangeHandler) handler);
  }

  @Override
  public String getValue() {
    return valueType.getLabel();
  }

  @Override
  public void setValue(String value) {
    valueType = ValueType.valueOf(value.toUpperCase());
    for(int i = 0; i < getItemCount(); i++) {
      String text = getItemText(i);
      if(text.equals(valueType.getLabel())) {
        setSelectedIndex(i);
        break;
      }
    }
  }

  @Override
  public void setValue(String value, boolean fireEvents) {
    if(fireEvents) {
      setValue(value);
    } else {
      valueType = ValueType.valueOf(value);
    }
  }

}
