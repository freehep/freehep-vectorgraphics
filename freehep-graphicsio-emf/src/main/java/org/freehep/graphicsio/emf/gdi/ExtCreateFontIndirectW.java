// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * ExtCreateFontIndirectW TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/ExtCreateFontIndirectW.java 11783e27e55b 2007/01/15 16:30:03 duns $
 */
public class ExtCreateFontIndirectW extends EMFTag {

    private int index;

    private ExtLogFontW font;

    public ExtCreateFontIndirectW() {
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

    public int getIndex() {
        return index;
    }

    public ExtLogFontW getFont() {
        return font;
    }

    public String toString() {
        return super.toString() + "\n" + "  index: 0x"
                + Integer.toHexString(index) + "\n" + font.toString();
    }
}
