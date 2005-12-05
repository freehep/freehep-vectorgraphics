// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.io.IOException;

/**
 * SWF FillStyle.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/FillStyle.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class FillStyle {

    public static final int SOLID = 0x00;

    public static final int LINEAR_GRADIENT = 0x10;

    public static final int RADIAL_GRADIENT = 0x12;

    public static final int TILED_BITMAP = 0x40;

    public static final int CLIPPED_BITMAP = 0x41;

    private int type;

    private Color color, endColor;

    private AffineTransform matrix, endMatrix;

    private Gradient[] gradient;

    private int bitmap;

    public FillStyle(Color color) {
        type = SOLID;
        this.color = color;
    }

    public FillStyle(Color color, Color endColor) {
        this(color);
        this.endColor = endColor;
    }

    public FillStyle(Gradient[] gradient, boolean linear, AffineTransform matrix) {
        type = (linear) ? LINEAR_GRADIENT : RADIAL_GRADIENT;
        this.gradient = gradient;
        this.matrix = matrix;
    }

    public FillStyle(Gradient[] gradient, boolean linear,
            AffineTransform matrix, AffineTransform endMatrix) {
        this(gradient, linear, matrix);
        this.endMatrix = endMatrix;
    }

    public FillStyle(int bitmap, boolean tiled, AffineTransform matrix) {
        type = (tiled) ? TILED_BITMAP : CLIPPED_BITMAP;
        this.bitmap = bitmap;
        this.matrix = matrix;
    }

    public FillStyle(int bitmap, boolean tiled, AffineTransform matrix,
            AffineTransform endMatrix) {
        this(bitmap, tiled, matrix);
        this.endMatrix = endMatrix;
    }

    public FillStyle(SWFInputStream input, boolean isMorphStyle,
            boolean hasAlpha) throws IOException {
        type = input.readUnsignedByte();
        switch (type) {
        case SOLID:
            color = input.readColor(hasAlpha);
            if (isMorphStyle)
                endColor = input.readColor(hasAlpha);
            break;

        case LINEAR_GRADIENT:
        case RADIAL_GRADIENT:
            matrix = input.readMatrix();
            if (isMorphStyle)
                endMatrix = input.readMatrix();
            int gradientCount = input.readUnsignedByte();
            gradient = new Gradient[gradientCount];
            for (int i = 0; i < gradientCount; i++) {
                gradient[i] = new Gradient(input, hasAlpha, isMorphStyle);
            }
            break;

        case TILED_BITMAP:
        case CLIPPED_BITMAP:
            bitmap = input.readUnsignedShort();
            matrix = input.readMatrix();
            if (isMorphStyle)
                endMatrix = input.readMatrix();
            break;

        default:
            System.err.println("FillStyle type " + type + " not implemented.");
            break;
        }
    }

    public int getType() {
        return type;
    }

    public void write(SWFOutputStream swf, boolean hasAlpha) throws IOException {

        swf.writeUnsignedByte(type);

        switch (type) {
        case SOLID:
            swf.writeColor(color, hasAlpha || (endColor != null));
            if (endColor != null)
                swf.writeColor(endColor, true);
            break;

        case LINEAR_GRADIENT:
        case RADIAL_GRADIENT:
            swf.writeMatrix(matrix);
            if (endMatrix != null)
                swf.writeMatrix(endMatrix);
            swf.writeUnsignedByte(gradient.length);
            for (int i = 0; i < gradient.length; i++) {
                gradient[i].write(swf, hasAlpha);
            }
            break;

        case TILED_BITMAP:
        case CLIPPED_BITMAP:
            swf.writeUnsignedShort(bitmap);
            swf.writeMatrix(matrix);
            if (endMatrix != null)
                swf.writeMatrix(endMatrix);
            break;

        default:
            System.err.println("FillStyle type " + type + " not implemented.");
            break;
        }
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        switch (type) {
        case SOLID:
            s.append("Solid: ");
            s.append(color);
            if (endColor != null)
                s.append("; " + endColor);
            break;
        case LINEAR_GRADIENT:
        case RADIAL_GRADIENT:
            s.append("Gradient: ");
            for (int i = 0; i < gradient.length; i++) {
                s.append(gradient[i]);
                s.append("; ");
            }
            s.append(matrix);
            if (endMatrix != null)
                s.append("; " + endMatrix);
            break;
        case TILED_BITMAP:
        case CLIPPED_BITMAP:
            s.append("BitMap: ");
            s.append(bitmap);
            s.append(", ");
            s.append(matrix);
            if (endMatrix != null)
                s.append("; " + endMatrix);
            break;
        default:
            s.append("Unknown Type:");
            s.append(type);
            break;
        }
        return s.toString();
    }
}
