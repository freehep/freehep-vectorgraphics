// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.font.truetype;

import java.io.IOException;

/**
 * Concrete instances derived from this class hold data stored in true type
 * tables. Right now the data is accessible as public attributes. In some cases
 * methods may return more convenient objects (such as Shapes instead of point
 * arrays).
 * 
 * @author Simon Fischer
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/font/truetype/TTFTable.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public abstract class TTFTable {

    public static final String[] TT_TAGS = new String[] { "cmap", "glyf",
            "head", "hhea", "hmtx", "loca", "maxp", "name", "OS/2", "post" };

    public static final Class[] TABLE_CLASSES = new Class[] {
            TTFCMapTable.class, TTFGlyfTable.class, TTFHeadTable.class,
            TTFHHeaTable.class, TTFHMtxTable.class, TTFLocaTable.class,
            TTFMaxPTable.class, TTFNameTable.class, TTFOS_2Table.class,
            TTFPostTable.class };

    private TTFFont ttfFont;

    TTFInput ttf;

    private boolean isRead = false;

    public void init(TTFFont font, TTFInput ttf) throws IOException {
        this.ttfFont = font;
        this.ttf = ttf;
    }

    public void read() throws IOException {
        ttf.pushPos();
        ttf.seek(0);
        readTable();
        isRead = true;
        ttf.popPos();
    }

    public abstract void readTable() throws IOException;

    public abstract String getTag();

    public boolean isRead() {
        return isRead;
    }

    public TTFTable getTable(String tag) throws IOException {
        return ttfFont.getTable(tag);
    }

    // --------------------------------------------------------------------------------

    public String toString() {
        return ttf + ": [" + getTag() + "/" + getClass().getName() + "]";
    }

}
