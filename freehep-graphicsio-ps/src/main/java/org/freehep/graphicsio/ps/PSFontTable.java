// Copyright 2001-2005, FreeHEP.
package org.freehep.graphicsio.ps;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import org.freehep.graphics2d.font.CharTable;
import org.freehep.graphics2d.font.FontUtilities;
import org.freehep.graphics2d.font.Lookup;
import org.freehep.graphicsio.FontConstants;
import org.freehep.graphicsio.font.FontEmbedderType1;
import org.freehep.graphicsio.font.FontIncluder;
import org.freehep.graphicsio.font.FontTable;

/**
 * FontTable for PS files. The fonts name is used as a reference for the font.
 * When the font is first used, it is embedded to the file if it is not a
 * standard font. If it is unknown it is not substituted.
 * 
 * @author Simon Fischer
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/PSFontTable.java 59372df5e0d9 2007/02/06 21:11:19 duns $
 */
public class PSFontTable extends FontTable {

    private OutputStream out;

    private FontRenderContext context;

    public PSFontTable(OutputStream out, FontRenderContext context) {
        super();
        this.out = out;
        this.context = context;
    }

    public CharTable getEncodingTable() {
        return Lookup.getInstance().getTable("STDLatin");
    }

    protected void firstRequest(Entry e, boolean embed, String embedAs)
            throws IOException {
        FontIncluder fontIncluder = null;
        e.setWritten(true);

        // There are NO standard fonts in PS.
        // if (isStandardFont(e.getFont())) return;

        out.flush();

        if (embed) {
            if (embedAs.equals(FontConstants.EMBED_FONTS_TYPE3)) {
                fontIncluder = new PSFontEmbedder(context, new PrintStream(out));
            } else if (embedAs.equals(FontConstants.EMBED_FONTS_TYPE1)) {
                fontIncluder = new FontEmbedderType1(context, out, true);
            } else {
                System.err
                        .println("PSFontTable: not a valid value for embedAs: "
                                + embedAs);
            }
        } else {
            // FIXME: set the best standard font
            // e.setReference(standardfontName)
            return;
        }
        
        fontIncluder
                .includeFont(e.getFont(), e.getEncoding(), e.getReference());
        out.flush();
    }

    /**
     * Java font names -> PS Font names, used by {@link #normalize(java.util.Map)}
     */
    private static final Properties replaceFonts = new Properties();
    static {
        replaceFonts.setProperty("timesroman", "Times");
        replaceFonts.setProperty("dialog", "Helvetica");
        replaceFonts.setProperty("dialoginput", "Courier-New");
        // FIXME: works well on windows, others?
        replaceFonts.setProperty("serif", "Times");
        replaceFonts.setProperty("sansserif", "Helvetica");
        // FIXME: works well on windows, others?
        replaceFonts.setProperty("monospaced", "Courier-New");
        replaceFonts.setProperty("typewriter", "Courier-New");
    }

    /**
     * fonts that have no TextAttribute.WEIGHT and TextAttribute.POSTURE,
     * used by {@link #createFontReference(java.awt.Font)}
     */
    private static final HashSet ignoreAtributes = new HashSet();
    static {
        ignoreAtributes.add("Symbol");
        ignoreAtributes.add("ZapfDingbats");
    }

    /**
     * removes any transformation and superscript, changes the names
     * to PS font name
     *
     * @param font
     * @return derived font
     */
    protected Font substituteFont(Font font) {
        Map attributes = FontUtilities.getAttributes(font);
        // change names
        // normalize(attributes);
        // remove transformations
        attributes.remove(TextAttribute.TRANSFORM);
        attributes.remove(TextAttribute.SUPERSCRIPT);
        return new Font(attributes);
    }

    /**
     * Uses the font name as a reference. Whitespace is stripped. The font style
     * (italic/bold) is added as a suffix delimited by a dash.
     * Uses {@link #normalize(java.util.Map)}
     */
    protected String createFontReference(Font font) {
        Map /*<TextAttribute, ?>*/ attributes = FontUtilities.getAttributes(font);
        normalize(attributes);

        // replace the name
        StringBuffer result = new StringBuffer();

        // insert family at the end because oft the "-" between
        // name and TextAttribute
        String family = (String) attributes.get(TextAttribute.FAMILY);

        // weight
        Object weight = ignoreAtributes.contains(family) ?
            null : attributes.get(TextAttribute.WEIGHT);

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
            // result.append("WRegular");
        } else if (TextAttribute.WEIGHT_SEMIBOLD.equals(weight)) {
            result.append("SemiBold");
        } else if (TextAttribute.WEIGHT_ULTRABOLD.equals(weight)) {
            result.append("UltraBold");
        }

