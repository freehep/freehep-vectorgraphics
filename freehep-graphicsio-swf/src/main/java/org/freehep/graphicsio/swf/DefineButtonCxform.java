// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * DefineButtonCxform TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineButtonCxform.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineButtonCxform extends DefinitionTag {

    private int character;

    private ColorXform cxform;

    public DefineButtonCxform(int id, ColorXform cxform) {
        this();
        character = id;
        this.cxform = cxform;
    }

    public DefineButtonCxform() {
        super(23, 2);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DefineButtonCxform tag = new DefineButtonCxform();
        tag.character = swf.readUnsignedShort();
        swf.getDictionary().put(tag.character, tag);
        tag.cxform = new ColorXform(swf, false);
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {

        swf.writeUnsignedShort(character);
        cxform.write(swf, false);
    }

    public String toString() {
        return super.toString() + "\n" + "  character: " + character + "\n"
                + "  cxform:    " + cxform + "\n";
    }
}
