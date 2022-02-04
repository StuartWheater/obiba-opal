/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.magma.view;

import javax.validation.constraints.NotNull;

import org.obiba.magma.ValueView;
import org.obiba.magma.views.View;
import org.obiba.opal.web.model.Magma.TableDto;
import org.obiba.opal.web.model.Magma.ViewDto;

/**
 * Contract for converting a View to a ViewDto and back.
 */
public interface ViewDtoExtension {

  boolean isExtensionOf(@NotNull ViewDto viewDto);

  boolean isDtoOf(@NotNull ValueView view);

  View fromDto(ViewDto viewDto, View.Builder viewBuilder);

  TableDto asTableDto(ViewDto viewDto, TableDto.Builder tableDtoBuilder);

  ViewDto asDto(ValueView view);
}