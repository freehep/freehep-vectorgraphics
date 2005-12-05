// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * DefineMovie TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineMovie.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineMovie extends DefinitionTag {

    private int character;

    private String name;

    public DefineMovie(int id, String name) {
        this();
        character = id;
        this.name = name;
    }

    public DefineMovie() {
        super(38, 4);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DefineMovie tag = new DefineMovie();
        tag.character = swf.readUnsignedShort();
        swf.getDictionary().put(tag.character, tag);

        tag.name = swf.readString();
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeUnsignedShort(character);
        swf.writeString(name);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  character:  " + character + "\n");
        s.append("  name:       " + name);
        return s.toString();
    }
}
