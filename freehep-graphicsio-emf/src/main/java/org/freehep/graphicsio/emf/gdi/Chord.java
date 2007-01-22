// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * Chord TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/Chord.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class Chord extends EMFTag {

    private Rectangle bounds;

    private Point start, end;

    public Chord() {
        super(46, 1);
    }

    public Chord(Rectangle bounds, Point start, Point end) {
        this();
        this.bounds = bounds;
        this.start = start;
        this.end = end;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new Chord(
            emf.readRECTL(),
            emf.readPOINTL(),
            emf.readPOINTL());
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
        emf.writePOINTL(start);
        emf.writePOINTL(end);
    }

    public String toString() {
        return super.toString() +
            "\n  bounds: " + bounds +
            "\n  start: " + start +
            "\n  end: " + end;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // The Chord function draws a chord (a region bounded by the
        // intersection of an
        // ellipse and a line segment, called a secant). The chord is
        // outlined by using the
        // current pen and filled by using the current brush.

        // normalize start and end point to a circle
        double nx0 = start.getX() / bounds.width;

        // double ny0 = arc.getStart().y / arc.getBounds().height;
        double nx1 = end.getX() / bounds.width;

        // double ny1 = arc.getEnd().y / arc.getBounds().height;
        // calculate angle of start point
        double alpha0 = Math.acos(nx0);
        double alpha1 = Math.acos(nx1);

        renderer.fillAndDrawOrAppend(new Arc2D.Double(
            start.getX(),
            start.getY(),
            bounds.getWidth(),
            bounds.getHeight(),
            alpha0,
            alpha1 - alpha0,
            Arc2D.CHORD));
    }
}
