// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.geom.AffineTransform;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * ModifyWorldTransform TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/ModifyWorldTransform.java 63c8d910ece7 2007/01/20 15:30:50 duns $
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

        ModifyWorldTransform tag = new ModifyWorldTransform(emf.readXFORM(),
                emf.readDWORD());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeXFORM(transform);
        emf.writeDWORD(mode);
    }

    public AffineTransform getTransform() {
        return transform;
    }

    public String toString() {
        return super.toString() + "\n" + "  transform: " + transform + "\n"
                + "  mode: " + mode;
    }
}
