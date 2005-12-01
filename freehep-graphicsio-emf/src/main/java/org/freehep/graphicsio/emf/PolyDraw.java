// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import org.freehep.util.io.Tag;

/**
 * PolyDraw TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/PolyDraw.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class PolyDraw
    extends EMFTag implements EMFConstants {

    private Rectangle bounds;
    private Point[] points;
    private byte[] types;

    PolyDraw() {
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
        PolyDraw tag = new PolyDraw(emf.readRECTL(),
                                    emf.readPOINTL(n = emf.readDWORD()),
                                    emf.readBYTE(n));
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
        emf.writeDWORD(points.length);
        emf.writePOINTL(points);
        emf.writeBYTE(types);
    }

    public String toString() {
        return super.toString()+"\n"+
            "  bounds: "+bounds+"\n"+
            "  #points: "+points.length;
    }
}
