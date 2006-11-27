// Copyright 2001-2006, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;

import org.freehep.util.io.ActionHeader;
import org.freehep.util.io.TagHeader;
import org.freehep.util.io.TaggedOutputStream;

/**
 * This class extends the TaggedOutputStream with several methods to write SWF
 * primitives to the stream and to write TagHeaders.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SWFOutputStream.java fe6d709a107e 2006/11/27 18:25:46 duns $
 */
public class SWFOutputStream extends TaggedOutputStream implements SWFConstants {

    private Dimension size;

    private float frameRate;

    private int frameCount = 0;

    private boolean compress;

    private final static AffineTransform identityMatrix = new AffineTransform();

    public SWFOutputStream(OutputStream os, Dimension size, float frameRate,
            boolean compress) throws IOException {

        this(os, DEFAULT_VERSION, size, frameRate, compress);
    }

    public SWFOutputStream(OutputStream os, int version, Dimension size,
            float frameRate, boolean compress) throws IOException {

        this(os, new SWFTagSet(version), new SWFActionSet(version), size,
                frameRate, compress);
    }

    public SWFOutputStream(OutputStream os, SWFTagSet tagSet,
            SWFActionSet actionSet, Dimension size, float frameRate,
            boolean compress) throws IOException {

        // SWF is little-endian
        super(os, tagSet, actionSet, true);
        this.size = size;
        this.frameRate = frameRate;
        this.compress = compress;

        // will be popped by close()
        pushBuffer();
        
        // add FileAttributes
        writeTag(new FileAttributes());
    }

    public void close() throws IOException {
        long len = popBuffer() + SWFHeader.size();
        SWFHeader header = new SWFHeader(getVersion(), len, size, frameRate,
                frameCount, compress);
        writeHeader(header);
        append();

        super.close();
    }

    public void writeTag(ShowFrame tag) throws IOException {

        frameCount++;
        super.writeTag(tag);
    }

    public void writeFixed(double d) throws IOException {

        byteAlign();
        long whole = (long) d;
        long frac = (long) ((d - whole) * 0x10000);

        long fixed = ((whole & 0xFFFF) << 16) | (frac & 0xFFFF);
        writeUnsignedInt(fixed);
    }

    public void writeFixed8(double d) throws IOException {

        byteAlign();
        int whole = (int) d;
        int frac = (int) ((d - whole) * 0x100);

        int fixed = ((whole & 0xFF) << 8) | (frac & 0xFF);
        writeUnsignedShort(fixed);
    }

    public void writeRect(Rectangle2D rect) throws IOException {

        int nbits = 0;
        nbits = Math.max(nbits, minBits((int) (rect.getMinX() * TWIPS), true));
        nbits = Math.max(nbits, minBits((int) (rect.getMaxX() * TWIPS), true));
        nbits = Math.max(nbits, minBits((int) (rect.getMinY() * TWIPS), true));
        nbits = Math.max(nbits, minBits((int) (rect.getMaxY() * TWIPS), true));
        writeRect(rect, nbits);
    }

    public void writeRect(Rectangle2D rect, int nbits) throws IOException {

        byteAlign();
        writeUBits(nbits, 5);
        writeSBits((int) (rect.getMinX() * TWIPS), nbits);
        writeSBits((int) (rect.getMaxX() * TWIPS), nbits);
        writeSBits((int) (rect.getMinY() * TWIPS), nbits);
        writeSBits((int) (rect.getMaxY() * TWIPS), nbits);
    }

    public void writeColor(Color color, boolean alpha) throws IOException {

        byteAlign();
        writeUnsignedByte(color.getRed());
        writeUnsignedByte(color.getGreen());
        writeUnsignedByte(color.getBlue());
        if (alpha)
            writeUnsignedByte(color.getAlpha());
    }

    public void writeMatrix(AffineTransform matrix) throws IOException {

        if (matrix == null)
            matrix = identityMatrix;

        byteAlign();

        // Get the scale bits.
        if ((matrix.getScaleX() != 1.f) || (matrix.getScaleY() != 1.f)) {
            writeBitFlag(true);
            int nbits = 0;
            nbits = Math.max(nbits, minBits((float) matrix.getScaleX()));
            nbits = Math.max(nbits, minBits((float) matrix.getScaleY()));
            writeUBits(nbits, 5);
            writeFBits((float) matrix.getScaleX(), nbits);
            writeFBits((float) matrix.getScaleY(), nbits);
        } else {
            writeBitFlag(false);
        }

        // Rotate or skew values.
        if ((matrix.getShearX() != 0.f) || (matrix.getShearY() != 0.f)) {
            writeBitFlag(true);
            int nbits = 0;
            nbits = Math.max(nbits, minBits((float) matrix.getShearY()));
            nbits = Math.max(nbits, minBits((float) matrix.getShearX()));
            writeUBits(nbits, 5);
            writeFBits((float) matrix.getShearY(), nbits);
            writeFBits((float) matrix.getShearX(), nbits);
        } else {
            writeBitFlag(false);
        }

        // Translation values.
        int nbits = 0;
        nbits = Math.max(nbits, minBits((int) (matrix.getTranslateX() * TWIPS),
                true));
        nbits = Math.max(nbits, minBits((int) (matrix.getTranslateY() * TWIPS),
                true));
        writeUBits(nbits, 5);
        writeSBits((int) (matrix.getTranslateX() * TWIPS), nbits);
        writeSBits((int) (matrix.getTranslateY() * TWIPS), nbits);
    }

    public void writeString(String s) throws IOException {
        if (getVersion() >= 6) {
            writeUTF(s);
            writeByte(0);
        } else {
            writeAsciiZString(s);
        }
    }

    public void writeLanguageCode(int code) throws IOException {
        writeUnsignedByte(code);
    }

    protected void writeTagHeader(TagHeader tagHeader) throws IOException {
        byteAlign();
        int th = (tagHeader.getTag() << 6);
        if (tagHeader.getLength() >= 0x3f) {
            th |= 0x3f;
            writeUnsignedShort(th);
            writeUnsignedInt(tagHeader.getLength());
        } else {
            th |= tagHeader.getLength();
            writeUnsignedShort(th);
        }
    }

    protected void writeActionHeader(ActionHeader header) throws IOException {

        int actionCode = header.getAction();
        long length = header.getLength();
        writeUnsignedByte(actionCode);

        if ((actionCode & 0x80) > 0) {
            writeUnsignedShort((int) length);
        }
    }

    public void writeHeader(SWFHeader header) throws IOException {
        header.write(this);
    }

    public int getVersion() {
        return ((SWFTagSet) tagSet).getVersion();
    }
}