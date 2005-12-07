// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.ps.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-ps/src/test/java/org/freehep/graphicsio/ps/test/PSTestSuite.java e3449d5a3c6c 2005/12/07 22:14:47 duns $
 */
public class PSTestSuite extends TestSuite {

    protected void addTests(String fmt, String pkg, String dir, String ext, boolean compare) {
        super.addTests(fmt, pkg, dir, ext, compare);
        addTest(new TestCase(
                "org.freehep.graphicsio.ps.test.PSTestPreviewThumbnail", fmt,
                pkg, dir, ext, compare, null));
    }

    public static TestSuite suite() {
        PSTestSuite suite = new PSTestSuite();
        suite.addTests("PS");
        return suite;
    }

}
