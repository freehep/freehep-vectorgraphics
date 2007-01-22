// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * CreateBrushIndirect TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/CreateBrushIndirect.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class CreateBrushIndirect extends EMFTag {

    private int index;

    private LogBrush32 brush;

    public CreateBrushIndirect() {
        super(39, 1);
    }

    public CreateBrushIndirect(int index, LogBrush32 brush) {
        this();
        this.index = index;
        this.brush = brush;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new CreateBrushIndirect(
            emf.readDWORD(),
            new LogBrush32(emf));
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(index);
        brush.write(emf);
    }

    public String toString() {
        return super.toString() +
            "\n  index: 0x" + Integer.toHexString(index) +
            "\n" + brush.toString();
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // CreateBrushIndirect
        //
        // The CreateBrushIndirect function creates a logical brush that has the
        // specified style, color, and pattern.
        //
        // HBRUSH CreateBrushIndirect(
        //   CONST LOGBRUSH *lplb   // brush information
        // );
        renderer.storeGDIObject(index, brush);
    }
}