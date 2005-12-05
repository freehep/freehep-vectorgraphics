// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Color;
import java.io.IOException;

import org.freehep.util.io.BitOutputStream;

/**
 * SWF Color Transform
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/ColorXform.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class ColorXform {

    private int rx = 0x0100;

    private int gx = 0x0100;

    private int bx = 0x0100;

    private int ax = 0x0100;

    private int rp = 0;

    private int gp = 0;

    private int bp = 0;

    private int ap = 0;

    public ColorXform(int rx, int gx, int bx, int ax, int rp, int gp, int bp,
            int ap) {
        this.rx = rx;
        this.gx = gx;
        this.bx = bx;
        this.ax = ax;
        this.rp = rp;
        this.gp = gp;
        this.bp = bp;
        this.ap = ap;
    }

    /**
     * Read a color transform from the stream.
     */
    public ColorXform(SWFInputStream swf, boolean hasAlpha) throws IOException {

        swf.byteAlign();

        boolean add = swf.readBitFlag();
        boolean mult = swf.readBitFlag();
        int nbits = (int) swf.readUBits(4);

        if (mult) {
            rx = (int) swf.readSBits(nbits);
            gx = (int) swf.readSBits(nbits);
            bx = (int) swf.readSBits(nbits);
            if (hasAlpha)
                ax = (int) swf.readSBits(nbits);
        }

        if (add) {
            rp = (int) swf.readSBits(nbits);
            gp = (int) swf.readSBits(nbits);
            bp = (int) swf.readSBits(nbits);
            if (hasAlpha)
                ap = (int) swf.readSBits(nbits);
        }
    }

    public Color transform(Color c) {
        int red = (int) Math.max(0, Math.min(((c.getRed() * rx) / 256.0) + rp,
                255));
        int green = (int) Math.max(0, Math.min(((c.getGreen() * gx) / 256.0)
                + gp, 255));
        int blue = (int) Math.max(0, Math.min(
                ((c.getBlue() * bx) / 256.0) + bp, 255));
        int alpha = (int) Math.max(0, Math.min(((c.getAlpha() * ax) / 256.0)
                + ap, 255));
        return new Color(red, green, blue, alpha);
    }

    public void write(SWFOutputStream swf, boolean hasAlpha) throws IOException {

        swf.byteAlign();

        boolean add = (rp != 0) || (gp != 0) || (bp != 0)
                || (hasAlpha && (ap != 0));
        boolean mult = (rx != 1) || (gx != 1) || (bx != 1)
                || (hasAlpha && (ax != 1));
        int nbits = 0;
        if (mult) {
            nbits = Math.max(nbits, BitOutputStream.minBits(rx, true));
            nbits = Math.max(nbits, BitOutputStream.minBits(gx, true));
            nbits = Math.max(nbits, BitOutputStream.minBits(bx, true));
            if (hasAlpha) {
                nbits = Math.max(nbits, BitOutputStream.minBits(ax, true));
            }
        }
        if (add) {
            nbits = Math.max(nbits, BitOutputStream.minBits(rp, true));
            nbits = Math.max(nbits, BitOutputStream.minBits(gp, true));
            nbits = Math.max(nbits, BitOutputStream.minBits(bp, true));
            if (hasAlpha) {
                nbits = Math.max(nbits, BitOutputStream.minBits(ap, true));
            }
        }
        swf.writeBitFlag(add);
        swf.writeBitFlag(mult);
        swf.writeUBits(nbits, 4);

        if (mult) {
            swf.writeSBits(rx, nbits);
            swf.writeSBits(gx, nbits);
            swf.writeSBits(bx, nbits);
            if (hasAlpha)
                swf.writeSBits(ax, nbits);
        }
        if (add) {
            swf.writeSBits(rp, nbits);
            swf.writeSBits(gp, nbits);
            swf.writeSBits(bp, nbits);
            if (hasAlpha)
                swf.writeSBits(ap, nbits);
        }
    }

    public String toString() {
        return "CXForm(" + rx + ", " + gx + ", " + bx + ", " + ax + " : " + rp
                + ", " + gp + ", " + bp + ", " + ap + ")";
    }
}
