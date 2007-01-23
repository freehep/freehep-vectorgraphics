// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Rectangle;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * IntersectClipRect TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/IntersectClipRect.java cb17a8f71934 2007/01/23 15:44:34 duns $
 */
public class IntersectClipRect extends EMFTag {

    private Rectangle bounds;

    public IntersectClipRect() {
        super(30, 1);
    }

    public IntersectClipRect(Rectangle bounds) {
        this();
        this.bounds = bounds;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new IntersectClipRect(emf.readRECTL());
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
    }

    public String toString() {
        return super.toString() + "\n  bounds: " + bounds;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // The IntersectClipRect function creates a new clipping
        // region from the intersection of the current clipping
        // region and the specified rectangle.
        renderer.clip(bounds);
    }
}
