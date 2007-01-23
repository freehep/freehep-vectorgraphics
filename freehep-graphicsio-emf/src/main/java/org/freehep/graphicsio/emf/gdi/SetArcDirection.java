// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;

import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * SetArcDirection TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/SetArcDirection.java cb17a8f71934 2007/01/23 15:44:34 duns $
 */
public class SetArcDirection extends EMFTag implements EMFConstants {

    private int direction;

    public SetArcDirection() {
        super(57, 1);
    }

    public SetArcDirection(int direction) {
        this();
        this.direction = direction;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new SetArcDirection(emf.readDWORD());
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(direction);
    }

    public String toString() {
        return super.toString() + "\n  direction: " + direction;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // The SetArcDirection sets the drawing direction to
        // be used for arc and rectangle functions.
        renderer.setArcDirection(direction);
    }
}
