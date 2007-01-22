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
 * Polyline TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/Polyline.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class Polyline extends AbstractPolygon {

    public Polyline() {
        super(4, 1, null, 0, null);
    }

    public Polyline(Rectangle bounds, int numberOfPoints, Point[] points) {
        super(4, 1, bounds, numberOfPoints, points);
    }

    protected Polyline (int id, int version, Rectangle bounds, int numberOfPoints, Point[] points) {
        super(id, version, bounds, numberOfPoints, points);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        Rectangle r = emf.readRECTL();
        int n = emf.readDWORD();
        return new Polyline(r, n, emf.readPOINTL(n));
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        Point[] points = getPoints();
        int numberOfPoints = getNumberOfPoints();

        if (points != null && points.length > 0) {
            GeneralPath gp = new GeneralPath(
                renderer.getWindingRule());
            Point p;
            for (int point = 0; point < numberOfPoints; point ++) {
                // add a point to gp
                p = points[point];
                if (point > 0) {
                    gp.lineTo((float) p.getX(),  (float)p.getY());
                } else {
                    gp.moveTo((float) p.getX(),  (float)p.getY());
                }
            }
            renderer.drawOrAppend(gp);
        }
    }
}
