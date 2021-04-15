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

import org.obiba.opal.web.model.client.magma.DatasourceFactoryDto;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

/**
 *
 */
public interface DatasourceFormPresenter {

  boolean isForType(String type);

  PresenterWidget<? extends Display> getPresenter();

  DatasourceFactoryDto getDatasourceFactory();

  /**
   * True if the form data in the form are valid. Responsible for displaying the appropriate error message.
   *
   * @return
   */
  boolean validateFormData();

  void clearForm();

  interface Display extends View {}

}
