// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * DefineSound TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineSound.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineSound extends DefinitionTag {

    public final static int NONE = 0;

    public final static int ADPCM = 1;

    public final static int MP3 = 2;

    public final static int NELLYMOSER = 3;

    public final static int RATE_5_5 = 0;

    public final static int RATE_11 = 1;

    public final static int RATE_22 = 2;

    public final static int RATE_44 = 3;

    public final static int BYTE = 0;

    public final static int SHORT = 1;

    public final static int MONO = 0;

    public final static int STEREO = 1;

    private int character;

    private int format;

    private int rate;

    private int size;

    private boolean stereo;

    private long samples;

    private int[] data;

    public DefineSound(int id, int format, int rate, int size, boolean stereo,
            long samples, int[] data) {
        this();
        character = id;
        this.format = format;
        this.rate = rate;
        this.size = size;
        this.stereo = stereo;
        this.samples = samples;
        this.data = data;
    }

    public DefineSound() {
        super(14, 1);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DefineSound tag = new DefineSound();
        tag.character = swf.readUnsignedShort();
        swf.getDictionary().put(tag.character, tag);
        tag.format = (int) swf.readUBits(4);
        tag.rate = (int) swf.readUBits(2);
        tag.size = swf.readBitFlag() ? SHORT : BYTE;
        tag.stereo = swf.readBitFlag();
        tag.samples = swf.readUnsignedInt();
        tag.data = swf.readUnsignedByte(len - 7);
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeUnsignedShort(character);
        swf.writeUBits(format, 4);
        swf.writeUBits(rate, 2);
        swf.writeBitFlag(size == SHORT);
        swf.writeBitFlag(stereo);
        swf.writeUnsignedInt(samples);
        swf.writeUnsignedByte(data);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  character:  " + character + "\n");
        s.append("  format:" + format + "\n");
        s.append("  rate:" + rate + "\n");
        s.append("  size:" + size + "\n");
        s.append("  stereo:" + stereo + "\n");
        s.append("  samples:" + samples + "\n");
        s.append("  length:" + data.length + "\n");
        return s.toString();
    }
}
