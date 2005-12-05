// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * VDCType TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/VDCType.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class VDCType extends CGMTag {

    public final static int INTEGER = 0;

    public final static int REAL = 1;

    private int type;

    public VDCType() {
        super(1, 3, 1);
    }

    public VDCType(int type) {
        this();
        this.type = type;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.setVDCReal((type == INTEGER) ? false : true);
        cgm.writeEnumerate(type);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.setVDCReal((type == INTEGER) ? false : true);
        cgm.print("VDCTYPE ");
        cgm.print((type == INTEGER) ? "INTEGER" : "REAL");
    }
}
