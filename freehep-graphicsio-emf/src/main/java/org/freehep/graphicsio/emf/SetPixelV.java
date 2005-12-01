// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;

import org.freehep.util.io.Tag;

/**
 * SetPixelV TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/SetPixelV.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class SetPixelV
    extends EMFTag {

    private Point point;
    private Color color;

    SetPixelV() {
        super(15, 1);
    }

    public SetPixelV(Point point, Color color) {
        this();
        this.point = point;
        this.color = color;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) 
        throws IOException {
    
        SetPixelV tag = new SetPixelV(emf.readPOINTL(), emf.readCOLORREF());
        return tag;
    }
    
    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writePOINTL(point);
        emf.writeCOLORREF(color);
    }

    public String toString() {
        return super.toString()+"\n"+
            "  point: "+point+"\n"+
            "  color: "+color;
    }       
}
