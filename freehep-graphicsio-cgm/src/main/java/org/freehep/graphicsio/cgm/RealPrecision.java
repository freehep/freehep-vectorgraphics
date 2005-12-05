// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * RealPrecision TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/RealPrecision.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class RealPrecision extends CGMTag {

    // FIXME: not complete
    public RealPrecision() {
        super(1, 5, 1);
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {

        cgm.setRealPrecision(false, true);
        cgm.writeEnumerate(0); // floating
        cgm.writeInteger(12); // double exp
        cgm.writeInteger(52); // double fract
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.setRealPrecision(false, true);
        cgm.print("REALPREC ");
        cgm.writeReal(Double.MIN_VALUE);
        cgm.print(", ");
        cgm.writeReal(Double.MAX_VALUE);
        cgm.print(", ");
        cgm.writeInteger(20); // FIXME: check
    }
}
