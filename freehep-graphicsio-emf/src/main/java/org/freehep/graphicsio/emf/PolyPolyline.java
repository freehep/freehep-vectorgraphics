// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import org.freehep.util.io.Tag;

/**
 * PolyPolyline TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/PolyPolyline.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class PolyPolyline
    extends EMFTag {

    private Rectangle bounds;
    private int start, end;
    private int[] numberOfPoints;
    private Point[][] points;

    PolyPolyline() {
        super(7, 1);
    }

    public PolyPolyline(Rectangle bounds, int start, int end, int[] numberOfPoints, Point[][] points) {
        this();
        this.bounds = bounds;
        this.start = start;
        this.end = Math.min(end, numberOfPoints.length-1);
        this.numberOfPoints = numberOfPoints;
        this.points = points;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
        throws IOException {

        Rectangle bounds = emf.readRECTL();
        int np = emf.readDWORD();
        int totalNumberOfPoints = emf.readDWORD();
        int[] pc = new int[np];
        Point[][] points = new Point[np][];
        for (int i=0; i<np; i++) {
            pc[i] = emf.readDWORD();
            points[i] = new Point[pc[i]];
        }
        for (int i=0; i<np; i++) {
            points[i] = emf.readPOINTL(pc[i]);
        }
        PolyPolyline tag = new PolyPolyline(bounds, 0, np-1, pc, points);
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
        emf.writeDWORD(end - start + 1);
        int c = 0;
        for (int i=start; i<end+1; i++) {
            c += numberOfPoints[i];
        }
        emf.writeDWORD(c);
        for (int i=start; i<end+1; i++) {
            emf.writeDWORD(numberOfPoints[i]);
        }
        for (int i=start; i<end+1; i++) {
            emf.writePOINTL(numberOfPoints[i], points[i]);
        }
    }

    public String toString() {
        return super.toString()+"\n"+
            "  bounds: "+bounds+"\n"+
            "  #polys: "+(end - start);
    }
}
