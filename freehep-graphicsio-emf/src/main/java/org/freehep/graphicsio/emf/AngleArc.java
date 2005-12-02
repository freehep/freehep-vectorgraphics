// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Point;
import java.io.IOException;

/**
 * AngleArc TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/AngleArc.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class AngleArc extends EMFTag {

    private Point center;

    private int radius;

    private float startAngle, sweepAngle;

    AngleArc() {
        super(41, 1);
    }

    public AngleArc(Point center, int radius, float startAngle, float sweepAngle) {
        this();
        this.center = center;
        this.radius = radius;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        AngleArc tag = new AngleArc(emf.readPOINTL(), emf.readDWORD(), emf
                .readFLOAT(), emf.readFLOAT());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writePOINTL(center);
        emf.writeDWORD(radius);
        emf.writeFLOAT(startAngle);
        emf.writeFLOAT(sweepAngle);
    }

    public String toString() {
        return super.toString() + "\n" + "  center: " + center + "\n"
                + "  radius: " + radius + "\n" + "  startAngle: " + startAngle
                + "\n" + "  sweepAngle: " + sweepAngle;
    }
}
