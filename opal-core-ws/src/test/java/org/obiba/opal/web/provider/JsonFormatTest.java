/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.provider;

import org.junit.Test;
import org.obiba.opal.web.model.Magma.DatasourceDto;

import com.googlecode.protobuf.format.JsonFormat;

import static org.fest.assertions.api.Assertions.assertThat;

public class JsonFormatTest {

  @Test
  public void test_DtoContainsNonAsciiCharacters() throws JsonFormat.ParseException {
    String testValue = "ªºÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâäãåæçèéêëìíî";

    String json = JsonFormat.printToString(DatasourceDto.newBuilder().setName(testValue).setType("type").build());
    assertThat(json).isEqualTo("{\"name\": \"" + testValue + "\",\"type\": \"type\"}");

    // Make sure we can read it back
    DatasourceDto.Builder builder = DatasourceDto.newBuilder();
    JsonFormat.merge(json, builder);
    assertThat(builder.getName()).isEqualTo(testValue);
  }

}
