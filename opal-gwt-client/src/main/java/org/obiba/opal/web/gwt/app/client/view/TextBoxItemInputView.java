/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.gwt.app.client.view;

import org.obiba.opal.web.gwt.app.client.presenter.ItemSelectorPresenter.EnterKeyHandler;
import org.obiba.opal.web.gwt.app.client.presenter.ItemSelectorPresenter.ItemInputDisplay;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TextBoxItemInputView implements ItemInputDisplay {

  private final TextBox textBox;

  private EnterKeyHandler enterKeyHandler;

  public TextBoxItemInputView() {
    textBox = new TextBox();

    addEnterKeyHandler();
  }

  @Override
  public void clear() {
    textBox.setText("");
  }

  @Override
  public String getItem() {
    return textBox.getText();
  }

  @Override
  public String renderItem(String item) {
    return item;
  }

  @Override
  public Widget asWidget() {
    return textBox;
  }

  @Override
  public void setEnterKeyHandler(EnterKeyHandler handler) {
    enterKeyHandler = handler;
  }

  private void addEnterKeyHandler() {
    textBox.addKeyDownHandler(new KeyDownHandler() {

      @Override
      public void onKeyDown(KeyDownEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          if(enterKeyHandler != null) {
            enterKeyHandler.enterKeyPressed();
          }
        }
      }
    });
  }
}