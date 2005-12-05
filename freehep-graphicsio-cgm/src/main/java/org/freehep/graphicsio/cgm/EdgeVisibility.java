// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * EdgeVisibility TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/EdgeVisibility.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class EdgeVisibility extends CGMTag {

    private boolean on;

    public EdgeVisibility() {
        super(5, 30, 1);
    }

    public EdgeVisibility(boolean on) {
        this();
        this.on = on;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeEnumerate((on) ? 1 : 0);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("EDGEVIS ");
        cgm.print((on) ? "ON" : "OFF");
    }
}
