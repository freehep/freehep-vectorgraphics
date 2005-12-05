// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Ellipse TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/Ellipse.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class Ellipse extends CGMTag {

    protected Point2D p, c1, c2;

    public Ellipse() {
        super(4, 17, 1);
    }

    public Ellipse(Point2D p, Point2D c1, Point2D c2) {
        this();
        this.p = p;
        this.c1 = c1;
        this.c2 = c2;
    }

    protected Ellipse(int elementClass, int elementID, int version) {
        super(elementClass, elementID, version);
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writePoint(p);
        cgm.writePoint(c1);
        cgm.writePoint(c2);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("ELLIPSE ");
        writeEllipseSpec(cgm);
    }

    protected void writeEllipseSpec(CGMWriter cgm) throws IOException {
        cgm.writePoint(p);
        cgm.print(", ");
        cgm.writePoint(c1);
        cgm.print(", ");
        cgm.writePoint(c2);
    }
}
