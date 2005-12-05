// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;
import java.util.Vector;

/**
 * DefineButton2 TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineButton2.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineButton2 extends DefinitionTag {

    private int character;

    private boolean trackAsMenu;

    private Vector buttons;

    private Vector conditions;

    public DefineButton2(int id, boolean trackAsMenu, Vector buttons,
            Vector conditions) {
        this();
        character = id;
        this.trackAsMenu = trackAsMenu;
        this.buttons = buttons;
        this.conditions = conditions;
    }

    public DefineButton2() {
        super(34, 3);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        System.out.println(len);

        DefineButton2 tag = new DefineButton2();
        tag.character = swf.readUnsignedShort();
        swf.getDictionary().put(tag.character, tag);
        /* int reserved = (int) */ swf.readUBits(7);
        trackAsMenu = swf.readBitFlag();

        // ignored
        int offset = swf.readUnsignedShort();

        tag.buttons = new Vector();
        ButtonRecord record = new ButtonRecord(swf, true);
        while (!record.isEndRecord()) {
            tag.buttons.add(record);
            record = new ButtonRecord(swf, true);
        }

        tag.conditions = new Vector();
        if (offset != 0) {
            int actionOffset;
            do {
                actionOffset = swf.readUnsignedShort();
                System.err.println("AO " + actionOffset);
                tag.conditions.add(new ButtonCondAction(swf));
            } while (actionOffset != 0);
        }
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {

        swf.writeUnsignedShort(character);
        swf.writeUBits(0, 7);
        swf.writeBitFlag(trackAsMenu);

        swf.pushBuffer();
        for (int i = 0; i < buttons.size(); i++) {
            ButtonRecord b = (ButtonRecord) buttons.get(i);
            b.write(swf);
        }
        swf.writeUnsignedByte(0);
        int offset = swf.popBuffer();
        swf.writeUnsignedShort(offset);
        swf.append();

        for (int i = 0; i < conditions.size(); i++) {
            swf.pushBuffer();
            ButtonCondAction c = (ButtonCondAction) conditions.get(i);
            c.write(swf);
            int actionOffset = swf.popBuffer();
            swf.writeUnsignedShort((i == conditions.size() - 1) ? 0
                    : actionOffset);
            swf.append();
        }
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  character: " + character + "\n");
        s.append("  menu:     " + trackAsMenu + "\n");
        for (int i = 0; i < buttons.size(); i++) {
            s.append("  " + buttons.get(i) + "\n");
        }
        for (int i = 0; i < conditions.size(); i++) {
            s.append("  " + conditions.get(i) + "\n");
        }
        return s.toString();
    }

}
