// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.geom.AffineTransform;
import java.io.IOException;

/**
 * SetWorldTransform TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/SetWorldTransform.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class SetWorldTransform extends EMFTag {

    private AffineTransform transform;

    SetWorldTransform() {
        super(35, 1);
    }

    public SetWorldTransform(AffineTransform transform) {
        this();
        this.transform = transform;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        SetWorldTransform tag = new SetWorldTransform(emf.readXFORM());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeXFORM(transform);
    }

    public String toString() {
        return super.toString() + "\n" + "  transform: " + transform;
    }
}
