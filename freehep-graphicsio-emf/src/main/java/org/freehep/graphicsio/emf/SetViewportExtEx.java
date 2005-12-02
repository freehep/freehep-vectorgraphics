// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Dimension;
import java.io.IOException;

/**
 * SetViewportExtEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/SetViewportExtEx.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class SetViewportExtEx extends EMFTag {

    private Dimension size;

    SetViewportExtEx() {
        super(11, 1);
    }

    public SetViewportExtEx(Dimension size) {
        this();
        this.size = size;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        SetViewportExtEx tag = new SetViewportExtEx(emf.readSIZEL());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeSIZEL(size);
    }

    public String toString() {
        return super.toString() + "\n" + "  size: " + size;
    }
}
