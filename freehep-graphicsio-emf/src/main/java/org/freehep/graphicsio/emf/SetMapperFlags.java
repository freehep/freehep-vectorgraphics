// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * SetMapperFlags TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/SetMapperFlags.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class SetMapperFlags extends EMFTag {

    private int flags;

    SetMapperFlags() {
        super(16, 1);
    }

    public SetMapperFlags(int flags) {
        this();
        this.flags = flags;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        SetMapperFlags tag = new SetMapperFlags(emf.readDWORD());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(flags);
    }

    public String toString() {
        return super.toString() + "\n" + "  flags: " + flags;
    }
}
