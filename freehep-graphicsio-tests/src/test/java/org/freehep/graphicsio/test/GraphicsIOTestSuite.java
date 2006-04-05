// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.test;


/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/test/java/org/freehep/graphicsio/test/GraphicsIOTestSuite.java f2ace2456064 2006/04/05 00:26:49 duns $
 */
public class GraphicsIOTestSuite extends TestSuite {

    public static TestSuite suite() {
        GraphicsIOTestSuite suite = new GraphicsIOTestSuite();
        suite.addTests("GIF");
//        suite.addTests("JPG");
        suite.addTests("PNG");
        return suite;
    }

}
