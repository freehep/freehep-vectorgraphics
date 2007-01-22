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
 * ArcTo TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/ArcTo.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class ArcTo extends EMFTag {

    private Rectangle bounds;

    private Point start, end;

    public ArcTo() {
        super(55, 1);
    }

    public ArcTo(Rectangle bounds, Point start, Point end) {
        this();
        this.bounds = bounds;
        this.start = start;
        this.end = end;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new ArcTo(
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
        // The ArcTo function draws an elliptical arc.
        //
        // BOOL ArcTo(
        // HDC hdc, // handle to device context
        // int nLeftRect, // x-coord of rectangle's upper-left corner
        // int nTopRect, // y-coord of rectangle's upper-left corner
        // int nRightRect, // x-coord of rectangle's lower-right corner
        // int nBottomRect, // y-coord of rectangle's lower-right corner
        // int nXRadial1, // x-coord of first radial ending point
        // int nYRadial1, // y-coord of first radial ending point
        // int nXRadial2, // x-coord of second radial ending point
        // int nYRadial2 // y-coord of second radial ending point
        // );
        // ArcTo is similar to the Arc function, except that the current
        // position is updated.
        //
        // The points (nLeftRect, nTopRect) and (nRightRect, nBottomRect)
        // specify the bounding rectangle.
        // An ellipse formed by the specified bounding rectangle defines the
        // curve of the arc. The arc extends
        // counterclockwise from the point where it intersects the radial
        // line from the center of the bounding
        // rectangle to the (nXRadial1, nYRadial1) point. The arc ends where
        // it intersects the radial line from
        // the center of the bounding rectangle to the (nXRadial2,
        // nYRadial2) point. If the starting point and
        // ending point are the same, a complete ellipse is drawn.
        //
        // A line is drawn from the current position to the starting point
        // of the arc.
        // If no error occurs, the current position is set to the ending
        // point of the arc.
        //
        // The arc is drawn using the current pen; it is not filled.

        // normalize start and end point to a circle
        double nx0 = start.getX() / bounds.getWidth();

        // double ny0 = arc.getStart().y / arc.getBounds().height;
        double nx1 = end.getX() / bounds.getWidth();

        // double ny1 = arc.getEnd().y / arc.getBounds().height;
        // calculate angle of start point
        double alpha0 = Math.acos(nx0);
        double alpha1 = Math.acos(nx1);

        renderer.getFigure().append(new Arc2D.Double(
            start.getX(),
            start.getY(),
            bounds.getWidth(),
            bounds.getHeight(),
            alpha0,
            alpha1 - alpha0,
            Arc2D.OPEN), true);
    }
}
