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

import org.obiba.opal.web.gwt.rest.client.authorization.HasAuthorization;

/**
 *
 */
public interface ResourceAuthorizationRequestBuilder {

  ResourceAuthorizationRequestBuilder forResource(String resource);

  ResourceAuthorizationRequestBuilder get();

  ResourceAuthorizationRequestBuilder post();

  ResourceAuthorizationRequestBuilder put();

  ResourceAuthorizationRequestBuilder delete();

  ResourceAuthorizationRequestBuilder request(String resource, HttpMethod method);

  ResourceAuthorizationRequestBuilder authorize(HasAuthorization toAuthorize);

  void send();

}
