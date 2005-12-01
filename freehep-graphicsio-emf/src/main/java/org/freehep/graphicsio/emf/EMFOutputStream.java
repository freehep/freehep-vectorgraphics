// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.io.OutputStream;
import java.io.IOException;

import org.freehep.util.io.ActionHeader;
import org.freehep.util.io.Tag;
import org.freehep.util.io.TagHeader;
import org.freehep.util.io.TaggedOutputStream;

/**
 * EMF Binary Output Stream. Tags written with this OutputStream will
 * produce a binary EMF file.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFOutputStream.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class EMFOutputStream extends TaggedOutputStream {

    private String application;
    private String name;
    private int recordCount;
    private Rectangle imageBounds;
    private int version;
    private EMFHandleManager handles;
    private Dimension device;

    public EMFOutputStream(OutputStream os, Rectangle imageBounds,
                           EMFHandleManager handles,
                           String application, String name,
                           Dimension device, int version)
        throws IOException {

        // EMF is little-endian
        super(os, new EMFTagSet(version), null, true);
        this.recordCount = 0;
        this.version = version;
        this.imageBounds = imageBounds;
        this.handles = handles;
        this.application = application;
        this.name = name;
        this.device = device;

        // will be popped by close()
        pushBuffer();
    }

    public EMFOutputStream(OutputStream os, Rectangle imageBounds,
                           EMFHandleManager handles,
                           String application, String name, Dimension device)
        throws IOException {

        this(os, imageBounds, handles, application, name, device, 1);
    }

    public void close() throws IOException {
        int len = popBuffer();
        recordCount++;
        EMFHeader header = new EMFHeader(imageBounds, getVersion(), 0, len, recordCount,
                                         handles.maxHandlesUsed(), application, name, device);
        writeHeader(header);
        append();

        super.close();
    }

    // DWORD
    public void writeDWORD(int i) throws IOException {
        writeUnsignedInt(i);
    }

    // DWORD []
    public void writeDWORD(int[] w) throws IOException {
        for (int i=0; i<w.length; i++) {
            writeDWORD(w[i]);
        }
    }

    // WORD
    public void writeWORD(int s) throws IOException {
        writeUnsignedShort(s);
    }

    public void writeFLOAT(float f) throws IOException {
        writeFloat(f);
    }

    public void writeCOLORREF(Color c) throws IOException {
        writeByte(c.getRed());
        writeByte(c.getGreen());
        writeByte(c.getBlue());
        writeByte(0x00);
    }

    // COLOR16
    public void writeCOLOR16(Color c) throws IOException {
        writeShort(c.getRed() << 8);
        writeShort(c.getGreen() << 8);
        writeShort(c.getBlue() << 8);
        writeShort(c.getAlpha() << 8);
    }

    public void writeXFORM(AffineTransform t) throws IOException {
        writeFLOAT((float)t.getScaleX());
        writeFLOAT((float)t.getShearY());
        writeFLOAT((float)t.getShearX());
        writeFLOAT((float)t.getScaleY());
        writeFLOAT((float)t.getTranslateX());
        writeFLOAT((float)t.getTranslateY());
    }

    // POINTS []
    public void writePOINTS(Point[] p) throws IOException {
        writePOINTS(p.length, p);
    }

    public void writePOINTS(int n, Point[] p) throws IOException {
        for (int i=0; i<n; i++) {
            writePOINTS(p[i]);
        }
    }

    public void writePOINTS(Point p) throws IOException {
        writeSHORT((short)p.x);
        writeSHORT((short)p.y);
    }

    // POINTL []
    public void writePOINTL(Point[] p) throws IOException {
        writePOINTL(p.length, p);
    }

    public void writePOINTL(int n, Point[] p) throws IOException {
        for (int i=0; i<n; i++) {
            writePOINTL(p[i]);
        }
    }

    // POINTL
    public void writePOINTL(Point p) throws IOException {
        writeLONG(p.x);
        writeLONG(p.y);
    }

    // RECTL
    public void writeRECTL(Rectangle r) throws IOException {
        writeLONG(r.x);
        writeLONG(r.y);
        writeLONG(r.x+r.width);
        writeLONG(r.y+r.height);
    }

    // SIZEL
    public void writeSIZEL(Dimension d) throws IOException {
        writeLONG(d.width);
        writeLONG(d.height);
    }

    // UINT
    public void writeUINT(int i) throws IOException {
        writeUnsignedInt(i);
    }

    // ULONG
    public void writeULONG(int i) throws IOException {
        writeUnsignedInt(i);
    }

    // LONG
    public void writeLONG(int i) throws IOException {
        writeInt(i);
    }

    public void writeSHORT(short i) throws IOException {
        writeShort(i);
    }

    // BYTE []
    public void writeBYTE(byte[] b) throws IOException {
        writeByte(b);
    }

    // BYTE
    public void writeBYTE(byte b) throws IOException {
        writeByte(b);
    }

    // BYTE
    public void writeBYTE(int b) throws IOException {
        writeByte(b);
    }

    public void writeBYTE(boolean b) throws IOException {
        writeBYTE((b) ? 1 : 0);
    }

    public void writeWORD(boolean b) throws IOException {
        writeWORD((b) ? 1 : 0);
    }

    public void writeDWORD(boolean b) throws IOException {
        writeDWORD((b) ? 1 : 0);
    }

    public void writeWCHAR(String s) throws IOException {
        writeByte(s.getBytes("UTF-16LE"));
    }

    public void writeWCHAR(String s, int size) throws IOException {
        writeWCHAR(s);
        for (int i = size - s.length(); i > 0; i--) {
            writeWORD(0);
        }
    }

    protected int getTagAlignment() {
        return 4;
    }

    protected void writeTagHeader(TagHeader header) throws IOException {

        int tagID = header.getTag();
        long length = header.getLength();
        writeUnsignedInt(tagID);
        writeUnsignedInt(length + 8);
    }

    public void writeTag(Tag tag)
        throws IOException {

        recordCount++;
        super.writeTag(tag);
    }

    protected void writeActionHeader(ActionHeader header)
        throws IOException {
        // empty
    }

    public void writeHeader(EMFHeader header) throws IOException {
        header.write(this);
    }

    public int getVersion() {
        return version;
    }
}

