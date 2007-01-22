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
 * PolyBezier TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/PolyBezier.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class PolyBezier extends AbstractPolygon {

    public PolyBezier() {
        super(2, 1, null, 0, null);
    }

    public PolyBezier(Rectangle bounds, int numberOfPoints, Point[] points) {
        super(2, 1, bounds, numberOfPoints, points);
    }

    protected PolyBezier (int id, int version, Rectangle bounds, int numberOfPoints, Point[] points) {
        super(id, version, bounds, numberOfPoints, points);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        Rectangle r = emf.readRECTL();
        int n = emf.readDWORD();
        return new PolyBezier(r, n, emf.readPOINTL(n));
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
            Point p = points[0];
            gp.moveTo((float) p.getX(),  (float)p.getY());

            for (int point = 1; point < numberOfPoints; point = point + 3) {
                // add a point to gp
                Point p1 = points[point];
                Point p2 = points[point + 1];
                Point p3 = points[point + 2];
                if (point > 0) {
                    gp.curveTo(
                        (float)p1.getX(), (float)p1.getY(),
                        (float)p2.getX(), (float)p2.getY(),
                        (float)p3.getX(), (float)p3.getY());
                }
            }
            renderer.fillAndDrawOrAppend(gp);
        }
    }
}
