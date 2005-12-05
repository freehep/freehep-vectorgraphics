// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * RemoveObject2 TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/RemoveObject2.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class RemoveObject2 extends ControlTag {

    private int depth;

    public RemoveObject2(int depth) {
        this();
        this.depth = depth;
    }

    public RemoveObject2() {
        super(28, 3);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {
        RemoveObject2 tag = new RemoveObject2();
        tag.depth = swf.readUnsignedShort();
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeUnsignedShort(depth);
    }

    public String toString() {
        return super.toString() + "\n" + "  depth: " + depth;
    }
}
