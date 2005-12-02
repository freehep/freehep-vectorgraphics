// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

/**
 * PolyPolygon16 TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/PolyPolygon16.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class PolyPolygon16 extends EMFTag {

    private Rectangle bounds;

    private int numberOfPolys;

    private int[] numberOfPoints;

    private Point[][] points;

    PolyPolygon16() {
        super(91, 1);
    }

    public PolyPolygon16(Rectangle bounds, int numberOfPolys,
            int[] numberOfPoints, Point[][] points) {
        this();
        this.bounds = bounds;
        this.numberOfPolys = numberOfPolys;
        this.numberOfPoints = numberOfPoints;
        this.points = points;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        Rectangle bounds = emf.readRECTL();
        int np = emf.readDWORD();
        /* int totalNumberOfPoints = */ emf.readDWORD();
        int[] pc = new int[np];
        Point[][] points = new Point[np][];
        for (int i = 0; i < np; i++) {
            pc[i] = emf.readDWORD();
            points[i] = new Point[pc[i]];
        }
        for (int i = 0; i < np; i++) {
            points[i] = emf.readPOINTS(pc[i]);
        }
        PolyPolygon16 tag = new PolyPolygon16(bounds, np, pc, points);
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
        emf.writeDWORD(numberOfPolys);
        int c = 0;
        for (int i = 0; i < numberOfPolys; i++) {
            c += numberOfPoints[i];
        }
        emf.writeDWORD(c);
        for (int i = 0; i < numberOfPolys; i++) {
            emf.writeDWORD(numberOfPoints[i]);
        }
        for (int i = 0; i < numberOfPolys; i++) {
            emf.writePOINTS(numberOfPoints[i], points[i]);
        }
    }

    public String toString() {
        return super.toString() + "\n" + "  bounds: " + bounds + "\n"
                + "  #polys: " + numberOfPolys;
    }
}
