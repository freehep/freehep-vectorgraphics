// Copyright 2001-2005 freehep
package org.freehep.graphicsio.font;

import java.util.*;
import java.awt.Font;
import java.io.IOException;

import org.freehep.graphics2d.font.Lookup;
import org.freehep.graphics2d.font.CharTable;

/**
 *  A table to remember which fonts were used while writing a document.
 *  @author Simon Fischer
 *  @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/font/FontTable.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public abstract class FontTable {

    protected class Entry {
        private Font font;
        private String ref;
        private CharTable encoding;
        private boolean written;

        private Entry(Font f, CharTable encoding) {
            this.font = f.deriveFont((float)FontEmbedder.FONT_SIZE);
            this.ref = createFontReference(f);
            this.encoding = encoding;
            this.written = false;
        }

        public Font getFont() { return font; }
        public String getReference() { return ref; }
        protected void setReference(String ref) { this.ref = ref; }
        public CharTable getEncoding() { return encoding; }
        public void setWritten(boolean written) { this.written = written; }
        public boolean isWritten() { return written; }
        public String toString() { return ref + "=" + font; }
    }

    private Hashtable table;

    public FontTable() {
        this.table = new Hashtable();
    }

    /** Returns a default CharTable to be used for normal text
     *  (not Symbol or Dingbats). */
    public abstract CharTable getEncodingTable();

    /** Called whenever a specific font is used for the first time.
     *  Subclasses may use this method to include the font instantly.
     *  This method may change the value of the reference by calling
     *  <tt>e.setReference(String)</tt> e.g. if it wants to substitute
     *  the font by a standard font that can be addressed under a name
     *  different from the generated one.*/
    protected abstract void firstRequest(Entry e, boolean embed, String embedAs) throws IOException;

    /** Creates a unique reference to address this font. */
    protected abstract String createFontReference(Font f);

    protected abstract Font substituteFont(Font font);

    /** Returns a name for this font that can be used in the document.
     *  A new name is generated if the font was not used yet. For different
     *  fontsizes the same name is returned. */
    public String fontReference(Font font, boolean embed, String embedAs) {
        font = substituteFont(font);
        String key = font.getName() +
                    (font.isBold()   ? "[bold]"   : "") +
                    (font.isItalic() ? "[italic]" : "");
        Entry e = (Entry)table.get(key);
        if (e != null) return e.ref;
        e = new Entry(font, getEncodingTable(font));
        try {
            firstRequest(e, embed, embedAs);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        table.put(key, e);
        return e.ref;
    }

    /** Returns a Collection view of all fonts. The elements of the
     *  collection are <tt>Entrie</tt>s. */
    public Collection getEntries() {
        return table.values();
    }

    private CharTable getEncodingTable(Font font) {
        String fontname = font.getName().toLowerCase();
        if (fontname.indexOf("symbol") >= 0)
            return Lookup.getInstance().getTable("Symbol");
        if (fontname.indexOf("zapfdingbats") >= 0)
            return Lookup.getInstance().getTable("Zapfdingbats");
        return getEncodingTable();
    }

}
