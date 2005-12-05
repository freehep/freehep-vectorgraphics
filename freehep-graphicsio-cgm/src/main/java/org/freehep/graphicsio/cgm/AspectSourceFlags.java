// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * AspectSourceFlags TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/AspectSourceFlags.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class AspectSourceFlags extends CGMTag {

    public static final int LINE_TYPE = 0;

    public static final int LINE_WIDTH = 1;

    public static final int LINE_COLOR = 2;

    public static final int MARKER_TYPE = 3;

    public static final int MARKER_SIZE = 4;

    public static final int MARKER_COLOR = 5;

    public static final int TEXT_FONT_INDEX = 6;

    public static final int TEXT_PRECISION = 7;

    public static final int CHARACTER_EXPANSION_FACTOR = 8;

    public static final int CHARACTER_SPACING = 9;

    public static final int TEXT_COLOR = 10;

    public static final int INTERIOR_STYLE = 11;

    public static final int FILL_COLOR = 12;

    public static final int HATCH_INDEX = 13;

    public static final int PATTERN_INDEX = 14;

    public static final int EDGE_TYPE = 15;

    public static final int EDGE_WIDTH = 16;

    public static final int EDGE_COLOR = 17;

    public final static int INDIVIDUAL = 0;

    public final static int BUNDLED = 1;

    private int[] asfType, asfValue;

    public AspectSourceFlags() {
        super(5, 35, 1);
    }

    public AspectSourceFlags(int[] asfType, int[] asfValue) {
        this();
        this.asfType = asfType;
        this.asfValue = asfValue;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        for (int i = 0; i < asfType.length; i++) {
            cgm.writeEnumerate(asfType[i]);
            cgm.writeEnumerate(asfValue[i]);
        }
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.println("ASF");
        cgm.indent();
        for (int i = 0; i < asfType.length; i++) {
            switch (asfType[i]) {
            default:
            case LINE_TYPE:
                cgm.print("LINETYPE");
                break;
            case LINE_WIDTH:
                cgm.print("LINEWIDTH");
                break;
            case LINE_COLOR:
                cgm.print("LINECOLR");
                break;
            case MARKER_TYPE:
                cgm.print("MARKERTYPE");
                break;
            case MARKER_SIZE:
                cgm.print("MARKERSIZE");
                break;
            case MARKER_COLOR:
                cgm.print("MARKERCOLR");
                break;
            case TEXT_FONT_INDEX:
                cgm.print("TEXTFONTINDEX");
                break;
            case TEXT_PRECISION:
                cgm.print("TEXTPREC");
                break;
            case CHARACTER_EXPANSION_FACTOR:
                cgm.print("CHAREXPAN");
                break;
            case CHARACTER_SPACING:
                cgm.print("CHARSPACE");
                break;
            case TEXT_COLOR:
                cgm.print("TEXTCOLR");
                break;
            case INTERIOR_STYLE:
                cgm.print("INTSTYLE");
                break;
            case FILL_COLOR:
                cgm.print("FILLCOLR");
                break;
            case HATCH_INDEX:
                cgm.print("HATCHINDEX");
                break;
            case PATTERN_INDEX:
                cgm.print("PATINDEX");
                break;
            case EDGE_TYPE:
                cgm.print("EDGETYPE");
                break;
            case EDGE_WIDTH:
                cgm.print("EDGEWIDTH");
                break;
            case EDGE_COLOR:
                cgm.print("EDGECOLR");
                break;
            }
            cgm.print(", ");
            switch (asfValue[i]) {
            default:
            case INDIVIDUAL:
                cgm.print("INDIV");
                break;
            case BUNDLED:
                cgm.print("BUNDLED");
                break;
            }
            cgm.println();
        }
        cgm.outdent();
    }
}
