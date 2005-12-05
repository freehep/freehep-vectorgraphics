// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * DefineFontInfo2 TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineFontInfo2.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineFontInfo2 extends DefineFontInfo {

    int languageCode;

    public DefineFontInfo2(int fontID, String name, boolean italic,
            boolean bold, int languageCode, int[] codes) {
        super(fontID, name, false, false, italic, bold, true, codes);
        this.languageCode = languageCode;
    }

    public DefineFontInfo2() {
        super(62, 6);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DefineFontInfo2 tag = new DefineFontInfo2();
        tag.fontID = swf.readUnsignedShort();

        // associate this fontinfo with font
        DefineFont font = (DefineFont) swf.getDictionary().get(tag.fontID);
        int numGlyphs = font.getGlyphCount();
        font.setFontInfo(tag);

        /* int nameLength = */ swf.readUnsignedByte();
        tag.name = swf.readUTF();
        /* int reserved = (int) */ swf.readUBits(3);
        tag.shiftJIS = swf.readBitFlag();
        tag.ansi = swf.readBitFlag();
        tag.italic = swf.readBitFlag();
        tag.bold = swf.readBitFlag();
        tag.wideCodes = swf.readBitFlag();
        tag.languageCode = swf.readLanguageCode();

        tag.codes = swf.readUnsignedShort(numGlyphs);
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeUnsignedShort(fontID);
        swf.writeUnsignedByte(name.length());
        swf.writeUTF(name);
        swf.writeUBits(0, 3);
        swf.writeBitFlag(false);
        swf.writeBitFlag(false);
        swf.writeBitFlag(italic);
        swf.writeBitFlag(bold);
        swf.writeBitFlag(true);
        swf.writeLanguageCode(languageCode);
        swf.writeUnsignedShort(codes);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  name:      " + name + "\n");
        s.append("  numGlyphs: " + codes.length + "\n");
        s.append("    ");
        for (int i = 0; i < codes.length; i++) {
            s.append("[" + codes[i] + ",'" + ((char) codes[i]) + "'] ");
        }
        s.append("\n");
        return s.toString();
    }
}
