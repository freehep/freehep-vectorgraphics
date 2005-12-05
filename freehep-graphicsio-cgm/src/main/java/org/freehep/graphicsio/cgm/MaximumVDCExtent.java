// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * MaximumVDCExtent TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/MaximumVDCExtent.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class MaximumVDCExtent extends CGMTag {

    private Point2D p0, p1;

    public MaximumVDCExtent() {
        super(1, 17, 2);
    }

    public MaximumVDCExtent(Point2D p0, Point2D p1) {
        this();
        this.p0 = p0;
        this.p1 = p1;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writePoint(p0);
        cgm.writePoint(p1);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("MAXVDCEXT ");
        cgm.writePoint(p0);
        cgm.print(", ");
        cgm.writePoint(p1);
    }
}
