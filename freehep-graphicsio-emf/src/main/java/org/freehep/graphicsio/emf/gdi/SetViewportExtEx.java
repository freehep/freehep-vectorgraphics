// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Dimension;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * SetViewportExtEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/SetViewportExtEx.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class SetViewportExtEx extends EMFTag {

    private Dimension size;

    public SetViewportExtEx() {
        super(11, 1);
    }

    public SetViewportExtEx(Dimension size) {
        this();
        this.size = size;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new SetViewportExtEx(emf.readSIZEL());
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeSIZEL(size);
    }

    public String toString() {
        return super.toString() + "\n  size: " + size;
    }

    // The SetViewportExtEx function sets the horizontal and vertical
    // extents of the viewport for a device context by using the specified values.

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // The viewport refers to the device coordinate system of the device space.
        // The extent is the maximum value of an axis. This function sets the maximum
        // values for the horizontal and vertical axes of the viewport in device
        // coordinates (or pixels). When mapping between page space and device space,
        // SetWindowExtEx and SetViewportExtEx determine the scaling factor between
        // the window and the viewport.
        renderer.setViewportSize(size);
    }
}
