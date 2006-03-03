// Copyright 2001-2006, FreeHEP.
package org.freehep.graphicsio.svg;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.font.GlyphMetrics;

/**
 * Class for embedding a Font in a SVG file.
 *
 * @author Steffen Greiffenberg
 * @version $Id: freehep-graphicsio-svg/src/main/java/org/freehep/graphicsio/svg/SVGGlyph.java d9a2ef8950b1 2006/03/03 19:08:18 duns $
 */
public class SVGGlyph {

    public static int FONT_SIZE = 100;

    public static int UNITS_PER_EM = 2048;

    /**
     * glyp
     */
    private Shape glyph;

    /**
     * Character index
     */
    private int unicode;

    /**
     * Metrics of that character
     */
    private GlyphMetrics glyphMetrics;

    /**
     * Flip the drawing upside down
     */
    private static AffineTransform defaultTransform = new AffineTransform(1, 0, 0, -1, 0, 0);


    /**
     * stores the glyph data
     *
     * @param unicode
     * @param glyph
     */
    public SVGGlyph(Shape glyph, int unicode, GlyphMetrics glyphMetrics) {

	this.unicode = unicode;
	this.glyph = glyph;
	this.glyphMetrics = glyphMetrics;
    }


    public String toString() {
	StringBuffer result = new StringBuffer("<glyph ");

	// unicode
	result.append("unicode=\"");

	String hex = "0000" + Integer.toHexString(unicode);
	result.append("&#x");
	result.append(hex.substring(hex.length()-4));
	result.append(';');
	result.append("\" ");

	if (glyphMetrics != null) {
	    // advance X
	    result.append(getHorizontalAdvanceXString());

	    // advance Y
	    result.append(getHorizontalAdvanceYString());
	}

	// path
	result.append(getPathString());
	result.append("/>");

	return result.toString();
    }

    /**
     * @return SVG path tag using SVGGraphics2D and defaultTransform
     */
    protected String getPathString() {
	return SVGGraphics2D.getPathContent(glyph.getPathIterator(defaultTransform));
    }

    /**
     * @return SVG tag horiz-adv-x
     */
    public String getHorizontalAdvanceXString() {
	StringBuffer result = new StringBuffer();

	if (glyphMetrics.getAdvanceX() != 0) {
	    result.append("horiz-adv-x=\"");
	    result.append(SVGGraphics2D.fixedPrecision(glyphMetrics.getAdvanceX()));
	    result.append("\" ");
	}

	return result.toString();
    }

    /**
     * @return SVG tag horiz-adv-y
     */
    public String getHorizontalAdvanceYString() {
	StringBuffer result = new StringBuffer();

	if (glyphMetrics.getAdvanceY() != 0) {
	    result.append("horiz-adv-y=\"");
	    result.append(SVGGraphics2D.fixedPrecision(glyphMetrics.getAdvanceY()));
	    result.append("\" ");
	}

	return result.toString();
    }
}
