// Copyright 2001-2006, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Color;
import java.io.IOException;

/**
 * SWF LineStyle.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/LineStyle.java 3e48ba4ef214 2006/11/27 22:51:07 duns $
 */
public class LineStyle {

    public static final int CAP_ROUND = 0;
    public static final int CAP_NONE = 1;
    public static final int CAP_SQUARE = 2;
    
    public static final int JOIN_ROUND = 0;
    public static final int JOIN_BEVEL = 1;
    public static final int JOIN_MITER = 2;
    
    private int width, endWidth;
    private int startCapStyle, joinStyle, endCapStyle;
    private boolean hasFillFlag, noHScaleFlag, noVScaleFlag, pixelHintingFlag, noClose;
    private float miterLimitFactor;
    private FillStyle fillStyle;
    
    private Color color, endColor;

    public LineStyle(int width, Color color) {
        this.width = width;
        this.color = color;
        this.startCapStyle = CAP_ROUND;
        this.joinStyle = JOIN_ROUND;
        this.endCapStyle = CAP_ROUND;
        this.hasFillFlag = false;
        this.noHScaleFlag = false;
        this.noVScaleFlag = false;
        this.pixelHintingFlag = false;
        this.noClose = false;
    }

    public LineStyle(int width, int endWidth, Color color, Color endColor) {
        this(width, color);
        this.endWidth = endWidth;
        this.endColor = endColor;
    }

    public LineStyle(SWFInputStream input, boolean isMorphStyle,
            boolean hasAlpha, boolean hasStyles) throws IOException {

        width = input.readUnsignedShort();
        if (isMorphStyle) {
            endWidth = input.readUnsignedShort();
        }
        
        if (hasStyles) {
            startCapStyle = (int)input.readUBits(2);
            joinStyle = (int)input.readUBits(2);
            hasFillFlag = input.readBitFlag();
            noHScaleFlag = input.readBitFlag();
            noVScaleFlag = input.readBitFlag();
            pixelHintingFlag = input.readBitFlag();
            input.readUBits(5);
            noClose = input.readBitFlag();
            endCapStyle = (int)input.readUBits(2);
            if (joinStyle == JOIN_MITER) {
                input.readFixed8();
            }
            if (hasFillFlag) {
                fillStyle = new FillStyle(input, false, true);
            } else {
                color = input.readColor(true);
           }
        } else {
            color = input.readColor(hasAlpha);
        }
        if (isMorphStyle) {
            endColor = input.readColor(true);
        }
    }

    public void write(SWFOutputStream swf, boolean isMorphStyle, boolean hasAlpha, boolean hasStyles) throws IOException {

        swf.writeUnsignedShort(width);
        if (isMorphStyle) {
            swf.writeUnsignedShort(endWidth);
        }
        
        if (hasStyles) {
            swf.writeUBits(startCapStyle, 2);
            swf.writeUBits(joinStyle, 2);
            swf.writeBitFlag(hasFillFlag);
            swf.writeBitFlag(noHScaleFlag);
            swf.writeBitFlag(noVScaleFlag);
            swf.writeBitFlag(pixelHintingFlag);
            swf.writeUBits(0, 5);
            swf.writeBitFlag(noClose);
            swf.writeUBits(endCapStyle, 2);
            if (joinStyle == JOIN_MITER) {
                swf.writeFixed8(miterLimitFactor);
            }
            if (hasFillFlag) {
                fillStyle.write(swf, isMorphStyle, true);
            } else {
                swf.writeColor(color, true);
            }
        } else {
            swf.writeColor(color, hasAlpha || isMorphStyle);
            if (isMorphStyle) {
                swf.writeColor(endColor, true);
            }
        }
    }

    public String toString() {
        StringBuffer s = new StringBuffer("LineStyle " + width + ", " + color);
        if (endColor != null)
            s.append("; " + endWidth + ", " + endColor);
        return s.toString();
    }
}
