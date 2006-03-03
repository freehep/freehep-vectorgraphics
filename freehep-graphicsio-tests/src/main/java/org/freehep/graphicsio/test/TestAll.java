package org.freehep.graphicsio.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.TexturePaint;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.freehep.graphics2d.TagString;
import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphics2d.VectorGraphicsConstants;
import org.freehep.util.images.ImageHandler;

/**
 * @author Simon Fischer
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestAll.java d9a2ef8950b1 2006/03/03 19:08:18 duns $
 */
public class TestAll extends TestingPanel implements VectorGraphicsConstants {

    private BufferedImage marble, sky;

    private static float[] dash = { 10.f, 5.f, 2.f, 5.f };

    public TestAll(String[] args) throws Exception {
        super(args);
        setName("All");
    }

    public void paintComponent(Graphics g) {

        if (g == null)
            return;

        if (marble == null) {
            MediaTracker t = new MediaTracker(this);
	    Image skyI = ImageHandler.getImage("images/sky.gif", TestAll.class);
            t.addImage(skyI, 0);
            try {
                t.waitForAll();
            } catch (Exception e) {
                e.printStackTrace();
            }

            sky = new BufferedImage(skyI.getWidth(this), skyI.getHeight(this),
                    BufferedImage.TYPE_INT_RGB);
            sky.createGraphics().drawImage(skyI, 0, 0, this);
        }

        VectorGraphics vg = VectorGraphics.create(g);

        // Rectangle dim = vg.getClip().getBounds();
        Dimension dim = getSize();
        Insets insets = getInsets();
        double width = dim.getWidth();
        double height = dim.getHeight();

        vg.setColor(Color.white);
        vg.fillRect(insets.left, insets.top, dim.width - insets.left
                - insets.right, dim.height - insets.top - insets.bottom);

        vg.setPaint(new GradientPaint((int) 0, (int) 10, Color.blue,
                (int) width, (int) 10, Color.white));
        vg.fillRect(0, 10, width, 30);
        vg.setFont(new Font("Helvetica", Font.BOLD, 18));
        vg.setPaint(new GradientPaint((int) 0, (int) 10, Color.black,
                (int) width, (int) 10, Color.blue));
        String str = "Testing " + vg.getClass().getName();
        vg.drawString(str, 10, 30);

        double fx = 3 * width / 16;
        double fy = 5 * height / 16;
        double fw = width / 4;
        for (int i = 0; i < 15; i++) {
            vg.setColor(Color.black);
            vg.fillRect(fx - fw / 2, fy - fw / 2, fw, fw);
            vg.setColor(Color.white);
            vg.fillOval(fx - fw / 2, fy - fw / 2, fw, fw);
            fw /= Math.sqrt(2);
        }

        vg.setColor(Color.black);
        vg.setFont(new Font("TimesRoman", Font.PLAIN, 11));
        // vg.setFont(new Font("Arial", Font.PLAIN, 11));

        vg
                .drawString(
                        new TagString(
                                "The <i>drawString</i> methods in <i>VectorGraphics</i> support"),
                        fx + 3 * width / 16, fy - width / 8 + 10);
        vg
                .drawString(
                        new TagString(
                                "output of strings using a subset of the <b>HTML language</b>."),
                        fx + 3 * width / 16, fy - width / 8 + 24);

        vg.setColor(Color.red);
        double sx = width / 2;
        double sy = 5 * height / 16;
        for (int i = 0; i < NUMBER_OF_SYMBOLS; i++) {
            vg.drawSymbol(sx + 15 * i, sy, 12, i);
            vg.fillSymbol(sx + 15 * i, sy + 15, 10, i);
        }

        vg.setPaint(new TexturePaint(sky, new Rectangle2D.Double(0, 0, sky
                .getWidth(), sky.getHeight())));
        double mx = 0;
        double my = height / 2;
        vg.fillRect(mx, my, width / 2, height / 2);
        vg.setPaint(Color.black);
        vg.setFont(new Font("Impact", Font.BOLD, 60));
        vg.drawString("\u2729Impact\u2729", mx + width / 4, my + height / 4,
                VectorGraphicsConstants.TEXT_CENTER, VectorGraphicsConstants.TEXT_BASELINE);

        GeneralPath shape = new GeneralPath();
        shape.moveTo(0.f, 0.f);
        shape.lineTo(25.f, 50.f);
        shape.lineTo(-25.f, 50.f);
        shape.lineTo(25.f, -50.f);
        shape.lineTo(-25.f, -50.f);
        shape.closePath();

        Graphics subgraphics = vg.create();
        VectorGraphics svg = VectorGraphics.create(subgraphics);
        svg.translate(width / 2, height / 2);
        svg.setStroke(new BasicStroke(8.f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 10.f, null, 0.f));
        svg.draw(shape);

        subgraphics = svg.create();
        VectorGraphics svg2 = VectorGraphics.create(subgraphics);
        svg2.setColor(Color.black);
        svg2.setStroke(new BasicStroke(3.f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND, 10.f, dash, 0.f));
        svg2.rotate(Math.PI / 5);
        svg2.draw(shape);
        svg2.dispose();

        svg.setLineWidth(1);
        svg.shear(0.5, 0.5);
        svg.draw(shape);
        svg.dispose();

        subgraphics = vg.create();
        svg = VectorGraphics.create(subgraphics);
        double tx = 3 * width / 4;
        double ty = 3 * height / 4;
        double d = width / 10;
        svg.setColor(Color.black);
        svg.translate(tx, ty);
        svg.fillOval(-d, -d, 2 * d, 2 * d);
        svg.setStroke(new BasicStroke(4.f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND, 10.f, null, 0.f));
        svg.drawOval(-1.4 * d, -1.4 * d, 1.4 * 2 * d, 1.4 * 2 * d);

        String circle = "ORAETLAB";
        svg.setFont(new Font("TimesRoman", Font.BOLD, 16));
        for (int i = 0; i < circle.length(); i++) {
            svg.drawString(circle.substring(i, i + 1), 0, -1.1 * d);
            svg.rotate(Math.PI * 2 / circle.length());
        }
        svg.dispose();
    }

    public static void main(String[] args) throws Exception {
        new TestAll(args).runTest();
    }
}
