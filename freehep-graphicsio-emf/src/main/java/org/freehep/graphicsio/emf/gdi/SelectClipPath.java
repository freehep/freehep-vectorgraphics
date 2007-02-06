// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;

import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * SelectClipPath TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/SelectClipPath.java 59372df5e0d9 2007/02/06 21:11:19 duns $
 */
public class SelectClipPath extends AbstractClipPath {

    public SelectClipPath() {
        super(67, 1, EMFConstants.RGN_AND);
    }

    public SelectClipPath(int mode) {
        super(67, 1, mode);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new SelectClipPath(emf.readDWORD());
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(getMode());
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        render(renderer, renderer.getPath());
    }
}
