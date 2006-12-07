// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * GDIComment TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/GDIComment.java f2f1115939ae 2006/12/07 07:50:41 duns $
 */
public class GDIComment extends EMFTag {

    private String comment;

    public GDIComment() {
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
        if (l % 4 != 0)
            emf.readBYTE(4 - l % 4);
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        byte[] b = comment.getBytes();
        emf.writeDWORD(b.length);
        emf.writeBYTE(b);
        if (b.length % 4 != 0)
            for (int i = 0; i < 4 - b.length % 4; i++)
                emf.writeBYTE(0);
    }

    public String toString() {
        return super.toString() + "\n" + "  length: " + comment.length();
    }
}
