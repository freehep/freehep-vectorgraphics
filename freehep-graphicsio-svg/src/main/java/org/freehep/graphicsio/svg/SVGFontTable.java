// Copyright 2001-2006, FreeHEP.
package org.freehep.graphicsio.svg;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

/**
 * A table to remember which fonts were used while writing a svg file.
 * Entries are added by calling {@link #addGlyphs(String, java.awt.Font)}.
 * The final SVG tag for the <defs> section is generated using {@link #toString()}.
 * Use {@link #getEmbeddedFontName(java.awt.Font)} for referencing embedded fonts
 * in <text> tags.
 *
 * @author Steffen Greiffenberg
 * @version $Id: freehep-graphicsio-svg/src/main/java/org/freehep/graphicsio/svg/SVGFontTable.java cbe5b99bb13b 2006/03/09 21:55:10 duns $
 */
public class SVGFontTable {

    /**
     * stores fonts and a glyph-hastable
     */
    private Hashtable fonts = new Hashtable();

    /**
     * creates a glyph for the string character
     * 
     * @param c
     * @param font
     * @return unique font name
     */
    private SVGGlyph addGlyph(int c, Font font) {
        // is the font stored?
        Hashtable glyphs = getGlyphs(font);

        // does a glyph allready exist?
        SVGGlyph result = (SVGGlyph) glyphs.get(String.valueOf(c));

        // create a new one?
        if (result == null) {
            // create and store the SVG Glyph
            result = createGlyph(c, font);
            glyphs.put(String.valueOf(c), result);
        }

        return result;
    }

    /**
     * @param c
     * @param font
     * @return GlyphVector using a default rendering context
     */
    private SVGGlyph createGlyph(int c, Font font) {
        GlyphVector glyphVector = font.createGlyphVector(
            // flipping is done by SVGGlyph
            new FontRenderContext(null, true, true),
            // unicode to char
            String.valueOf((char) c));

        // create and store the SVG Glyph
        return new SVGGlyph(glyphVector.getGlyphOutline(0), c, glyphVector.getGlyphMetrics(0));
    }

    /**
     * creates the glyph for the string
     * 
     * @param string
     * @param font
     * @return unique font name
     */
    protected String addGlyphs(String string, Font font) {
        font = substituteFont(font);

        // add characters
        for (int i = 0; i < string.length(); i ++) {
            addGlyph(string.charAt(i), font);
        }
        return getEmbeddedFontName(font);
    }

    /**
     * @param font
     * @return glyph vectors for font
     */
    private Hashtable getGlyphs(Font font) {
        // derive a default font for the font table
        font = substituteFont(font);

        Hashtable result = (Hashtable) fonts.get(font);
        if (result == null) {
            result = new Hashtable();
            fonts.put(font, result);
        }
        return result;
    }

    /**
     * creates the font entry:
     * <p/>
     * <font>
     * <glyph ... />
     * ...
     * </font>
     *
     * @return string representing the entry
     */
    public String toString() {
        StringBuffer result = new StringBuffer();

        Iterator fonts = this.fonts.keySet().iterator();
        while (fonts.hasNext()) {
            Font font = (Font) fonts.next();

            // familiy
            result.append("<font id=\"");
            result.append(getEmbeddedFontName(font));
            result.append("\">\n");

            // font-face
            result.append("<font-face font-family=\"");
            result.append(getEmbeddedFontName(font));
            result.append("\" ");

            // bold
            if (font.isBold()) {
                result.append("font-weight=\"bold\" ");
            } else {
                result.append("font-weight=\"normal\" ");
            }

            // italic
            if (font.isItalic()) {
                result.append("font-style=\"italic\" ");
            } else {
                result.append("font-style=\"normal\" ");
            }

            // size
            result.append("font-size=\"");
            result.append(SVGGraphics2D.fixedPrecision(font.getSize2D()));
            result.append("\" ");

            // number of coordinate units on the em square,
            // the size of the design grid on which glyphs are laid out
            result.append("units-per-em=\"");
            result.append(SVGGraphics2D.fixedPrecision(SVGGlyph.FONT_SIZE));
            result.append("\" ");

            LineMetrics lm = font.getLineMetrics("By", new FontRenderContext(new AffineTransform(), true, true));

            // The maximum unaccented height of the font within the font coordinate system.
            // If the attribute is not specified, the effect is as if the attribute were set
            // to the difference between the units-per-em value and the vert-origin-y value
            // for the corresponding font.
            result.append("ascent=\"");
            result.append(lm.getAscent());
            result.append("\" ");

            // The maximum unaccented depth of the font within the font coordinate system.
            // If the attribute is not specified, the effect is as if the attribute were set
            // to the vert-origin-y value for the corresponding font.
            result.append("desscent=\"");
            result.append(lm.getDescent());
            result.append("\" ");

            // For horizontally oriented glyph layouts, indicates the alignment
            // coordinate for glyphs to achieve alphabetic baseline alignment.
            // result.append("alphabetic=\"0\"

            // close "<font-face"
            result.append("/>\n");

            // missing glyph
            SVGGlyph glyph = createGlyph(font.getMissingGlyphCode(), font);
            result.append("<missing-glyph ");
            result.append(glyph.getHorizontalAdvanceXString());
            result.append(" ");
            result.append(glyph.getPathString());
            result.append("/>\n");

            // regular glyphs
            Iterator glyphs = getGlyphs(font).values().iterator();
            while (glyphs.hasNext()) {
                result.append(glyphs.next().toString());
                result.append("\n");
            }

            // close "<font>"
            result.append("</font>\n");
        }

        return result.toString();
    }

