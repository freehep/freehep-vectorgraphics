// Copyright 2003-2005, FreeHEP.
package org.freehep.graphicsio.test;

import org.freehep.util.UserProperties;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestFontType1.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestFontType1 extends TestTaggedString {

    public TestFontType1(String[] args) throws Exception {
        super(args);
        setName("Test Font Type1");
    }

    public static void main(String[] args) throws Exception {
        UserProperties p = new UserProperties();
        new TestFontType1(args).runTest(p);
    }
}
