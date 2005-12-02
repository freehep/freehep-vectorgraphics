// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * SelectObject TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/SelectObject.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class SelectObject extends EMFTag {

    private int index;

    SelectObject() {
        super(37, 1);
    }

    public SelectObject(int index) {
        this();
        this.index = index;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        SelectObject tag = new SelectObject(emf.readDWORD());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(index);
    }

    public String toString() {
        return super.toString() + "\n" + "  index: 0x"
                + Integer.toHexString(index);
    }

    public int getIndex() {
        return index;
    }
}
