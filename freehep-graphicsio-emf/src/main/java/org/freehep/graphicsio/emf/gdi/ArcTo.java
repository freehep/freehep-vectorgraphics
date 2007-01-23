// Copyright 2002-2007, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * ArcTo TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/ArcTo.java cb17a8f71934 2007/01/23 15:44:34 duns $
 */
public class ArcTo extends AbstractArc {

    public ArcTo() {
        super(55, 1, null, null, null);
    }

    public ArcTo(Rectangle bounds, Point start, Point end) {
        super(55, 1, bounds, start, end);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new ArcTo(
            emf.readRECTL(),
            emf.readPOINTL(),
            emf.readPOINTL());
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

        renderer.getFigure().append(
            getShape(renderer, Arc2D.OPEN), true);
    }
}
