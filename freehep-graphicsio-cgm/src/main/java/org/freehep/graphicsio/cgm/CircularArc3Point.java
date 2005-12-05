// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * CircularArc3Point TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CircularArc3Point.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CircularArc3Point extends CGMTag {

    protected Point2D ps, pi, pe;

    public CircularArc3Point() {
        super(4, 13, 1);
    }

    public CircularArc3Point(Point2D ps, Point2D pi, Point2D pe) {
        this();
        this.ps = ps;
        this.pi = pi;
        this.pe = pe;
    }

    protected CircularArc3Point(int elementClass, int elementID, int version) {
        super(elementClass, elementID, version);
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writePoint(ps);
        cgm.writePoint(pi);
        cgm.writePoint(pe);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("ARC3PT ");
        writeArcSpec(cgm);
    }

    protected void writeArcSpec(CGMWriter cgm) throws IOException {
        cgm.writePoint(ps);
        cgm.print(", ");
        cgm.writePoint(pi);
        cgm.print(", ");
        cgm.writePoint(pe);
    }
}
