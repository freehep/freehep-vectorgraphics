// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Point;
import java.io.IOException;

/**
 * SetBrushOrgEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/SetBrushOrgEx.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class SetBrushOrgEx extends EMFTag {

    private Point point;

    SetBrushOrgEx() {
        super(13, 1);
    }

    public SetBrushOrgEx(Point point) {
        this();
        this.point = point;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        SetBrushOrgEx tag = new SetBrushOrgEx(emf.readPOINTL());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writePOINTL(point);
    }

    public String toString() {
        return super.toString() + "\n" + "  point: " + point;
    }
}
