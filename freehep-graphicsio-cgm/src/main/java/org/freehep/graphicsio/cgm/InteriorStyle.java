// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * InteriorStyle TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/InteriorStyle.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class InteriorStyle extends CGMTag {

    public static final int HOLLOW = 0;

    public static final int SOLID = 1;

    public static final int PATTERN = 2;

    public static final int HATCH = 3;

    public static final int EMPTY = 4;

    public static final int GEOMETRIC_PATTERN = 5;

    public static final int INTERPOLATED = 6;

    private int style;

    public InteriorStyle() {
        super(5, 22, 1);
    }

    public InteriorStyle(int style) {
        this();
        this.style = style;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeEnumerate(style);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("INTSTYLE ");
        switch (style) {
        default:
        case HOLLOW:
            cgm.print("HOLLOW");
            break;
        case SOLID:
            cgm.print("SOLID");
            break;
        case PATTERN:
            cgm.print("PAT");
            break;
        case HATCH:
            cgm.print("HATCH");
            break;
        case EMPTY:
            cgm.print("EMPTY");
            break;
        case GEOMETRIC_PATTERN:
            cgm.print("GEOPAT");
            break;
        case INTERPOLATED:
            cgm.print("INTERP");
            break;
        }
    }
}