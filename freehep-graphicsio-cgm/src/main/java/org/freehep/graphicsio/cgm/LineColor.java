// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.Color;
import java.io.IOException;

/**
 * LineBundleIndex TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/LineColor.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class LineColor extends CGMTag {

    private Color color;

    public LineColor() {
        super(5, 4, 1);
    }

    public LineColor(Color color) {
        this();
        this.color = color;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeColor(color);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("LINECOLR ");
        cgm.writeColor(color);
    }
}
