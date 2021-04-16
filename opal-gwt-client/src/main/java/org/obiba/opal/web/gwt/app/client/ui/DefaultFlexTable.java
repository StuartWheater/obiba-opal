/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.gwt.app.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * A FlexTable with a header and basic styling from bootstrap.
 */
public class DefaultFlexTable extends FlexTable {
  private final Element head;

  private final Element headerTr;

  public DefaultFlexTable() {
    head = DOM.createTHead();
    headerTr = DOM.createTR();
    DOM.insertChild(getElement(), head, 0);
    DOM.insertChild(head, headerTr, 0);
    setStyled(true);
    setCondensed(true);
    setZebra(true);
    setBordered(true);
  }

  public void setStyled(boolean styled) {
    if(styled) {
      addStyleName("table");
    } else {
      removeStyleName("table");
    }
  }

  public void setBordered(boolean bordered) {
    if(bordered) {
      addStyleName("table-bordered");
    } else {
      removeStyleName("table-bordered");
    }
  }

  public void setCondensed(boolean condensed) {
    if(condensed) {
      addStyleName("table-condensed");
    } else {
      removeStyleName("table-condensed");
    }
  }

  public void setZebra(boolean zebra) {
    if(zebra) {
      addStyleName("table-striped");
    } else {
      removeStyleName("table-striped");
    }
  }

  public void setHeader(int column, String text) {
    prepareHeader(column);
    if(text != null) {
      DOM.setInnerText(DOM.getChild(headerTr, column), text);
    }
  }

  public void setHeaderWidget(int column, Widget widget) {
    prepareHeader(column);
    if(widget != null) {
      widget.removeFromParent();
      // Physical attach.
      DOM.appendChild(DOM.getChild(headerTr, column), widget.getElement());
      adopt(widget);
    }
  }

  private void prepareHeader(int column) {
    if(column < 0) {
      throw new IndexOutOfBoundsException("Cannot create a column with a negative index: " + column);
    }
    int cellCount = DOM.getChildCount(headerTr);
    int required = column + 1 - cellCount;
    if(required > 0) {
      addHeaderCells(head, 0, required);
    }
  }

  private native void addHeaderCells(Element table, int row, int num)/*-{
      var rowElem = table.rows[row];
      for (var i = 0; i < num; i++) {
          var cell = $doc.createElement("th");
          rowElem.appendChild(cell);
      }
  }-*/;

}
