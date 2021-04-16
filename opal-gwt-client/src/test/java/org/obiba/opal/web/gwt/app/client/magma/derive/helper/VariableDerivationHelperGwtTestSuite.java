/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.gwt.app.client.magma.derive.helper;

import com.google.gwt.junit.tools.GWTTestSuite;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 *
 */
public class VariableDerivationHelperGwtTestSuite extends GWTTestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite("Variable Derivation Helper Tests");
//    suite.addTestSuite(BooleanVariableDerivationHelperGwtTest.class);
//    suite.addTestSuite(CategoricalVariableDerivationHelperGwtTest.class);
//    suite.addTestSuite(NumericalVariableDerivationHelperGwtTest.class);
//    suite.addTestSuite(OpenTextualVariableDerivationHelperGwtTest.class);
//    suite.addTestSuite(TemporalVariableDerivationHelperGwtTest.class);
    return suite;
  }

  @Override
  public void run(TestResult result) {

  }
}
