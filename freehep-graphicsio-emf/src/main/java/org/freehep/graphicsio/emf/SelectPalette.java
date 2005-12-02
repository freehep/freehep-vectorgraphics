// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * SelectPalette TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/SelectPalette.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class SelectPalette extends EMFTag {

    private int index;

    SelectPalette() {
        super(48, 1);
    }

    public SelectPalette(int index) {
        this();
        this.index = index;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        SelectPalette tag = new SelectPalette(emf.readDWORD());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(index);
    }

    public String toString() {
        return super.toString() + "\n" + "  index: 0x"
                + Integer.toHexString(index);
    }
}
