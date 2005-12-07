// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.svg.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-svg/src/test/java/org/freehep/graphicsio/svg/test/SVGTestSuite.java e3449d5a3c6c 2005/12/07 22:14:47 duns $
 */
public class SVGTestSuite extends TestSuite {

    public static TestSuite suite() {
        SVGTestSuite suite = new SVGTestSuite();
        suite.addTests("SVG", "org.freehep.graphicsio.svg", "svg", "svg", true);
        return suite;
    }

}
