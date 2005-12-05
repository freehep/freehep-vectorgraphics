// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.Color;
import java.io.IOException;

/**
 * PatternTable TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/PatternTable.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class PatternTable extends CGMTag {

    private int index;

    private Color[][] colorArray;

    private int precision;

    public PatternTable() {
        super(5, 32, 1);
    }

    public PatternTable(int index, Color[][] colorArray, int precision) {
        this();
        this.index = index;
        this.colorArray = colorArray;
        // FIXME
        this.precision = 0;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeIntegerIndex(index);
        cgm.writeInteger(colorArray.length);
        cgm.writeInteger(colorArray[0].length);
        cgm.writeInteger(precision);
        for (int i = 0; i < colorArray.length; i++) {
            for (int j = 0; j < colorArray[i].length; j++) {
                cgm.writeColor(colorArray[i][j]);
            }
        }
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("PATTABLE ");
        cgm.writeInteger(index);
        cgm.print(", ");
        cgm.writeInteger(colorArray.length);
        cgm.print(", ");
        cgm.writeInteger(colorArray[0].length);
        cgm.print(", ");
        cgm.writeInteger(0); // FIXME
        cgm.println(", ");
        cgm.indent();
        for (int i = 0; i < colorArray.length; i++) {
            cgm.print("(");
            for (int j = 0; j < colorArray[i].length; j++) {
                cgm.writeColor(colorArray[i][j]);
                cgm.print(" ");
            }
            cgm.println(")");
        }
        cgm.outdent();
    }
}
