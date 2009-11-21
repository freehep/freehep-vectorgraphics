// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;
import java.util.Vector;

import org.freehep.util.io.Action;

/**
 * SWF Clip Action Record
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/ClipActionRecord.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class ClipActionRecord {

    private ClipEventFlags eventFlags;

    private long actionRecordSize;

    private int keyCode;

    private Vector<Action> actions;

    /**
     * Read a ClipActionRecord from the stream.
     */
    public ClipActionRecord(SWFInputStream swf) throws IOException {

        eventFlags = new ClipEventFlags(swf);
        if (eventFlags.isEndFlag())
            return;

        actionRecordSize = swf.readUnsignedInt();
        // FIXME is actually a long (unsigned int)
        swf.pushBuffer((int) (actionRecordSize - 4));
        if (eventFlags.isKeyPress())
            keyCode = swf.readUnsignedByte();

        while (swf.getLength() > 0) {
            actions.add(swf.readAction());
        }
        byte[] rest = swf.popBuffer();
        if (rest != null) {
            System.err.println("Corrupted ClipActionRecord, " + rest.length
                    + " bytes leftoever.");
        }
    }

    public void write(SWFOutputStream swf) throws IOException {
        eventFlags.write(swf);
        // FIXME, should do PushBuffer and calculate this size...
        swf.writeUnsignedInt(actionRecordSize);
        if (eventFlags.isKeyPress())
            swf.writeUnsignedByte(keyCode);

        for (int i = 0; i < actions.size(); i++) {
            SWFAction a = (SWFAction) actions.get(i);
            swf.writeAction(a);
        }
    }

    public boolean isEndRecord() {
        return eventFlags.isEndFlag();
    }

    public String toString() {
        return "ClipActionRecord " + actions.size();
    }
}
