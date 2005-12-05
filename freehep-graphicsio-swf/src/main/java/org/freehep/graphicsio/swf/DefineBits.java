// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * DefineBits TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineBits.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineBits extends DefinitionTag {

    private int character;

    private byte[] data;

//    private BufferedImage image;

    public DefineBits() {
        super(6, 1);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DefineBits tag = new DefineBits();
        tag.character = swf.readUnsignedShort();
        swf.getDictionary().put(tag.character, tag);

        tag.data = swf.readByte(len - 2);

        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {

        swf.writeUnsignedShort(character);
        swf.writeByte(data);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  character:  " + character + "\n");
        s.append("  length:     " + data.length + "\n");
        return s.toString();
    }
}
