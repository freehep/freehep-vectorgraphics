// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Rectangle;
import java.io.IOException;

/**
 * StrokePath TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/StrokePath.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class StrokePath extends EMFTag {

    private Rectangle bounds;

    StrokePath() {
        super(64, 1);
    }

    public StrokePath(Rectangle bounds) {
        this();
        this.bounds = bounds;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        StrokePath tag = new StrokePath(emf.readRECTL());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
    }

    public String toString() {
        return super.toString() + "\n" + "  bounds: " + bounds;
    }
}
