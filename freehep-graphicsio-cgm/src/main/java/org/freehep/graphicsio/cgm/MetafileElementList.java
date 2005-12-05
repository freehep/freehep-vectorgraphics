// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * MetafileElementList TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/MetafileElementList.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class MetafileElementList extends CGMTag {

    // FIXME: not complete
    public static final int DRAWING_SET = 0;

    public static final int DRAWING_PLUS_SET = 1;

    public static final int VERSION_2_SET = 2;

    public static final int EXTENDED_PRIMITIVES_SET = 3;

    public static final int VERSION_2_GKSM_SET = 4;

    public static final int VERSION_3_SET = 5;

    public static final int VERSION_4_SET = 6;

    private int set;

    public MetafileElementList() {
        super(1, 11, 1);
    }

    public MetafileElementList(int set) {
        this();
        this.set = set;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {

        cgm.writeInteger(1);
        cgm.writeIntegerIndex(-1);
        cgm.writeIntegerIndex(set);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("MFELEMLIST ");
        switch (set) {
        default:
        case DRAWING_SET:
            cgm.writeString("DRAWINGSET");
            break;
        case DRAWING_PLUS_SET:
            cgm.writeString("DRAWINGPLUS");
            break;
        case VERSION_2_SET:
            cgm.writeString("VERSION2");
            break;
        case EXTENDED_PRIMITIVES_SET:
            cgm.writeString("EXTDPRIM");
            break;
        case VERSION_2_GKSM_SET:
            cgm.writeString("VERSION2GKSM");
            break;
        case VERSION_3_SET:
            cgm.writeString("VERSION3");
            break;
        case VERSION_4_SET:
            cgm.writeString("VERSION4");
            break;
        }
    }
}
