// Copyright 2001-2005, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * SWF File Header.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SWFHeader.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class SWFHeader implements SWFConstants {

    private int version;

    private long length;

    private Dimension size;

    private float rate;

    private int count;

    private boolean compress;

    public SWFHeader(int version, long length, Dimension size, float rate,
            int count, boolean compress) {
        this.version = version;
        this.length = length;
        this.size = size;
        this.rate = rate;
        this.count = count;
        this.compress = compress;
    }

    public SWFHeader(SWFInputStream swf) throws IOException {

        // NOTE: page 8 and 13 are in conflict about the HEADER for compressed
        // files.
        // the tag that can be read by Flash is CWS (and not FWC).

        int s = swf.readUnsignedByte();
        if (((int) 'F' != s) && ((int) 'C' != s))
            throw new IOException();
        if ((int) 'W' != swf.readUnsignedByte())
            throw new IOException();
        if ((int) 'S' != swf.readUnsignedByte())
            throw new IOException();
        version = swf.readUnsignedByte();
        length = swf.readUnsignedInt();

        compress = (version >= 6) && ((int) 'C' == s);
        if (compress)
            swf.startDecompressing();

        Rectangle2D frame = swf.readRect();
        size = new Dimension((int) frame.getMaxX(), (int) frame.getMaxY());
        rate = (float) swf.readUnsignedShort() / 256f;
        count = swf.readUnsignedShort();
    }

    public void write(SWFOutputStream swf) throws IOException {
        // NOTE: page 8 and 13 are in conflict about the HEADER for compressed
        // files.
        // the tag that can be read by Flash is CWS (and not FWC).

        if ((version >= 6) && compress) {
            swf.writeUnsignedByte('C');
        } else {
            swf.writeUnsignedByte('F');
        }
        swf.writeUnsignedByte('W');
        swf.writeUnsignedByte('S');
        swf.writeUnsignedByte(version);
        swf.writeUnsignedInt(length);

        if ((version >= 6) && compress)
            swf.startCompressing();

        // the rectangle seems to may be 16 bits instead of 15...
        Rectangle2D frame = new Rectangle2D.Double(0, 0, size.width,
                size.height);
        swf.writeRect(frame);
        swf.writeUnsignedShort((int) (rate * 256f));
        swf.writeUnsignedShort(count);
    }

    public static int size() {
        return 21; // this is fixed
    }

    public int getVersion() {
        return version;
    }

    public long getLength() {
        return length;
    }

    public boolean isCompressed() {
        return compress;
    }

    public Dimension getSize() {
        return size;
    }

    public float getRate() {
        return rate;
    }

    public int getCount() {
        return count;
    }

    public String toString() {
        StringBuffer s = new StringBuffer("SWF Header\n");
        s.append("  version: " + version + "\n");
        s.append("  length: " + length + "\n");
        s.append("  compress: " + compress + "\n");
        s.append("  size: " + size + "\n");
        s.append("  rate: " + rate + "\n");
        s.append("  count: " + count);
        return s.toString();
    }
}
