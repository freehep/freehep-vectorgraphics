// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * LineCap TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/LineCap.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class LineCap extends CGMTag {

    public static final int UNSPECIFIED = 1;

    public static final int BUTT = 2;

    public static final int ROUND = 3;

    public static final int MATCH = 3; // for DASH

    public static final int SQUARE = 4;

    public static final int TRIANGLE = 5;

    private int cap;

    private int dash;

    public LineCap() {
        super(5, 37, 3);
    }

    public LineCap(int cap) {
        this(cap, BUTT);
    }

    public LineCap(int cap, int dash) {
        this();
        this.cap = cap;
        this.dash = dash;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeIntegerIndex(cap);
        cgm.writeIntegerIndex(dash);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("LINECAP ");
        cgm.writeInteger(cap);
        cgm.print(", ");
        cgm.writeInteger(dash);
    }
}
