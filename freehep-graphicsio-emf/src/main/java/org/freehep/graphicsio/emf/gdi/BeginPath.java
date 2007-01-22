// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;
import java.awt.geom.GeneralPath;
import java.awt.geom.AffineTransform;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * BeginPath TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/BeginPath.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class BeginPath extends EMFTag {

    public BeginPath() {
        super(59, 1);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return this;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // The BeginPath function opens a path bracket in the specified
        // device context.
        renderer.setPath(new GeneralPath(
            renderer.getWindingRule()));
        renderer.setPathTransform(new AffineTransform());
    }
}
