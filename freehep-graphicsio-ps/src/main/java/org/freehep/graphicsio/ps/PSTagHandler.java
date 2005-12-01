// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphicsio.ps;

import java.awt.*;
import java.util.*;

import org.freehep.graphics2d.TagHandler;
import org.freehep.graphics2d.TagString;

/**
 *
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/PSTagHandler.java 2689041eec29 2005/12/01 22:37:27 duns $
 */
public class PSTagHandler extends TagHandler {

    String baseFont = "";
    private int fontSize;

    // This is a stack to keep track of the font styles.
    private Stack fStack = new Stack();

    // This is a stack to keep track of the combined superscript and
    // subscripts to ensure that the nesting is done correctly.
    private Stack vStack = new Stack();

    // Some constants for the style parameters.
    private final String UNSTYLED = "";
    private final String BOLD = "B";
    private final String ITALIC = "I";
    private final String BOLD_ITALIC = "BI";

    // Constant strings for the styles.
    private final String UNSTYLED_FLAG = "\\340\\000";
    private final String BOLD_CHAR = "\\340\\001";
    private final String ITALIC_CHAR = "\\340\\002";
    private final String BOLD_ITALIC_CHAR = "\\340\\003";

    // Constant strings for the ornaments (underlining, etc.)
    private final String SUPERSUBSCRIPT_FLAG = "\\360\\000";
    private final String STRIKEOUT_FLAG = "\\360\\001";
    private final String UNDERLINE_FLAG = "\\360\\002";
    private final String DASHED_UNDERLINE_FLAG = "\\360\\003";
    private final String DOTTED_UNDERLINE_FLAG = "\\360\\004";
    private final String GRAY_UNDERLINE_FLAG = "\\360\\005";
    private final String THICK_UNDERLINE_FLAG = "\\360\\006";
    private final String OVERLINE_FLAG = "\\360\\007";

    // Constant strings giving the begin-group and end-group markers.
    private final String BEGIN_GROUP = "\\360\\376";
    private final String END_GROUP = "\\360\\377";

    public PSTagHandler(Font font) {
        super();

	fStack.push(UNSTYLED);
	vStack.push(Boolean.FALSE);
	StringBuffer psFont = new StringBuffer("cfont");

	String fontName = font.getName();
	fontName = fontName.toLowerCase();

	if (fontName.indexOf("helvetica")>=0) {
	    psFont.append("H");
	} else if (fontName.indexOf("sansserif")>=0) {
	    psFont.append("H");
	} else if (fontName.indexOf("times")>=0) {
	    psFont.append("T");
	} else if (fontName.indexOf("serif")>=0) {
	    psFont.append("T");
	} else if (fontName.indexOf("courier")>=0) {
	    psFont.append("C");
	} else {
	    psFont.append("H");
	}
	baseFont = psFont.toString();

	this.fontSize = font.getSize();
    }

    /**
     * Must override the parse method because the PostScript string
     * must be initialized and terminated. */
    public String parse(TagString string) {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(fontSize+" "+baseFont+"\n(");
    	buffer.append(super.parse(string));
    	buffer.append(")");
    	return buffer.toString();
    }

