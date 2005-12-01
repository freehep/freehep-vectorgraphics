package org.freehep.graphicsio.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Stroke;

import org.freehep.graphics2d.VectorGraphics;

/**
 * @author Andre Bach
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestGraphicsContexts.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestGraphicsContexts extends TestingPanel {

    static float[] dash2 = new float[2];
    static {
        dash2[0] = 5.f;
        dash2[1] = 4.f;
    }

    Stroke stroke1 = new BasicStroke(5.f);

    Stroke stroke2 = new BasicStroke(10.f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL, 10.f, dash2, 0.f);

    Stroke stroke3 = new BasicStroke(2.f);

    public TestGraphicsContexts(String[] args) throws Exception {
        super(args);
        setName("Line Styles");
    }

    public void paintComponent(Graphics g) {

        VectorGraphics vg1 = VectorGraphics.create(g);

        Dimension dim = getSize();
        Insets insets = getInsets();

        vg1.setColor(Color.white);
        vg1.fillRect(insets.left, insets.top, dim.width - insets.left
                - insets.right, dim.height - insets.top - insets.bottom);

        vg1.setColor(Color.black);
        vg1.setStroke(stroke1);
        vg1.drawLine(200, 25, 350, 25);
        vg1.drawSymbol(400, 25, 40, 5);

        Graphics g2 = vg1.create();
        VectorGraphics vg2 = VectorGraphics.create(g2);
        vg2.setStroke(stroke2);
        vg2.setColor(Color.green);
        vg2.translate(-137.5, 0);
        vg2.scale(1.5, 1);
        vg2.drawLine(200, 125, 350, 125);
        vg2.drawSymbol(400, 125, 40, 6);

        Graphics g3 = vg2.create();
        VectorGraphics vg3 = VectorGraphics.create(g3);
        vg3.setStroke(stroke3);
        vg3.setColor(Color.blue);
        vg3.drawLine(200, 225, 350, 225);
        vg3.fillSymbol(400, 225, 40, 7);
        vg3.dispose();

        vg2.drawLine(200, 325, 350, 325);
        vg2.drawSymbol(400, 325, 40, 6);
        vg2.dispose();

        vg1.drawLine(200, 425, 350, 425);
        vg1.drawSymbol(400, 425, 40, 5);
    }

    public static void main(String[] args) throws Exception {
        new TestGraphicsContexts(args).runTest();
    }
}