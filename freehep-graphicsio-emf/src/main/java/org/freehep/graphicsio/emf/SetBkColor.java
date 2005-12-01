// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Color;
import java.io.IOException;

import org.freehep.util.io.Tag;

/**
 * SetBkColor TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/SetBkColor.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class SetBkColor
    extends EMFTag {

    private Color color;

    SetBkColor() {
        super(25, 1);
    }

    public SetBkColor(Color color) {
        this();
        this.color = color;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) 
        throws IOException {
    
        SetBkColor tag = new SetBkColor(emf.readCOLORREF());
        return tag;
    }
    
    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeCOLORREF(color);
    }

    public String toString() {
        return super.toString()+"\n"+
            "  color: "+color;
    }       
}