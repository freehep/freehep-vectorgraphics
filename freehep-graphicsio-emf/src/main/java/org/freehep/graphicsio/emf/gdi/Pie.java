// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * Pie TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/Pie.java cb17a8f71934 2007/01/23 15:44:34 duns $
 */
public class Pie extends AbstractArc {

    public Pie() {
        super(47, 1, null, null, null);
    }

    public Pie(Rectangle bounds, Point start, Point end) {
        super(47, 1, bounds, start, end);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new Pie(
            emf.readRECTL(),
            emf.readPOINTL(),
            emf.readPOINTL());
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        renderer.fillAndDrawOrAppend(
            getShape(renderer, Arc2D.PIE));
    }
}
