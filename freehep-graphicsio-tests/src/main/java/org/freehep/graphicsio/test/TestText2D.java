// University of California, Santa Cruz, USA and
// CERN, Geneva, Switzerland, Copyright (c) 2000
package org.freehep.graphicsio.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import org.freehep.graphics2d.TagString;
import org.freehep.graphics2d.VectorGraphics;

/**
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestText2D.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestText2D extends TestingPanel {

    public TestText2D(String[] args) throws Exception {
        super(args);
        setName("Tag Strings");
    }

    public void paintComponent(Graphics g) {
        if (g != null) {

            VectorGraphics vg = VectorGraphics.create(g);

            Dimension dim = getSize();
            Insets insets = getInsets();
            vg.setColor(Color.white);
            vg.fillRect(insets.left, insets.top, dim.width - insets.left
                    - insets.right, dim.height - insets.top - insets.bottom);

            int dw = dim.width / 3;
            int dh = dim.height / 10;

            vg.setColor(Color.green);
            for (int y = 0; y < 8; y++) {
                int iy = (int) ((y + 0.5) * dh);
                vg.drawLine(0, iy, dim.width, iy);
            }

            for (int x = 0; x < 3; x++) {
                int ix = (int) ((x + 0.5) * dw);
                vg.drawLine(ix, 0, ix, dim.height);
            }

            String saying = "&lt;Vector<sup><b>\\Graphics%</b></sup> "
                    + "&amp; Card<i><sub>)Adapter)</sub></i>&gt;";
            TagString text = new TagString(saying);

            vg.setColor(Color.red);
            vg.setFont(new Font("SansSerif", Font.PLAIN, 10));

            for (int y = 0; y < 8; y++) {
                int iy = (int) ((y + 0.5) * dh);
                for (int x = 0; x < 3; x++) {
                    int ix = (int) ((x + 0.5) * dw);
                    if (y == 4) {
                        vg.drawString(text, ix, iy, 3 - x, y % 4, true,
                                Color.cyan, 2, true, Color.black);
                    } else if (y == 5) {
                        vg.drawString(text, ix, iy, 3 - x, y % 4, false,
                                Color.cyan, 2, true, Color.black);
                    } else if (y == 6) {
                        vg.drawString(text, ix, iy, 3 - x, y % 4, true,
                                Color.cyan, 2, false, Color.black);
                    } else if (y == 7) {
                        vg.drawString(text, ix, iy, 3 - x, y % 4, false,
                                Color.cyan, 2, false, Color.black);
                    } else {
                        vg.drawString(text, ix, iy, 3 - x, y % 4);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new TestText2D(args).runTest();
    }
}
