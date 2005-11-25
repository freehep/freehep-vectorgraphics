// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.font.truetype;

import java.io.IOException;

/**
 * VERSION Table.
 *
 *  @author Simon Fischer
 *  @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/font/truetype/TTFVersionTable.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public abstract class TTFVersionTable extends TTFTable {

    public int minorVersion;
    public int majorVersion;

    public void readVersion() throws IOException {
        majorVersion = ttf.readUShort();
        minorVersion = ttf.readUShort();
    }

    public String toString() {
	    return super.toString()+" v"+majorVersion+"."+minorVersion;
    }

}
