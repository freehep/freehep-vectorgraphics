// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.Color;
import java.io.IOException;

/**
 * AuxiliaryColor TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/AuxiliaryColor.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class AuxiliaryColor extends CGMTag {

    private Color color;

    public AuxiliaryColor() {
        super(3, 3, 1);
    }

    public AuxiliaryColor(Color color) {
        this();
        this.color = color;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeColor(color);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("AUXCOLR ");
        cgm.writeColor(color);
    }
}
