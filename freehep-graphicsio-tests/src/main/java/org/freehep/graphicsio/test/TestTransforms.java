// University of California, Santa Cruz, USA and
// CERN, Geneva, Switzerland, Copyright (c) 2000
package org.freehep.graphicsio.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import org.freehep.graphics2d.VectorGraphics;

/**
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestTransforms.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestTransforms extends TestingPanel {

    static Stroke stroke = new BasicStroke(5.f, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND);

    public TestTransforms(String[] args) throws Exception {
        super(args);
        setName("Transformations");
    }

    public void paintComponent(Graphics g) {

        VectorGraphics vg = VectorGraphics.create(g);
        AffineTransform transform = vg.getTransform();

        vg.setColor(Color.white);
        vg.fillRect(0, 0, getWidth(), getHeight());

        int size = 50;
        GeneralPath sshape = new GeneralPath();
        sshape.moveTo((float)0*size,   (float)0*size);
        sshape.lineTo((float).5*size,  (float)size);
        sshape.lineTo((float)-.5*size, (float)size);
        sshape.lineTo((float).5*size,  (float)-size);
        sshape.lineTo((float)-.5*size, (float)-size);
        sshape.closePath();

        vg.setColor(Color.black);

        Graphics subgraphics = vg.create();
        VectorGraphics svg = VectorGraphics.create(subgraphics);
        svg.setStroke(stroke);
        svg.translate(100, 100);
        svg.draw(sshape);
        svg.dispose();

        subgraphics = vg.create();
        svg = VectorGraphics.create(subgraphics);
        svg.setStroke(stroke);
        svg.translate(300, 100);
        svg.rotate(Math.PI / 4.);
        svg.fill(sshape);
        svg.dispose();

        subgraphics = vg.create();
        svg = VectorGraphics.create(subgraphics);
        svg.setStroke(stroke);
        svg.translate(500, 100);
        svg.scale(2., 0.5);
        svg.fillAndDraw(sshape, Color.red);
        svg.dispose();

        subgraphics = vg.create();
        svg = VectorGraphics.create(subgraphics);
        svg.setStroke(stroke);
        svg.translate(100, 300);
        svg.shear(1., 0.);
        svg.draw(sshape);
        svg.dispose();

        subgraphics = vg.create();
        svg = VectorGraphics.create(subgraphics);
        svg.setStroke(stroke);
        svg.translate(300, 300);
        svg.shear(0., 1.);
        svg.draw(sshape);
        svg.dispose();

        subgraphics = vg.create();
        svg = VectorGraphics.create(subgraphics);
        svg.setStroke(stroke);
        svg.translate(500, 300);
        svg.rotate(-Math.PI / 4., 50., 50.);
        svg.draw(sshape);
        svg.dispose();

        subgraphics = vg.create();
        svg = VectorGraphics.create(subgraphics);
        svg.setStroke(stroke);
        svg.translate(100, 500);
        svg.transform(new AffineTransform(2., 0., 1., 0.5, 50., 0.));
        svg.draw(sshape);
        svg.dispose();

        subgraphics = vg.create();
        svg = VectorGraphics.create(subgraphics);
        svg.setStroke(stroke);
        svg.translate(300, 500);
        svg.transform(new AffineTransform(0.5, 1., 0., 2., 50., -50.));
        svg.draw(sshape);
        svg.dispose();

        vg.setTransform(new AffineTransform(1., 0., 0., 1., 400., 400.));

        subgraphics = vg.create();
        svg = VectorGraphics.create(subgraphics);
        svg.setStroke(stroke);
        svg.transform(new AffineTransform(0.5, 1., 0., 1., 0., 0.));
        svg.draw(sshape);
        svg.dispose();

        vg.setTransform(transform);
    }

    public static void main(String[] args) throws Exception {
        new TestTransforms(args).runTest();
    }
}
