// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * RestrictedText TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/RestrictedText.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class RestrictedText extends Text {

    protected double deltaWidth, deltaHeight;

    public RestrictedText() {
        super(4, 5, 1);
    }

    public RestrictedText(double deltaWidth, double deltaHeight, Point2D p,
            String text) {
        this();
        this.deltaWidth = deltaWidth;
        this.deltaHeight = deltaHeight;
        this.p = p;
        this.text = text;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeVDC(deltaWidth);
        cgm.writeVDC(deltaHeight);
        super.write(tagID, cgm);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("RESTRTEXT ");
        cgm.writeVDC(deltaWidth);
        cgm.print(", ");
        cgm.writeVDC(deltaHeight);
        cgm.print(", ");
        cgm.writePoint(p);
        cgm.print(", ");
        writeTextPiece(cgm);
    }
}
