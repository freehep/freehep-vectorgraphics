// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SWF FillStyleArray.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/FillStyleArray.java 3e48ba4ef214 2006/11/27 22:51:07 duns $
 */
public class FillStyleArray {

    protected List /* FillStyle */fillStyles;

    public FillStyleArray() {
        this.fillStyles = new ArrayList();
    }

    public FillStyleArray(SWFInputStream swf, boolean isMorphStyle,
            boolean hasAlpha) throws IOException {

        this();

        int fillStyleCount = swf.readUnsignedByte();
        if (fillStyleCount == 0xFF) {
            fillStyleCount = swf.readUnsignedShort();
        }

        for (int i = 0; i < fillStyleCount; i++) {
            fillStyles.add(new FillStyle(swf, isMorphStyle, hasAlpha));
        }
    }

    public void add(FillStyle fillStyle) {
        this.fillStyles.add(fillStyle);
    }

    public FillStyle get(int index) {
        return (FillStyle) fillStyles.get(index);
    }

    public void write(SWFOutputStream swf, boolean isMorphStyle, boolean hasAlpha) throws IOException {

        if (fillStyles.size() >= 0xFF) {
            swf.writeUnsignedByte(0xFF);
            swf.writeUnsignedShort(fillStyles.size());
        } else {
            swf.writeUnsignedByte(fillStyles.size());
        }
        for (Iterator i = fillStyles.iterator(); i.hasNext();) {
            ((FillStyle) i.next()).write(swf, isMorphStyle, hasAlpha);
        }
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("  fillStyles: " + fillStyles.size() + "\n");
        int n = 0;
        for (Iterator i = fillStyles.iterator(); i.hasNext();) {
            s.append("    " + (n + 1) + " " + (i.next()) + "\n");
            n++;
        }
        return s.toString();
    }

}
