// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * LineType TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/LineType.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class LineType extends CGMTag {

    public static final int SOLID = 1;

    public static final int DASH = 2;

    public static final int DOT = 3;

    public static final int DASH_DOT = 4;

    public static final int DASH_DOT_DOT = 5;

    private int type;

    public LineType() {
        super(5, 2, 1);
    }

    public LineType(int type) {
        this();
        this.type = type;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeIntegerIndex(type);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("LINETYPE ");
        cgm.writeInteger(type);
    }
}
