// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.latex.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-latex/src/test/java/org/freehep/graphicsio/latex/test/LatexTestSuite.java e3449d5a3c6c 2005/12/07 22:14:47 duns $
 */
public class LatexTestSuite extends TestSuite {

    public static TestSuite suite() {
        LatexTestSuite suite = new LatexTestSuite();
        suite.addTests("LATEX", "org.freehep.graphicsio.latex", "latex", "tex", true);
        return suite;
    }

}
