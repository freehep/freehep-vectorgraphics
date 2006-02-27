// Copyright 2001-2006 freehep
package org.freehep.graphicsio.test;

import java.awt.Dimension;

/**
 * @author Simon Fischer
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestApplication.java 40d86979195e 2006/02/27 19:52:33 duns $
 */
public class TestApplication {

    public static void main(String[] args) throws Exception {

        // Create a new frame to hold everything.
        TestingFrame frame = new TestingFrame("Test Application");

        // Create a new instance of this class and add it to the frame.
        frame.addPanel("All", new TestAll(null));
        frame.addPanel("Colors", new TestColors(null));
        frame.addPanel("Clip", new TestClip(null));
        frame.addPanel("Custom Strokes", new TestCustomStrokes(null));
        frame.addPanel("Fonts", new TestFonts(null));
        frame.addPanel("HTML", new TestHTML(null));
        frame.addPanel("Image2D", new TestImage2D(null));
        frame.addPanel("Images", new TestImages(null));
        frame.addPanel("Labels", new TestLabels(null));
        frame.addPanel("Lines", new TestLineStyles(null));
        frame.addPanel("Paint", new TestPaint(null));
        frame.addPanel("PrintColors", new TestPrintColors(null));
        frame.addPanel("Shapes", new TestShapes(null));
        frame.addPanel("Symbols", new TestSymbols2D(null));
        frame.addPanel("Text", new TestText2D(null));
        frame.addPanel("Tagged String", new TestTaggedString(null));
        frame.addPanel("Transforms", new TestTransforms(null));
        frame.addPanel("Transparency", new TestTransparency(null));

        // Give the frame a size and make it visible.
        frame.pack();
        frame.setSize(new Dimension(1024, 768));
        frame.setVisible(true);
    }
}
