// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.java.test;

import java.util.Properties;

import org.freehep.graphicsio.java.JAVAGraphics2D;
import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-java/src/test/java/org/freehep/graphicsio/java/test/JAVATestSuite.java ca9d6c536849 2006/11/12 00:49:46 duns $
 */
public class JAVATestSuite extends TestSuite {

    public static TestSuite suite() {
        JAVATestSuite suite = new JAVATestSuite();

        Properties properties = new Properties();
        properties.setProperty(JAVAGraphics2D.PACKAGE_NAME,
                "org.freehep.graphicsio.java.test");

        suite.addTests("JAVA", "JAVA", "org/freehep/graphicsio/java/test", "java", true, properties);
        return suite;
    }
}
