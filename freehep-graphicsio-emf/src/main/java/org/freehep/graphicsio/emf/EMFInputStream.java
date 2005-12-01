// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.InputStream;
import java.io.IOException;
import java.util.Vector;

import org.freehep.util.io.ActionHeader;
import org.freehep.util.io.ActionSet;
import org.freehep.util.io.TagHeader;
import org.freehep.util.io.TagSet;
import org.freehep.util.io.TaggedInputStream;

/**
 * This class extends the TaggedInputStream with several methods
 * to read EMF primitives from the stream and to read TagHeaders.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFInputStream.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class EMFInputStream extends TaggedInputStream implements EMFConstants {

    public static int DEFAULT_VERSION = 1;

    public EMFInputStream(InputStream is)
        throws IOException {

        this(is, DEFAULT_VERSION);
    }

    public EMFInputStream(InputStream is, int version)
        throws IOException {

        this(is, new EMFTagSet(version));
    }

    public EMFInputStream(InputStream is, EMFTagSet tagSet)
        throws IOException {

        // EMF is little-endian
        super(is, tagSet, null, true);
    }

    public int readDWORD() throws IOException {
        long i = readUnsignedInt();
        return (int)i;
    }

    public int[] readDWORD(int size) throws IOException {
        int[] x = new int[size];
        for (int i=0; i<x.length; i++) {
            x[i] = readDWORD();
        }
        return x;
    }

    public int readWORD() throws IOException {
        return readUnsignedShort();
    }

    public int readLONG() throws IOException {
        return readInt();
    }

    public int[] readLONG(int size) throws IOException {
        int[] x = new int[size];
        for (int i=0; i<x.length; i++) {
            x[i] = readLONG();
        }
        return x;
    }

    public float readFLOAT() throws IOException {
        return readFloat();
    }

    public int readUINT() throws IOException {
        return (int)readUnsignedInt();
    }

    public int readULONG() throws IOException {
        return (int)readUnsignedInt();
    }

    public Color readCOLORREF() throws IOException {
        Color c = new Color(readUnsignedByte(),
                            readUnsignedByte(),
                            readUnsignedByte());
        readByte();
        return c;
    }

    public Color readCOLOR16() throws IOException {
        return new Color(readShort() >> 8,
                         readShort() >> 8,
                         readShort() >> 8,
                         readShort() >> 8);
    }

    public AffineTransform readXFORM() throws IOException {
        return new AffineTransform(readFLOAT(), readFLOAT(),
                                   readFLOAT(), readFLOAT(),
                                   readFLOAT(), readFLOAT());
    }

    public Rectangle readRECTL() throws IOException {
        int x = readLONG();
        int y = readLONG();
        int w = readLONG() - x;
        int h = readLONG() - y;
        return new Rectangle(x, y, w, h);
    }

    public Point readPOINTL() throws IOException {
        int x = readLONG();
        int y = readLONG();
        return new Point(x, y);
    }

    public Point[] readPOINTL(int size) throws IOException {
        Point[] p = new Point[size];
        for (int i=0; i<p.length; i++) {
            p[i] = readPOINTL();
        }
        return p;
    }

    public Point readPOINTS() throws IOException {
        int x = readShort();
        int y = readShort();
        return new Point(x, y);
    }

    public Point[] readPOINTS(int size) throws IOException {
        Point[] p = new Point[size];
        for (int i=0; i<p.length; i++) {
            p[i] = readPOINTS();
        }
        return p;
    }

    public Dimension readSIZEL() throws IOException {
        return new Dimension(readLONG(), readLONG());
    }

    public int readBYTE() throws IOException {
        return readByte();
    }

    public byte[] readBYTE(int size) throws IOException {
        byte[] x = new byte[size];
        for (int i=0; i<x.length; i++) {
            x[i] = (byte)readBYTE();
        }
        return x;
    }

    public boolean readBOOLEAN() throws IOException {
        return (readBYTE() != 0) ? true : false;
    }

    public String readWCHAR(int size) throws IOException {
       byte[] bytes = readByte(2*size);
       int length = 2*size;
       for (int i=0; i<2*size; i+=2)
       {
          if (bytes[i]==0 && bytes[i+1] ==0)
          {
             length = i;
             break;
          }
       }
       return new String(bytes, 0, length, "UTF-16LE");
    }
    
    protected TagHeader readTagHeader() throws IOException {
        // Read the tag.
//        byteAlign();
        int tagID = read();
        // End of stream
        if (tagID == -1) return null;

        tagID |= readUnsignedByte() << 8;
        tagID |= readUnsignedByte() << 16;
        tagID |= readUnsignedByte() << 24;

        long length = readDWORD();
        return new TagHeader(tagID, length-8);
    }

    protected ActionHeader readActionHeader() throws IOException {
        return null;
    }

    private EMFHeader header;

    public EMFHeader readHeader() throws IOException {
        if (header == null) {
            header = new EMFHeader(this);
        }
        return header;
    }

    public int getVersion() {
        return DEFAULT_VERSION;
    }
}

