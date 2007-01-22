// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Point;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * LineTo TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/LineTo.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class LineTo extends EMFTag {

    private Point point;

    public LineTo() {
        super(54, 1);
    }

    public LineTo(Point point) {
        this();
        this.point = point;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new LineTo(emf.readPOINTL());
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writePOINTL(point);
    }

    public String toString() {
        return super.toString() + "\n  point: " + point;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // The LineTo function draws a line from the current position up to,
        // but not including, the specified point.
        // The line is drawn by using the current pen and, if the pen is a
        // geometric pen, the current brush.
        renderer.getFigure().lineTo(
            (float)point.getX(),
            (float)point.getY());
    }
}
