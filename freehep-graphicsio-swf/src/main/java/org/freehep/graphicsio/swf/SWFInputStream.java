// Copyright 2001-2006, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;

import org.freehep.util.io.ActionHeader;
import org.freehep.util.io.TagHeader;
import org.freehep.util.io.TaggedInputStream;

/**
 * This class extends the TaggedInputStream with several methods to read SWF
 * primitives from the stream and to read TagHeaders. It also handles the
 * management of the SWFDictionary.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SWFInputStream.java 3e48ba4ef214 2006/11/27 22:51:07 duns $
 */
public class SWFInputStream extends TaggedInputStream implements SWFConstants {

    private SWFDictionary dictionary;

    private byte[] jpegTable;

    public SWFInputStream(InputStream is) throws IOException {

        this(is, DEFAULT_VERSION);
    }

    public SWFInputStream(InputStream is, int version) throws IOException {

        this(is, new SWFTagSet(version), new SWFActionSet(version));
    }

    public SWFInputStream(InputStream is, SWFSpriteTagSet tagSet,
            SWFActionSet actionSet) throws IOException {

        // SWF is little-endian
        super(is, tagSet, actionSet, true);

        dictionary = new SWFDictionary();
    }

    /**
     * Read a fixed point value (16.16).
     */
    public float readFixed() throws IOException {

        byteAlign();
        int frac = readUnsignedByte();
        frac |= readUnsignedByte() << 8;
        int whole = readUnsignedByte();
        whole |= readUnsignedByte() << 8;
        return ((float) whole) + ((float) frac) / ((float) 0x10000);
    }

    /**
     * Read a fixed point value (8.8).
     */
    public float readFixed8() throws IOException {

        byteAlign();
        int frac = readUnsignedByte();
        int whole = readUnsignedByte();
        return ((float) whole) + ((float) frac) / ((float) 0x100);
    }

    /**
     * Read a rectangle from the stream.
     */
    public Rectangle2D readRect() throws IOException {

        byteAlign();
        int nbits = (int) readUBits(5);
        int xmin = (int) readSBits(nbits);
        int xmax = (int) readSBits(nbits);
        int ymin = (int) readSBits(nbits);
        int ymax = (int) readSBits(nbits);
        return new Rectangle2D.Double(xmin / TWIPS, ymin / TWIPS, (xmax - xmin)
                / TWIPS, (ymax - ymin) / TWIPS);
    }

    /**
     * Read an RGB value from the stream.
     */
    public Color readColor(boolean alpha) throws IOException {
        int r = readUnsignedByte();
        int g = readUnsignedByte();
        int b = readUnsignedByte();
        int a = (alpha) ? readUnsignedByte() : 255;
        return new Color(r, g, b, a);
    }

    /**
     * Read a matrix from the stream.
     */
    public AffineTransform readMatrix() throws IOException {

        byteAlign();

        // Set default values.
        float sx = 1.f;
        float sy = 1.f;
        float kx = 0.f;
        float ky = 0.f;
        float tx = 0.f;
        float ty = 0.f;

        // Get the scale bits.
        if (readBitFlag()) {
            int nbits = (int) readUBits(5);
            sx = readFBits(nbits);
            sy = readFBits(nbits);
        }

        // Rotate or skew values.
        if (readBitFlag()) {
            int nbits = (int) readUBits(5);
            kx = readFBits(nbits);
            ky = readFBits(nbits);
        }

        // Translation values.
        int nbits = (int) readUBits(5);
        tx = (float) readSBits(nbits) / TWIPS;
        ty = (float) readSBits(nbits) / TWIPS;

        return new AffineTransform(sx, ky, kx, sy, tx, ty);
    }

    /**
     * Read a string from the stream.
     */
    public String readString() throws IOException {
        if (getVersion() >= 6) {
            String s = readUTF();
            readByte();
            return s;
        } else {
            return readAsciiZString();
        }
    }

    public int readLanguageCode() throws IOException {
        return readUnsignedByte();
    }

    protected TagHeader readTagHeader() throws IOException {
        // Read the tag.
        byteAlign();
        int temp = read();
        // End of stream
        if (temp == -1)
            return null;

        temp |= readUnsignedByte() << 8;
        int tagID = temp >> 6;
        long length = temp & FIELD_MASK[5];
        if (length == 0x3f)
            length = readUnsignedInt();
        return new TagHeader(tagID, length);
    }

    protected ActionHeader readActionHeader() throws IOException {

        int actionCode = readUnsignedByte();
        if (actionCode == 0)
            return null;

        int length = 0;
        if ((actionCode & 0x80) > 0) {
            length = readUnsignedShort();
        }

        return new ActionHeader(actionCode, length);
    }

    private SWFHeader header;

    public SWFHeader readHeader() throws IOException {
        if (header == null) {
            header = new SWFHeader(this);
            int version = header.getVersion();
            tagSet = new SWFTagSet(version);
            actionSet = new SWFActionSet(version);
        }
        return header;
    }

    public SWFDictionary getDictionary() {
        return dictionary;
    }

    public void setJPEGTable(byte[] table) {
        jpegTable = table;
    }

    public byte[] getJPEGTable() {
        return jpegTable;
    }

    public int getVersion() {
        return ((SWFSpriteTagSet) tagSet).getVersion();
    }
}
