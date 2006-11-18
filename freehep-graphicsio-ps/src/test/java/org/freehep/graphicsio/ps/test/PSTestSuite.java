// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.ps.test;

import java.util.Properties;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-ps/src/test/java/org/freehep/graphicsio/ps/test/PSTestSuite.java 3e3b6c4e40de 2006/11/18 00:33:39 duns $
 */
public class PSTestSuite extends TestSuite {
/*
    protected void addTests(String category, String fmt, String dir, String ext, boolean compare, Properties properties) {
        super.addTests(category, fmt, dir, ext, compare, properties);
        addTest(new TestCase(
                "org.freehep.graphicsio.ps.test.PSTestPreviewThumbnail", category, fmt,
                dir, ext, compare, null));
    }
*/
    public static TestSuite suite() {
        PSTestSuite suite = new PSTestSuite();
        suite.addTests("PS");
        return suite;
    }

}
