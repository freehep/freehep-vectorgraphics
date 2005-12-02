// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Point;
import java.io.IOException;

/**
 * OffsetClipRgn TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/OffsetClipRgn.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class OffsetClipRgn extends EMFTag {

    private Point offset;

    OffsetClipRgn() {
        super(26, 1);
    }

    public OffsetClipRgn(Point offset) {
        this();
        this.offset = offset;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        OffsetClipRgn tag = new OffsetClipRgn(emf.readPOINTL());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writePOINTL(offset);
    }

    public String toString() {
        return super.toString() + "\n" + "  offset: " + offset;
    }
}
