// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * SoundStreamHead2 TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SoundStreamHead2.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class SoundStreamHead2 extends SoundStreamHead {

    public SoundStreamHead2(int playbackSoundRate, boolean playbackSoundStereo,
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

    public SoundStreamHead2() {
        super(45, 3);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        SoundStreamHead2 tag = new SoundStreamHead2();
        tag.read(swf, len);
        return tag;
    }
}
