// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DefineFont TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineFont.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineFont extends DefinitionTag {

    private int character;

    private List /* SWFShape */<SWFShape>shapes;

    private DefineFontInfo info;

    public DefineFont(int id) {
        this();
        character = id;
        this.shapes = new ArrayList<SWFShape>();
    }

    public void add(SWFShape shape) {
        shapes.add(shape);
    }

    public DefineFont() {
        super(10, 1);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DefineFont tag = new DefineFont();
        tag.character = swf.readUnsignedShort();
        swf.getDictionary().put(tag.character, tag);

        int offset0 = swf.readUnsignedShort();
        int glyphCount = offset0 / 2;

        // read offsets but ignore them
        int[] offsets = new int[glyphCount];
        offsets[0] = offset0;
        for (int i = 1; i < glyphCount; i++) {
            offsets[i] = swf.readUnsignedShort();
        }

        tag.shapes = new ArrayList<SWFShape>();
        for (int i = 0; i < glyphCount; i++) {
            tag.shapes.add(new SWFShape(swf));
        }
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeUnsignedShort(character);
        swf.pushBuffer();
        int[] offsets = new int[shapes.size()];

        for (int i = 0; i < shapes.size(); i++) {
            offsets[i] = swf.getBufferLength();
            shapes.get(i).write(swf);
        }

        swf.popBuffer();
        for (int i = 0; i < shapes.size(); i++) {
            swf.writeUnsignedShort(shapes.size() * 2 + offsets[i]);
        }
        swf.append();
    }

    public int getGlyphCount() {
        return shapes.size();
    }

    public void setFontInfo(DefineFontInfo info) {
        this.info = info;
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  character:  " + character + "\n");
        s.append("  glyphcount: " + shapes.size() + "\n");
        for (int i = 0; i < shapes.size(); i++) {
            s.append(shapes.get(i) + "\n");
        }
        s.append("  fontInfo: " + info + "\n");
        return s.toString();
    }
}
