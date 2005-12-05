// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * FillBundleIndex TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/FillBundleIndex.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class FillBundleIndex extends CGMTag {

    private int index;

    public FillBundleIndex() {
        super(5, 21, 1);
    }

    public FillBundleIndex(int index) {
        this();
        this.index = index;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeIntegerIndex(index);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("FILLINDEX ");
        cgm.writeInteger(index);
    }
}
