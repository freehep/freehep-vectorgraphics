// Copyright 2007, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;

/**
 * abstract parent for PolyPolygon drawing
 *
 * @author Steffen Greiffenberg
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/AbstractPolyPolygon.java 9c0688d78e6b 2007/01/30 23:58:16 duns $
 */
public abstract class AbstractPolyPolygon extends EMFTag {

    private Rectangle bounds;

    private int[] numberOfPoints;

    private Point[][] points;

    /**
     * Constructs a EMFTag.
     *
     * @param id      id of the element
     * @param version emf version in which this element was first supported
     * @param bounds bounds of figure
     * @param numberOfPoints number of points
     * @param points points
     */
    protected AbstractPolyPolygon(
        int id, int version,
        Rectangle bounds,
        int[] numberOfPoints,
        Point[][] points) {

        super(id, version);
        this.bounds = bounds;
        this.numberOfPoints = numberOfPoints;
        this.points = points;
    }

    public String toString() {
        return super.toString() +
            "\n  bounds: " + bounds +
            "\n  #polys: " + numberOfPoints.length;
    }

    protected Rectangle getBounds() {
        return bounds;
    }

    protected int[] getNumberOfPoints() {
        return numberOfPoints;
    }

    protected Point[][] getPoints() {
        return points;
    }

    /**
     * displays the tag using the renderer. The default behavior
     * is to close and fill the polgygons for rendering.
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        render(renderer,  true);
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     * @param closePath if true the path is closed and filled
     */
    protected void render(EMFRenderer renderer, boolean closePath) {
        // create a GeneralPath containing GeneralPathes
        GeneralPath path = new GeneralPath(
            renderer.getWindingRule());

        // iterate the polgons
        Point p;
        for (int polygon = 0; polygon < numberOfPoints.length; polygon++) {

            // create a new member of path
            GeneralPath gp = new GeneralPath(
                renderer.getWindingRule());
            for (int point = 0; point < numberOfPoints[polygon]; point ++) {
                // add a point to gp
                p = points[polygon][point];
                if (point > 0) {
                    gp.lineTo((float) p.getX(),  (float)p.getY());
                } else {
                    gp.moveTo((float) p.getX(),  (float)p.getY());
                }
            }

            // close the member, add it to path
            if (closePath) {
                gp.closePath();
            }

            path.append(gp, false);
        }

        // draw the complete path
        if (closePath) {
            renderer.fillAndDrawOrAppend(path);
        } else {
            renderer.drawOrAppend(path);
        }
    }
}
