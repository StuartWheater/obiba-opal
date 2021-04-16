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

import com.google.web.bindery.event.shared.EventBus;

public class CategoricalVariableSuggestOracle extends TableVariableSuggestOracle {

  /**
   * Same behavior as VariableSuggestOracle but the list of results do not display the datasource and table name
   *
   * @param eventBus
   */
  public CategoricalVariableSuggestOracle(EventBus eventBus) {
    super(eventBus);
  }

  @Override
  public String getOriginalQuery() {
    // Filter query for categorical variables
    return originalQuery + " nature:CATEGORICAL";
  }
}
