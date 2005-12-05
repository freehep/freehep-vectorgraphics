// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * TextBundleIndex TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/TextBundleIndex.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class TextBundleIndex extends CGMTag {

    private int index;

    public TextBundleIndex() {
        super(5, 9, 1);
    }

    public TextBundleIndex(int index) {
        this();
        this.index = index;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeIntegerIndex(index);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("TEXTINDEX ");
        cgm.writeInteger(index);
    }
}
