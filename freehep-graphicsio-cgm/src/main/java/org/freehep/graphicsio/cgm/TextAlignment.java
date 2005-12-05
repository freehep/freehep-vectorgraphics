// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * TextAlignment TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/TextAlignment.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class TextAlignment extends CGMTag {

    public static final int NORMAL_HORIZONTAL = 0;

    public static final int LEFT = 1;

    public static final int CENTRE = 2;

    public static final int RIGHT = 3;

    public static final int CONTINUOUS_HORIZONTAL = 4;

    public static final int NORMAL_VERTICAL = 0;

    public static final int TOP = 1;

    public static final int CAP = 2;

    public static final int HALF = 3;

    public static final int BASE = 4;

    public static final int BOTTOM = 5;

    public static final int CONTINUOUS_VERTICAL = 6;

    private int horizontalType, verticalType;

    private double horizontal, vertical;

    public TextAlignment() {
        super(5, 18, 1);
    }

    public TextAlignment(int horizontalType, int verticalType,
            double horizontal, double vertical) {
        this();
        this.horizontalType = horizontalType;
        this.verticalType = verticalType;
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeEnumerate(horizontalType);
        cgm.writeEnumerate(verticalType);
        cgm.writeReal(horizontal);
        cgm.writeReal(vertical);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("TEXTALIGN ");
        switch (horizontalType) {
        default:
        case NORMAL_HORIZONTAL:
            cgm.print("NORHORIZ");
            break;
        case LEFT:
            cgm.print("LEFT");
            break;
        case CENTRE:
            cgm.print("CTR");
            break;
        case RIGHT:
            cgm.print("RIGHT");
            break;
        case CONTINUOUS_HORIZONTAL:
            cgm.print("CONTHORIZ");
            break;
        }
        cgm.print(", ");
        switch (verticalType) {
        default:
        case NORMAL_VERTICAL:
            cgm.print("NORMVERT");
            break;
        case TOP:
            cgm.print("TOP");
            break;
        case CAP:
            cgm.print("CAP");
            break;
        case HALF:
            cgm.print("HALF");
            break;
        case BASE:
            cgm.print("BASE");
            break;
        case BOTTOM:
            cgm.print("BOTTOM");
            break;
        case CONTINUOUS_VERTICAL:
            cgm.print("CONTVERT");
            break;
        }
        cgm.print(", ");
        cgm.writeReal(horizontal);
        cgm.print(", ");
        cgm.writeReal(vertical);
    }
}
