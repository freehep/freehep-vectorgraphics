// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * PolylineTo TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/EMFPolygon.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class EMFPolygon extends AbstractPolygon {

    public EMFPolygon() {
        super(3, 1, null, 0, null);
    }

    public EMFPolygon(Rectangle bounds, int numberOfPoints, Point[] points) {
        super(3, 1, bounds, numberOfPoints, points);
    }

    protected EMFPolygon (int id, int version, Rectangle bounds, int numberOfPoints, Point[] points) {
        super(id, version, bounds, numberOfPoints, points);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        Rectangle r = emf.readRECTL();
        int n = emf.readDWORD();
        return new EMFPolygon(r, n, emf.readPOINTL(n));
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        Point[] points = getPoints();

        // Safety check.
        if (points.length > 1) {
            GeneralPath path = new GeneralPath(
                renderer.getWindingRule());
            path.moveTo((float)points[0].getX(), (float)points[0].getY());
            for (int i = 1; i < points.length; i++) {
                path.lineTo((float)points[i].getX(), (float)points[i].getY());
            }
            path.closePath();
            renderer.fillAndDrawOrAppend(path);
        }
    }
}
