// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * SetMiterLimit TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/SetMiterLimit.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class SetMiterLimit extends EMFTag {

    private int limit;

    public SetMiterLimit() {
        super(58, 1);
    }

    public SetMiterLimit(int limit) {
        this();
        this.limit = limit;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new SetMiterLimit(emf.readDWORD());
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(limit);
    }

    public String toString() {
        return super.toString() + "\n  limit: " + limit;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // The SetMiterLimit function sets the limit for the length of miter
        // joins for the specified device context.
        // The miter length is defined as the distance from the intersection
        // of the line walls on the inside of the join to the intersection of
        // the line walls on the outside of the join. The miter limit is the
        // maximum allowed ratio of the miter length to the line width.

        // The default miter limit is 10.0.
        renderer.setMeterLimit(limit);
    }
}
