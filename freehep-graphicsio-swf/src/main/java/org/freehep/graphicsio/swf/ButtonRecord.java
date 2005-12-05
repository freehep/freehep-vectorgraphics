// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.geom.AffineTransform;
import java.io.IOException;

/**
 * SWF Button Record
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/ButtonRecord.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class ButtonRecord {

    public final static int UP = 0x01;

    public final static int OVER = 0x02;

    public final static int DOWN = 0x04;

    public final static int HIT = 0x08;

    private boolean hitTest;

    private boolean down;

    private boolean over;

    private boolean up;

    private int id;

    private int depth;

    private AffineTransform matrix;

    private ColorXform cxform;

    public ButtonRecord(boolean hitTest, boolean down, boolean over,
            boolean up, int id, int depth, AffineTransform matrix,
            ColorXform cxform) {
        this.hitTest = hitTest;
        this.down = down;
        this.over = over;
        this.up = up;
        this.id = id;
        this.depth = depth;
        this.matrix = matrix;
        this.cxform = cxform;
    }

    public ButtonRecord(boolean hitTest, boolean down, boolean over,
            boolean up, int id, int depth, AffineTransform matrix) {
        this(hitTest, down, over, up, id, depth, matrix, null);
    }

    public ButtonRecord(SWFInputStream input, boolean hasColorXform)
            throws IOException {
        /* int reserved = (int) */ input.readUBits(4);
        hitTest = input.readBitFlag();
        down = input.readBitFlag();
        over = input.readBitFlag();
        up = input.readBitFlag();

        if (isEndRecord())
            return;

        id = input.readUnsignedShort();
        depth = input.readUnsignedShort();
        matrix = input.readMatrix();
        if (hasColorXform) {
            cxform = new ColorXform(input, true);
        } else {
            // NOTE: it may be just alignment, FREEHEP-535
            input.readUnsignedByte();
        }
    }

    public void write(SWFOutputStream swf) throws IOException {
        swf.writeUBits(0, 4);
        swf.writeBitFlag(hitTest);
        swf.writeBitFlag(down);
        swf.writeBitFlag(over);
        swf.writeBitFlag(up);
        swf.writeUnsignedShort(id);
        swf.writeUnsignedShort(depth);
        swf.writeMatrix(matrix);
        if (cxform != null) {
            cxform.write(swf, true);
        } else {
            // NOTE: it may be just alignment, FREEHEP-535
            swf.writeUnsignedByte(0);
        }
    }

    public boolean isEndRecord() {
        return !(hitTest || down || over || up);
    }

    public String toString() {
        return "ButtonRecord char:" + id + " depth:" + depth + " " + matrix
                + " " + ((cxform != null) ? "" + cxform : "");
    }
}
