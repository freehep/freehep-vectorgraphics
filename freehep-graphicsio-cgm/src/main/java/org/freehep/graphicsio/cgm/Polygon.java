// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Polygon TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/Polygon.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class Polygon extends Polyline {

    public Polygon() {
        super(4, 7, 1);
    }

    public Polygon(Point2D[] p) {
        this();
        this.p = p;
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.println("POLYGON");
        writePointList(cgm);
    }
}
