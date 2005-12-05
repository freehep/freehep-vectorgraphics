// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * TextFontIndex TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/TextFontIndex.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class TextFontIndex extends CGMTag {

    private int index;

    public TextFontIndex() {
        super(5, 10, 1);
    }

    public TextFontIndex(int index) {
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
