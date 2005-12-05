// Copyright 2003 FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.Color;
import java.io.IOException;

/**
 * Transparent Cell Colour TAG.
 * 
 * @author Ian Graham
 */
public class TransparentCellColour extends CGMTag {
    private boolean on;

    private Color colour;

    public TransparentCellColour() {
        super(3, 20, 3);
    }

    public TransparentCellColour(boolean on, Color colour) {
        this();
        this.on = on;
        this.colour = colour;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeEnumerate(on ? 1 : 0);
        cgm.writeColor(colour);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        // TODO: TRANSPCELLCOLR UNTESTED
        cgm.print("TRANSPCELLCOLR ");
        cgm.print((on) ? "ON " : "OFF ");
        cgm.writeColor(colour);
    }

}
