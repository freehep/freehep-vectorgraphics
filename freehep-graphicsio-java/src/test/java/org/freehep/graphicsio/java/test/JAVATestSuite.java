// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.java.test;

import org.freehep.graphicsio.java.JAVAGraphics2D;
import org.freehep.graphicsio.test.TestSuite;
import org.freehep.util.UserProperties;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-java/src/test/java/org/freehep/graphicsio/java/test/JAVATestSuite.java 01c7a0af1b3a 2005/12/05 06:02:47 duns $
 */
public class JAVATestSuite extends TestSuite {

    public static TestSuite suite() {
        JAVATestSuite suite = new JAVATestSuite();

        UserProperties user = new UserProperties();
        user.setProperty(JAVAGraphics2D.PACKAGE_NAME,
                "org.freehep.graphicsio.java.test");

        suite.addTests("JAVA", "java", "java", true, user);
        return suite;
    }

}
