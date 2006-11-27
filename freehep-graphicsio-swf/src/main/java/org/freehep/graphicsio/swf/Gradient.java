// Copyright 2001-2006, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Color;
import java.io.IOException;

/**
 * SWF Gradient.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/Gradient.java 3e48ba4ef214 2006/11/27 22:51:07 duns $
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

    public Gradient(SWFInputStream input, boolean isMorphStyle, boolean hasAlpha) 
            throws IOException {
        ratio = input.readUnsignedByte();
        color = input.readColor(hasAlpha);
        if (isMorphStyle) {
            endRatio = input.readUnsignedByte();
            endColor = input.readColor(true);
        }
    }

    public void write(SWFOutputStream swf, boolean isMorphStyle, boolean hasAlpha) throws IOException {
        swf.writeUnsignedByte(ratio);
        swf.writeColor(color, hasAlpha);
        if (isMorphStyle) {
            swf.writeUnsignedByte(endRatio);
            swf.writeColor(endColor, true);
        }
    }

    public String toString() {
        return "Gradient " + ratio + ", " + color
                + ((endColor != null) ? ", " + endRatio + ", " + endColor : "");
    }
}
