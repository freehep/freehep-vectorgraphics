// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.emf.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/test/java/org/freehep/graphicsio/emf/test/EMFTestSuite.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class EMFTestSuite extends TestSuite {

    public static TestSuite suite() {
        EMFTestSuite suite = new EMFTestSuite();
        suite.addTests("EMF");
        return suite;
    }

}
