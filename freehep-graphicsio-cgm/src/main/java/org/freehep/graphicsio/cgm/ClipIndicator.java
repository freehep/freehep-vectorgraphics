// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * ClipIndicator TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/ClipIndicator.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class ClipIndicator extends CGMTag {

    private boolean on;

    public ClipIndicator() {
        super(3, 4, 1);
    }

    public ClipIndicator(boolean on) {
        this();
        this.on = on;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeEnumerate((on) ? 1 : 0);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("CLIP ");
        cgm.print((on) ? "ON" : "OFF");
    }
}
