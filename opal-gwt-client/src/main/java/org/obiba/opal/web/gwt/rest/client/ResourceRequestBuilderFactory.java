/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.gwt.rest.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 */
public class ResourceRequestBuilderFactory {

  private ResourceRequestBuilderFactory() {}

  public static <T extends JavaScriptObject> ResourceRequestBuilder<T> newBuilder() {
    return GWT.create(ResourceRequestBuilder.class);
  }

}