    /**
     * Must override to make this into a unicode string. */
    protected String text(String str) {

	// Copy string to temporary string buffer.
	StringBuffer codedString = new StringBuffer();

	for (int i=0; i<str.length(); i++) {
	    int chr = (int) str.charAt(i);
	    int cvalue = (chr & 0x000000ff);
	    int fvalue = (chr & 0x0000ff00)>>>8;
	    String cbyte = Integer.toOctalString(cvalue);
	    String fbyte = Integer.toOctalString(fvalue);

	    // Put in the font byte first.
	    codedString.append('\\');
	    for (int j=0; j<(3-fbyte.length()); j++) {
		codedString.append('0');
	    }
	    codedString.append(fbyte);

	    // Now the character itself.  Write as octal codes
            // non-printable characters, the backslash, parentheses,
            // and the percent.
	    if (cvalue<32 || cvalue>126 ||
                cvalue=='\\' || cvalue=='%' ||
                cvalue=='(' || cvalue==')') {
		codedString.append('\\');
		for (int j=0; j<(3-cbyte.length()); j++) {
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
     * handles bold <b>, italic <i>, superscript <sup>, subscript <sub>
     */
    protected String openTag(String tag) {
        StringBuffer tagString = new StringBuffer();
        if (tag.equalsIgnoreCase("b")) {
	    if (fStack.peek()==ITALIC || fStack.peek()==BOLD_ITALIC) {
		fStack.push(BOLD_ITALIC);
		tagString.append(BOLD_ITALIC_CHAR);
	    } else {
		fStack.push(BOLD);
		tagString.append(BOLD_CHAR);
	    }
            tagString.append(BEGIN_GROUP);
        } else if (tag.equalsIgnoreCase("i")) {
	    if (fStack.peek()==BOLD || fStack.peek()==BOLD_ITALIC) {
		fStack.push(BOLD_ITALIC);
		tagString.append(BOLD_ITALIC_CHAR);
	    } else {
		fStack.push(ITALIC);
		tagString.append(ITALIC_CHAR);
	    }
            tagString.append(BEGIN_GROUP);
        } else if (tag.equalsIgnoreCase("v")) {
            vStack.push(Boolean.TRUE);
            tagString.append(SUPERSUBSCRIPT_FLAG);
        } else if (tag.equalsIgnoreCase("sup")) {
            if (!((Boolean) vStack.peek()).booleanValue()) {
                tagString.append(SUPERSUBSCRIPT_FLAG);
            }
            tagString.append(BEGIN_GROUP);
            vStack.push(Boolean.FALSE);
        } else if (tag.equalsIgnoreCase("sub")) {
            if (!((Boolean) vStack.peek()).booleanValue()) {
                tagString.append(SUPERSUBSCRIPT_FLAG);
                tagString.append(BEGIN_GROUP);
                tagString.append(END_GROUP);
            }
            tagString.append(BEGIN_GROUP);
            vStack.push(Boolean.FALSE);
        } else if (tag.equalsIgnoreCase("u")) {
            tagString.append(UNDERLINE_FLAG);
            tagString.append(BEGIN_GROUP);
        } else if (tag.equalsIgnoreCase("udash")) {
            tagString.append(DASHED_UNDERLINE_FLAG);
            tagString.append(BEGIN_GROUP);
        } else if (tag.equalsIgnoreCase("udot")) {
            tagString.append(DOTTED_UNDERLINE_FLAG);
            tagString.append(BEGIN_GROUP);
        } else if (tag.equalsIgnoreCase("uthick")) {
            tagString.append(THICK_UNDERLINE_FLAG);
            tagString.append(BEGIN_GROUP);
        } else if (tag.equalsIgnoreCase("ugray")) {
            tagString.append(GRAY_UNDERLINE_FLAG);
            tagString.append(BEGIN_GROUP);
        } else if (tag.equalsIgnoreCase("strike")) {
            tagString.append(STRIKEOUT_FLAG);
            tagString.append(BEGIN_GROUP);
        } else if (tag.equalsIgnoreCase("over")) {
            tagString.append(OVERLINE_FLAG);
            tagString.append(BEGIN_GROUP);
        }
        return tagString.toString();
    }

    /**
     * handles bold <b>, italic <i>, superscript <sup>, subscript <sub>
     */
    protected String closeTag(String tag) {
        StringBuffer tagString = new StringBuffer();
        if (tag.equalsIgnoreCase("b")) {
            tagString.append(END_GROUP);
	    fStack.pop();
        } else if (tag.equalsIgnoreCase("i")) {
            tagString.append(END_GROUP);
	    fStack.pop();
        } else if (tag.equalsIgnoreCase("v")) {
            vStack.pop();
        } else if (tag.equalsIgnoreCase("sup")) {
            vStack.pop();
            if (!((Boolean) vStack.peek()).booleanValue()) {
                tagString.append(END_GROUP);
                tagString.append(BEGIN_GROUP);
            }
            tagString.append(END_GROUP);
        } else if (tag.equalsIgnoreCase("sub")) {
            vStack.pop();
            tagString.append(END_GROUP);
        } else if (tag.equalsIgnoreCase("u")) {
            tagString.append(END_GROUP);
        } else if (tag.equalsIgnoreCase("udash")) {
            tagString.append(END_GROUP);
        } else if (tag.equalsIgnoreCase("udot")) {
            tagString.append(END_GROUP);
        } else if (tag.equalsIgnoreCase("uthick")) {
            tagString.append(END_GROUP);
        } else if (tag.equalsIgnoreCase("ugray")) {
            tagString.append(END_GROUP);
        } else if (tag.equalsIgnoreCase("strike")) {
            tagString.append(END_GROUP);
        } else if (tag.equalsIgnoreCase("over")) {
            tagString.append(END_GROUP);
        }
        return tagString.toString();
    }

}
