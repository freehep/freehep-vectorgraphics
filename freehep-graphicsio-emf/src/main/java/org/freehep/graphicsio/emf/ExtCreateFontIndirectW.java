// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * ExtCreateFontIndirectW TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/ExtCreateFontIndirectW.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class ExtCreateFontIndirectW extends EMFTag {

    private int index;

    private ExtLogFontW font;

    ExtCreateFontIndirectW() {
        super(82, 1);
    }

    public ExtCreateFontIndirectW(int index, ExtLogFontW font) {
        this();
        this.index = index;
        this.font = font;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        ExtCreateFontIndirectW tag = new ExtCreateFontIndirectW(
                emf.readDWORD(), new ExtLogFontW(emf));
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(index);
        font.write(emf);
    }

    public String toString() {
        return super.toString() + "\n" + "  index: 0x"
                + Integer.toHexString(index) + "\n" + font.toString();
    }
}
