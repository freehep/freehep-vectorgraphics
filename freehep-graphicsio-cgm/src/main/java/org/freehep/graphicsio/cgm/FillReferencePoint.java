// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * FillReferencePoint TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/FillReferencePoint.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class FillReferencePoint extends CGMTag {

    private Point2D p;

    public FillReferencePoint() {
        super(5, 31, 1);
    }

    public FillReferencePoint(Point2D p) {
        this();
        this.p = p;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writePoint(p);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("FILLREFPT ");
        cgm.writePoint(p);
    }
}
