// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * PolyPolyline TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/PolyPolyline.java f2f1115939ae 2006/12/07 07:50:41 duns $
 */
public class PolyPolyline extends EMFTag {

    private Rectangle bounds;

    private int start, end;

    private int[] numberOfPoints;

    private Point[][] points;

    public PolyPolyline() {
        super(7, 1);
    }

    public PolyPolyline(Rectangle bounds, int start, int end,
            int[] numberOfPoints, Point[][] points) {
        this();
        this.bounds = bounds;
        this.start = start;
        this.end = Math.min(end, numberOfPoints.length - 1);
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
            points[i] = emf.readPOINTL(pc[i]);
        }
        PolyPolyline tag = new PolyPolyline(bounds, 0, np - 1, pc, points);
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
        emf.writeDWORD(end - start + 1);
        int c = 0;
        for (int i = start; i < end + 1; i++) {
            c += numberOfPoints[i];
        }
        emf.writeDWORD(c);
        for (int i = start; i < end + 1; i++) {
            emf.writeDWORD(numberOfPoints[i]);
        }
        for (int i = start; i < end + 1; i++) {
            emf.writePOINTL(numberOfPoints[i], points[i]);
        }
    }

    public String toString() {
        return super.toString() + "\n" + "  bounds: " + bounds + "\n"
                + "  #polys: " + (end - start);
    }
}
