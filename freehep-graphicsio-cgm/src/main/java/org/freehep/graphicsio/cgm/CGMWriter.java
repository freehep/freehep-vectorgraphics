// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.Writer;

import org.freehep.util.io.IndentPrintWriter;
import org.freehep.util.io.Tag;
import org.freehep.util.io.TaggedOutput;

/**
 * CGM Clear Text Writer. Tags written with this Writer will produce a clear
 * text CGM file. The class also holds the state for the various precisions,
 * defaults, etc...
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CGMWriter.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CGMWriter extends IndentPrintWriter implements TaggedOutput {

    private int version;

    private boolean direct = true;

    private int colorIndexPrecision = 8;

    private int directColorPrecision = 8;

    private int integerPrecision = 16;

    private boolean fixedPrecision = true;

    private boolean doublePrecision = false;

    private boolean vdcReal = false;

    private boolean vdcFixedPrecision = true;

    private boolean vdcDoublePrecision = false;

    private int vdcIntegerPrecision = 16;

    private int namePrecision = 16;

    private int lineWidthSpecificationMode = LineWidthSpecificationMode.ABSOLUTE;

    private int markerSizeSpecificationMode = MarkerSizeSpecificationMode.ABSOLUTE;

    private int edgeWidthSpecificationMode = EdgeWidthSpecificationMode.ABSOLUTE;

    private int interiorStyleSpecificationMode = InteriorStyleSpecificationMode.ABSOLUTE;

    /**
     * Constructs a Clear Text CGM writer of a specified version.
     * 
     * @param writer the underlying writer
     * @param version the requested cgm version
     */
    public CGMWriter(Writer writer, int version) throws IOException {

        super(writer);
        this.version = version;
    }

    /**
     * Constructs a Clear Text CGM Writer of version 1.
     * 
     * @param writer the underlying writer.
     */
    public CGMWriter(Writer writer) throws IOException {

        this(writer, 1);
    }

    void setColorMode(boolean direct) {
        this.direct = direct;
    }

    void setColorIndexPrecision(int precision) {
        colorIndexPrecision = precision;
    }

    void setDirectColorPrecision(int precision) {
        directColorPrecision = precision;
    }

    void setIntegerPrecision(int precision) {
        integerPrecision = precision;
    }

    void setRealPrecision(boolean fixedPrecision, boolean doublePrecision) {
        this.fixedPrecision = fixedPrecision;
        this.doublePrecision = doublePrecision;
    }

    void setVDCReal(boolean real) {
        vdcReal = real;
    }

    void setVDCIntegerPrecision(int precision) {
        vdcIntegerPrecision = precision;
    }

    void setVDCRealPrecision(boolean fixedPrecision, boolean doublePrecision) {
        this.vdcFixedPrecision = fixedPrecision;
        this.vdcDoublePrecision = doublePrecision;
    }

    void setNamePrecision(int precision) {
        namePrecision = precision;
    }

    void setLineWidthSpecificationMode(int mode) {
        lineWidthSpecificationMode = mode;
    }

    int getLineWidthSpecificationMode() {
        return lineWidthSpecificationMode;
    }

    void setMarkerSizeSpecificationMode(int mode) {
        markerSizeSpecificationMode = mode;
    }

    int getMarkerSizeSpecificationMode() {
        return markerSizeSpecificationMode;
    }

    void setEdgeWidthSpecificationMode(int mode) {
        edgeWidthSpecificationMode = mode;
    }

    int getEdgeWidthSpecificationMode() {
        return edgeWidthSpecificationMode;
    }

    void setInteriorStyleSpecificationMode(int mode) {
        interiorStyleSpecificationMode = mode;
    }

    int getInteriorStyleSpecificationMode() {
        return interiorStyleSpecificationMode;
    }

    /**
     * Write a Fixed Point value (FX)
     * 
     * @param d value to the written
     */
    public void writeFixedPoint(double d) throws IOException {
        boolean negative = d < 0;
        d = Math.abs(d);
        int whole = (int) Math.floor(d);
        int frac = (int) Math.floor((d - whole) * 10000);
        if (negative)
            print("-");
        print(whole);
        print(".");
        print(frac);
    }

    /**
     * Writes a Floating Point value (FP)
     * 
     * @param d value to be written
     */
    public void writeFloatingPoint(double d) throws IOException {
        if (doublePrecision) {
            // FIXME: ignored number of digits
            print(d);
        } else {
            // FIXME: ignored number of digits
            print((float) d);
        }
    }

    /**
     * Writes a Color Index (CI)
     * 
     * @param i index to be written
     */
    public void writeColorIndex(int i) throws IOException {
        switch (colorIndexPrecision) {
        case 8:
            print(Math.max(0, Math.min(255, i)));
            break;
        case 16:
            print(Math.max(0, Math.min(65535, i)));
            break;
        default:
        case 24: // FIXME
        case 32:
            print(i);
            break;
        }
    }

    /**
     * Writes a Color Component (CCO)
     * 
     * @param c color component to be written
     */
    public void writeColorComponent(int c) throws IOException {
        switch (directColorPrecision) {
        default:
        case 8:
            print(Math.max(0, Math.min(255, c)));
            break;
        case 16:
            print(Math.max(0, Math.min(65535, c)));
            break;
        case 24: // FIXME
        case 32:
            print(c);
            break;
        }
    }

    /**
     * Writes a Color Direct (CD)
     * 
     * @param c color to be written
     */
    public void writeColorDirect(Color c) throws IOException {
        writeColorComponent(c.getRed());
        print(", ");
        writeColorComponent(c.getGreen());
        print(", ");
        writeColorComponent(c.getBlue());
    }

    /**
     * Writes an Integer (I)
     * 
     * @param i integer to be written
     */
    public void writeInteger(int i) throws IOException {
        switch (integerPrecision) {
        case 8:
            print((byte) i);
            break;
        default:
        case 16:
            print((short) i);
            break;
        case 24: // FIXME
        case 32:
            print(i);
            break;
        }
    }

    /**
     * Writes a Real (R)
     * 
     * @param r value to be written
     */
    public void writeReal(double r) throws IOException {
        if (fixedPrecision) {
            writeFixedPoint(r);
        } else {
            writeFloatingPoint(r);
        }
    }

    /**
     * Writes a String or String Fixed (S, SF)
     * 
     * @param s string to be written
     */
    public void writeString(String s) throws IOException {
        StringBuffer b = new StringBuffer();
        b.append('"');
        for (int i = 0; i < s.length(); i++) {
            b.append(s.charAt(i));
            if (s.charAt(i) == '"') {
                b.append('"');
            }
        }
        b.append('"');
        print(b.toString());
    }

    /**
     * Writes a chunk of Data (D)
     * 
     * @param data data to be written
     */
    public void writeData(byte[] data) throws IOException {
        // FIXME: good unless strings will do encoding
        writeString(new String(data));
    }

    /**
     * Writes a Virtual Display Coordinate (VDC)
     * 
     * @param d VDC to be written
     */
    public void writeVDC(double d) throws IOException {
        if (vdcReal) {
            if (vdcFixedPrecision) {
                writeFixedPoint(d);
            } else {
                if (vdcDoublePrecision) {
                    print(d);
                } else {
                    print((float) d);
                }
            }
        } else {
            switch (vdcIntegerPrecision) {
            default:
            case 16:
                print((short) d);
                break;
            case 24: // FIXME
            case 32:
                print(d);
                break;
            }
        }
    }

    /**
     * Writes a Point (P)
     * 
     * @param p value to be written
     */
    public void writePoint(Point2D p) throws IOException {
        print("(");
        writeVDC(p.getX());
        print(", ");
        writeVDC(p.getY());
        print(")");
    }

    /**
     * Writes a Color (CO)
     * 
     * @param c color to be written
     */
    public void writeColor(Color c) throws IOException {
        if (direct) {
            writeColorDirect(c);
        } else {
            // FIXME: we should look up the color
            writeColorIndex((c.getRed() << 16) + (c.getGreen() << 8)
                    + c.getBlue());
        }
    }

    /**
     * Writes a Name (N)
     * 
     * @param name name to be written
     */
    public void writeName(int name) throws IOException {
        switch (namePrecision) {
        case 8:
            print(Math.max(0, Math.min(255, name)));
            break;
        default:
        case 16:
            print(Math.max(0, Math.min(65535, name)));
            break;
        case 24: // FIXME
        case 32:
            print(name);
            break;
        }
    }

    /**
     * Writes a Tag and a Terminator (;)
     * 
     * @param tag tag to be written
     */
    public void writeTag(Tag tag) throws IOException {

        CGMTag cgm = (CGMTag) tag;
        cgm.write(cgm.getTag(), this);
        println(";");
    }

    /**
     * @return the version of this CGM Writer
     */
    public int getVersion() {
        return version;
    }
}
