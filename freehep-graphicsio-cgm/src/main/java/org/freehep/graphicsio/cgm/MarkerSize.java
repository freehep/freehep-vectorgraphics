// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * MarkerSize TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/MarkerSize.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class MarkerSize extends CGMTag {

    private double size;

    public MarkerSize() {
        super(5, 7, 1);
    }

    public MarkerSize(int size) {
        this();
        this.size = size;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        if (cgm.getMarkerSizeSpecificationMode() == MarkerSizeSpecificationMode.ABSOLUTE) {
            cgm.writeVDC(size);
        } else {
            cgm.writeReal(size);
        }
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("MARKERSIZE ");
        if (cgm.getMarkerSizeSpecificationMode() == MarkerSizeSpecificationMode.ABSOLUTE) {
            cgm.writeVDC(size);
        } else {
            cgm.writeReal(size);
        }
    }
}
