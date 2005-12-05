// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * SWF SoundInfo
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SoundInfo.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class SoundInfo {

    private boolean syncStop;

    private boolean syncNoMultiple;

    private long inPoint = -1;

    private long outPoint = -1;

    private int loop = -1;

    private SoundEnvelope[] envelope;

    public SoundInfo(boolean syncStop, boolean syncNoMultiple, long inPoint,
            long outPoint, int loop, SoundEnvelope[] envelope) {
        this.syncStop = syncStop;
        this.syncNoMultiple = syncNoMultiple;
        this.inPoint = inPoint;
        this.outPoint = outPoint;
        this.loop = loop;
        this.envelope = envelope;
    }

    public SoundInfo(SWFInputStream input) throws IOException {
        input.readUBits(2);
        syncStop = input.readBitFlag();
        syncNoMultiple = input.readBitFlag();
        boolean hasEnvelope = input.readBitFlag();
        boolean hasLoops = input.readBitFlag();
        boolean hasOutPoint = input.readBitFlag();
        boolean hasInPoint = input.readBitFlag();

        if (hasInPoint) {
            inPoint = input.readUnsignedInt();
        }
        if (hasOutPoint) {
            outPoint = input.readUnsignedInt();
        }
        if (hasLoops) {
            loop = input.readUnsignedShort();
        }

        if (hasEnvelope) {
            int numPoints = input.readUnsignedByte();
            envelope = new SoundEnvelope[numPoints];
            for (int i = 0; i < numPoints; i++) {
                envelope[i] = new SoundEnvelope(input);
            }
        }
    }

    public void write(SWFOutputStream swf) throws IOException {

        swf.writeUBits(0, 2);
        swf.writeBitFlag(syncStop);
        swf.writeBitFlag(syncNoMultiple);
        swf.writeBitFlag(envelope != null);
        swf.writeBitFlag(loop >= 0);
        swf.writeBitFlag(outPoint >= 0);
        swf.writeBitFlag(inPoint >= 0);

        if (inPoint >= 0) {
            swf.writeUnsignedInt(inPoint);
        }
        if (outPoint >= 0) {
            swf.writeUnsignedInt(outPoint);
        }
        if (loop >= 0) {
            swf.writeUnsignedShort(loop);
        }

        if (envelope != null) {
            swf.writeUnsignedByte(envelope.length);
            for (int i = 0; i < envelope.length; i++) {
                envelope[i].write(swf);
            }
        }

    }

    public String toString() {
        StringBuffer s = new StringBuffer("SoundInfo ");
        s.append("syncStop: " + syncStop);
        s.append(", syncNoMultiple: " + syncNoMultiple);
        s.append(", in: " + inPoint);
        s.append(", out: " + outPoint);
        s.append(", loop: " + loop);
        if (envelope != null) {
            s.append(" [");
            for (int i = 0; i < envelope.length; i++) {
                s.append(envelope[i]);
                s.append(" ");
            }
            s.append("]");
        }
        return s.toString();
    }
}
