package org.freehep.graphicsio.ps;

import java.awt.font.TextAttribute;
import java.util.Map;

import org.freehep.graphics2d.GenericTagHandler;
import org.freehep.util.ScientificFormat;
import org.freehep.util.Value;

/**
 * Class to create a "cfont" tag in PS. Use {@link PSStringStyler#getStyledString(java.util.Map, String)}
 * to operate.
 *
 * @author Charles Loomis
 * @author Steffen Greiffenberg
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/PSStringStyler.java cba39eb5843a 2006/03/20 18:04:28 duns $
 */
public class PSStringStyler {

    /**
     * The value 5 / 6 was found by try and error, e.g.
     * a font size of 300 has a valid cfont size of 250.
     * Not clear why.
     */
    private static final float FONTSIZE_CORRECTION = 5.0f / 6.0f;

    // Constant strings for the styles.
    // private final String UNSTYLED_FLAG = "\\340\\000";

    private static final String BOLD_CHAR = "\\340\\001";

    private static final String ITALIC_CHAR = "\\340\\002";

    private static final String BOLD_ITALIC_CHAR = "\\340\\003";

    // Constant strings for the ornaments (underlining, etc.)
    // superscript is done by transformation
    // private final String SUPERSUBSCRIPT_FLAG = "\\360\\000";

    private static final String STRIKEOUT_FLAG = "\\360\\001";

    private static final String UNDERLINE_FLAG = "\\360\\002";

    private static final String DASHED_UNDERLINE_FLAG = "\\360\\003";

    private static final String DOTTED_UNDERLINE_FLAG = "\\360\\004";

    private static final String GRAY_UNDERLINE_FLAG = "\\360\\005";

    private static final String THICK_UNDERLINE_FLAG = "\\360\\006";

    private static final String OVERLINE_FLAG = "\\360\\007";

    // Constant strings giving the begin-group and end-group markers.
    private static final String BEGIN_GROUP = "\\360\\376";

    private static final String END_GROUP = "\\360\\377";

    /**
     * Must override the parse method because the PostScript string must be
     * initialized and terminated.
     */
    public static String getStyledString(Map /*<TextAttribute,?>*/ attributes, String string) {
        StringBuffer result = new StringBuffer();

        Float size = (Float) attributes.get(TextAttribute.SIZE);

        // FIXME: The lines are taken from PSTagHandler, but
        // they produced a to large text output.
        result.append(new ScientificFormat(6, 9, false).format(size.floatValue() * FONTSIZE_CORRECTION));
        result.append(" ");

        // cfont tag
        result.append("cfont");

        // replace font name
        String fontName = ((String)attributes.get(TextAttribute.FAMILY)).toLowerCase();
        if (fontName.indexOf("helvetica") != -1) {
            result.append("H");
        } else if (fontName.indexOf("dialog") != -1) {
            result.append("H");
        } else if (fontName.indexOf("dialoginput") != -1) {
            result.append("H");
        } else if (fontName.indexOf("sansserif") != -1) {
            result.append("H");
        } else if (fontName.indexOf("times") != -1) {
            result.append("T");
        } else if (fontName.indexOf("serif") != -1) {
            result.append("T");
        } else if (fontName.indexOf("courier") != -1) {
            result.append("C");
        } else if (fontName.indexOf("monospaced") != -1) {
            result.append("C");
        } else if (fontName.indexOf("typewriter") != -1) {
            result.append("C");
        } else {
            result.append("H");
        }

        // weight
        Object weight = attributes.get(TextAttribute.WEIGHT);
        if (TextAttribute.WEIGHT_BOLD.equals(weight)) {
            result.append("B");
        }

        // posture
        Object posture = attributes.get(TextAttribute.POSTURE);
        if (TextAttribute.POSTURE_OBLIQUE.equals(posture)) {
            result.append("I");
        }

        // open the brackets for text string
        result.append("\n(");

        // count BEGIN_GROUP for writing END_GROUP
        Value openGroups = new Value();
        openGroups.set(0);

        result.append(getOpenTag(attributes, openGroups));
        result.append(getUnicodes(string));
        result.append(getCloseTag(openGroups));

        result.append(")");

        return result.toString();
    }

