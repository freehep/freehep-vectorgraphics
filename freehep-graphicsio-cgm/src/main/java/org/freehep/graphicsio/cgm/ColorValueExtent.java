// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.Color;
import java.io.IOException;

/**
 * ColorValueExtent TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/ColorValueExtent.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class ColorValueExtent extends CGMTag {

    Color minColor, maxColor;

    public ColorValueExtent() {
        super(1, 10, 1);
    }

    // FIXME: only RGB ColorModel allowed
    public ColorValueExtent(Color minColor, Color maxColor) {
        this();
        this.minColor = minColor;
        this.maxColor = maxColor;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {

        cgm.writeColorDirect(minColor);
        cgm.writeColorDirect(maxColor);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("COLRVALUEEXT ");
        cgm.writeColor(minColor);
        cgm.print(", ");
        cgm.writeColor(maxColor);
    }
}
