// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

/**
 * Polygon16 TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/Polygon16.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class Polygon16 extends EMFTag {

    private Rectangle bounds;

    private int numberOfPoints;

    private Point[] points;

    Polygon16() {
        super(86, 1);
    }

    public Polygon16(Rectangle bounds, int numberOfPoints, Point[] points) {
        this();
        this.bounds = bounds;
        this.numberOfPoints = numberOfPoints;
        this.points = points;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        Rectangle r = emf.readRECTL();
        int n = emf.readDWORD();
        Polygon16 tag = new Polygon16(r, n, emf.readPOINTS(n));
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
        emf.writeDWORD(numberOfPoints);
        emf.writePOINTS(numberOfPoints, points);
    }

    public String toString() {
        return super.toString() + "\n" + "  bounds: " + bounds + "\n"
                + "  #points: " + numberOfPoints;
    }
}
