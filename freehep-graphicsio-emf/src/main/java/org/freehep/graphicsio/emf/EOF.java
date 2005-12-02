// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * Rectangle TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EOF.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class EOF extends EMFTag {

    public EOF() {
        super(14, 1);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        /* int[] bytes = */ emf.readUnsignedByte(len);
        EOF tag = new EOF();
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(0); // # of palette entries
        emf.writeDWORD(0x10); // offset for palette
        // ... palette
        emf.writeDWORD(0x14); // offset to start of record
    }
}
