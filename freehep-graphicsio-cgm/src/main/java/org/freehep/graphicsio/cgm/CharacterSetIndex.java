// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * CharacterSetIndex TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CharacterSetIndex.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CharacterSetIndex extends CGMTag {

    private int index;

    public CharacterSetIndex() {
        super(5, 19, 1);
    }

    public CharacterSetIndex(int index) {
        this();
        this.index = index;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeIntegerIndex(index);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("CHARSETINDEX ");
        cgm.writeInteger(index);
    }
}
