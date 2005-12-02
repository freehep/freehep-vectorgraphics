// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Rectangle;
import java.io.IOException;

/**
 * IntersectClipRect TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/IntersectClipRect.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class IntersectClipRect extends EMFTag {

    private Rectangle bounds;

    IntersectClipRect() {
        super(30, 1);
    }

    public IntersectClipRect(Rectangle bounds) {
        this();
        this.bounds = bounds;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        IntersectClipRect tag = new IntersectClipRect(emf.readRECTL());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
    }

    public String toString() {
        return super.toString() + "\n" + "  bounds: " + bounds;
    }
}
