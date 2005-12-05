// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * ColorIndexPrecision TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/ColorIndexPrecision.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class ColorIndexPrecision extends CGMTag {

    // FIXME: incomplete
    public ColorIndexPrecision() {
        super(1, 8, 1);
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {

        cgm.setColorIndexPrecision(24);
        cgm.writeInteger(24);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.setColorIndexPrecision(24);
        cgm.print("COLRINDEXPREC ");
        cgm.writeInteger(Short.MAX_VALUE);
    }
}
