/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.opal.web.identifiers;

import java.util.Locale;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.obiba.magma.ValueTable;
import org.obiba.opal.core.runtime.OpalRuntime;
import org.obiba.opal.core.service.IdentifiersTableService;
import org.obiba.opal.core.service.OpalGeneralConfigService;
import org.obiba.opal.web.magma.DatasourceTablesResource;
import org.obiba.opal.web.magma.DroppableTableResource;
import org.obiba.opal.web.magma.TableResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

@Component
@Transactional
@Scope("request")
@Path("/identifiers")
public class IdentifiersResource extends AbstractIdentifiersResource {

  @Autowired
  private OpalRuntime opalRuntime;

  @Autowired
  private OpalGeneralConfigService serverService;

  @Autowired
  private IdentifiersTableService identifiersTableService;

  @Autowired
  private ApplicationContext applicationContext;

  @Override
  protected OpalRuntime getOpalRuntime() {
    return opalRuntime;
  }

  @Override
  protected IdentifiersTableService getIdentifiersTableService() {
    return identifiersTableService;
  }

  @Path("/tables")
  public DatasourceTablesResource getTables() {
    DatasourceTablesResource resource = applicationContext.getBean(DatasourceTablesResource.class);
    resource.setDatasource(getDatasource());
    return resource;
  }

  @Path("/table/{table}")
  public TableResource getTable(@PathParam("table") String table) {
    return getTableResource(getDatasource().getValueTable(table));
  }

  //
  // Private methods
  //

  private TableResource getTableResource(ValueTable table) {
    TableResource resource = getDatasource().canDropTable(table.getName())
        ? applicationContext.getBean("droppableTableResource", DroppableTableResource.class)
        : applicationContext.getBean("tableResource", TableResource.class);
    resource.setValueTable(table);
    resource.setLocales(getLocales());
    return resource;
  }

  private Set<Locale> getLocales() {
    // Get locales from server config
    return Sets.newHashSet(serverService.getConfig().getLocales());
  }

}
