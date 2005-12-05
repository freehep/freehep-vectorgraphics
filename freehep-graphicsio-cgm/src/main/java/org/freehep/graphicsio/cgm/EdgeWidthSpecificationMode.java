// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * EdgeWidthSpecificationMode TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/EdgeWidthSpecificationMode.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class EdgeWidthSpecificationMode extends CGMTag {

    public static final int ABSOLUTE = 0;

    public static final int SCALED = 1;

    public static final int FRACTIONAL = 2;

    public static final int MM = 3;

    private int mode;

    public EdgeWidthSpecificationMode() {
        super(2, 5, 1);
    }

    public EdgeWidthSpecificationMode(int mode) {
        this();
        this.mode = mode;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.setEdgeWidthSpecificationMode(mode);
        cgm.writeEnumerate(mode);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.setEdgeWidthSpecificationMode(mode);
        cgm.print("EDGEWIDTHMODE ");
        switch (mode) {
        default:
        case ABSOLUTE:
            cgm.print("ABS");
            break;
        case SCALED:
            cgm.print("SCALED");
            break;
        case FRACTIONAL:
            cgm.print("FRACTIONAL");
            break;
        case MM:
            cgm.print("METRIC");
            break;
        }
    }
}
