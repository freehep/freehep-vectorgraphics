// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Dimension;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * SetViewportExtEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/SetViewportExtEx.java 63c8d910ece7 2007/01/20 15:30:50 duns $
 */
public class SetViewportExtEx extends EMFTag {

    private Dimension size;

    public SetViewportExtEx() {
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

    public Dimension getSize() {
        return size;
    }
}
