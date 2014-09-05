// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphics2d;


/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphics2d/src/main/java/org/freehep/graphics2d/TagHandler.java cba39eb5843a 2006/03/20 18:04:28 duns $
 */
public class TagHandler {

    public TagHandler() {
    }

    /**
     * parses string and calls methods for every tag and every not recognized
     * entity The characters &lt; and &gt; have to be written as &amp;lt; and &amp;gt; while
     * the &amp; is written as &amp;amp;
     * 
     * The following three methods are called: defaultEntity(entity) for &amp;amp;
     * &amp;lt; &amp;gt; &amp;quot; &amp;apos; entity(entity) for all other entities
     * openTag(tag) for all &lt;tags&gt; endTag(tag) for all &lt;/tags&gt; text(text) for
     * all text
     * 
     * The startTag, endTag and text methods returns a string which is added to
     * the fully parsed string.
     * 
     * Strings returned from the entity methods will show up in the text methods
     * parameter.
     * 
     * It returns the fully parsed string, including any additions made by the
     * three methods above.
     */
    public String parse(TagString string) {
        String src = string.toString();
        StringBuffer parsedString = new StringBuffer();
        StringBuffer textString = new StringBuffer();
        int i = 0;
        int p = 0;
        try {
            while (i < src.length()) {
                switch (src.charAt(i)) {
                case '&':
                    // handle entities
                    // look for closing ';'
                    i++;
                    p = i;
                    while (src.charAt(i) != ';') {
                        i++;
                    }
                    String ent = src.substring(p, i);
                    if (ent.equals("amp") || ent.equals("gt")
                            || ent.equals("lt") || ent.equals("quot")
                            || ent.equals("apos")) {
                        textString.append(defaultEntity(ent));
                    } else {
                        textString.append(entity(ent));
                    }
                    break;

                case '<':
                    // handle tags

                    // handle any outstanding text
                    if (textString.length() > 0) {
                        parsedString.append(text(textString.toString()));
                        textString = new StringBuffer();
                    }

                    // look for closing '>'
                    i++;
                    p = i;
                    while (src.charAt(i) != '>') {
                        i++;
                    }

                    if (src.charAt(p) == '/') {
                        parsedString.append(closeTag(src.substring(p + 1, i)));
                    } else {
                        parsedString.append(openTag(src.substring(p, i)));
                    }
                    break;
                default:
                    // just move the pointer
                    textString.append(src.charAt(i));
                    break;
                } // switch
                i++;
            } // while

        } catch (ArrayIndexOutOfBoundsException aoobe) {
            // just abort, but give most of the string
            parsedString.append("!PARSEERROR!");
        }

        // final part
        if (textString.length() > 0) {
            parsedString.append(text(textString.toString()));
        }
        return parsedString.toString();
    }

    protected String defaultEntity(String entity) {
        StringBuffer dst = new StringBuffer();
        if (entity.equals("amp")) {
            dst.append('&');
        } else if (entity.equals("gt")) {
            dst.append('>');
        } else if (entity.equals("lt")) {
            dst.append('<');
        } else if (entity.equals("quot")) {
            dst.append('"');
        } else if (entity.equals("apos")) {
            dst.append('\'');
        }
        return dst.toString();
    }

    protected String entity(String entity) {
        StringBuffer dst = new StringBuffer();
        dst.append('&');
        dst.append(entity);
        dst.append(';');
        return dst.toString();
    }

    protected String openTag(String tag) {
        StringBuffer dst = new StringBuffer();
        dst.append('<');
        dst.append(tag);
        dst.append('>');
        return dst.toString();
    }

    protected String closeTag(String tag) {
        StringBuffer dst = new StringBuffer();
        dst.append("</");
        dst.append(tag);
        dst.append('>');
        return dst.toString();
    }

    protected String text(String text) {
        return text;
    }

    public static void main(String[] args) {
        String text = "&lt;Vector<sup><b>Graphics</b></sup> &amp; Card<i><sub>Adapter</sub></i>&gt;";

        TagString s = new TagString(text);
        TagHandler handler = new TagHandler();
        System.out.println("\"" + s + "\"");
        System.out.println(handler.parse(s));
    }
}
