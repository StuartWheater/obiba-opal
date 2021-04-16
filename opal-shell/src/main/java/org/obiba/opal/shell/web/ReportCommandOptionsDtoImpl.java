/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.shell.web;

import org.obiba.opal.shell.commands.options.ReportCommandOptions;
import org.obiba.opal.web.model.Commands.ReportCommandOptionsDto;

public class ReportCommandOptionsDtoImpl implements ReportCommandOptions {

  private final ReportCommandOptionsDto dto;

  public ReportCommandOptionsDtoImpl(ReportCommandOptionsDto dto) {
    this.dto = dto;
  }

  @Override
  public boolean isHelp() {
    return false;
  }

  @Override
  public String getName() {
    return dto.getName();
  }

  @Override
  public String getProject() {
    return dto.getProject();
  }
}
