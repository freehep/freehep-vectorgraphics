// Copyright 2004, FreeHEP.
package org.freehep.graphicsio.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.geom.AffineTransform;

import org.freehep.graphics2d.VectorGraphics;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestFontDerivation.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestFontDerivation extends TestingPanel {

    public TestFontDerivation(String[] args) throws Exception {
        super(args);
        setName("Font Derivation");
    }

    public void paintComponent(Graphics g) {
        if (g == null)
            return;

        VectorGraphics vg = VectorGraphics.create(g.create());

        Dimension dim = getSize();
        Insets insets = getInsets();

        int w = dim.width;
        int h = dim.height;
        vg.translate(w / 2, h / 2);

        vg.setColor(Color.white);
        vg.fillRect(insets.left - w / 2, insets.top - h / 2, w - insets.left
                - insets.right, h - insets.top - insets.bottom);

        String text = "FreeHEP";

        Font font = vg.getFont();
        double fw = w / 120.0;
        double fh = h / 120.0;

        vg.setColor(Color.BLACK);

        for (int i = 1; i < 36; i++) {
            AffineTransform t = AffineTransform.getRotateInstance(Math
                    .toRadians(10 * i));
            double s = 1.0 + i / 20.0;
            t.scale(fw / s, fh / s);
            vg.setFont(font.deriveFont(t));
            vg.drawString(text, 0, 0);
        }

        vg.setColor(Color.BLUE);
        vg.setFont(font.deriveFont(AffineTransform.getScaleInstance(fw, fh)));
        vg.drawString(text, 0, 0);

        vg.dispose();
    }

    public static void main(String[] args) throws Exception {
        new TestFontDerivation(args).runTest();
    }
}
