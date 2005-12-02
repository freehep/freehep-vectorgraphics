// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Rectangle;
import java.io.IOException;

/**
 * ExcludeClipRect TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/ExcludeClipRect.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class ExcludeClipRect extends EMFTag {

    private Rectangle bounds;

    ExcludeClipRect() {
        super(29, 1);
    }

    public ExcludeClipRect(Rectangle bounds) {
        this();
        this.bounds = bounds;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        ExcludeClipRect tag = new ExcludeClipRect(emf.readRECTL());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
    }

    public String toString() {
        return super.toString() + "\n" + "  bounds: " + bounds;
    }
}
