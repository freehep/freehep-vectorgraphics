// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * CircularArcCentre TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CircularArcCentre.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CircularArcCentre extends CGMTag {

    protected Point2D p, dps, dpe;

    protected double radius;

    public CircularArcCentre() {
        super(4, 15, 1);
    }

    public CircularArcCentre(Point2D p, Point2D dps, Point2D dpe, double radius) {
        this();
        this.p = p;
        this.dps = dps;
        this.dpe = dpe;
        this.radius = radius;
    }

    protected CircularArcCentre(int elementClass, int elementID, int version) {
        super(elementClass, elementID, version);
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        // FIXME: should these be points?
        cgm.writePoint(p);
        cgm.writeVDC(dps.getX());
        cgm.writeVDC(dps.getY());
        cgm.writeVDC(dpe.getX());
        cgm.writeVDC(dpe.getY());
        cgm.writeVDC(radius);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("ARCCTR ");
        writeArcSpec(cgm);
    }

    protected void writeArcSpec(CGMWriter cgm) throws IOException {
        cgm.writePoint(p);
        cgm.print(", ");
        cgm.writePoint(dps);
        cgm.print(", ");
        cgm.writePoint(dpe);
    }
}
