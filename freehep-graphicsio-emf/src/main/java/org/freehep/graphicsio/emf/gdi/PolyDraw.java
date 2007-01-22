// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * PolyDraw TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/PolyDraw.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class PolyDraw extends EMFTag implements EMFConstants {

    private Rectangle bounds;

    private Point[] points;

    private byte[] types;

    public PolyDraw() {
        super(56, 1);
    }

    public PolyDraw(Rectangle bounds, Point[] points, byte[] types) {
        this();
        this.bounds = bounds;
        this.points = points;
        this.types = types;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        int n;
        return new PolyDraw(
            emf.readRECTL(),
            emf.readPOINTL(n = emf.readDWORD()),
            emf.readBYTE(n));
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
        emf.writeDWORD(points.length);
        emf.writePOINTL(points);
        emf.writeBYTE(types);
    }

    public String toString() {
        return super.toString() +
            "\n  bounds: " + bounds +
            "\n  #points: " + points.length;
    }
}
