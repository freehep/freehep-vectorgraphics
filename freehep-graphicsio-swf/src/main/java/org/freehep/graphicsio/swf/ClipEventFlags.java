// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * SWF Clip Event Flags
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/ClipEventFlags.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class ClipEventFlags {

    private boolean keyPress, dragOut, dragOver, rollOut, rollOver;

    private boolean releaseOutside, release, press, initialize, data;

    private boolean keyUp, keyDown, mouseUp, mouseDown, mouseMove;

    private boolean unload, enterFrame, load;

    /**
     * Read a ClipEventFlags from the stream.
     */
    public ClipEventFlags(SWFInputStream swf) throws IOException {

        if (swf.getVersion() >= 6) {
            swf.readUBits(14);
            keyPress = swf.readBitFlag();
            dragOut = swf.readBitFlag();
        }

        dragOver = swf.readBitFlag();
        rollOut = swf.readBitFlag();
        rollOver = swf.readBitFlag();
        releaseOutside = swf.readBitFlag();
        release = swf.readBitFlag();
        press = swf.readBitFlag();
        initialize = swf.readBitFlag();
        data = swf.readBitFlag();
        keyUp = swf.readBitFlag();
        keyDown = swf.readBitFlag();
        mouseUp = swf.readBitFlag();
        mouseDown = swf.readBitFlag();
        mouseMove = swf.readBitFlag();
        unload = swf.readBitFlag();
        enterFrame = swf.readBitFlag();
        load = swf.readBitFlag();
    }

    public void write(SWFOutputStream swf) throws IOException {
        if (swf.getVersion() >= 6) {
            swf.writeUBits(0, 14);
            swf.writeBitFlag(keyPress);
            swf.writeBitFlag(dragOut);
        }

        swf.writeBitFlag(dragOver);
        swf.writeBitFlag(rollOut);
        swf.writeBitFlag(rollOver);
        swf.writeBitFlag(releaseOutside);
        swf.writeBitFlag(release);
        swf.writeBitFlag(press);
        swf.writeBitFlag(initialize);
        swf.writeBitFlag(data);
        swf.writeBitFlag(keyUp);
        swf.writeBitFlag(keyDown);
        swf.writeBitFlag(mouseUp);
        swf.writeBitFlag(mouseDown);
        swf.writeBitFlag(mouseMove);
        swf.writeBitFlag(unload);
        swf.writeBitFlag(enterFrame);
        swf.writeBitFlag(load);
    }

    public boolean isKeyPress() {
        return keyPress;
    }

    public boolean isEndFlag() {
        // All bits were 0?
        return !(keyPress || dragOut || dragOver || rollOut || rollOver
                || releaseOutside || release || press || initialize || data
                || keyUp || keyDown || mouseUp || mouseDown || mouseMove
                || unload || enterFrame || load);
    }

    public String toString() {
        return "ClipEventFlags";
    }
}
