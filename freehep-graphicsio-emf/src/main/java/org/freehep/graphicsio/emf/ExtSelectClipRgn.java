// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

import org.freehep.util.io.Tag;

/**
 * ExtSelectClipRgn TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/ExtSelectClipRgn.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class ExtSelectClipRgn
    extends EMFTag implements EMFConstants {

    private int mode;
    private Region rgn;
    
    ExtSelectClipRgn() {
        super(75, 1);
    }

    public ExtSelectClipRgn(int mode, Region rgn) {
        this();
        this.mode = mode;
        this.rgn = rgn;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) 
        throws IOException {
    
        int length = emf.readDWORD();
        int mode = emf.readDWORD();
        ExtSelectClipRgn tag = new ExtSelectClipRgn(mode , length > 8 ? new Region(emf) : null);
        return tag;
    }
    
    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(rgn.length());
        emf.writeDWORD(mode);
        rgn.write(emf);
    }

    public String toString() {
        return super.toString()+"\n"+
            "  mode: "+mode+"\n"+rgn;
    }   
}
