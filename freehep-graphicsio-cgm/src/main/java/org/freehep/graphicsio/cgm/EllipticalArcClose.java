// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * EllipticalArcClose TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/EllipticalArcClose.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class EllipticalArcClose extends EllipticalArc {

    public static final int PIE = 0;

    public static final int CHORD = 1;

    private int closure;

    public EllipticalArcClose() {
        super(4, 19, 1);
    }

    public EllipticalArcClose(Point2D p, Point2D c1, Point2D c2,
            Point2D delta0, Point2D delta1, int closure) {
        this();
        this.p = p;
        this.c1 = c1;
        this.c2 = c2;
        this.delta0 = delta0;
        this.delta1 = delta1;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        super.write(tagID, cgm);
        cgm.writeEnumerate(closure);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("ELLIPARCCLOSE ");
        writeEllipseSpec(cgm);
        writeArcBounds(cgm);
        cgm.print((closure == PIE) ? " PIE" : " CHORD");
    }
}
