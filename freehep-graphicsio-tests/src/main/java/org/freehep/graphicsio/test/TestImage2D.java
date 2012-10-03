// University of California, Santa Cruz, USA and
// CERN, Geneva, Switzerland, Copyright (c) 2000
package org.freehep.graphicsio.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsbase.util.images.ImageHandler;

/**
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestImage2D.java 68aaba5d4589 2006/11/15 20:23:12 duns $
 */
public class TestImage2D extends TestingPanel {

    static Stroke stroke = new BasicStroke(5.f, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND);

    private Image image;
    private BufferedImage image1, image2;

    public TestImage2D(String[] args) throws Exception {
        super(args);
        setName("Images2D");

        MediaTracker t = new MediaTracker(this);
        image = ImageHandler.getImage("images/transparent-image.gif",
                TestImage2D.class);
        t.addImage(image, 0);
        try {
            t.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        image1 = new BufferedImage(image.getWidth(this), image.getHeight(this),
                BufferedImage.TYPE_INT_RGB);
        image1.createGraphics().drawImage(image, 0, 0, this);
        
        image2 = new BufferedImage(image.getWidth(this), image
                .getHeight(this), BufferedImage.TYPE_INT_RGB);
        image2.createGraphics().drawImage(image, 0, 0, this);
    }

    public void paintComponent(Graphics g) {

        VectorGraphics vg = VectorGraphics.create(g);
        AffineTransform transform = vg.getTransform();

        vg.setColor(Color.white);
        vg.fillRect(0, 0, getWidth(), getHeight());

        vg.drawImage(image1, new AffineTransform(), this);

        vg.shear(0.2, 0.2);
        AffineTransform t = new AffineTransform();
        t.rotate(0.5, -40, 0);
        t.translate(250, -50);
        t.shear(-0.3, 0);
        vg.drawImage(image, t, this);

        float[] SHARPEN3x3 = new float[] { 0.f, -1.f, 0.f, -1.f, 5.0f, -1.f,
                0.f, -1.f, 0.f };
        Kernel kernel = new Kernel(3, 3, SHARPEN3x3);
        BufferedImageOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP,
                null);
        vg.drawImage(image2, cop, 300, -80);

        vg.setTransform(transform);
    }

    public static void main(String[] args) throws Exception {
        new TestImage2D(args).runTest();
    }
}
