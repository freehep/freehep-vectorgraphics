// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.svg.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-svg/src/test/java/org/freehep/graphicsio/svg/test/SVGTestSuite.java 4e4ed8246a90 2006/04/05 23:00:50 duns $
 */
public class SVGTestSuite extends TestSuite {

    public static TestSuite suite() {
        SVGTestSuite suite = new SVGTestSuite();
        suite.addTests("SVG");
        return suite;
    }

}
