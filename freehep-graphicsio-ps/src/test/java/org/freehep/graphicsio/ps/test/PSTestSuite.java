// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.ps.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-ps/src/test/java/org/freehep/graphicsio/ps/test/PSTestSuite.java 8af7d1b1e4d0 2005/12/01 22:46:17 duns $
 */
public class PSTestSuite extends TestSuite {

    protected void addTests(String fmt, String dir, String ext, boolean compare) {
        super.addTests(fmt, dir, ext, compare);
        addTest(new TestCase("org.freehep.graphicsio.ps.test.PSTestPreviewThumbnail", fmt, dir, ext, compare, null));      
    }

    public static TestSuite suite() {
        PSTestSuite suite = new PSTestSuite();
        suite.addTests("PS");
        return suite;
    }

}
