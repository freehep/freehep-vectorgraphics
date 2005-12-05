// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * MarkerType TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/MarkerType.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class MarkerType extends CGMTag {

    public static final int DOT = 1;

    public static final int PLUS = 2;

    public static final int ASTERISK = 3;

    public static final int CIRCLE = 4;

    public static final int CROSS = 5;

    private int type;

    public MarkerType() {
        super(5, 6, 1);
    }

    public MarkerType(int type) {
        this();
        this.type = type;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeIntegerIndex(type);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("MARKERTYPE ");
        cgm.writeInteger(type);
    }
}
