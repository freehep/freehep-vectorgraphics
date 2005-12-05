// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * JPEGTables TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/JPEGTables.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class JPEGTables extends DefinitionTag {

    private byte[] table;

    public JPEGTables() {
        super(8, 1);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        JPEGTables tag = new JPEGTables();
        tag.table = swf.readByte(len);
        swf.setJPEGTable(tag.table);
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeByte(table);
    }

    public String toString() {
        return super.toString() + "\n" + "  length: " + table.length + "\n";
    }
}
