// Copyright 2000-2005, FreeHEP.
package org.freehep.graphicsio.test;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.util.UserProperties;
import org.freehep.util.images.ImageHandler;

public class TestPaint extends TestingPanel {

    public TestPaint(String[] args) throws Exception {
        super(args);
        setName("Paint");
    }

    public void paintComponent(Graphics g) {

        VectorGraphics vg = VectorGraphics.create(g);

        vg.setColor(Color.white);
        vg.fillRect(0, 0, getWidth(), getHeight());

        int dw = getWidth() / 3;
        int dh = getHeight() / 3;

        MediaTracker t = new MediaTracker(this);
        Image limage = ImageHandler.getImage("images/BrokenCursor.gif",
                getClass());
        t.addImage(limage, 0);
        try {
            t.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BufferedImage image = new BufferedImage(limage.getWidth(this), limage
                .getHeight(this), BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(limage, 0, 0, this);

        Paint[] paint = new Paint[] {
                new TexturePaint(image, new Rectangle2D.Double(0, 0, image
                        .getWidth(), image.getHeight())),
                new TexturePaint(image, new Rectangle2D.Double(0, 0, image
                        .getWidth() / 2, image.getHeight() / 2)),
                new TexturePaint(image, new Rectangle2D.Double(
                        image.getWidth() / 2, image.getHeight() / 2, image
                                .getWidth(), image.getHeight())) };

        double row = 1;
        for (int x = 0; x < paint.length; x++) {
            vg.setColor(Color.black);
            vg.drawRect(dw * x, dh * row, dw * 0.9, dh * 0.9);
            vg.setPaint(paint[x]);
            vg.fillRect(dw * x, dh * row, dw * 0.9, dh * 0.9);
        }

        row = 2;
        for (int x = 0; x < 3; x++) {
            Paint p = null;
            switch (x) {
            case 0:
                p = new GradientPaint(dw * x, (int) row * dh, Color.red,
                        (int) (dw * x + dw * 0.9), (int) (row * dh + 0.9 * dh),
                        Color.blue);
                break;
            case 1:
                p = new GradientPaint(dw * x + dw / 4, (int) row * dh + dh / 4,
                        Color.green, (int) (dw * x + dw * 0.6),
                        (int) (row * dh + 0.6 * dh), new Color(255, 0, 255));
                break;
            case 2:
                p = new GradientPaint(dw * x, (int) row * dh, Color.red,
                        (int) (dw * x + dw * 0.2), (int) (row * dh + 0.2 * dh),
                        Color.yellow, true);
                break;
            }
            vg.setPaint(p);
            vg.fillRect(dw * x, row * dh, dw * 0.9, dh * 0.9);
        }

        Graphics subgraphics = vg.create();
        VectorGraphics svg = VectorGraphics.create(subgraphics);
        double x1 = 0;
        double y1 = 0;
        double x2 = x1 + dw * 0.6;
        double y2 = y1 + dh * 0.6;
        svg.shear(0.5, 0.5);

        svg.setPaint(new GradientPaint((int) x1, (int) y1, Color.red, (int) x2,
                (int) y2, Color.blue));
        svg.fillRect(x1, y1, x2 - x1, y2 - y1);

        svg.setPaint(new TexturePaint(image, new Rectangle2D.Double(0, 0, image
                .getWidth(), image.getHeight())));
        svg.fillRect(x1 + dw, y1 - dh / 2, x2 - x1, y2 - y1);

        svg.dispose();
    }

    public static void main(String[] args) throws Exception {
        UserProperties p = new UserProperties();
        new TestPaint(args).runTest(p);
    }
}
