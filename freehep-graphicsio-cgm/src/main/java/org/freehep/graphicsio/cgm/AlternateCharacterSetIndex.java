// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * AlternateCharacterSetIndex TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/AlternateCharacterSetIndex.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class AlternateCharacterSetIndex extends CGMTag {

    private int index;

    public AlternateCharacterSetIndex() {
        super(5, 20, 1);
    }

    public AlternateCharacterSetIndex(int index) {
        this();
        this.index = index;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeIntegerIndex(index);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("ALTCHARSETINDEX ");
        cgm.writeInteger(index);
    }
}
