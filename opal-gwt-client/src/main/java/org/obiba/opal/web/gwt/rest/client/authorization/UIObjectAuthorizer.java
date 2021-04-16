/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.gwt.rest.client.authorization;

import com.google.gwt.user.client.ui.UIObject;

/**
 *
 */
public class UIObjectAuthorizer implements HasAuthorization {

  private final UIObject o;

  public UIObjectAuthorizer(UIObject o) {
    this.o = o;
  }

  @Override
  public void beforeAuthorization() {
    o.setVisible(false);
  }

  @Override
  public void authorized() {
    o.setVisible(true);
  }

  @Override
  public void unauthorized() {
    o.setVisible(false);
  }

}
