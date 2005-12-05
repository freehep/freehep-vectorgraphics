// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * RemoveObject TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/RemoveObject.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class RemoveObject extends ControlTag {

    private int depth;

    private int character;

    public RemoveObject(int depth, int id) {
        this();
        this.depth = depth;
        character = id;
    }

    public RemoveObject() {
        super(5, 1);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {
        RemoveObject tag = new RemoveObject();
        tag.character = swf.readUnsignedShort();
        tag.depth = swf.readUnsignedShort();
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeUnsignedShort(character);
        swf.writeUnsignedShort(depth);
    }

    public String toString() {
        return super.toString() + "\n" + "  depth: " + depth + "\n"
                + "  character: " + character;
    }

}
