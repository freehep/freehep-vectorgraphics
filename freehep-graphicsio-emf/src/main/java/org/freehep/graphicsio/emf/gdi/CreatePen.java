// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * CreatePen TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/CreatePen.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class CreatePen extends EMFTag {

    private int index;

    private LogPen pen;

    public CreatePen() {
        super(38, 1);
    }

    public CreatePen(int index, LogPen pen) {
        this();
        this.index = index;
        this.pen = pen;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new CreatePen(emf.readDWORD(), new LogPen(emf));
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(index);
        pen.write(emf);
    }

    public String toString() {
        return super.toString() +
            "\n  index: 0x" + Integer.toHexString(index) +
            "\n" + pen.toString();
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // ExtCreatePen
        //
        // The ExtCreatePen function creates a logical cosmetic or
        // geometric pen that has the specified style, width,
        // and brush attributes.
        //
        // HPEN ExtCreatePen(
        //  DWORD dwPenStyle,      // pen style
        //  DWORD dwWidth,         // pen width
        //  CONST LOGBRUSH *lplb,  // brush attributes
        //  DWORD dwStyleCount,    // length of custom style array
        //  CONST DWORD *lpStyle   // custom style array
        //);
        renderer.storeGDIObject(index, pen);
    }
}
