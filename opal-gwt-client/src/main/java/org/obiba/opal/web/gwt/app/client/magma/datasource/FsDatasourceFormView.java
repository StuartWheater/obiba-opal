/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.gwt.app.client.magma.datasource;

import org.obiba.opal.web.gwt.app.client.fs.presenter.FileSelectionPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class FsDatasourceFormView extends ViewImpl implements FsDatasourceFormPresenter.Display {

  @UiTemplate("FsDatasourceFormView.ui.xml")
  interface ViewUiBinder extends UiBinder<Widget, FsDatasourceFormView> {}

  private static final ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);

  private final Widget widget;

  @UiField
  SimplePanel fileSelectionPanel;

  public FsDatasourceFormView() {
    widget = uiBinder.createAndBindUi(this);
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @Override
  public void setFileSelectorWidgetDisplay(FileSelectionPresenter.Display display) {
    fileSelectionPanel.setWidget(display.asWidget());
    display.setFieldWidth("20em");
  }

}
