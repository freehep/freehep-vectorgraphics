// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.latex.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-latex/src/test/java/org/freehep/graphicsio/latex/test/LatexTestSuite.java 08eeb27f101d 2006/04/06 00:34:37 duns $
 */
public class LatexTestSuite extends TestSuite {

    public static TestSuite suite() {
        LatexTestSuite suite = new LatexTestSuite();
        suite.addTests("LATEX");
        return suite;
    }

}
