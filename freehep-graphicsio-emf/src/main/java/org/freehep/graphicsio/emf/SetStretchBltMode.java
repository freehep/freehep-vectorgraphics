// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

import org.freehep.util.io.Tag;

/**
 * SetStretchBltMode TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/SetStretchBltMode.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class SetStretchBltMode
    extends EMFTag implements EMFConstants {

    private int mode;

    SetStretchBltMode() {
        super(21, 1);
    }

    public SetStretchBltMode(int mode) {
        this();
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) 
        throws IOException {
    
        SetStretchBltMode tag = new SetStretchBltMode(emf.readDWORD());
        return tag;
    }
    
    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(mode);
    }

    public String toString() {
        return super.toString()+"\n"+
            "  mode: "+mode;
    }       
}
