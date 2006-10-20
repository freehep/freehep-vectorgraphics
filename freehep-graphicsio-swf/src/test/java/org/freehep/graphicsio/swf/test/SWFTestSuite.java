// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.swf.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-swf/src/test/java/org/freehep/graphicsio/swf/test/SWFTestSuite.java e9a332397947 2006/10/20 22:37:10 duns $
 */
public class SWFTestSuite extends TestSuite {

    public static TestSuite suite() {
        SWFTestSuite suite = new SWFTestSuite();
        suite.addTests("SWF");
        return suite;
    }

    public static void main(String[] args) {
        new junit.textui.TestRunner().doRun(suite());
    }

}
