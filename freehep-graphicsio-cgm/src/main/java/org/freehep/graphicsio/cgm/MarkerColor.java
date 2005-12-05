// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.Color;
import java.io.IOException;

/**
 * MarkerColor TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/MarkerColor.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class MarkerColor extends CGMTag {

    private Color color;

    public MarkerColor() {
        super(5, 8, 1);
    }

    public MarkerColor(Color color) {
        this();
        this.color = color;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeColor(color);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("MARKERCOLR ");
        cgm.writeColor(color);
    }
}
