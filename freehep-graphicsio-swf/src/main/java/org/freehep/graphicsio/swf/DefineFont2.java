// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * DefineFont2 TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @author Steve Hannah
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineFont2.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineFont2 extends DefinitionTag implements SWFConstants {

    private int character;

    private boolean shiftJIS = false;

    private boolean ansi = false;

    private boolean wideOffsets = false;

    private boolean wideCodes = false;

    private boolean italic = false;

    private boolean bold = false;

    private int languageCode;

    private String name;

    private long[] offsets;

    private long codeOffset;

    private SWFShape[] shapes;

    private int[] codes;

    private int ascent, descent, leading;

    private short[] advances;

    private Rectangle2D[] bounds;

    private KerningRecord[] kerning = new KerningRecord[0];

    public DefineFont2() {
        super(48, 3);
    }

    public DefineFont2(int character, boolean italic, boolean bold,
            String name, SWFShape[] shapes, int[] codes) {
        this(character, false, false, italic, bold, true, true, LANGUAGE_LATIN,
                name, shapes, codes, 0, 0, 0, null, null, null);
    }

    public DefineFont2(int character, boolean shiftJIS, boolean ansi,
            boolean italic, boolean bold, boolean wideOffsets,
            boolean wideCodes, int languageCode, String name,
            SWFShape[] shapes, int[] codes, int ascent, int descent,
            int leading, short[] advances, Rectangle2D[] bounds,
            KerningRecord[] kerning) {
        this();
        this.character = character;
        this.shiftJIS = shiftJIS;
        this.ansi = ansi;
        this.italic = italic;
        this.bold = bold;
        this.wideOffsets = wideOffsets;
        this.wideCodes = wideCodes;
        this.languageCode = languageCode;
        this.name = name;
        this.shapes = shapes;
        this.codes = codes;
        this.ascent = ascent;
        this.descent = descent;
        this.leading = leading;
        this.advances = advances;
        this.bounds = bounds;
        this.kerning = kerning;
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DefineFont2 tag = new DefineFont2();
        tag.character = swf.readUnsignedShort();
        swf.getDictionary().put(tag.character, tag);

        boolean hasLayout = swf.readBitFlag();
        tag.shiftJIS = swf.readBitFlag();
        /* boolean reserved = */ swf.readBitFlag();
        tag.ansi = swf.readBitFlag();
        tag.wideOffsets = swf.readBitFlag();
        tag.wideCodes = swf.readBitFlag();
        tag.italic = swf.readBitFlag();
        tag.bold = swf.readBitFlag();
        tag.languageCode = swf.readLanguageCode();

        int nameLength = swf.readUnsignedByte();
        if (swf.getVersion() >= 6) {
            tag.name = swf.readUTF();
        } else {
            tag.name = new String(swf.readByte(nameLength));
        }

        int glyphCount = swf.readUnsignedShort();

        tag.offsets = new long[glyphCount];
        for (int i = 0; i < glyphCount; i++) {
            tag.offsets[i] = (tag.wideOffsets) ? swf.readUnsignedInt()
                    : (long) swf.readUnsignedShort();
        }

        tag.codeOffset = (tag.wideOffsets) ? swf.readUnsignedInt() : (long) swf
                .readUnsignedShort();

        tag.shapes = new SWFShape[glyphCount];
        for (int i = 0; i < glyphCount; i++) {
            tag.shapes[i] = new SWFShape(swf);
        }

        tag.codes = (tag.wideCodes) ? swf.readUnsignedShort(glyphCount) : swf
                .readUnsignedByte(glyphCount);

        if (hasLayout) {
            tag.ascent = swf.readShort();
            tag.descent = swf.readShort();
            tag.leading = swf.readShort();
            tag.advances = swf.readShort(glyphCount);
            tag.bounds = new Rectangle2D[glyphCount];
            for (int i = 0; i < glyphCount; i++) {
                tag.bounds[i] = swf.readRect();
            }

            int kerningCount = swf.readUnsignedShort();
            tag.kerning = new KerningRecord[kerningCount];
            for (int i = 0; i < kerningCount; i++) {
                tag.kerning[i] = new KerningRecord(swf, tag.wideCodes);
            }
        }
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        boolean hasLayout = (ascent != 0) || (descent != 0) || (leading != 0)
                || (advances != null) || (bounds != null) || (kerning != null);

        swf.writeUnsignedShort(character);
        swf.writeBitFlag(hasLayout);
        swf.writeBitFlag(shiftJIS);
        swf.writeUBits(0, 1); // reserved
        swf.writeBitFlag(ansi);
        swf.writeBitFlag(wideOffsets);
        swf.writeBitFlag(wideCodes);
        swf.writeBitFlag(italic);
        swf.writeBitFlag(bold);
        if (swf.getVersion() >= 6) {
            swf.writeLanguageCode(languageCode);
        } else {
            swf.writeUnsignedByte(0);
        }

        swf.writeUnsignedByte(name.length());
        if (swf.getVersion() >= 6) {
            swf.writeUTF(name);
        } else {
            swf.writeByte(name.getBytes());
        }

        // Shapes
        swf.pushBuffer();
        int[] offsets = new int[shapes.length];
        int inc = wideOffsets ? 4 : 2;
        inc = (offsets.length + 1) * inc;
        for (int i = 0; i < shapes.length; i++) {
            offsets[i] = swf.getBufferLength() + inc;
            shapes[i].write(swf);
            swf.byteAlign();
        }
        codeOffset = swf.getBufferLength() + inc;
        swf.popBuffer();
        swf.writeUnsignedShort(offsets.length);

        // Offset Table
        for (int i = 0; i < offsets.length; i++) {
            if (wideOffsets) {
                swf.writeUnsignedInt(offsets[i]);
            } else {
                swf.writeUnsignedShort((int) offsets[i]);
            }
        }
        if (wideOffsets) {
            swf.writeUnsignedInt(codeOffset);
        } else {
            swf.writeUnsignedShort((int) codeOffset);
        }
        swf.append();

        if (wideCodes) {
            for (int i = 0; i < offsets.length; i++) {
                swf.writeUnsignedShort(codes[i]);
            }
        } else {
            for (int i = 0; i < offsets.length; i++) {
                swf.writeUnsignedByte(codes[i]);
            }
        }
        if (hasLayout) {
            swf.writeShort(ascent);
            swf.writeShort(descent);
            swf.writeShort(leading);
            for (int i = 0; i < offsets.length; i++) {
                swf.writeShort(advances[i]);
            }
            for (int i = 0; i < bounds.length; i++) {
                swf.writeRect(bounds[i]);
            }
            swf.writeUnsignedShort(kerning.length);
            for (int i = 0; i < kerning.length; i++) {
                kerning[i].write(swf, wideCodes);
            }
        }
    }

    public long getId() {
        return character;
    }

    public boolean getShiftJIS() {
        return shiftJIS;
    }

    public boolean isAnsi() {
        return ansi;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean hasWideOffsets() {
        return wideOffsets;
    }

    public boolean hasWideCodes() {
        return wideCodes;
    }

    public String getName() {
        return name;
    }

    public SWFShape[] getShapes() {
        return shapes;
    }

    public int[] getCodes() {
        return codes;
    }

    public int getAscent() {
        return ascent;
    }

    public int getDescent() {
        return descent;
    }

    public int getLeading() {
        return leading;
    }

    public short[] getAdvances() {
        return advances;
    }

    public Rectangle2D[] getBounds() {
        return bounds;
    }

    public KerningRecord[] getKerning() {
        return kerning;
    }

    public void setId(int id) {
        this.character = id;
    }

    public void setShiftJIS(boolean shiftJIS) {
        this.shiftJIS = shiftJIS;
    }

    public void setAnsi(boolean ansi) {
        this.ansi = ansi;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public void setWideOffsets(boolean wideOffsets) {
        this.wideOffsets = wideOffsets;
    }

    public void setWideCodes(boolean wideCodes) {
        this.wideCodes = wideCodes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCodes(int[] codes) {
        this.codes = codes;
    }

    public void setShapes(SWFShape[] shapes) {
        this.shapes = shapes;
    }

    public void setAscent(int ascent) {
        this.ascent = ascent;
    }

    public void setDescent(int descent) {
        this.descent = descent;
    }

    public void setLeading(int leading) {
        this.leading = leading;
    }

    public void setAdvances(short[] advances) {
        this.advances = advances;
    }

    public void setAdvances(int[] advances) {
        short[] s = new short[advances.length];
        for (int i = 0; i < s.length; i++) {
            s[i] = (short) advances[i];
        }
        setAdvances(s);
    }

    public void setBounds(Rectangle2D[] bounds) {
        this.bounds = bounds;
    }

    public void setKerning(KerningRecord[] kerning) {
        this.kerning = kerning;
    }

    public String toString() {
        // FIXME: add other stuff...
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  character:  " + character + "\n");
        s.append("  name: " + name + "\n");
        if (shiftJIS)
            s.append("  shiftJIS\n");
        if (ansi)
            s.append("  ansi\n");
        if (italic)
            s.append("  italic\n");
        if (bold)
            s.append("  bold\n");
        if (wideOffsets)
            s.append("  wideOffsets\n");
        if (wideCodes)
            s.append("  wideCodes\n");
        if (languageCode > 0)
            s.append("  languageCode: " + languageCode + "\n");
        if (ascent != 0)
            s.append("  ascent: " + ascent + "\n");
        if (descent != 0)
            s.append("  descent: " + descent + "\n");
        if (leading != 0)
            s.append("  leading: " + leading + "\n");
        if (advances != null) {
            for (int i = 0; i < advances.length; i++) {
                s.append("  advances[" + i + "]: " + advances[i] + "\n");
            }
        }
        if (bounds != null) {
            for (int i = 0; i < bounds.length; i++) {
                s.append("  bounds[" + i + "]: " + bounds[i] + "\n");
            }
        }
        s.append("  glyphCount: " + shapes.length + "\n");
        for (int i = 0; i < shapes.length; i++) {
            s.append("  >>> Shape " + i + "\n");
            s.append(shapes[i] + "\n");
        }

        return s.toString();
    }

    public static class KerningRecord {

        private int code1, code2;

        private int adjustment;

        public KerningRecord(int code1, int code2, int adjustment) {
            this.code1 = code1;
            this.code2 = code2;
            this.adjustment = adjustment;
        }

        public KerningRecord(SWFInputStream input, boolean wideCodes)
                throws IOException {

            code1 = (wideCodes) ? input.readUnsignedShort() : input
                    .readUnsignedByte();
            code2 = (wideCodes) ? input.readUnsignedShort() : input
                    .readUnsignedByte();
            adjustment = input.readShort();
        }

        public void write(SWFOutputStream swf, boolean wideCodes)
                throws IOException {

            if (wideCodes)
                swf.writeUnsignedShort(code1);
            else
                swf.writeUnsignedByte(code1);
            if (wideCodes)
                swf.writeUnsignedShort(code2);
            else
                swf.writeUnsignedByte(code2);
            swf.writeShort(adjustment);
        }

        public String toString() {
            return "Kerning[" + code1 + ", " + code2 + ", " + adjustment + "]";
        }
    }

}
