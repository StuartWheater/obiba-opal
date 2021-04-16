/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.opal.web.gwt.app.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ModalClosedEvent extends GwtEvent<ModalClosedEvent.Handler> {

  public interface Handler extends EventHandler {
    void onModalClosed(ModalClosedEvent event);
  }

  private static final Type<Handler> TYPE = new Type<Handler>();

  private final Object source;

  public ModalClosedEvent(Object source) {
    this.source = source;
  }

  @Override
  public Object getSource() {
    return source;
  }

  public static Type<Handler> getType() {
    return TYPE;
  }

  @Override
  protected void dispatch(Handler handler) {
    handler.onModalClosed(this);
  }

  @Override
  public GwtEvent.Type<Handler> getAssociatedType() {
    return getType();
  }

}
