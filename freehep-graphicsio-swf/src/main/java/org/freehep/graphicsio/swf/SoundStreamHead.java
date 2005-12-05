// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * SoundStreamHead TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SoundStreamHead.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class SoundStreamHead extends DefinitionTag {

    protected int playbackSoundRate;

    protected boolean playbackSoundSize16;

    protected boolean playbackSoundStereo;

    protected int streamSoundCompression;

    protected int streamSoundRate;

    protected boolean streamSoundSize16;

    protected boolean streamSoundStereo;

    protected int samples;

    protected int latencySeek;

    public SoundStreamHead(int playbackSoundRate, boolean playbackSoundStereo,
            int streamSoundRate, boolean streamSoundStereo, int samples) {
        this();
        this.playbackSoundRate = playbackSoundRate;
        this.playbackSoundSize16 = true;
        this.playbackSoundStereo = playbackSoundStereo;
        this.streamSoundCompression = 1;
        this.streamSoundRate = streamSoundRate;
        this.streamSoundSize16 = true;
        this.streamSoundStereo = streamSoundStereo;
        this.samples = samples;
    }

    public SoundStreamHead() {
        super(18, 1);
    }

    protected SoundStreamHead(int tagID, int version) {
        super(tagID, version);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {
        SoundStreamHead tag = new SoundStreamHead();
        tag.read(swf, len);
        return tag;
    }

    protected void read(SWFInputStream swf, int len) throws IOException {
        /* int reserved = (int) */ swf.readUBits(4);
        playbackSoundRate = (int) swf.readUBits(2);
        playbackSoundSize16 = swf.readBitFlag();
        playbackSoundStereo = swf.readBitFlag();
        streamSoundCompression = (int) swf.readUBits(4);
        streamSoundRate = (int) swf.readUBits(2);
        streamSoundSize16 = swf.readBitFlag();
        streamSoundStereo = swf.readBitFlag();
        samples = swf.readUnsignedShort();
        if (streamSoundCompression == 2)
            latencySeek = swf.readShort();
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        write(swf);
    }

    protected void write(SWFOutputStream swf) throws IOException {
        swf.writeUBits(0, 4);
        swf.writeUBits(playbackSoundRate, 2);
        swf.writeBitFlag(playbackSoundSize16);
        swf.writeBitFlag(playbackSoundStereo);
        swf.writeUBits(streamSoundCompression, 4);
        swf.writeUBits(streamSoundRate, 2);
        swf.writeBitFlag(streamSoundSize16);
        swf.writeBitFlag(streamSoundStereo);
        swf.writeUnsignedShort(samples);
        if (streamSoundCompression == 2)
            swf.writeShort(latencySeek);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString());
        return s.toString();
    }

}
