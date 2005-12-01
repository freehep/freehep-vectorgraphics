// Copyright 2005 freehep
package org.freehep.graphicsio.test;


/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestMultiPage.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestMultiPage extends TestingPanel {

    public TestMultiPage(String[] args) throws Exception {
        super(args);
        // Create a new instance of this class and add it to the frame.
        addPage("Colors", new TestColors(null));
        addPage("Clip", new TestClip(null));
        addPage("Lines", new TestLineStyles(null));
        addPage("Shapes", new TestShapes(null));
        addPage("Symbols", new TestSymbols2D(null));
    }

    public static void main(String[] args) throws Exception {
        new TestMultiPage(args).runTest();
    }
}
