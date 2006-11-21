// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.test;


/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/test/java/org/freehep/graphicsio/test/GraphicsIOTestSuite.java d06da55b1d6a 2006/11/21 08:45:39 duns $
 */
public class GraphicsIOTestSuite extends TestSuite {

    public static TestSuite suite() {
        GraphicsIOTestSuite suite = new GraphicsIOTestSuite();
//        suite.addTests("BMP");
        suite.addTests("GIF");
        suite.addTests("JPG");
        suite.addTests("PNG");
//        suite.addTests("WBMP");
        return suite;
    }

}
