// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Rectangle;
import java.io.IOException;

/**
 * Rectangle TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFRectangle.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class EMFRectangle extends EMFTag {

    private Rectangle bounds;

    EMFRectangle() {
        super(43, 1);
    }

    public EMFRectangle(Rectangle bounds) {
        this();
        this.bounds = bounds;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        EMFRectangle tag = new EMFRectangle(emf.readRECTL());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
    }

    public String toString() {
        return super.toString() + "\n" + "  bounds: " + bounds;
    }
}
