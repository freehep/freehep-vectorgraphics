// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * InteriorStyleSpecificationMode TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: InteriorStyleSpecificationMode.java 4436 2001-11-29 01:45:36Z
 *          duns $
 */
public class InteriorStyleSpecificationMode extends CGMTag {

    public static final int ABSOLUTE = 0;

    public static final int SCALED = 1;

    public static final int FRACTIONAL = 2;

    public static final int MM = 3;

    private int mode;

    public InteriorStyleSpecificationMode() {
        super(2, 16, 3);
    }

    public InteriorStyleSpecificationMode(int mode) {
        this();
        this.mode = mode;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.setInteriorStyleSpecificationMode(mode);
        cgm.writeEnumerate(mode);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.setInteriorStyleSpecificationMode(mode);
        cgm.print("INTSTYLEMODE ");
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
