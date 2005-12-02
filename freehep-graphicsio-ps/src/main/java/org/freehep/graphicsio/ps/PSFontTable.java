// Copyright 2001-2005, FreeHEP.
package org.freehep.graphicsio.ps;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Properties;

import org.freehep.graphics2d.font.CharTable;
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
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/PSFontTable.java f24bd43ca24b 2005/12/02 00:39:35 duns $
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

/*
    private static boolean isStandardFont(Font font) {
        String fontName = font.getName().toLowerCase();
        return (fontName.indexOf("helvetica") >= 0)
                || (fontName.indexOf("avantgarde") >= 0)
                || (fontName.indexOf("bookman") >= 0)
                || (fontName.indexOf("courier") >= 0)
                || (fontName.indexOf("newcenturysclbk") >= 0)
                || (fontName.indexOf("palatino") >= 0)
                || (fontName.indexOf("times") >= 0)
                || (fontName.indexOf("zapfdingbats") >= 0)
                || (fontName.indexOf("zapfchancery") >= 0)
                || (fontName.indexOf("symbol") >= 0);
    }
*/
    /*
     * public static String toStandardFontName(Font font) { String oblique =
     * "Oblique"; boolean setstyle = true; String fontName = ""; String
     * oFontName = font.getName().toLowerCase(); if
     * ((oFontName.indexOf("sansserif") >= 0) || (oFontName.indexOf("helvetica") >=
     * 0)) { fontName = "Helvetica"; } else if ((oFontName.indexOf("times") >=
     * 0) || (oFontName.indexOf("serif") >= 0)) { fontName = "Times"; oblique =
     * "Italic"; } else if ((oFontName.indexOf("courier") >= 0)) { fontName =
     * "Courier"; } else if ((oFontName.indexOf("dingbats") >= 0)) { fontName =
     * "ZapfDingbats"; setstyle = false; } else if ((oFontName.indexOf("symbol") >=
     * 0)) { fontName = "Symbol"; setstyle = false; } else { fontName =
     * "Helvetica"; }
     * 
     * if (setstyle) { boolean hyphen = false; if (font.isItalic()) { fontName +=
     * "-"+oblique; hyphen = true; } if (font.isBold()) { fontName += (hyphen ? "" :
     * "-") + "Bold"; } } return fontName; }
     */

    private static final Properties psFontNames = new Properties();
    static {
        psFontNames.setProperty("TimesRoman", "Times-Roman");
        psFontNames.setProperty("TimesRoman-Italic", "Times-Italic");
        psFontNames.setProperty("TimesRoman-Bold", "Times-Bold");
        psFontNames.setProperty("TimesRoman-BoldItalic", "Times-BoldItalic");
        psFontNames.setProperty("Helvetica-Italic", "Helvetica-Oblique");
        psFontNames
                .setProperty("Helvetica-BoldItalic", "Helvetica-BoldOblique");
        psFontNames.setProperty("Courier-Italic", "Courier-Oblique");
        psFontNames.setProperty("Courier-BoldItalic", "Courier-BoldOblique");
        psFontNames.setProperty("Avantgarde-Italic", "Avantgarde-Oblique");
        psFontNames.setProperty("Avantgarde-BoldItalic",
                "Avantgarde-BoldOblique");
        psFontNames.setProperty("Symbol-Italic", "Symbol");
        psFontNames.setProperty("Symbol-Bold", "Symbol");
        psFontNames.setProperty("Symbol-BoldItalic", "Symbol");
        psFontNames.setProperty("ZapfDingbats-Italic", "ZapfDingbats");
        psFontNames.setProperty("ZapfDingbats-Bold", "ZapfDingbats");
        psFontNames.setProperty("ZapfDingbats-BoldItalic", "ZapfDingbats");
    }

    protected Font substituteFont(Font font) {
        return font;
    }

    /**
     * Uses the font name as a reference. Whitespace is stripped. The font style
     * (italic/bold) is added as a suffix delimited by a dash.
     */
    protected String createFontReference(Font font) {
        String fontName = font.getName();

        StringBuffer psFontName = new StringBuffer();
        for (int i = 0; i < fontName.length(); i++) {
            char c = fontName.charAt(i);
            if (!Character.isWhitespace(c)) {
                psFontName.append(c);
            }
        }

        boolean hyphen = false;
        if (font.isBold()) {
            hyphen = true;
            psFontName.append("-Bold");
        }
        if (font.isItalic()) {
            psFontName.append((hyphen ? "" : "-") + "Italic");
        }

        fontName = psFontName.toString();
        fontName = psFontNames.getProperty(fontName, fontName);
        return fontName;
    }

}
