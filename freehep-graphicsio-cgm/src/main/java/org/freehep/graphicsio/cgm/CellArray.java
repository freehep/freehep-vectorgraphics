// Copyright 2001-2003 FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * CellArray TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @author Ian Graham - fixed bugs and completed usable implementation
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CellArray.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CellArray extends CGMTag {

    public final static int RUN_LENGTH = 0;

    public final static int PACKED = 1;

    private Point2D p, q, r;

    private Color[][] colorArray;

    private int precision;

    private int mode;

    public CellArray() {
        super(4, 9, 1);
    }

    private CellArray(Point2D p, Point2D q, Point2D r, Color[][] colorArray,
            int precision, int mode) {
        this();
        this.p = p;
        this.q = q;
        this.r = r;
        this.colorArray = colorArray;
        this.precision = precision;
        this.mode = mode;
    }

    /**
     * Constructor for CellArray. Only 8-bit packed mode supported at present.
     */
    public CellArray(Point2D p, Point2D q, Point2D r, Color[][] colorArray) {
        this(p, q, r, colorArray, 8, PACKED);
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writePoint(p);
        cgm.writePoint(q);
        cgm.writePoint(r);
        cgm.writeInteger(colorArray[0].length);
        cgm.writeInteger(colorArray.length);
        cgm.writeInteger(precision);
        cgm.writeEnumerate(mode);
        cgm.pushBuffer(); // start buffer for tracking word alignment of rows
        for (int i = 0; i < colorArray.length; i++) {
            wordAlign(cgm);
            for (int j = 0; j < colorArray[0].length; j++) {
                cgm.writeColor(colorArray[i][j]);
            }
        }
        cgm.popBuffer();
        cgm.append();
    }

    private void wordAlign(CGMOutputStream cgm) throws IOException {
        int align = 2;
        int pad = (align - (cgm.getBufferLength() % align)) % align;
        for (int i = 0; i < pad; i++) {
            cgm.write(0);
        }
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.println("CELLARRAY");
        cgm.indent();
        cgm.writePoint(p);
        cgm.println(",");
        cgm.writePoint(q);
        cgm.println(",");
        cgm.writePoint(r);
        cgm.println(",");
        cgm.writeInteger(colorArray[0].length);
        cgm.println(",");
        cgm.writeInteger(colorArray.length);
        cgm.println(",");
        cgm.writeInteger(precision);
        cgm.println(",");
        for (int i = 0; i < colorArray.length; i++) {
            cgm.print("(");
            int widthMinus1 = colorArray[0].length - 1;
            for (int j = 0; j < widthMinus1; j++) {
                cgm.writeColor(colorArray[i][j]);
                cgm.print(", ");
            }
            cgm.writeColor(colorArray[i][widthMinus1]);
            if (i == colorArray.length - 1) {
                cgm.print(")");
            } else {
                cgm.println(") ,");
            }
        }
        cgm.outdent();
    }
}
