// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Vector;

/**
 * PlaceObject2 TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/PlaceObject2.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class PlaceObject2 extends ControlTag {

    private boolean move;

    private int depth;

    private int character = -1;

    private AffineTransform matrix = null;

    private ColorXform cxform = null;

    private int ratio = -1;

    private String name = null;

    private int clipDepth = -1;

    private ClipEventFlags allEventFlags = null;

    private Vector<ClipActionRecord> clipActionRecords = null;

    public PlaceObject2(int id, int depth, AffineTransform matrix) {
        this(false, depth, id, matrix, null, 0, null, 0);
    }

    public PlaceObject2(int id, int depth, AffineTransform matrix, int clipDepth) {
        this(false, depth, id, matrix, null, 0, null, clipDepth);
    }

    public PlaceObject2(boolean move, int depth, int id,
            AffineTransform matrix, ColorXform cxform, int ratio, String name,
            int clipDepth) {
        this();
        this.move = move;
        this.depth = depth;
        character = id;
        this.matrix = matrix;
        this.cxform = cxform;
        this.ratio = ratio;
        this.name = name;
        this.clipDepth = clipDepth;
    }

    public PlaceObject2() {
        super(26, 3);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        PlaceObject2 tag = new PlaceObject2();
        boolean hasClipActions = swf.readBitFlag();
        boolean hasClipDepth = swf.readBitFlag();
        boolean hasName = swf.readBitFlag();
        boolean hasRatio = swf.readBitFlag();
        boolean hasColorXform = swf.readBitFlag();
        boolean hasMatrix = swf.readBitFlag();
        boolean hasCharacter = swf.readBitFlag();
        move = swf.readBitFlag();

        tag.depth = swf.readUnsignedShort();

        if (hasCharacter)
            tag.character = swf.readUnsignedShort();
        if (hasMatrix)
            tag.matrix = swf.readMatrix();
        if (hasColorXform)
            tag.cxform = new ColorXform(swf, true);
        if (hasRatio)
            tag.ratio = swf.readUnsignedShort();
        if (hasName)
            tag.name = swf.readString();
        if (hasClipDepth)
            tag.clipDepth = swf.readUnsignedShort();
        if (hasClipActions) {
            swf.readUnsignedShort(); // always 0
            tag.allEventFlags = new ClipEventFlags(swf);

            tag.clipActionRecords = new Vector<ClipActionRecord>();
            ClipActionRecord clipActionRecord = new ClipActionRecord(swf);
            while (clipActionRecord.isEndRecord()) {
                tag.clipActionRecords.add(clipActionRecord);
                clipActionRecord = new ClipActionRecord(swf);
            }
        }

        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeBitFlag(allEventFlags != null);
        swf.writeBitFlag(clipDepth > 0);
        swf.writeBitFlag(name != null);
        swf.writeBitFlag(ratio >= 0);
        swf.writeBitFlag(cxform != null);
        swf.writeBitFlag(matrix != null);
        swf.writeBitFlag(character >= 0);
        swf.writeBitFlag(move);

        swf.writeUnsignedShort(depth);

        if (character >= 0)
            swf.writeUnsignedShort(character);
        if (matrix != null)
            swf.writeMatrix(matrix);
        if (cxform != null)
            cxform.write(swf, true);
        if (ratio >= 0)
            swf.writeUnsignedShort(ratio);
        if (name != null)
            swf.writeString(name);
        if (clipDepth > 0)
            swf.writeUnsignedShort(clipDepth);
        if (allEventFlags != null) {
            swf.writeUnsignedShort(0); // always 0
            allEventFlags.write(swf);

            for (int i = 0; i < clipActionRecords.size(); i++) {
                ClipActionRecord clipActionRecord = clipActionRecords
                        .get(i);
                clipActionRecord.write(swf);
            }
            swf.writeUnsignedInt(0); // end
        }
    }

    public String toString() {
        StringBuffer s = new StringBuffer(super.toString() + "\n");
        if (move)
            s.append("  move\n");
        s.append("  depth: " + depth + "\n");
        if (character >= 0)
            s.append("  character: " + character + "\n");
        if (matrix != null)
            s.append("  matrix: " + matrix + "\n");
        if (cxform != null)
            s.append("  xform: " + cxform + "\n");
        if (ratio >= 0)
            s.append("  ratio: " + ratio + "\n");
        if (name != null)
            s.append("  name: " + name + "\n");
        if (clipDepth > 0)
            s.append("  clipDepth: " + clipDepth + "\n");
        if (allEventFlags != null) {
            s.append("  allEventFlags: " + allEventFlags + "\n");
            for (int i = 0; i < clipActionRecords.size(); i++) {
                s.append("     " + clipActionRecords.get(i) + "\n");
            }
        }
        return s.toString();
    }
}
