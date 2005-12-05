// Copyright 2001-2003 FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;

import org.freehep.util.io.ActionHeader;
import org.freehep.util.io.Tag;
import org.freehep.util.io.TagHeader;
import org.freehep.util.io.TaggedOutputStream;
import org.freehep.util.io.UndefinedTagException;

/**
 * CGM Binary Output Stream. Tags written with this OutputStream will produce a
 * binary CGM file. The class also holds the state for the various precisions,
 * defaults, etc...
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @author Ian Graham - support for long commands through partitioning
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CGMOutputStream.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CGMOutputStream extends TaggedOutputStream {

    private static short COMMAND_PARTITION_SIZE = Short.MAX_VALUE;

    private int version;

    private boolean direct = true;

    private int colorIndexPrecision = 8;

    private int directColorPrecision = 8;

    private int indexPrecision = 16;

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

    public CGMOutputStream(OutputStream os, int version) throws IOException {

        // CGM is big-endian
        super(os, new CGMTagSet(version), null, false);
        this.version = version;
    }

    public CGMOutputStream(OutputStream os) throws IOException {

        this(os, 1);
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

    void setIndexPrecision(int precision) {
        indexPrecision = precision;
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

    // FX
    public void writeFixedPoint(double d) throws IOException {
        int whole = (int) Math.floor(d);
        int frac = (int) Math.floor((d - whole) * 0x10000);
        writeShort(whole);
        writeShort(frac);
    }

    // FP
    public void writeFloatingPoint(double d) throws IOException {
        if (doublePrecision) {
            writeDouble(d);
        } else {
            writeFloat((float) d);
        }
    }

    // CI
    public void writeColorIndex(int i) throws IOException {
        switch (colorIndexPrecision) {
        case 8:
            writeByte(i);
            break;
        case 16:
            writeShort(i);
            break;
        default:
        case 24: // FIXME
        case 32:
            writeInt(i);
            break;
        }
    }

    // CCO
    public void writeColorComponent(int c) throws IOException {
        switch (directColorPrecision) {
        default:
        case 8:
        case 16:
        case 24: // FIXME
        case 32:
            writeUnsignedByte(c);
            break;
        }
    }

    // CD
    public void writeColorDirect(Color c) throws IOException {
        // FIXME3: depends on color model from version 3
        writeColorComponent(c.getRed());
        writeColorComponent(c.getGreen());
        writeColorComponent(c.getBlue());
        // writeColorComponent(0); // Don't do this until color model support is
        // provided, RGB is default
    }

    // IX
    public void writeIntegerIndex(int i) throws IOException {
        switch (indexPrecision) {
        case 8:
            writeByte(i);
            break;
        default:
        case 16:
            writeShort(i);
            break;
        case 24: // FIXME
        case 32:
            writeInt(i);
            break;
        }
    }

    // E
    public void writeEnumerate(int i) throws IOException {
        writeShort(i);
    }

    // I
    public void writeInteger(int i) throws IOException {
        switch (integerPrecision) {
        case 8:
            writeByte(i);
            break;
        default:
        case 16:
            writeShort(i);
            break;
        case 24: // FIXME
        case 32:
            writeInt(i);
            break;
        }
    }

    // R
    public void writeReal(double r) throws IOException {
        if (fixedPrecision) {
            writeFixedPoint(r);
        } else {
            writeFloatingPoint(r);
        }
    }

    // S, SF
    public void writeString(String s) throws IOException {
        int len = s.length();
        if (len < 0xFF) {
            // short
            writeUnsignedByte(len);
        } else {
            // long
            writeUnsignedByte(0xFF);
            writeUnsignedShort(len & 0x7FFF);
        }
        writeBytes(s);
    }

    // D
    public void writeData(byte[] data) throws IOException {
        // FIXME: good unless strings will do encoding
        writeString(new String(data));
    }

    // VDC
    public void writeVDC(double d) throws IOException {
        if (vdcReal) {
            if (vdcFixedPrecision) {
                writeFixedPoint(d);
            } else {
                if (vdcDoublePrecision) {
                    writeDouble(d);
                } else {
                    writeFloat((float) d);
                }
            }
        } else {
            switch (vdcIntegerPrecision) {
            default:
            case 16:
                writeShort((int) d);
                break;
            case 24: // FIXME
            case 32:
                writeInt((int) d);
                break;
            }
        }
    }

    // P
    public void writePoint(Point2D p) throws IOException {
        writeVDC(p.getX());
        writeVDC(p.getY());
    }

    // CO
    public void writeColor(Color c) throws IOException {
        if (direct) {
            writeColorDirect(c);
        } else {
            // FIXME: we should look up the color
            writeColorIndex((c.getRed() << 16) + (c.getGreen() << 8)
                    + c.getBlue());
        }
    }

    // N
    public void writeName(int name) throws IOException {
        switch (namePrecision) {
        case 8:
            writeByte(name);
            break;
        default:
        case 16:
            writeShort((int) name);
            break;
        case 24: // FIXME
        case 32:
            writeInt((int) name);
            break;
        }
    }

    protected void writeTagHeader(TagHeader header) throws IOException {

        byteAlign();

        int tagID = header.getTag();
        long length = header.getLength();
        if (length < 0x1F) {
            // short form
            writeUnsignedShort((tagID << 5) + (int) length);
        } else {
            // long form - length is placed in partition rather than header
            writeUnsignedShort((tagID << 5) + 0x001F);
        }
    }

    public void writeTag(Tag tag) throws IOException {

        int tagID = tag.getTag();

        if (!tagSet.exists(tagID))
            throw new UndefinedTagException(tagID);

        pushBuffer();
        tag.write(tagID, this);
        int align = getTagAlignment();
        int pad = (align - (getBufferLength() % align)) % align;
        for (int i = 0; i < pad; i++) {
            write(0);
        }
        int len = getBufferLength();
        byte[] buffer = popBufferBytes();
        TagHeader header = new TagHeader(tagID, len);
        writeTagHeader(header);
        if (len <= 30) {
            write(buffer, 0, len);
        } else {
            writePartitioned(buffer, len);
        }

        // pad tag with 0 if necessary
        if ((size() % 2) != 0) {
            writeUnsignedByte(0);
        }
    }

    private void writePartitioned(byte[] buffer, int len) throws IOException {
        int bufferOffset = 0;
        final short partitionSize = COMMAND_PARTITION_SIZE;
        while (len > partitionSize) {
            writeUnsignedShort(0x8000 | partitionSize);
            write(buffer, bufferOffset, partitionSize);
            bufferOffset += partitionSize;
            len -= partitionSize;
        }
        writeUnsignedShort(len);
        write(buffer, bufferOffset, len);
    }

    protected void writeActionHeader(ActionHeader header) throws IOException {

        // empty
    }

    public int getVersion() {
        return version;
    }
}
