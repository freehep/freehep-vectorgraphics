// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * EdgeBundleIndex TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/EdgeBundleIndex.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class EdgeBundleIndex extends CGMTag {

    private int index;

    public EdgeBundleIndex() {
        super(5, 26, 1);
    }

    public EdgeBundleIndex(int index) {
        this();
        this.index = index;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeIntegerIndex(index);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("EDGEINDEX ");
        cgm.writeInteger(index);
    }
}
