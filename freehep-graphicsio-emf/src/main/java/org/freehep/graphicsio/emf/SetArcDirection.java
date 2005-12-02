// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * SetArcDirection TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/SetArcDirection.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class SetArcDirection extends EMFTag implements EMFConstants {

    private int direction;

    SetArcDirection() {
        super(57, 1);
    }

    public SetArcDirection(int direction) {
        this();
        this.direction = direction;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        SetArcDirection tag = new SetArcDirection(emf.readDWORD());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(direction);
    }

    public String toString() {
        return super.toString() + "\n" + "  direction: " + direction;
    }
}
