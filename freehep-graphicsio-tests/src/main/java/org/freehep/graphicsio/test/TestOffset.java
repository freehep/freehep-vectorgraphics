// Copyright 2004, FreeHEP.
package org.freehep.graphicsio.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import org.freehep.graphics2d.VectorGraphics;

/**
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestOffset.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestOffset extends TestingPanel {

    public TestOffset(String[] args) throws Exception {
        super(args);
        setName("Offset");
    }

    public void paintComponent(Graphics g) {

        if (g == null)
            return;

        VectorGraphics vg = VectorGraphics.create(g);

        Dimension dim = getSize();
        Insets insets = getInsets();

        vg.setColor(Color.white);
        vg.fillRect(insets.left, insets.top, dim.width - insets.left
                - insets.right, dim.height - insets.top - insets.bottom);

        vg.setColor(Color.black);

        vg.setLineWidth(4.0);
        double w = dim.width, h = dim.height;
        vg.translate(w / 2, h / 2);
        double xhi = w / 2 - 10, yhi = h / 2 - 10;
        vg.drawLine(-xhi, -yhi, xhi, yhi);
        vg.drawLine(-xhi, yhi, xhi, -yhi);
        vg.drawRect(-xhi, -yhi, w - 20, h - 20);
    }

    public static void main(String[] args) throws Exception {
        new TestOffset(args).runTest();
    }
}
