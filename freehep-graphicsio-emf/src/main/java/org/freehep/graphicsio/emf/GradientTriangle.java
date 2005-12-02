// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * EMF GradientTriangle
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/GradientTriangle.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class GradientTriangle extends Gradient {

    private int vertex1, vertex2, vertex3;

    public GradientTriangle(int vertex1, int vertex2, int vertex3) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
    }

    GradientTriangle(EMFInputStream emf) throws IOException {
        vertex1 = emf.readULONG();
        vertex2 = emf.readULONG();
        vertex3 = emf.readULONG();
    }

    public void write(EMFOutputStream emf) throws IOException {
        emf.writeULONG(vertex1);
        emf.writeULONG(vertex2);
        emf.writeULONG(vertex3);
    }

    public String toString() {
        return "  GradientTriangle: " + vertex1 + ", " + vertex2 + ", "
                + vertex3;
    }
}
