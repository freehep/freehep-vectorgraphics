// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Point;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * SetViewportOrgEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/SetViewportOrgEx.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class SetViewportOrgEx extends EMFTag {

    private Point point;

    public SetViewportOrgEx() {
        super(12, 1);
    }

    public SetViewportOrgEx(Point point) {
        this();
        this.point = point;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new SetViewportOrgEx(emf.readPOINTL());
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writePOINTL(point);
    }

    public String toString() {
        return super.toString() + "\n  point: " + point;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // The SetViewportOrgEx function specifies which device point maps
        // to the viewport origin (0,0).

        // This function (along with SetViewportExtEx and SetWindowExtEx) helps
        // define the mapping from the logical coordinate space (also known as a
        // window) to the device coordinate space (the viewport). SetViewportOrgEx
        // specifies which device point maps to the logical point (0,0). It has the
        // effect of shifting the axes so that the logical point (0,0) no longer
        // refers to the upper-left corner.
        renderer.setViewportOrigin(point);
        renderer.resetTransformation();
    }
}
