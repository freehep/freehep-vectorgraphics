// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

import org.freehep.util.io.Tag;

/**
 * RestoreDC TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/RestoreDC.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class RestoreDC
    extends EMFTag {

    private int savedDC = -1;

    public RestoreDC() {
        super(34, 1);
    }

    public RestoreDC(int savedDC) {
        this();
        this.savedDC = savedDC;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) 
        throws IOException {
    
        RestoreDC tag = new RestoreDC(emf.readDWORD());
        return tag;
    }
    
    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(savedDC);
    }

    public String toString() {
        return super.toString()+"\n"+
            "  savedDC: "+savedDC;
    }       
}
