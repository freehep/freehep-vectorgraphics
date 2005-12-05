// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * EdgeWidth TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/EdgeWidth.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class EdgeWidth extends CGMTag {

    private double width;

    public EdgeWidth() {
        super(5, 28, 1);
    }

    public EdgeWidth(double width) {
        this();
        this.width = width;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        if (cgm.getEdgeWidthSpecificationMode() == EdgeWidthSpecificationMode.ABSOLUTE) {
            cgm.writeVDC(width);
        } else {
            cgm.writeReal(width);
        }
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("EDGEWIDTH ");
        if (cgm.getEdgeWidthSpecificationMode() == EdgeWidthSpecificationMode.ABSOLUTE) {
            cgm.writeVDC(width);
        } else {
            cgm.writeReal(width);
        }
    }
}
