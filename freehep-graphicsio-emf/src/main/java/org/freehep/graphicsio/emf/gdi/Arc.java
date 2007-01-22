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
 * Arc TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/Arc.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class Arc extends EMFTag {

    private Rectangle bounds;

    private Point start, end;

    public Arc() {
        super(45, 1);
    }

    public Arc(Rectangle bounds, Point start, Point end) {
        this();
        this.bounds = bounds;
        this.start = start;
        this.end = end;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new Arc(emf.readRECTL(), emf.readPOINTL(), emf.readPOINTL());
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
        // The Arc function draws an elliptical arc.
        //
        // BOOL Arc(
        // HDC hdc, // handle to device context
        // int nLeftRect, // x-coord of rectangle's upper-left corner
        // int nTopRect, // y-coord of rectangle's upper-left corner
        // int nRightRect, // x-coord of rectangle's lower-right corner
        // int nBottomRect, // y-coord of rectangle's lower-right corner
        // int nXStartArc, // x-coord of first radial ending point
        // int nYStartArc, // y-coord of first radial ending point
        // int nXEndArc, // x-coord of second radial ending point
        // int nYEndArc // y-coord of second radial ending point
        // );
        // The points (nLeftRect, nTopRect) and (nRightRect, nBottomRect)
        // specify the bounding rectangle.
        // An ellipse formed by the specified bounding rectangle defines the
        // curve of the arc.
        // The arc extends in the current drawing direction from the point
        // where it intersects the
        // radial from the center of the bounding rectangle to the
        // (nXStartArc, nYStartArc) point.
        // The arc ends where it intersects the radial from the center of
        // the bounding rectangle to
        // the (nXEndArc, nYEndArc) point. If the starting point and ending
        // point are the same,
        // a complete ellipse is drawn.

        // normalize start and end point to a circle
        double nx0 = start.getX() / bounds.getWidth();

        // double ny0 = arc.getStart().y / arc.getBounds().height;
        double nx1 = end.getX() / bounds.getWidth();

        // double ny1 = arc.getEnd().y / arc.getBounds().height;
        // calculate angle of start point
        double alpha0 = Math.acos(nx0);
        double alpha1 = Math.acos(nx1);

        renderer.drawOrAppend(new Arc2D.Double(
            start.getX(),
            start.getY(),
            bounds.getWidth(),
            bounds.getHeight(),
            alpha0,
            alpha1 - alpha0,
            Arc2D.OPEN));
    }
}
