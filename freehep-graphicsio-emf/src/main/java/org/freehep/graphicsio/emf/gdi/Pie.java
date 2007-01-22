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
 * Pie TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/Pie.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class Pie extends EMFTag {

    private Rectangle bounds;

    private Point start, end;

    public Pie() {
        super(47, 1);
    }

    public Pie(Rectangle bounds, Point start, Point end) {
        this();
        this.bounds = bounds;
        this.start = start;
        this.end = end;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new Pie(emf.readRECTL(), emf.readPOINTL(), emf.readPOINTL());
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
        // normalize start and end point to a circle
        double nx0 = start.x / bounds.width;

        // double ny0 = arc.getStart().y / arc.getBounds().height;
        double nx1 = end.x / bounds.width;

        // double ny1 = arc.getEnd().y / arc.getBounds().height;
        // calculate angle of start point
        double alpha0 = Math.acos(nx0);
        double alpha1 = Math.acos(nx1);

        renderer.fillAndDrawOrAppend(new Arc2D.Double(
            start.x,
            start.y,
            bounds.width,
            bounds.height,
            alpha0,
            alpha1 - alpha0,
            Arc2D.PIE));
    }
}