    /**
     * Converts str into unicodes
     *
     * @return list of unicodes for str
     */
    public static String getUnicodes(String str) {

        // Copy string to temporary string buffer.
        StringBuffer codedString = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            int chr = (int) str.charAt(i);
            int cvalue = (chr & 0x000000ff);
            int fvalue = (chr & 0x0000ff00) >>> 8;
            String cbyte = Integer.toOctalString(cvalue);
            String fbyte = Integer.toOctalString(fvalue);

            // Put in the font byte first.
            codedString.append('\\');
            for (int j = 0; j < (3 - fbyte.length()); j++) {
                codedString.append('0');
            }
            codedString.append(fbyte);

            // Now the character itself. Write as octal codes
            // non-printable characters, the backslash, parentheses,
            // and the percent.
            if (cvalue < 32 || cvalue > 126 || cvalue == '\\' || cvalue == '%'
                    || cvalue == '(' || cvalue == ')') {
                codedString.append('\\');
                for (int j = 0; j < (3 - cbyte.length()); j++) {
                    codedString.append('0');
                }
                codedString.append(cbyte);
            } else {
                codedString.append((char) cvalue);
            }
        }

        return codedString.toString();
    }

    /**
     * @param str string to convert
     * @return str with replaced (, ), \, %
     */
    public static String getEscaped(String str) {

        // Then protect against unbalanced parentheses in the string.
        // Copy string to temporary string buffer.
        StringBuffer result = new StringBuffer();

        result.append("(");

        // Loop over all characters in the string and escape the
        // parentheses.
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(' || c == ')' || c == '\\' || c == '%') {
                result.append('\\');
                result.append(c);
            } else if (c == 0) {
                result.append('?');
            } else {
                result.append(c);
            }
        }

        result.append(")");

        return result.toString();
    }

    /**
     * handles various instances of {@link TextAttribute}
     *
     * @param attributes font to translate in PS tags
     */
    private static String getOpenTag(Map/* Map<TextAttribute,?>*/ attributes, Value open) {

        int openGroups = open.getInt();

        StringBuffer result = new StringBuffer();

        // weight
        Object weight = attributes.get(TextAttribute.WEIGHT);

        // italic
        Object posture = attributes.get(TextAttribute.POSTURE);

        // bold and italic?
        if (weight != null && !TextAttribute.WEIGHT_REGULAR.equals(weight)) {
            if (TextAttribute.POSTURE_OBLIQUE.equals(posture)) {
                result.append(BOLD_ITALIC_CHAR);
            } else {
                result.append(BOLD_CHAR);
            }
            openGroups ++;
            result.append(BEGIN_GROUP);

        } else

        // simple italic?
        if (TextAttribute.POSTURE_OBLIQUE.equals(posture)) {
            result.append(ITALIC_CHAR);
            openGroups ++;
            result.append(BEGIN_GROUP);
        }

        // superScript is done by font.getTransform()
        // Object superScript = attributes.get(TextAttribute.SUPERSCRIPT);

        // underline
        Object underline = attributes.get(TextAttribute.UNDERLINE);
        if (TextAttribute.UNDERLINE_LOW_DASHED.equals(underline)) {
            result.append(DASHED_UNDERLINE_FLAG);
            openGroups ++;
            result.append(BEGIN_GROUP);
        } else if (TextAttribute.UNDERLINE_ON.equals(underline)) {
            result.append(UNDERLINE_FLAG);
            openGroups ++;
            result.append(BEGIN_GROUP);
        } else if (TextAttribute.UNDERLINE_LOW_DOTTED.equals(underline)) {
            result.append(DOTTED_UNDERLINE_FLAG);
            openGroups ++;
            result.append(BEGIN_GROUP);
        } else if (TextAttribute.UNDERLINE_LOW_TWO_PIXEL.equals(underline)) {
            result.append(THICK_UNDERLINE_FLAG);
            openGroups ++;
            result.append(BEGIN_GROUP);
        } else if (TextAttribute.UNDERLINE_LOW_GRAY.equals(underline)) {
            result.append(GRAY_UNDERLINE_FLAG);
            openGroups ++;
            result.append(BEGIN_GROUP);
        } else if (TextAttribute.UNDERLINE_LOW_DASHED.equals(underline)) {
            result.append(DASHED_UNDERLINE_FLAG);
            openGroups ++;
            result.append(BEGIN_GROUP);
        } else if (GenericTagHandler.UNDERLINE_OVERLINE.equals(underline)) {
            result.append(OVERLINE_FLAG);
            result.append(BEGIN_GROUP);
        }

        // strike through
        Object strike = attributes.get(TextAttribute.STRIKETHROUGH);
        if (TextAttribute.STRIKETHROUGH_ON.equals(strike)) {
            result.append(STRIKEOUT_FLAG);
            openGroups ++;
            result.append(BEGIN_GROUP);
        }

        // set the counter
        open.set(openGroups);

        return result.toString();
    }

    /**
     * @return string for closing all BEGIN_GROUP's
     */
    private static String getCloseTag(Value open) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < open.getInt(); i++) {
            result.append(END_GROUP);
        }

        // not needed but to leave the counter in correct state
        open.set(0);

        return result.toString();
    }
}
