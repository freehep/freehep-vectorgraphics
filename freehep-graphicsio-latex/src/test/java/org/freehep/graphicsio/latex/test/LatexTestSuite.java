// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.latex.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-latex/src/test/java/org/freehep/graphicsio/latex/test/LatexTestSuite.java 937ca67e5f7c 2005/12/05 04:38:24 duns $
 */
public class LatexTestSuite extends TestSuite {

    public static TestSuite suite() {
        LatexTestSuite suite = new LatexTestSuite();
        suite.addTests("LATEX", "latex", "tex", true);
        return suite;
    }

}
