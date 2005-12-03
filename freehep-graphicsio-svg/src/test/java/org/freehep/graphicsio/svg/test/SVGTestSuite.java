// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.svg.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-svg/src/test/java/org/freehep/graphicsio/svg/test/SVGTestSuite.java 1d3bd2a557b2 2005/12/03 07:37:43 duns $
 */
public class SVGTestSuite extends TestSuite {

    public static TestSuite suite() {
        SVGTestSuite suite = new SVGTestSuite();
        suite.addTests("SVG", "svg", "svg", true);
        return suite;
    }

}
