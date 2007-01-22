// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.geom.AffineTransform;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * ModifyWorldTransform TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/ModifyWorldTransform.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class ModifyWorldTransform extends EMFTag implements EMFConstants {

    private AffineTransform transform;

    private int mode;

    public ModifyWorldTransform() {
        super(36, 1);
    }

    public ModifyWorldTransform(AffineTransform transform, int mode) {
        this();
        this.transform = transform;
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new ModifyWorldTransform(
            emf.readXFORM(),
            emf.readDWORD());
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeXFORM(transform);
        emf.writeDWORD(mode);
    }

    public String toString() {
        return super.toString() +
            "\n  transform: " + transform +
            "\n  mode: " + mode;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // TODO: this fixes an extra offset for embedded
        // EMF graphics, not quite clear why
        if (mode != EMFConstants.MWT_LEFTMULTIPLY) {
            return;
        }

        if (renderer.getPath() != null) {
            renderer.getPathTransform().concatenate(transform);
            renderer.transform(transform);
        } else {
            renderer.transform(transform);
        }
    }
}
