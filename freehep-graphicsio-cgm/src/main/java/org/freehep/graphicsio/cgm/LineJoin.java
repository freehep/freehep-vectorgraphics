// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * LineJoin TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/LineJoin.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class LineJoin extends CGMTag {

    public static final int UNSPECIFIED = 1;

    public static final int MITRE = 2;

    public static final int ROUND = 3;

    public static final int BEVEL = 4;

    private int type;

    public LineJoin() {
        super(5, 38, 3);
    }

    public LineJoin(int type) {
        this();
        this.type = type;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeIntegerIndex(type);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("LINEJOIN ");
        cgm.writeInteger(type);
    }
}
