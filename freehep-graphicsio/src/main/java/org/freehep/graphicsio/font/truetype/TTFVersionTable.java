// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.font.truetype;

import java.io.IOException;

/**
 * VERSION Table.
 * 
 * @author Simon Fischer
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/font/truetype/TTFVersionTable.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public abstract class TTFVersionTable extends TTFTable {

    public int minorVersion;

    public int majorVersion;

    public void readVersion() throws IOException {
        majorVersion = ttf.readUShort();
        minorVersion = ttf.readUShort();
    }

    public String toString() {
        return super.toString() + " v" + majorVersion + "." + minorVersion;
    }

}
