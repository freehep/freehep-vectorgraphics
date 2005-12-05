// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * DefineFontInfo TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineFontInfo.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineFontInfo extends DefinitionTag {

    protected int fontID;

    protected String name;

    protected boolean shiftJIS, ansi;

    protected boolean italic, bold;

    protected boolean wideCodes;

    protected int[] codes;

    public DefineFontInfo(int fontID, String name, boolean shiftJIS,
            boolean ansi, boolean italic, boolean bold, boolean wideCodes,
            int[] codes) {
        this();
        this.fontID = fontID;
        this.name = name;
        this.shiftJIS = shiftJIS;
        this.ansi = ansi;
        this.italic = italic;
        this.bold = bold;
        this.wideCodes = wideCodes;
        this.codes = codes;
    }

    public DefineFontInfo() {
        super(13, 1);
    }

    protected DefineFontInfo(int code, int version) {
        super(code, version);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DefineFontInfo tag = new DefineFontInfo();
        tag.fontID = swf.readUnsignedShort();

        // associate this fontinfo with font
        DefineFont font = (DefineFont) swf.getDictionary().get(tag.fontID);
        int numGlyphs = font.getGlyphCount();
        font.setFontInfo(tag);

        int nameLength = swf.readUnsignedByte();
        if (swf.getVersion() >= 6) {
            tag.name = swf.readUTF();
        } else {
            tag.name = new String(swf.readByte(nameLength));
        }
        /* int reserved = (int) */ swf.readUBits(3);
        tag.shiftJIS = swf.readBitFlag();
        tag.ansi = swf.readBitFlag();
        tag.italic = swf.readBitFlag();
        tag.bold = swf.readBitFlag();
        tag.wideCodes = swf.readBitFlag();

        tag.codes = (tag.wideCodes) ? swf.readUnsignedShort(numGlyphs) : swf
                .readUnsignedByte(numGlyphs);
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeUnsignedShort(fontID);
        swf.writeUnsignedByte(name.length());
        if (swf.getVersion() >= 6) {
            swf.writeUTF(name);
        } else {
            swf.writeByte(name.getBytes());
        }
        swf.writeUBits(0, 3);
        swf.writeBitFlag(shiftJIS);
        swf.writeBitFlag(ansi);
        swf.writeBitFlag(italic);
        swf.writeBitFlag(bold);
        swf.writeBitFlag(wideCodes);
        if (wideCodes) {
            swf.writeUnsignedShort(codes);
        } else {
            swf.writeUnsignedByte(codes);
        }
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
