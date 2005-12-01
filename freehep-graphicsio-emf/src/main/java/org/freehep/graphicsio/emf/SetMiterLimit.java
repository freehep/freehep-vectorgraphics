// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

import org.freehep.util.io.Tag;

/**
 * SetMiterLimit TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/SetMiterLimit.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class SetMiterLimit
    extends EMFTag {

    private int limit;

    SetMiterLimit() {
        super(58, 1);
    }

    public SetMiterLimit(int limit) {
        this();
        this.limit = limit;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) 
        throws IOException {
    
        SetMiterLimit tag = new SetMiterLimit(emf.readDWORD());
        return tag;
    }
    
    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(limit);
    }

    public String toString() {
        return super.toString()+"\n"+
            "  limit: "+limit;
    }       
}
