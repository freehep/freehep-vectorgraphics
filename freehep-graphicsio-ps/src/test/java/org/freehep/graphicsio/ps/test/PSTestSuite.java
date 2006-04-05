// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.ps.test;

import java.util.Properties;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-ps/src/test/java/org/freehep/graphicsio/ps/test/PSTestSuite.java 4e4ed8246a90 2006/04/05 23:00:50 duns $
 */
public class PSTestSuite extends TestSuite {

    protected void addTests(String fmt, String dir, String ext, boolean compare, Properties properties) {
        super.addTests(fmt, dir, ext, compare, properties);
        addTest(new TestCase(
                "org.freehep.graphicsio.ps.test.PSTestPreviewThumbnail", fmt,
                dir, ext, compare, null));
    }

    public static TestSuite suite() {
        PSTestSuite suite = new PSTestSuite();
        suite.addTests("PS");
        return suite;
    }

}
