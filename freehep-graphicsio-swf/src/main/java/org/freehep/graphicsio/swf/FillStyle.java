// Copyright 2001-2006, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.io.IOException;

/**
 * SWF FillStyle.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/FillStyle.java 3e48ba4ef214 2006/11/27 22:51:07 duns $
 */
public class FillStyle {

    public static final int SOLID = 0x00;

    public static final int LINEAR_GRADIENT = 0x10;

    public static final int RADIAL_GRADIENT = 0x12;
    
    public static final int FOCAL_GRADIENT = 0x13;

    public static final int TILED_BITMAP = 0x40;

    public static final int CLIPPED_BITMAP = 0x41;
    
    public static final int TILED_BITMAP_NOT_SMOOTHED = 0x42;
    
    public static final int CLIPPED_BITMAP_NOT_SMOOTHED = 0x43;

    public static final int SPREAD_MODE_PAD = 0;
    
    public static final int SPREAD_MODE_REFLECT = 1;
    
    public static final int SPREAD_MODE_REPEAT = 2;

    public static final int INTERPOLATION_MODE_NORMAL_RGB = 0;
    
    public static final int INTERPOLATION_MODE_LINEAR_RGB = 1;
        
    private int type;

    private Color color, endColor;

    private AffineTransform matrix, endMatrix;

    private Gradient[] gradient;
    
    private int spreadMode;
    
    private int interpolationMode;
    
    private float focalPoint;

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
        this(gradient, LINEAR_GRADIENT, SPREAD_MODE_PAD, INTERPOLATION_MODE_NORMAL_RGB, 0, matrix);
    }
    
    public FillStyle(Gradient[] gradient, int gradientType, int spreadMode, int interpolationMode, float focalPoint,
        AffineTransform matrix) {
        this.type = gradientType;
        this.gradient = gradient;
        this.spreadMode = spreadMode;
        this.interpolationMode = interpolationMode;
        this.focalPoint = focalPoint;
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
        case FOCAL_GRADIENT:
            matrix = input.readMatrix();
            if (isMorphStyle)
                endMatrix = input.readMatrix();
            input.byteAlign();
            spreadMode = (int)input.readUBits(2);
            interpolationMode = (int)input.readUBits(2);
            int gradientCount = (int)input.readUBits(4);
            gradient = new Gradient[gradientCount];
            for (int i = 0; i < gradientCount; i++) {
                gradient[i] = new Gradient(input, isMorphStyle, hasAlpha);
            }
            if (type == FOCAL_GRADIENT) {
                focalPoint = input.readFixed8();
            }
            break;

        case TILED_BITMAP:
        case CLIPPED_BITMAP:
        case TILED_BITMAP_NOT_SMOOTHED:
        case CLIPPED_BITMAP_NOT_SMOOTHED:
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

    public void write(SWFOutputStream swf, boolean isMorphStyle, boolean hasAlpha) throws IOException {

        swf.writeUnsignedByte(type);

        switch (type) {
        case SOLID:
            swf.writeColor(color, hasAlpha || (endColor != null));
            if (endColor != null)
                swf.writeColor(endColor, true);
            break;

        case LINEAR_GRADIENT:
        case RADIAL_GRADIENT:
        case FOCAL_GRADIENT:
            swf.writeMatrix(matrix);
            if (isMorphStyle)
                swf.writeMatrix(endMatrix);
            swf.byteAlign();
            swf.writeUBits(spreadMode, 2);
            swf.writeUBits(interpolationMode, 2);
            swf.writeUBits(gradient.length, 4);
            for (int i = 0; i < gradient.length; i++) {
                gradient[i].write(swf, isMorphStyle, hasAlpha);
            }
            if (type == FOCAL_GRADIENT) {
                swf.writeFixed8(focalPoint);
            }
            break;

        case TILED_BITMAP:
        case CLIPPED_BITMAP:
        case TILED_BITMAP_NOT_SMOOTHED:
        case CLIPPED_BITMAP_NOT_SMOOTHED:
            swf.writeUnsignedShort(bitmap);
            swf.writeMatrix(matrix);
            if (isMorphStyle)
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
