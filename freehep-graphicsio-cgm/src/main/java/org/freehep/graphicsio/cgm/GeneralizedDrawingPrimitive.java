// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * GeneralizedDrawingPrimitive TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/GeneralizedDrawingPrimitive.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class GeneralizedDrawingPrimitive extends CGMTag {

    private int gdp;

    private Point2D[] p;

    private byte[] data;

    public GeneralizedDrawingPrimitive() {
        super(4, 10, 1);
    }

    public GeneralizedDrawingPrimitive(int gdp, Point2D[] p, byte[] data) {
        this();
        this.gdp = gdp;
        this.p = p;
        this.data = data;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeInteger(gdp);
        cgm.writeInteger(p.length);
        for (int i = 0; i < p.length; i++) {
            cgm.writePoint(p[i]);
        }
        cgm.writeData(data);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("GDP ");
        cgm.indent();
        cgm.writeInteger(gdp);
        cgm.println();
        for (int i = 0; i < p.length; i++) {
            cgm.writePoint(p[i]);
            cgm.print(", ");
        }
        cgm.println();
        cgm.writeData(data);
        cgm.println();
        cgm.outdent();
    }
}
