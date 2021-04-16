/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.magma;

import java.io.File;
import java.io.FileFilter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.obiba.magma.MagmaEngine;
import org.obiba.magma.datasource.excel.ExcelDatasource;

import static org.obiba.core.util.FileUtil.getFileFromResource;

/**
 *
 */
public abstract class AbstractMagmaResourceTest {

  protected static final String DATASOURCES_FOLDER = "org/obiba/opal/web/magma/datasources-novalues";

  public static final String DATASOURCE1 = "datasource1-novalues.xlsx";

  public static final String DATASOURCE2 = "datasource2-novalues.xlsx";

  @BeforeClass
  public static void before() {
    new MagmaEngine();
  }

  @AfterClass
  public static void after() {
    MagmaEngine.get().shutdown();
  }

  protected static void addAllDatasources() {
    File folder = getFileFromResource(DATASOURCES_FOLDER);
    for(File file : folder.listFiles(new FileFilter() {

      @Override
      public boolean accept(File pathname) {
        return pathname.getName().endsWith("xlsx");
      }
    })) {
      MagmaEngine.get().addDatasource(new TestExcelDatasource(file.getName(), file));
    }
  }

  protected static void addDatasource(String name) {
    MagmaEngine.get()
        .addDatasource(new TestExcelDatasource(name, new File(getFileFromResource(DATASOURCES_FOLDER), name)));
  }

  protected static void removeDatasource(String name) {
    MagmaEngine.get().removeDatasource(MagmaEngine.get().getDatasource(name));
  }

  private static class TestExcelDatasource extends ExcelDatasource {

    private TestExcelDatasource(String name, File excelFile) {
      super(name, excelFile);
    }

    @Override
    protected void onDispose() {
      // do not write workbook
    }

  }

  protected String getDatasourcePath(String name) {
    return new File(getFileFromResource(DATASOURCES_FOLDER), name).getAbsolutePath();
  }

}