    /**
     * font replacements makes SVG in AdobeViewer look better, firefox replaces
     * all font settings, even the family fame
     */
    private static final Properties replaceFonts = new Properties();
    static {
        replaceFonts.setProperty("Dialog", "Helvetica");
        replaceFonts.setProperty("DialogInput", "Helvetica");
        replaceFonts.setProperty("Serif", "TimesRoman");
        replaceFonts.setProperty("SansSerif", "Helvetica");
        // replaceFonts.setProperty("Monospaced", "Courier");
    }

    /**
     * creates a font based on the parameter. The size will be {@link SVGGlyph.FONT_SIZE},
     * transformation will be removed and the family name is replaced using
     * {@link  SVGFontTable.replaceFonts}.
     *
     * @param font
     * @return font based on the parameter
     */
    protected Font substituteFont(Font font) {
        HashMap attributes = new HashMap(font.getAttributes());

        // set default font size
        attributes.put(TextAttribute.SIZE, new Float(SVGGlyph.FONT_SIZE));

        // remove font transformation
        attributes.remove(TextAttribute.TRANSFORM);

        // replace font family
        String replacedFamiliy = replaceFonts.getProperty((String) attributes.get(TextAttribute.FAMILY));
        if (replacedFamiliy != null) {
            attributes.put(TextAttribute.FAMILY, replacedFamiliy);
        }

        return new Font(attributes);
    }

    /**
     * replaces the font name using {@link  SVGFontTable.replaceFonts} and adds
     * Bold, Italic, Underline, UnderStroke
     *
     * @param font
     * @return unique name for font
     */
    protected String getFontName(Font font) {
        // replace font family
        String replacedFamiliy = replaceFonts.getProperty(font.getFamily());
        if (replacedFamiliy != null) {
            return replacedFamiliy;
        } else {
            return font.getFamily();
        }
    }

    /**
     * replaces the font name using {@link  SVGFontTable.replaceFonts} and adds
     * Bold, Italic, Underline, UnderStroke etc.
     *
     * @param font
     * @return unique name for font
     */
    protected String getEmbeddedFontName(Font font) {
        // replace the name
        StringBuffer result = new StringBuffer(getFontName(font));

        // weight
        Object weight = font.getAttributes().get(TextAttribute.WEIGHT);
        if (TextAttribute.WEIGHT_BOLD.equals(weight)) {
            result.append("Bold");
        } else if (TextAttribute.WEIGHT_DEMIBOLD.equals(weight)) {
            result.append("DemiBold");
        } else if (TextAttribute.WEIGHT_DEMILIGHT.equals(weight)) {
            result.append("DemiLight");
        } else if (TextAttribute.WEIGHT_EXTRA_LIGHT.equals(weight)) {
            result.append("ExtraLight");
        } else if (TextAttribute.WEIGHT_EXTRABOLD.equals(weight)) {
            result.append("ExtraBold");
        } else if (TextAttribute.WEIGHT_HEAVY.equals(weight)) {
            result.append("Heavy");
        } else if (TextAttribute.WEIGHT_LIGHT.equals(weight)) {
            result.append("Light");
        } else if (TextAttribute.WEIGHT_MEDIUM.equals(weight)) {
            result.append("Medium");
        } else if (TextAttribute.WEIGHT_REGULAR.equals(weight)) {
            result.append("WRegular");
        } else if (TextAttribute.WEIGHT_SEMIBOLD.equals(weight)) {
            result.append("SemiBold");
        } else if (TextAttribute.WEIGHT_ULTRABOLD.equals(weight)) {
            result.append("UltraBold");
        }

        // italic
        Object posture = font.getAttributes().get(TextAttribute.POSTURE);
        if (TextAttribute.POSTURE_OBLIQUE.equals(posture)) {
            result.append("Italic");
        } else if (TextAttribute.POSTURE_REGULAR.equals(posture)) {
            result.append("Plain");
        }

        // underline
        Object ul = font.getAttributes().get(TextAttribute.UNDERLINE);
        if (TextAttribute.UNDERLINE_LOW_DASHED.equals(ul)) {
            result.append("UnderlineLowDashed");
        } else if (TextAttribute.UNDERLINE_LOW_DOTTED.equals(ul)) {
            result.append("UnderlineLowDotted");
        } else if (TextAttribute.UNDERLINE_LOW_GRAY.equals(ul)) {
            result.append("UnderlineLowGray");
        } else if (TextAttribute.UNDERLINE_LOW_ONE_PIXEL.equals(ul)) {
            result.append("UnderlineLowOnePixel");
        } else if (TextAttribute.UNDERLINE_ON.equals(ul)) {
            result.append("Underline");
        }

        // strike through
        if (font.getAttributes().get(TextAttribute.STRIKETHROUGH) != null) {
            result.append("StrikeThrough");
        }

        // width
        Object width = font.getAttributes().get(TextAttribute.WIDTH);
        if (TextAttribute.WIDTH_CONDENSED.equals(width)) {
            result.append("Condensed");
        } else if (TextAttribute.WIDTH_EXTENDED.equals(width)) {
                result.append("Extended");
        } else if (TextAttribute.WIDTH_REGULAR.equals(width)) {
                result.append("WRegular");
        } else if (TextAttribute.WIDTH_SEMI_CONDENSED.equals(width)) {
                result.append("SemiCondensed");
        } else if (TextAttribute.WIDTH_SEMI_EXTENDED.equals(width)) {
                result.append("SemiExtended");
        }

        return result.toString();
    }

}
