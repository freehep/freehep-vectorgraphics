// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Point;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * OffsetClipRgn TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/OffsetClipRgn.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class OffsetClipRgn extends EMFTag {

    private Point offset;

    public OffsetClipRgn() {
        super(26, 1);
    }

    public OffsetClipRgn(Point offset) {
        this();
        this.offset = offset;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new OffsetClipRgn(emf.readPOINTL());
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writePOINTL(offset);
    }

    public String toString() {
        return super.toString() + "\n  offset: " + offset;
    }
}
