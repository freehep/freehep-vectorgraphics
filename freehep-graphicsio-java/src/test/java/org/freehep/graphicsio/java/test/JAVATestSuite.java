// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.java.test;

import java.util.Properties;

import org.freehep.graphicsio.java.JAVAGraphics2D;
import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-java/src/test/java/org/freehep/graphicsio/java/test/JAVATestSuite.java 4e4ed8246a90 2006/04/05 23:00:50 duns $
 */
public class JAVATestSuite extends TestSuite {

    public static TestSuite suite() {
        JAVATestSuite suite = new JAVATestSuite();

        Properties properties = new Properties();
        properties.setProperty(JAVAGraphics2D.PACKAGE_NAME,
                "org.freehep.graphicsio.java.test");

        suite.addTests("JAVA", "org/freehep/graphicsio/java/test", "java", true, properties);
        return suite;
    }
}
