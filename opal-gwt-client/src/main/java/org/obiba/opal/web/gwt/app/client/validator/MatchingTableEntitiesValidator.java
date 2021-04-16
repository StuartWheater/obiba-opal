/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.gwt.app.client.validator;

import java.util.Collection;
import java.util.Iterator;

import org.obiba.opal.web.gwt.app.client.ui.HasCollection;
import org.obiba.opal.web.model.client.magma.TableDto;

/**
 *
 */
public class MatchingTableEntitiesValidator extends AbstractFieldValidator {
  //
  // Instance Variables
  //

  private final HasCollection<TableDto> tableCollectionField;

  //
  // Constructors
  //

  public MatchingTableEntitiesValidator(HasCollection<TableDto> tableCollectionField) {
    this(tableCollectionField, null);
  }

  public MatchingTableEntitiesValidator(HasCollection<TableDto> tableCollectionField, String id) {
    super("TableEntityTypesDoNotMatch", id);
    this.tableCollectionField = tableCollectionField;
  }

  //
  // AbstractFieldValidator Methods
  //

  @Override
  protected boolean hasError() {
    Collection<TableDto> tables = tableCollectionField.getCollection();

    Iterator<TableDto> iterator = tables.iterator();

    if(iterator.hasNext()) {
      String entityType = iterator.next().getEntityType();

      while(iterator.hasNext()) {
        TableDto tableDto = iterator.next();
        if(!tableDto.getEntityType().equals(entityType)) {
          return true;
        }
      }
    }

    return false;
  }
}