        // italic
        Object posture = ignoreAtributes.contains(family) ?
            null : attributes.get(TextAttribute.POSTURE);

        if (TextAttribute.POSTURE_OBLIQUE.equals(posture)) {
            if (family.equals("Times")) {
                result.append("Italic");
            } else {
                result.append("Oblique");
            }
        } else if (TextAttribute.POSTURE_REGULAR.equals(posture)) {
            // result.append("IRegular");
        }

        // Times -> Times-Roman
        if (family.equals("Times") && result.length() == 0) {
            result.append("Roman");
        }

        // underline is not a specific font
        // Object ul = font.getAttributes().get(TextAttribute.UNDERLINE);
        // if (TextAttribute.UNDERLINE_LOW_DASHED.equals(ul)) {
        //     result.append("UnderlineLowDashed");
        // } else if (TextAttribute.UNDERLINE_LOW_DOTTED.equals(ul)) {
        //     result.append("UnderlineLowDotted");
        // } else if (TextAttribute.UNDERLINE_LOW_GRAY.equals(ul)) {
        //     result.append("UnderlineLowGray");
        // } else if (TextAttribute.UNDERLINE_LOW_ONE_PIXEL.equals(ul)) {
        //     result.append("UnderlineLowOnePixel");
        // } else if (TextAttribute.UNDERLINE_ON.equals(ul)) {
        //     result.append("Underline");
        // }

        // strike through is not a specific font
        // if (font.getAttributes().get(TextAttribute.STRIKETHROUGH) != null) {
        //     result.append("StrikeThrough");
        // }

        // width is not a specific font
        // Object width = font.getAttributes().get(TextAttribute.WIDTH);
        // if (TextAttribute.WIDTH_CONDENSED.equals(width)) {
        //     result.append("Condensed");
        // } else if (TextAttribute.WIDTH_EXTENDED.equals(width)) {
        //     result.append("Extended");
        // } else if (TextAttribute.WIDTH_REGULAR.equals(width)) {
        //    // result.append("WRegular");
        // } else if (TextAttribute.WIDTH_SEMI_CONDENSED.equals(width)) {
        //     result.append("SemiCondensed");
        // } else if (TextAttribute.WIDTH_SEMI_EXTENDED.equals(width)) {
        //     result.append("SemiExtended");
        // }

        // insert "name-" at the beginning or return plain "name"
        if (result.length() > 0) {
            result.insert(0, "-");
            result.insert(0, attributes.get(TextAttribute.FAMILY));
        } else {
            result.append(attributes.get(TextAttribute.FAMILY));
        }

        return result.toString();
    }

    /**
     * Replaces TextAttribute.FAMILY by values of replaceFonts.
     * Whitespace is family name stripped. When a
     * font created using the result of this method the transformation would be:
     *
     * <code>java.awt.Font[family=SansSerif,name=SansSerif,style=plain,size=30]</code><BR>
     * will result to:<BR>
     * <code>java.awt.Font[family=SansSerif,name=Helvetica,style=plain,size=30]</code><BR><BR>
     *
     * Uses {@link FontTable#normalize(java.util.Map)} first.
     *
     * @param attributes with font name to change
     */
    public static void normalize(Map /*<TextAttribute, ?>*/ attributes) {
        // dialog.bold -> Dialog with TextAttribute.WEIGHT_BOLD
        FontTable.normalize(attributes);

        // get replaced font family name (Yes it's right, not the name!)
        String family = replaceFonts.getProperty(
            ((String) attributes.get(TextAttribute.FAMILY)).toLowerCase());
        if (family == null) {
            family = (String) attributes.get(TextAttribute.FAMILY);
        }

        // remove spaces
        family = family.replaceAll(" ", "");

        // store family
        attributes.put(TextAttribute.FAMILY, family);
    }
}
