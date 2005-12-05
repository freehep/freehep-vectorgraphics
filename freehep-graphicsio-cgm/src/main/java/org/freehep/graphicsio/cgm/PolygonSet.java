// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * PolygonSet TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/PolygonSet.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class PolygonSet extends CGMTag {

    public static final int INVISIBLE = 0;

    public static final int VISIBLE = 1;

    public static final int CLOSE_INVISIBLE = 2;

    public static final int CLOSE_VISIBLE = 3;

    private Point2D[] p;

    private int[] flags;

    public PolygonSet() {
        super(4, 8, 1);
    }

    public PolygonSet(Point2D[] p, int[] flags) {
        this();
        this.p = p;
        this.flags = flags;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        for (int i = 0; i < p.length; i++) {
            cgm.writePoint(p[i]);
            cgm.writeEnumerate(flags[i]);
        }
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.println("POLYGONSET");
        for (int i = 0; i < p.length; i++) {
            cgm.writePoint(p[i]);
            cgm.print(", ");
            switch (flags[i]) {
            default:
            case INVISIBLE:
                cgm.print("INVIS");
                break;
            case VISIBLE:
                cgm.print("VIS");
                break;
            case CLOSE_INVISIBLE:
                cgm.print("CLOSEINVIS");
                break;
            case CLOSE_VISIBLE:
                cgm.print("CLOSEVIS");
                break;
            }
            cgm.println();
        }
    }
}
