// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Point;
import java.io.IOException;

import org.freehep.util.io.Tag;

/**
 * SetWindowOrgEx TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/SetWindowOrgEx.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class SetWindowOrgEx
    extends EMFTag {

    private Point point;

    SetWindowOrgEx() {
        super(10, 1);
    }

    public SetWindowOrgEx(Point point) {
        this();
        this.point = point;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) 
        throws IOException {
    
        SetWindowOrgEx tag = new SetWindowOrgEx(emf.readPOINTL());
        return tag;
    }
    
    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writePOINTL(point);
    }

    public String toString() {
        return super.toString()+"\n"+
            "  point: "+point;
    }       
}
