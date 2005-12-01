// University of California, Santa Cruz, USA and
// CERN, Geneva, Switzerland, Copyright (c) 2000
package org.freehep.graphicsio.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import org.freehep.graphics2d.VectorGraphics;

/**
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestLineStyles.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestLineStyles extends TestingPanel {

    static float[] dash1 = new float[2];
    static {
        dash1[0] = 5.f;
        dash1[1] = 2.f;
    }

    static float[] dash2 = new float[2];
    static {
        dash2[0] = 0.f;
        dash2[1] = 7.f;
    }

    static float[] dash3 = new float[4];
    static {
        dash3[0] = 10.f;
        dash3[1] = 5.f;
        dash3[2] = 2.f;
        dash3[3] = 5.f;
    }

    static Stroke[][] strokes = new Stroke[3][3];

    static {
        strokes[0][0] = new BasicStroke(3.f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 10.f, dash1, 0.f);
        strokes[0][1] = new BasicStroke(5.f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 10.f, dash1, 0.f);
        strokes[0][2] = new BasicStroke(20.f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 10.f);
        strokes[1][0] = new BasicStroke(3.f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_MITER, 10.f, dash2, 0.f);
        strokes[1][1] = new BasicStroke(5.f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_MITER, 10.f, dash2, 0.f);
        strokes[1][2] = new BasicStroke(20.f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_MITER, 10.f);
        strokes[2][0] = new BasicStroke(3.f, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_ROUND, 10.f, dash3, 0.f);
        strokes[2][1] = new BasicStroke(5.f, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_ROUND, 10.f, dash3, 0.f);
        strokes[2][2] = new BasicStroke(20.f, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_ROUND, 10.f);
    }

    public TestLineStyles(String[] args) throws Exception {
        super(args);
        setName("Line Styles");
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
            int dh = dim.height / 3;

            int size = (Math.min(dw, dh) * 2 / 3) / 2;

            vg.setColor(Color.black);

            // thinnest line possible
            vg.setStroke(new BasicStroke(0.0f));
            vg.drawLine(0, dim.height / 40, dim.width, dim.height / 40);

            GeneralPath sshape = new GeneralPath();
            sshape.moveTo(size, size);
            sshape.lineTo(-size, size);
            sshape.lineTo(-size, size / 2);
            sshape.lineTo(size, size / 2);
            sshape.lineTo(size, -size / 2);
            sshape.lineTo(-size, -size / 2);
            sshape.lineTo(-size, -size);
            sshape.lineTo(size, -size);

            for (int iy = 0; iy < 3; iy++) {
                int y = iy * dh + dh / 2;
                for (int ix = 0; ix < 3; ix++) {
                    int x = ix * dw + dw / 2;

                    vg.setStroke(strokes[ix][iy]);

                    AffineTransform xform = AffineTransform
                            .getTranslateInstance((double) x, (double) y);
                    vg.draw(sshape.createTransformedShape(xform));
                }
            }

        }
    }

    public static void main(String[] args) throws Exception {
        new TestLineStyles(args).runTest();
    }
}
