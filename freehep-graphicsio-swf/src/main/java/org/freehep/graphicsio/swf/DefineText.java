// Copyright 2001-2005, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Vector;

import org.freehep.util.io.BitOutputStream;

/**
 * DefineText TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineText.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineText extends DefinitionTag {

    protected int character;

    protected Rectangle2D bounds;

    protected AffineTransform matrix;

    protected Vector /* TextRecord */<Record>text;

    public DefineText(int id, Rectangle2D bounds, AffineTransform matrix,
            Vector /* TextRecord */<Record>text) {
        this();
        character = id;
        this.bounds = bounds;
        this.matrix = matrix;
        this.text = text;
    }

    public DefineText() {
        super(11, 1);
    }

    protected DefineText(int tagID, int version) {
        super(tagID, version);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {
        DefineText tag = new DefineText();
        tag.read(tagID, swf, len, false);
        return tag;
    }

    protected void read(int tagID, SWFInputStream swf, int len, boolean hasAlpha)
            throws IOException {

        character = swf.readUnsignedShort();
        swf.getDictionary().put(character, this);

        bounds = swf.readRect();
        matrix = swf.readMatrix();
        int glyphBits = swf.readUnsignedByte();
        int advanceBits = swf.readUnsignedByte();

        text = new Vector<Record>();
        boolean type1 = swf.readBitFlag();
        Record record = type1 ? (Record) new RecordType1(swf, glyphBits,
                advanceBits, hasAlpha) : (Record) new RecordType0(swf,
                glyphBits, advanceBits);
        while (!record.isEndRecord()) {
            text.add(record);
            type1 = swf.readBitFlag();
            record = type1 ? (Record) new RecordType1(swf, glyphBits,
                    advanceBits, hasAlpha) : (Record) new RecordType0(swf,
                    glyphBits, advanceBits);
        }
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {

        write(swf, false);
    }

    protected void write(SWFOutputStream swf, boolean hasAlpha)
            throws IOException {

        swf.writeUnsignedShort(character);
        swf.writeRect(bounds);
        swf.writeMatrix(matrix);
        int glyphBits = 0;
        int advanceBits = 0;
        for (int i = 0; i < text.size(); i++) {
            Record t = text.get(i);
            if (t instanceof RecordType0) {
                RecordType0 t0 = (RecordType0) t;
                glyphBits = Math.max(glyphBits, t0.getGlyphBits());
                advanceBits = Math.max(advanceBits, t0.getAdvanceBits());
            }
        }

        swf.writeUnsignedByte(glyphBits);
        swf.writeUnsignedByte(advanceBits);

        for (int i = 0; i < text.size(); i++) {
            Record t = text.get(i);
            t.write(swf, glyphBits, advanceBits, hasAlpha);
        }
        swf.writeUnsignedByte(0);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  character:  " + character + "\n");
        s.append("  bounds:     " + bounds + "\n");
        s.append("  matrix:     " + matrix + "\n");
        s.append("  texts:      " + text.size() + "\n");
        for (int i = 0; i < text.size(); i++) {
            s.append(text.get(i) + "\n");
        }
        return s.toString();
    }

    /**
     * Abstract Superclass for Text Records.
     */
    public static abstract class Record {
        public abstract void write(SWFOutputStream swf, int glyphBits,
                int advanceBits, boolean hasAlpha) throws IOException;

        public abstract boolean isEndRecord();
    }

    /**
     * Text0 Record, for the actual glyphs.
     */
    public static class RecordType0 extends Record {
        private Vector /* GlyphEntry */<GlyphEntry>glyphs = null;

        public RecordType0() {
            this.glyphs = new Vector<GlyphEntry>();
        }

        public void add(GlyphEntry glyph) {
            glyphs.add(glyph);
        }

        RecordType0(SWFInputStream swf, int glyphBits, int advanceBits)
                throws IOException {

            int glyphCount = (int) swf.readUBits(7);
            if (glyphCount == 0)
                return; // end record

            glyphs = new Vector<GlyphEntry>();
            for (int i = 0; i < glyphCount; i++) {
                GlyphEntry entry = new GlyphEntry(swf, glyphBits, advanceBits);
                glyphs.add(entry);
            }
            swf.byteAlign();
        }

        public void write(SWFOutputStream swf, int glyphBits, int advanceBits,
                boolean hasAlpha) throws IOException {

            swf.writeUBits(0, 1); // type 0
            swf.writeUBits(glyphs.size(), 7);
            for (int i = 0; i < glyphs.size(); i++) {
                glyphs.get(i).write(swf, glyphBits, advanceBits);
            }
            swf.byteAlign();
        }

        public boolean isEndRecord() {
            return glyphs == null;
        }

        public int getGlyphBits() {
            int glyphBits = 0;
            for (int i = 0; i < glyphs.size(); i++) {
                glyphBits = Math.max(glyphBits, glyphs.get(i)
                        .getGlyphBits());
            }
            return glyphBits;
        }

        public int getAdvanceBits() {
            int advanceBits = 0;
            for (int i = 0; i < glyphs.size(); i++) {
                advanceBits = Math.max(advanceBits,
                        glyphs.get(i).getAdvanceBits());
            }
            return advanceBits;
        }

        public String toString() {
            StringBuffer s = new StringBuffer();
            s.append("    glyphCount: " + glyphs.size() + "\n");
            s.append("    ");
            for (int i = 0; i < glyphs.size(); i++) {
                s.append(glyphs.get(i) + " ");
            }
            s.append("\n");
            return s.toString();
        }
    }

    /**
     * Text1 Record, for the attributes of the text.
     */
    public static class RecordType1 extends Record {
        private int fontID = -1;

        private Color color;

        private int xOffset, yOffset;

        private int height; // in TWIPS

        // only for swf >= 7
        private Vector /* GlyphEntry */<GlyphEntry>glyphs = null;

        public RecordType1(int fontID, Color color, int xOffset, int yOffset,
                int height) {
            this.fontID = fontID;
            this.color = color;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.height = height;

            glyphs = new Vector<GlyphEntry>();
        }

        // only for swf >= 7
        public void add(GlyphEntry glyph) {
            glyphs.add(glyph);
        }

        RecordType1(SWFInputStream input, int glyphBits, int advanceBits,
                boolean hasAlpha) throws IOException {
            /* int reserved = (int) */ input.readUBits(3);
            boolean hasFont = input.readBitFlag();
            boolean hasColor = input.readBitFlag();
            boolean hasYOffset = input.readBitFlag();
            boolean hasXOffset = input.readBitFlag();

            if (hasFont)
                fontID = input.readUnsignedShort();
            if (hasColor)
                color = input.readColor(hasAlpha);
            if (hasXOffset)
                xOffset = input.readShort();
            if (hasYOffset)
                yOffset = input.readShort();
            if (hasFont)
                height = input.readUnsignedShort();

            glyphs = new Vector<GlyphEntry>();
            if (input.getVersion() >= 7) {
                int glyphCount = (int) input.readUnsignedByte();
                for (int i = 0; i < glyphCount; i++) {
                    glyphs.add(new GlyphEntry(input, glyphBits, advanceBits));
                }
            }
        }

        public void write(SWFOutputStream swf, int glyphBits, int advanceBits,
                boolean hasAlpha) throws IOException {

            swf.writeBitFlag(true);
            swf.writeUBits(0, 3);
            swf.writeBitFlag(fontID >= 0);
            swf.writeBitFlag(color != null);
            swf.writeBitFlag(yOffset != 0);
            swf.writeBitFlag(xOffset != 0);

            if (fontID >= 0)
                swf.writeUnsignedShort(fontID);
            if (color != null)
                swf.writeColor(color, hasAlpha);
            if (xOffset != 0)
                swf.writeShort(xOffset);
            if (yOffset != 0)
                swf.writeShort(yOffset);
            if (fontID >= 0)
                swf.writeUnsignedShort(height);

            if (swf.getVersion() >= 7) {
                swf.writeUnsignedByte(glyphs.size());
                for (int i = 0; i < glyphs.size(); i++) {
                    glyphs.get(i).write(swf, glyphBits,
                            advanceBits);
                }
            }
        }

        public int getGlyphBits() {
            int glyphBits = 0;
            for (int i = 0; i < glyphs.size(); i++) {
                glyphBits = Math.max(glyphBits, glyphs.get(i)
                        .getGlyphBits());
            }
            return glyphBits;
        }

        public int getAdvanceBits() {
            int advanceBits = 0;
            for (int i = 0; i < glyphs.size(); i++) {
                advanceBits = Math.max(advanceBits,
                        glyphs.get(i).getAdvanceBits());
            }
            return advanceBits;
        }

        public boolean isEndRecord() {
            return false;
        }

        public String toString() {
            StringBuffer s = new StringBuffer();
            s.append("    FontID:  " + fontID + "\n");
            s.append("    Color:   " + color + "\n");
            s.append("    xOffset: " + xOffset + "\n");
            s.append("    yOffset: " + yOffset + "\n");
            s.append("    height:  " + height + "\n");
            s.append("    glyphCount (swf >= 7): " + glyphs.size() + "\n");
            s.append("    ");
            for (int i = 0; i < glyphs.size(); i++) {
                s.append(glyphs.get(i) + " ");
            }
            s.append("\n");
            return s.toString();
        }
    }

    public static class GlyphEntry {

        private int index;

        private int advance;

        public GlyphEntry(int index, int advance) {
            this.index = index;
            this.advance = advance;
        }

        public GlyphEntry(SWFInputStream input, int glyphBits, int advanceBits)
                throws IOException {
            index = (int) input.readUBits(glyphBits);
            advance = (int) input.readSBits(advanceBits);
        }

        public void write(SWFOutputStream swf, int glyphBits, int advanceBits)
                throws IOException {

            swf.writeUBits(index, glyphBits);
            swf.writeSBits(advance, advanceBits);
        }

        public int getGlyphBits() {
            return BitOutputStream.minBits(index, false);
        }

        public int getAdvanceBits() {
            return BitOutputStream.minBits(advance, true);
        }

        public String toString() {
            return "GlyphEntry[" + index + "," + advance + "]";
        }
    }
}
