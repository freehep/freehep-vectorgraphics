// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * SetMapperFlags TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/SetMapperFlags.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class SetMapperFlags extends EMFTag {

    private int flags;

    public SetMapperFlags() {
        super(16, 1);
    }

    public SetMapperFlags(int flags) {
        this();
        this.flags = flags;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new SetMapperFlags(emf.readDWORD());
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(flags);
    }

    public String toString() {
        return super.toString() + "\n  flags: " + flags;
    }
}
