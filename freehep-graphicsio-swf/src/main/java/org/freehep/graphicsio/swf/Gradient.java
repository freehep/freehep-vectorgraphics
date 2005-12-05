// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Color;
import java.io.IOException;

/**
 * SWF Gradient.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/Gradient.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class Gradient {

    private int ratio, endRatio;

    private Color color, endColor;

    public Gradient(int ratio, Color color) {
        this.ratio = ratio;
        this.color = color;
    }

    public Gradient(int ratio, int endRatio, Color color, Color endColor) {
        this(ratio, color);
        this.endRatio = endRatio;
        this.endColor = endColor;
    }

    public Gradient(SWFInputStream input, boolean hasAlpha, boolean isMorphStyle)
            throws IOException {
        ratio = input.readUnsignedByte();
        color = input.readColor(hasAlpha);
        if (isMorphStyle) {
            endRatio = input.readUnsignedByte();
            endColor = input.readColor(true);
        }
    }

    public void write(SWFOutputStream swf, boolean hasAlpha) throws IOException {
        swf.writeUnsignedByte(ratio);
        swf.writeColor(color, hasAlpha);
        if (endColor != null) {
            swf.writeUnsignedByte(endRatio);
            swf.writeColor(endColor, true);
        }
    }

    public String toString() {
        return "Gradient " + ratio + ", " + color
                + ((endColor != null) ? ", " + endRatio + ", " + endColor : "");
    }
}
