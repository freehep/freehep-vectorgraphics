// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

import org.freehep.util.io.Tag;

/**
 * DeleteObject TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/DeleteObject.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class DeleteObject
    extends EMFTag {

    private int index;

    DeleteObject() {
        super(40, 1);
    }

    public DeleteObject(int index) {
        this();
        this.index = index;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) 
        throws IOException {
    
        DeleteObject tag = new DeleteObject(emf.readDWORD());
        return tag;
    }
    
    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(index);
    }

    public String toString() {
        return super.toString()+"\n"+
            "  index: 0x"+Integer.toHexString(index);
    }   
    public int getIndex() {
            return index;
    }
}
