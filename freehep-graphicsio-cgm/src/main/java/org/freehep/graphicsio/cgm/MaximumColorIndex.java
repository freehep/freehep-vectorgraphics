// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * MaximumColorIndex TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/MaximumColorIndex.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class MaximumColorIndex extends CGMTag {

    private int index;

    public MaximumColorIndex() {
        super(1, 9, 1);
    }

    public MaximumColorIndex(int index) {
        this();
        this.index = index;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {

        cgm.writeColorIndex(index);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("MAXCOLRINDEX ");
        cgm.writeInteger(127); // FIXME
    }
}
