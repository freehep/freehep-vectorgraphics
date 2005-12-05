// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.EOFException;
import java.io.IOException;

/**
 * DefineButtonSound TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineButtonSound.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineButtonSound extends DefinitionTag {

    private int character;

    private int[] soundID;

    private SoundInfo[] info;

    public DefineButtonSound(int id, int[] soundID, SoundInfo[] info) {
        this();
        character = id;
        this.soundID = soundID;
        this.info = info;
    }

    public DefineButtonSound() {
        super(17, 2);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DefineButtonSound tag = new DefineButtonSound();
        tag.character = swf.readUnsignedShort();
        swf.getDictionary().put(tag.character, tag);
        tag.soundID = new int[4];
        tag.info = new SoundInfo[4];
        try {
            for (int i = 0; i < 4; i++) {
                tag.soundID[i] = swf.readUnsignedShort();
                tag.info[i] = new SoundInfo(swf);
            }
        } catch (EOFException e) { /* seems not always 4 are filled */
        }
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {

        swf.writeUnsignedShort(character);
        for (int i = 0; i < 4; i++) {
            swf.writeUnsignedShort(soundID[i]);
            if (info[i] != null)
                info[i].write(swf);
            else
                swf.writeUnsignedByte(0);
        }
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  character:  " + character + "\n");
        for (int i = 0; i < soundID.length; i++) {
            s.append("  ");
            s.append(soundID[i]);
            s.append(": ");
            s.append(info[i]);
            s.append("\n");
        }
        return s.toString();
    }
}
