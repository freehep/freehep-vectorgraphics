// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.font.truetype;

import java.io.IOException;

/**
 * POST Table.
 * 
 * @author Simon Fischer
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/font/truetype/TTFPostTable.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class TTFPostTable extends TTFTable {

    public double format;

    public double italicAngle;

    public short underlinePosition, underlineThickness;

    public long isFixedPitch;

    public long minMemType42, maxMemType42, minMemType1, maxMemType1;

    public int[] glyphNameIndex;

    public String getTag() {
        return "post";
    }

    public void readTable() throws IOException {
        format = ttf.readFixed();

        italicAngle = ttf.readFixed();

        underlinePosition = ttf.readFWord();
        underlineThickness = ttf.readFWord();

        isFixedPitch = ttf.readULong();

        minMemType42 = ttf.readULong();
        maxMemType42 = ttf.readULong();
        minMemType1 = ttf.readULong();
        maxMemType1 = ttf.readULong();

        if (format == 2.0) {
            glyphNameIndex = ttf.readUShortArray(ttf.readUShort());
        } else if (format == 2.5) {
            System.err.println("Format 2.5 for post notimplemented yet.");
        }
    }

    public String toString() {
        String str = super.toString() + " format: " + format + "\n  italic:"
                + italicAngle + " ulPos:" + underlinePosition + " ulThick:"
                + underlineThickness + " isFixed:" + isFixedPitch;
        if (glyphNameIndex != null) {
            str += "\n  glyphNamesIndex[" + glyphNameIndex.length + "] = {";
            for (int i = 0; i < glyphNameIndex.length; i++) {
                if (i % 16 == 0)
                    str += "\n    ";
                str += glyphNameIndex[i] + " ";
            }
            str += "\n  }";
        }
        return str;
    }
}
