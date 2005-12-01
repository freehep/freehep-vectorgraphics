// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * GDIComment TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/GDIComment.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class GDIComment
    extends EMFTag {

    private String comment;

    GDIComment() {
        super(70, 1);
    }
    
    public GDIComment(String comment) {
        this();
        this.comment = comment;
    }
    
    public EMFTag read(int tagID, EMFInputStream emf, int len) 
        throws IOException {
    
        int l = emf.readDWORD();
        GDIComment tag = new GDIComment(new String(emf.readBYTE(l)));
        // Align to 4-byte boundary
        if (l%4 != 0) emf.readBYTE(4-l%4);
        return tag;
    }
    
    public void write(int tagID, EMFOutputStream emf) throws IOException {
        byte[] b = comment.getBytes();
        emf.writeDWORD(b.length);
        emf.writeBYTE(b);
        if (b.length%4 != 0) for (int i=0; i<4-b.length%4; i++) emf.writeBYTE(0);
    }
    
    public String toString() {
        return super.toString()+"\n"+
            "  length: "+comment.length();
    }       
}
