// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * SetPixelV TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/SetPixelV.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class SetPixelV extends EMFTag {

    private Point point;

    private Color color;

    public SetPixelV() {
        super(15, 1);
    }

    public SetPixelV(Point point, Color color) {
        this();
        this.point = point;
        this.color = color;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new SetPixelV(emf.readPOINTL(), emf.readCOLORREF());
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writePOINTL(point);
        emf.writeCOLORREF(color);
    }

    public String toString() {
        return super.toString() +
            "\n  point: " + point +
            "\n  color: " + color;
    }
}
