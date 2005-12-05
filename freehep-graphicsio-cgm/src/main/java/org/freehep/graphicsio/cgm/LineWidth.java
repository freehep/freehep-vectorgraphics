// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * LineWidth TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/LineWidth.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class LineWidth extends CGMTag {

    private double width;

    public LineWidth() {
        super(5, 3, 1);
    }

    public LineWidth(double width) {
        this();
        this.width = width;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        if (cgm.getLineWidthSpecificationMode() == LineWidthSpecificationMode.ABSOLUTE) {
            cgm.writeVDC(width);
        } else {
            cgm.writeReal(width);
        }
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("LINEWIDTH ");
        if (cgm.getLineWidthSpecificationMode() == LineWidthSpecificationMode.ABSOLUTE) {
            cgm.writeVDC(width);
        } else {
            cgm.writeReal(width);
        }
    }
}
