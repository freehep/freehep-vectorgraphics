// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * SWF SoundEnvelope.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SoundEnvelope.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class SoundEnvelope {

    private long pos44;

    private int leftLevel, rightLevel;

    public SoundEnvelope(long pos44, int leftLevel, int rightLevel) {
        this.pos44 = pos44;
        this.leftLevel = leftLevel;
        this.rightLevel = rightLevel;
    }

    public SoundEnvelope(SWFInputStream input) throws IOException {
        pos44 = input.readUnsignedInt();
        leftLevel = input.readUnsignedShort();
        rightLevel = input.readUnsignedShort();
    }

    public void write(SWFOutputStream swf) throws IOException {
        swf.writeUnsignedInt(pos44);
        swf.writeUnsignedShort(leftLevel);
        swf.writeUnsignedShort(rightLevel);
    }

    public String toString() {
        return "SoundEnvelope pos44: " + pos44 + ", level(L,R): " + leftLevel
                + ", " + rightLevel;
    }
}
