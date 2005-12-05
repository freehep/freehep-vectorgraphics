// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.geom.AffineTransform;
import java.io.EOFException;
import java.io.IOException;

/**
 * PlaceObject TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/PlaceObject.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class PlaceObject extends ControlTag {

    private int character;

    private int depth;

    private AffineTransform matrix;

    private ColorXform cxform = null;

    public PlaceObject(int id, int depth, AffineTransform matrix) {
        this(id, depth, matrix, null);
    }

    public PlaceObject(int id, int depth, AffineTransform matrix,
            ColorXform cxform) {
        this();
        character = id;
        this.depth = depth;
        this.matrix = matrix;
        this.cxform = cxform;
    }

    public PlaceObject() {
        super(4, 1);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        PlaceObject tag = new PlaceObject();
        tag.character = swf.readUnsignedShort();
        tag.depth = swf.readUnsignedShort();
        tag.matrix = swf.readMatrix();

        try {
            tag.cxform = new ColorXform(swf, false);
        } catch (EOFException e) {
        }

        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeUnsignedShort(character);
        swf.writeUnsignedShort(depth);
        swf.writeMatrix(matrix);
        if (cxform != null) {
            cxform.write(swf, false);
        }
    }

    public String toString() {
        return super.toString() + "\n" + "  depth: " + depth + "\n"
                + "  character: " + character + "\n" + "  matrix: " + matrix
                + "\n" + "  xform: " + cxform;
    }
}
