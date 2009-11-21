// Copyright 2007, FreeHEP.
package org.freehep.graphicsio.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.util.UserProperties;
import org.freehep.util.images.ImageHandler;

/**
 * tests the ability to handle different rendering hints
 *
 * @author Steffen Greiffenberg
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestRenderingHints.java e908a30ae307 2007/01/13 00:45:55 duns $
 */
public class TestRenderingHints extends TestingPanel {

    Image image;

    public TestRenderingHints(String[] args) throws Exception {
        super(args);
        setName("Rendering Hints");
    }

    public void paintComponent(Graphics g) {

        MediaTracker t = new MediaTracker(this);
        image = ImageHandler.getImage(
            "images/BrokenCursor.gif",
            TestRenderingHints.class);
        t.addImage(image, 0);
        try {
            t.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        VectorGraphics vg = VectorGraphics.create(g);
        AffineTransform at = vg.getTransform();

        vg.setColor(Color.white);
        vg.fillRect(0, 0, getWidth(), getHeight());
        vg.setColor(Color.black);

        vg.setFont(new Font("Dialog", Font.ITALIC, 20));

        vg.setRenderingHints(new HashMap<Key, Object>());
        paint(vg, "No Hints");

        vg.setRenderingHints(new HashMap<Key, Object>());
        vg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paint(vg, "ANTIALIAS_ON");

        vg.setRenderingHints(new HashMap<Key, Object>());
        vg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        paint(vg, "TEXT_ANTIALIAS_ON");

        vg.setRenderingHints(new HashMap<Key, Object>());
        vg.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        paint(vg, "STROKE_NORMALIZE");

        vg.setRenderingHints(new HashMap<Key, Object>());
        vg.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        paint(vg, "STROKE_PURE");

        vg.setRenderingHints(new HashMap<Key, Object>());
        vg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        paint(vg, "INTERPOLATION_BICUBIC");

        vg.setRenderingHints(new HashMap<Key, Object>());
        vg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        paint(vg, "INTERPOLATION_NEAREST_NEIGHBOR");

        vg.setRenderingHints(new HashMap<Key, Object>());
        vg.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        paint(vg, "VALUE_DITHER_ENABLE");

        RenderingHints hints = new RenderingHints(
            RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        vg.addRenderingHints(hints);
        paint(vg, "VALUE_FRACTIONALMETRICS_ON (added)");

        vg.setTransform(at);
    }

    /**
     * draws a caption, a cross and to images
     *
     * @param vg painting context
     * @param text caption
     */
    private void paint(VectorGraphics vg, String text) {
        vg.drawString(text, 20, 30);
        vg.drawLine(20, 35, 200, 50);
        vg.drawLine(20, 50, 200, 35);
        vg.drawImage(image, 230, 35, 100, 25, Color.yellow, this);

        // test dithering
        BufferedImage ditheredImage = new BufferedImage(200, 2, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g = (Graphics2D) ditheredImage.getGraphics();
        g.setRenderingHints(vg.getRenderingHints());
        for (int i = 0; i < 20; i ++) {
            double red = 255d / 20d * i + 2d;
            g.setColor(new Color((int) red, 0, 0));
            g.fillRect(i * 10, 0, 10, 25);
        }
        g.dispose();

        vg.drawImage(ditheredImage, 360, 35, 200, 25, this);

        vg.translate(0, 60);
    }

    public static void main(String[] args) throws Exception {
        UserProperties p = new UserProperties();
        new TestRenderingHints(args).runTest(p);
    }
}
