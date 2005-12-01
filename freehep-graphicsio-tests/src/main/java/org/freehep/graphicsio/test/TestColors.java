// Copyright 2003, FreeHEP
package org.freehep.graphicsio.test;

import java.awt.Color;
import java.awt.Graphics;

import org.freehep.graphics2d.VectorGraphics;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestColors.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestColors extends TestingPanel {

    public TestColors(String[] args) throws Exception {
        super(args);
        setName("Colors");
    }

    public void paintComponent(Graphics g) {

        VectorGraphics vg = VectorGraphics.create(g);

        vg.setColor(Color.WHITE);
        vg.fillRect(0, 0, getWidth(), getHeight() / 3);

        vg.setColor(Color.BLACK);
        vg.fillRect(0, getHeight() / 3, getWidth(), getHeight() / 3);

        int radius = 55;

        int dx = getWidth() / 4;
        int dy = getHeight() / 6;
        int x = dx;
        int y = dy;

        drawRGB(x, y, radius, vg);

        x += (dx * 2);
        drawCMY(x, y, radius, vg);

        x = dx;
        y += (dy * 2);
        drawRGB(x, y, radius, vg);

        x += (dx * 2);
        drawCMY(x, y, radius, vg);

        x = dx;
        y += (dy * 2);
        drawRGB(x, y, radius, vg);

        x += (dx * 2);
        drawCMY(x, y, radius, vg);
    }

    private void drawRGB(int x, int y, int radius, VectorGraphics g) {
        int circleWidth = radius * 2;
        int radius53 = 5 * radius / 3;
        int radius3 = radius / 3;

        g.setColor(new Color(255, 0, 0, 128));
        g.fillOval(x - radius53, y - radius53, circleWidth, circleWidth);

        g.setColor(new Color(0, 255, 0, 128));
        g.fillOval(x - radius3, y - radius53, circleWidth, circleWidth);

        g.setColor(new Color(0, 0, 255, 128));
        g.fillOval(x - radius, y - radius3, circleWidth, circleWidth);
    }

    private void drawCMY(int x, int y, int radius, VectorGraphics g) {
        int circleWidth = radius * 2;
        int radius53 = 5 * radius / 3;
        int radius3 = radius / 3;

        g.setColor(new Color(0, 255, 255, 128));
        g.fillOval(x - radius53, y - radius53, circleWidth, circleWidth);

        g.setColor(new Color(255, 0, 255, 128));
        g.fillOval(x - radius3, y - radius53, circleWidth, circleWidth);

        g.setColor(new Color(255, 255, 0, 128));
        g.fillOval(x - radius, y - radius3, circleWidth, circleWidth);
    }

    public static void main(String[] args) throws Exception {
        new TestColors(args).runTest();
    }
}
