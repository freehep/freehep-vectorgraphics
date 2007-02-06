// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;

import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * ExtSelectClipRgn TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/ExtSelectClipRgn.java 10ec7516e3ce 2007/02/06 18:42:34 duns $
 */
public class ExtSelectClipRgn extends AbstractClipPath {

    private Region rgn;

    public ExtSelectClipRgn() {
        super(75, 1, EMFConstants.RGN_COPY);
    }

    public ExtSelectClipRgn(int mode, Region rgn) {
        super(75, 1, mode);
        this.rgn = rgn;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        int length = emf.readDWORD();
        int mode = emf.readDWORD();
        return new ExtSelectClipRgn(
            mode,
            length > 8 ? new Region(emf) : null);
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(rgn.length());
        emf.writeDWORD(getMode());
        rgn.write(emf);
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        if (rgn == null || rgn.getBounds() == null) {
            return;
        }

        render(renderer, rgn.getBounds());
    }
}
