// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * DefineEditText TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineEditText.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineEditText extends DefinitionTag {

    private int character;

    private Rectangle2D bounds;

    private boolean disableEditting;

    private boolean password;

    private boolean multiLine;

    private boolean wordWrap;

    private boolean drawBox;

    private boolean disableSelection;

    private int fontID;

    private int height;

    private Color color;

    private int maxLength = -1;

    private int alignment;

    private int leftMargin;

    private int rightMargin;

    private int indent;

    private int lineSpace;

    private String variable;

    private String initialText;

    // FIXME: constructor

    public DefineEditText() {
        super(37, 4);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DefineEditText tag = new DefineEditText();
        tag.character = swf.readUnsignedShort();
        swf.getDictionary().put(tag.character, tag);
        tag.bounds = swf.readRect();

        int flags = swf.readUnsignedShort();
        boolean hasLength = ((flags & 0x0002) > 0);
        tag.disableEditting = ((flags & 0x0008) > 0);
        tag.password = ((flags & 0x0010) > 0);
        tag.multiLine = ((flags & 0x0020) > 0);
        tag.wordWrap = ((flags & 0x0040) > 0);
        boolean hasText = ((flags & 0x0080) > 0);
        tag.drawBox = ((flags & 0x0800) > 0);
        tag.disableSelection = ((flags & 0x1000) > 0);

        tag.fontID = swf.readUnsignedShort();
        tag.height = swf.readUnsignedShort();
        tag.color = swf.readColor(true);
        if (hasLength)
            tag.maxLength = swf.readUnsignedShort();
        tag.alignment = swf.readUnsignedByte();
        tag.leftMargin = swf.readUnsignedShort();
        tag.rightMargin = swf.readUnsignedShort();
        tag.indent = swf.readUnsignedShort();
        tag.lineSpace = swf.readUnsignedShort();
        tag.variable = swf.readString();
        if (hasText)
            tag.initialText = swf.readString();

        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeUnsignedShort(character);
        swf.writeRect(bounds);
        int flags = 0;
        if (maxLength >= 0)
            flags |= 0x0002;
        if (disableEditting)
            flags |= 0x0008;
        if (password)
            flags |= 0x0010;
        if (multiLine)
            flags |= 0x0020;
        if (wordWrap)
            flags |= 0x0040;
        if (initialText != null)
            flags |= 0x0080;
        if (drawBox)
            flags |= 0x0800;
        if (disableSelection)
            flags |= 0x1000;
        swf.writeUnsignedShort(flags);

        swf.writeUnsignedShort(fontID);
        swf.writeUnsignedShort(height);
        swf.writeColor(color, true);
        if (maxLength >= 0)
            swf.writeUnsignedShort(maxLength);
        swf.writeUnsignedByte(alignment);
        swf.writeUnsignedShort(leftMargin);
        swf.writeUnsignedShort(rightMargin);
        swf.writeUnsignedShort(indent);
        swf.writeUnsignedShort(lineSpace);
        swf.writeString(variable);
        if (initialText != null)
            swf.writeString(initialText);
    }

    public String toString() {
        // FIXME: add other stuff...
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  character:  " + character + "\n");
        s.append("  ...\n");
        return s.toString();
    }

}