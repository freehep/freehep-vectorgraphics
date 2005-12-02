// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Color;
import java.io.IOException;

/**
 * EMF TriVertex
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/TriVertex.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class TriVertex {

    private int x, y;

    private Color color;

    public TriVertex(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    TriVertex(EMFInputStream emf) throws IOException {
        x = emf.readLONG();
        y = emf.readLONG();
        color = emf.readCOLOR16();
    }

    public void write(EMFOutputStream emf) throws IOException {
        emf.writeLONG(x);
        emf.writeLONG(y);
        emf.writeCOLOR16(color);
    }

    public String toString() {
        return "[" + x + ", " + y + "] " + color;
    }
}
