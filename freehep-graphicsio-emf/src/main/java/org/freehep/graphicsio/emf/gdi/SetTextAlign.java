// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;

import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * SetTextAlign TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/SetTextAlign.java 11783e27e55b 2007/01/15 16:30:03 duns $
 */
public class SetTextAlign extends EMFTag implements EMFConstants {

    private int mode;

    public SetTextAlign() {
        super(22, 1);
    }

    public SetTextAlign(int mode) {
        this();
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        SetTextAlign tag = new SetTextAlign(emf.readDWORD());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(mode);
    }

    public int getMode() {
        return mode;
    }

    public String toString() {
        return super.toString() + "\n" + "  mode: " + mode;
    }
}
