// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.swf.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-swf/src/test/java/org/freehep/graphicsio/swf/test/SWFTestSuite.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class SWFTestSuite extends TestSuite {

    public static TestSuite suite() {
        SWFTestSuite suite = new SWFTestSuite();
        suite.addTests("SWF");
        return suite;
    }

}
