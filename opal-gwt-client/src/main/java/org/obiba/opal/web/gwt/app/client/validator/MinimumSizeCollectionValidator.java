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

import org.obiba.opal.web.gwt.app.client.ui.HasCollection;

/**
 *
 */
public class MinimumSizeCollectionValidator<T> extends AbstractFieldValidator {
  //
  // Instance Variables
  //

  private final HasCollection<T> collectionField;

  private final int minSize;

  //
  // Constructors
  //

  public MinimumSizeCollectionValidator(HasCollection<T> collectionField, int minSize, String errorMessageKey) {
    this(collectionField, minSize, errorMessageKey, null);
  }

  public MinimumSizeCollectionValidator(HasCollection<T> collectionField, int minSize, String errorMessageKey, String id) {
    super(errorMessageKey, id);
    this.collectionField = collectionField;
    this.minSize = minSize;
  }

  //
  // AbstractFieldValidator Methods
  //

  @Override
  protected boolean hasError() {
    return collectionField.getCollection().size() < minSize;
  }
}
