// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.latex.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-latex/src/test/java/org/freehep/graphicsio/latex/test/LatexTestSuite.java 4e4ed8246a90 2006/04/05 23:00:50 duns $
 */
public class LatexTestSuite extends TestSuite {

    public static TestSuite suite() {
        LatexTestSuite suite = new LatexTestSuite();
        suite.addTests("LATEX", "latex", "tex", true, null);
        return suite;
    }

}
