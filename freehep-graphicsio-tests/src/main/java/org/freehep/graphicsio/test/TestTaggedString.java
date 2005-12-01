// Copyright 2002, SLAC, Stanford University, U.S.A.
package org.freehep.graphicsio.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import org.freehep.graphics2d.TagString;
import org.freehep.graphics2d.VectorGraphics;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestTaggedString.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestTaggedString extends TestingPanel {

    public TestTaggedString(String[] args) throws Exception {
        super(args);
        setName("Tagged String");
    }

    public void paintComponent(Graphics g) {
        if (g != null) {

            VectorGraphics vg = VectorGraphics.create(g);

            Dimension dim = getSize();
            Insets insets = getInsets();

            vg.setColor(Color.white);
            vg.fillRect(insets.left, insets.top, dim.width - insets.left
                    - insets.right, dim.height - insets.top - insets.bottom);

            int x = insets.left;
            int dy = dim.height / 4;

            TagString text = new TagString("Ant<sup><b>Bull</b></sup>"
                    + "Cat<i><sub>Dog</sub></i>"
                    + "<u>Eel</u><sup><udash>Frog</udash></sup>"
                    + "<udot>Gecko</udot><sub>" + "<strike>Hog</strike></sub>");
            vg.setColor(Color.black);
            vg.setFont(new Font("SansSerif", Font.PLAIN, 30));
            vg.drawString(text, x, 1 * dy + insets.top);
            vg.setFont(new Font("Serif", Font.PLAIN, 30));
            vg.drawString(text, x, 2 * dy + insets.top);
            vg.setFont(new Font("Monospaced", Font.PLAIN, 30));
            vg.drawString(text, x, 3 * dy + insets.top);

        }
    }

    public static void main(String[] args) throws Exception {
        new TestTaggedString(args).runTest();
    }
}
