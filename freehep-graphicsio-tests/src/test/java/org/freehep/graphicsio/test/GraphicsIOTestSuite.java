// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.test;


/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/test/java/org/freehep/graphicsio/test/GraphicsIOTestSuite.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class GraphicsIOTestSuite extends TestSuite {

    public static TestSuite suite() {
        GraphicsIOTestSuite suite = new GraphicsIOTestSuite();
        suite.addTests("GIF");
        suite.addTests("JPG");
        suite.addTests("PNG");
        return suite;
    }

}
