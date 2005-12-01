package org.freehep.graphicsio.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import org.freehep.graphics2d.VectorGraphics;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestClip.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestClip extends TestingPanel {

    private Shape[] clip;

    private Shape[] path;

    public TestClip(String[] args) throws Exception {
        super(args);
        setName("Clip");

        GeneralPath p;

        clip = new Shape[6];
        clip[0] = new Rectangle2D.Double(1, 1, 6, 2);
        p = new GeneralPath();
        p.moveTo(1, 1);
        p.lineTo(1, 3);
        p.lineTo(7, 3);
        p.lineTo(7, 1);
        p.lineTo(5, 1);
        p.lineTo(4, 3);
        p.lineTo(3, 1);
        p.closePath();
        clip[1] = p;

        clip[2] = new Rectangle2D.Double(1, 1, 6, 2);
        clip[3] = new Rectangle2D.Double(1, 1, 6, 2);
        clip[4] = new Rectangle2D.Double(1, 1, 6, 2);
        clip[5] = new Ellipse2D.Double(1, 1, 6, 2);

        path = new Shape[6];
        p = new GeneralPath();
        p.moveTo(0, 2);
        p.lineTo(2, 2);
        p.lineTo(3, 1);
        p.lineTo(4, 1);
        p.lineTo(5, 0);
        p.lineTo(6, 2);
        p.lineTo(8, 2);
        p.lineTo(8, 0);
        p.lineTo(0, 0);
        p.closePath();
        path[0] = p;

        path[1] = new Rectangle2D.Double(0, 0, 8, 2);

        p = new GeneralPath();
        p.moveTo(0, 2);
        p.lineTo(2, 2);
        p.lineTo(3, 1);
        p.lineTo(5, 1);
        p.lineTo(6, 2);
        p.lineTo(8, 2);
        p.lineTo(8, 0);
        p.lineTo(0, 0);
        p.closePath();
        path[2] = p;

        path[3] = new Ellipse2D.Double(2, 0, 4, 4);

        p = new GeneralPath();
        p.moveTo(0, 2);
        p.lineTo(3, 2);
        p.lineTo(4, 1);
        p.lineTo(5, 2);
        p.lineTo(8, 2);
        p.lineTo(8, 0);
        p.lineTo(0, 0);
        p.closePath();
        path[4] = p;

        path[5] = new Ellipse2D.Double(2, 0, 4, 4);
    }

    public void paintComponent(Graphics g) {

        VectorGraphics vg = VectorGraphics.create(g);
        Dimension dim = getSize();

        vg.setColor(Color.lightGray);
        vg.fillRect(0, 0, dim.width, dim.height);

        int nx = 6;
        int ny = 6;

        int dw = dim.width / (nx + 1);
        int dh = dim.height / ny;

        double factor = Math.min(dw, dh) / 10; // size of shape
        vg.setColor(Color.black);
        // vg.setFont(new Font("Lucida", Font.PLAIN, 10));
        VectorGraphics svg;

        // Java Draw
        svg = (VectorGraphics) vg.create();
        svg.drawString("NoClip", 10, 10 + 10 + 0 * dh);
        svg.scale(factor, factor);
        svg.translate(10 / factor, 10 / factor + 0 * dh / factor);
        for (int i = 0; i < nx; i++) {
            svg.translate(dw / factor, 0);
            svg.setColor(Color.red);
            svg.setLineWidth(1.0 / factor);
            svg.draw(clip[i]);
            svg.setColor(Color.black);
            svg.setLineWidth(3.0 / factor);
            svg.draw(path[i]);
        }
        svg.dispose();

        // Java Clip and Draw
        svg = (VectorGraphics) vg.create();
        svg.drawString("JavaClip", 10, 10 + 10 + 1 * dh);
        svg.scale(factor, factor);
        svg.translate(10 / factor, 10 / factor + 1 * dh / factor);
        for (int i = 0; i < nx; i++) {
            svg.translate(dw / factor, 0);
            VectorGraphics svg2 = (VectorGraphics) svg.create();
            svg2.setColor(Color.red);
            svg2.setLineWidth(1.0 / factor);
            svg2.draw(clip[i]);
            svg2.clip(clip[i]);
            svg2.setColor(Color.black);
            svg2.setLineWidth(3.0 / factor);
            svg2.draw(path[i]);
            svg2.dispose();
        }
        svg.dispose();

        // Our Clip and Draw
        svg = (VectorGraphics) vg.create();
        svg.drawString("OurClip", 10, 10 + 10 + 2 * dh);
        svg.scale(factor, factor);
        svg.translate(10 / factor, 10 / factor + 2 * dh / factor);
        for (int i = 0; i < nx; i++) {
            svg.translate(dw / factor, 0);
            VectorGraphics svg2 = (VectorGraphics) svg.create();
            svg2.setColor(Color.red);
            svg2.setLineWidth(1.0 / factor);
            svg2.draw(clip[i]);
            svg2.setColor(Color.black);
            svg2.setLineWidth(3.0 / factor);
            Area clippedPath = new Area(clip[i]);
            clippedPath.intersect(new Area(path[i]));
            svg2.draw(clippedPath);
            svg2.dispose();
        }
        svg.dispose();

        // Java Fill
        svg = (VectorGraphics) vg.create();
        svg.drawString("NoClip", 10, 10 + 10 + 3 * dh);
        svg.scale(factor, factor);
        svg.translate(10 / factor, 10 / factor + 3 * dh / factor);
        for (int i = 0; i < nx; i++) {
            svg.translate(dw / factor, 0);
            svg.setColor(Color.red);
            svg.setLineWidth(1.0 / factor);
            svg.draw(clip[i]);
            svg.setColor(Color.black);
            svg.setLineWidth(3.0 / factor);
            svg.fill(path[i]);
        }
        svg.dispose();

        // Java Clip and Fill
        svg = (VectorGraphics) vg.create();
        svg.drawString("JavaClip", 10, 10 + 10 + 4 * dh);
        svg.scale(factor, factor);
        svg.translate(10 / factor, 10 / factor + 4 * dh / factor);
        for (int i = 0; i < nx; i++) {
            svg.translate(dw / factor, 0);
            VectorGraphics svg2 = (VectorGraphics) svg.create();
            svg2.setColor(Color.red);
            svg2.setLineWidth(1.0 / factor);
            svg2.draw(clip[i]);
            svg2.clip(clip[i]);
            svg2.setColor(Color.black);
            svg2.setLineWidth(3.0 / factor);
            svg2.fill(path[i]);
            svg2.dispose();
        }
        svg.dispose();

        // Our Clip and Fill
        svg = (VectorGraphics) vg.create();
        svg.drawString("AreaClip", 10, 10 + 10 + 5 * dh);
        svg.scale(factor, factor);
        svg.translate(10 / factor, 10 / factor + 5 * dh / factor);
        for (int i = 0; i < nx; i++) {
            svg.translate(dw / factor, 0);
            VectorGraphics svg2 = (VectorGraphics) svg.create();
            svg2.setColor(Color.red);
            svg2.setLineWidth(1.0 / factor);
            svg2.draw(clip[i]);
            svg2.setColor(Color.black);
            svg2.setLineWidth(3.0 / factor);
            Area clippedPath = new Area(clip[i]);
            clippedPath.intersect(new Area(path[i]));
            svg2.fill(clippedPath);
            svg2.dispose();
        }

        svg.dispose();
    }

    public static void main(String[] args) throws Exception {
        new TestClip(args).runTest();
    }
}
