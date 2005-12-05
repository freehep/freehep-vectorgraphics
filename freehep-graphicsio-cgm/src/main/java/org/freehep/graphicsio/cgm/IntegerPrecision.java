// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * IntegerPrecision TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/IntegerPrecision.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class IntegerPrecision extends CGMTag {

    // FIXME: not complete
    public IntegerPrecision() {
        super(1, 4, 1);
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {

        cgm.setIntegerPrecision(16);
        cgm.writeInteger(16);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.setIntegerPrecision(16);
        cgm.print("INTEGERPREC ");
        cgm.writeInteger(Short.MIN_VALUE);
        cgm.print(", ");
        cgm.writeInteger(Short.MAX_VALUE);
    }
}
