// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * MoveToEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/MoveToEx.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class MoveToEx extends EMFTag {

    private Point point;

    public MoveToEx() {
        super(27, 1);
    }

    public MoveToEx(Point point) {
        this();
        this.point = point;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new MoveToEx(emf.readPOINTL());
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
        // The MoveToEx function updates the current position to the
        // specified point
        // and optionally returns the previous position.
        GeneralPath currentFigure = new GeneralPath(
            renderer.getWindingRule());
        currentFigure.moveTo(
            (float) point.getX(),
            (float) point.getY());
        renderer.setFigure(currentFigure);
    }
}
