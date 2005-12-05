// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.cgm.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-cgm/src/test/java/org/freehep/graphicsio/cgm/test/CGMTestSuite.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CGMTestSuite extends TestSuite {

    public static TestSuite suite() {
        CGMTestSuite suite = new CGMTestSuite();
        suite.addTests("CGM");
        return suite;
    }

}
