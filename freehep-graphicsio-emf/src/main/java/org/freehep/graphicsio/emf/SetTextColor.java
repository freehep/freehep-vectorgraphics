// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Color;
import java.io.IOException;

/**
 * SetTextColor TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/SetTextColor.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class SetTextColor extends EMFTag {

    private Color color;

    SetTextColor() {
        super(24, 1);
    }

    public SetTextColor(Color color) {
        this();
        this.color = color;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        SetTextColor tag = new SetTextColor(emf.readCOLORREF());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeCOLORREF(color);
    }

    public String toString() {
        return super.toString() + "\n" + "  color: " + color;
    }

    public Color getColor() {
        return color;
    }
}
