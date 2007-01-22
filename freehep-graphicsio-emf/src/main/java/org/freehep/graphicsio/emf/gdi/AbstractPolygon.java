// Copyright 2007, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFOutputStream;

import java.awt.Rectangle;
import java.awt.Point;
import java.io.IOException;

/**
 * @author Steffen Greiffenberg
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/AbstractPolygon.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public abstract class AbstractPolygon extends EMFTag {

    private Rectangle bounds;

    private int numberOfPoints;

    private Point[] points;

    protected AbstractPolygon(int id, int version) {
        super(id, version);
    }

    protected AbstractPolygon(int id, int version, Rectangle bounds, int numberOfPoints, Point[] points) {
        super(id, version);
        this.bounds = bounds;
        this.numberOfPoints = numberOfPoints;
        this.points = points;
    }


    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
        emf.writeDWORD(numberOfPoints);
        emf.writePOINTL(numberOfPoints, points);
    }

    public String toString() {
        return super.toString() + "\n  bounds: " + bounds + "\n"
                + "  #points: " + numberOfPoints;
    }

    protected Rectangle getBounds() {
        return bounds;
    }

    protected int getNumberOfPoints() {
        return numberOfPoints;
    }

    protected Point[] getPoints() {
        return points;
    }
}
