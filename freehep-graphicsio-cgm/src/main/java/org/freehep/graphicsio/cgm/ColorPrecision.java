// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * ColorPrecision TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/ColorPrecision.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class ColorPrecision extends CGMTag {

    // FIXME: not complete
    public ColorPrecision() {
        super(1, 7, 1);
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {

        cgm.setDirectColorPrecision(24);
        cgm.writeInteger(24);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.setDirectColorPrecision(24);
        cgm.print("COLRPREC ");
        cgm.writeInteger(255);
    }
}
