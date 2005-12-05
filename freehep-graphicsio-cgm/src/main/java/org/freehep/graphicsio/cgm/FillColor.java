// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.Color;
import java.io.IOException;

/**
 * FillColor TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/FillColor.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class FillColor extends CGMTag {

    private Color color;

    public FillColor() {
        super(5, 23, 1);
    }

    public FillColor(Color color) {
        this();
        this.color = color;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeColor(color);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("FILLCOLR ");
        cgm.writeColor(color);
    }
}
