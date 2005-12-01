// Copyright 2003, FreeHEP
package org.freehep.graphicsio.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.util.images.ImageHandler;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestTransparency.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestTransparency extends TestingPanel {

    static Stroke stroke = new BasicStroke(5.f, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND);

    private Image image;

    private Color bkg = Color.ORANGE;

    public TestTransparency(String[] args) throws Exception {
        super(args);
        setName("Transparency");

        MediaTracker t = new MediaTracker(this);
        image = ImageHandler.getImage("images/transparent-image.gif",
                getClass());
        t.addImage(image, 0);
        try {
            t.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {

        VectorGraphics vg = VectorGraphics.create(g);

        int numberOfImages = 16;
        int dalpha = 255 / (numberOfImages - 1);
        int imagesPerSide = (int) Math.sqrt(numberOfImages);
        int x0 = 10;
        int y0 = 10;
        int width = getWidth();
        int height = getHeight();
        int dw = (width - 2 * x0) / imagesPerSide;
        int dh = (height - 2 * y0) / imagesPerSide;
        int x = x0;
        int y = y0;

        vg.setBackground(bkg);
        vg.clearRect(0, 0, width, height);

        BufferedImage[] bi = new BufferedImage[numberOfImages];
        for (int n = 0; n < numberOfImages; n++) {
            bi[n] = new BufferedImage(image.getWidth(this), image
                    .getHeight(this), BufferedImage.TYPE_INT_ARGB);
            bi[n].createGraphics().drawImage(image, 0, 0, this);
            for (int j = 0; j < bi[n].getHeight(); j++) {
                for (int i = 0; i < bi[n].getWidth(); i++) {
                    int data = bi[n].getRGB(i, j);
                    // System.out.println(Integer.toHexString(data));
                    if ((data >> 24) == 0) {
                        // mend transparent pixels
                        data |= ((dalpha * n) << 24);
                        data |= 0x00008080;
                        // System.out.println(Integer.toHexString(data));
                    }
                    bi[n].setRGB(i, j, data);
                }
            }
        }

        int n = 0;
        for (int i = 0; i < imagesPerSide; i++) {
            for (int j = 0; j < imagesPerSide; j++) {
                vg.drawImage(bi[n], x, y, dw, dh, this);
                n++;
                x += dw;
            }
            x = x0;
            y += dh;
        }
    }

    public static void main(String[] args) throws Exception {
        new TestTransparency(args).runTest();
    }
}
