// Copyright FreeHEP, 2000-2007
package org.freehep.graphics2d;

import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedString;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import org.freehep.graphics2d.font.FontUtilities;

/**
 * The class converts HTML tags like &lt;u&gt; in instances of {@link TextAttribute}.
 *
 * @author Mark Donszelmann
 * @author Steffen Greiffenberg
 * @version $Id: freehep-graphics2d/src/main/java/org/freehep/graphics2d/GenericTagHandler.java 4c4708a97391 2007/06/12 22:32:31 duns $
 */
public class GenericTagHandler extends TagHandler {

    /**
     * TextAttribute for overline, not a standard
     */
    public static Integer UNDERLINE_OVERLINE = new Integer(128);

    /**
     * context to draw AttributedString
     */
    private Graphics2D graphics;

    /**
     * text without any tags, e.g. "&lt;sub&gt;text&lt;/sub&gt;" would become "text".
     * filled by {@link #text(String)}
     */
    private StringBuffer clearedText;

    /**
     * stores AttributeEntries created by {@link #closeTag(String)}
     */
    private Vector/*<AttributeEntry>*/<AttributeEntry> attributes;

    /**
     * stores all open tags, e.g. "&lt;sub&gt;" an the position
     * on which it placed in text. Filled by {@link #openTag(String)},
     * emptied and translated into <code>attributes</code> by
     * {@link #closeTag(String)}
     */
    private Hashtable<String, Integer> tags;

    /**
     * store the names of font families before they are changed by openTag()
     */
    private Stack/*<String>*/<?> fontFamilyStack;

    /**
     * if we aplly TextAttribute.SUPERSCRIPT with correction of
     * transformation the text is to high / to low by some points
     */
    private double superscriptCorrection;

    /**
     * creates a tag handler for printing text and calculating its size
     *
     * @param graphics stores the font for calculations
     */
    public GenericTagHandler(Graphics2D graphics) {
        super();
        this.graphics = graphics;
        this.clearedText = new StringBuffer();
        this.tags = new Hashtable<String, Integer>();
    }

    /**
     * prints the tagged string at x:y
     *
     * @param s string to print using the stored graphics
     * @param x coordinate for drawing
     * @param y coordinate for drawing
     * @param superscriptCorrection correction for to high / to low text
     */
    public void print(TagString s, double x, double y, double superscriptCorrection) {

        fontFamilyStack = new Stack<Object>();

        this.clearedText = new StringBuffer();
        this.attributes = new Vector<AttributeEntry>();
        this.superscriptCorrection = superscriptCorrection;

        parse(s);

        // close all open tags to ensure all
        // open attributes are applied
        while (tags.size() > 0) {
            closeTag(tags.keys().nextElement());
        }

        // create attributed string to print
        // with current font settings
        AttributedString attributedString = new AttributedString(
            clearedText.toString(),
            FontUtilities.getAttributes(graphics.getFont()));

        // aplly attributes
        for (int i = 0; i < attributes.size(); i++) {
            attributes.elementAt(i).apply(attributedString);
        }

        graphics.drawString(attributedString.getIterator(), (float)x, (float)y);
    }

    /**
     * calculates the string bounds using the current font of {@link #graphics}.
     *
     * @param s string to calculate
     * @return bouding box after parsing s
     */
    public TextLayout createTextLayout(TagString s, double superscriptCorrection) {

        fontFamilyStack = new Stack<Object>();

        this.clearedText = new StringBuffer();
        this.attributes = new Vector<AttributeEntry>();
        this.superscriptCorrection = superscriptCorrection;

        parse(s);

        // close all open tags to ensure all
        // open attributes are applied
        while (tags.size() > 0) {
            closeTag(tags.keys().nextElement());
        }

        // create attributed string to print
        // with current font settings
        AttributedString attributedString = new AttributedString(
            clearedText.toString(),
            FontUtilities.getAttributes(graphics.getFont()));

        // aplly attributes
        for (int i = 0; i < attributes.size(); i++) {
            attributes.elementAt(i).apply(attributedString);
        }

        // create the layout
        return new TextLayout(
            attributedString.getIterator(),
            graphics.getFontRenderContext());
    }

    /**
     * handles bold &lt;b&gt;, italic &lt;i&gt;, superscript &lt;sup&gt;, subscript &lt;sub&gt;,
     * vertical &lt;v&gt;, overline &lt;over&gt;, underline &lt;u&gt;, strikethrough &lt;s&gt;,
     * underline dashed &lt;udash&gt;, underline dotted &lt;udot&gt; and typewriter &lt;tt&gt;
     *
     * @param tag one of the known tags, otherwise the overloaded methode is called
     * @return empty string or the result of the overloaded method
     */
    // FIXME: check if we can support overline and vertical?
    protected String openTag(String tag) {
        // store position of parser for openening tag only if
        // it the first openened, e.g. <b>text<b>text2</b> will draw
        // text and text2 in bold weight
        if (!tags.containsKey(tag)) {
            tags.put(tag, new Integer(clearedText.length()));
        }
        return "";
    }

    /**
     * closes the given html tag. It doesn't matter, if that one was opened, so &lt;/udot&gt;
     * closes a &lt;udash&gt; too, because the use the same TextAttribute.UNDERLINE.
     *
     * @param tag to close
     * @return empty string or the result of the overloaded method
     */
    protected String closeTag(String tag) {
        // begin is stored in 'tags'
        int begin;

        // do nothing if tag wasn't opened
        if (!tags.containsKey(tag)) {
            return super.closeTag(tag);
        } else {
            begin = tags.get(tag).intValue();
            tags.remove(tag);
        }

        // change attributes
        if (tag.equalsIgnoreCase("b")) {
            this.attributes.add(new AttributeEntry(
                begin,
                clearedText.length(),
                TextAttribute.WEIGHT,
                TextAttribute.WEIGHT_BOLD));
        } else if (tag.equalsIgnoreCase("i")) {
            this.attributes.add(new AttributeEntry(
                begin,
                clearedText.length(),
                TextAttribute.POSTURE,
                TextAttribute.POSTURE_OBLIQUE));
        } else if (tag.equalsIgnoreCase("s") || tag.equalsIgnoreCase("strike")) {
            this.attributes.add(new AttributeEntry(
                begin,
                clearedText.length(),
                TextAttribute.STRIKETHROUGH,
                TextAttribute.STRIKETHROUGH_ON));
        } else if (tag.equalsIgnoreCase("udash")) {
            this.attributes.add(new AttributeEntry(
                begin,
                clearedText.length(),
                TextAttribute.UNDERLINE,
                TextAttribute.UNDERLINE_LOW_DASHED));
        } else if (tag.equalsIgnoreCase("udot")) {
            this.attributes.add(new AttributeEntry(
                begin,
                clearedText.length(),
                TextAttribute.UNDERLINE,
                TextAttribute.UNDERLINE_LOW_DOTTED));
        } else if (tag.equalsIgnoreCase("u")) {
            this.attributes.add(new AttributeEntry(
                begin,
                clearedText.length(),
                TextAttribute.UNDERLINE,
                TextAttribute.UNDERLINE_ON));
        } else if (tag.equalsIgnoreCase("tt")) {
            this.attributes.add(new AttributeEntry(
                begin,
                clearedText.length(),
                TextAttribute.FAMILY,
                fontFamilyStack.pop()));
        } else if (tag.equalsIgnoreCase("v")) {
            // vertical = false;
        } else if (tag.equalsIgnoreCase("over")) {
            this.attributes.add(new AttributeEntry(
                begin,
                clearedText.length(),
                TextAttribute.UNDERLINE,
                UNDERLINE_OVERLINE));
        } else if (tag.equalsIgnoreCase("sup")) {

            // FIXME: not quite clear why this is necessary
            this.attributes.add(new AttributeEntry(
                begin,
                clearedText.length(),
                TextAttribute.TRANSFORM,
                AffineTransform.getTranslateInstance(0, superscriptCorrection)));

            this.attributes.add(new AttributeEntry(
                begin,
                clearedText.length(),
                TextAttribute.SUPERSCRIPT,
                TextAttribute.SUPERSCRIPT_SUPER));

        } else if (tag.equalsIgnoreCase("sub")) {

            // FIXME: not quite clear why this is necessary
            this.attributes.add(new AttributeEntry(
                begin,
                clearedText.length(),
                TextAttribute.TRANSFORM,
                AffineTransform.getTranslateInstance(0, -superscriptCorrection)));

            this.attributes.add(new AttributeEntry(
                begin,
                clearedText.length(),
                TextAttribute.SUPERSCRIPT,
                TextAttribute.SUPERSCRIPT_SUB));
        } else {
            return super.closeTag(tag);
        }

        // set the font
        return "";
    }

    /**
     * calculates miny und maxy for {@link #createTextLayout(TagString, double)}. If
     * {@link #print} is set, text is drawed using
     * {@link Graphics2D#drawString(String, float, float)} of
     * {@link #graphics}
     *
     * @param text text to draw
     * @return unmodified text parameter
     */
    protected String text(String text) {
        // appand text as cleared
        clearedText.append(text);

        return text;
    }

    /**
     * Helper to store an TextAttribute, its value and the range
     * it should cover in <code>text</code>. Entries are created
     * by {@link GenericTagHandler#closeTag(String)} and apllied
     * in {@link GenericTagHandler#print(TagString, double, double, double)}
     */
    private class AttributeEntry {

        /**
         * start offset in text
         */
        private int begin;

        /**
         * end position for TextAttribute in text
         */
        private int end;

        /**
         * the TextAttribute key to layout the text,
         * e.g. TextAttribute.WEIGHT
         */
        private TextAttribute textAttribute;

        /**
         * the TextAttribute key to layout the text,
         * e.g. TextAttribute.WEIGHT_BOLD
         */
        private Object value;

        /**
         * stores the given parameters
         *
         * @param begin
         * @param end
         * @param textAttribute
         * @param value
         */
        protected AttributeEntry(int begin, int end, TextAttribute textAttribute, Object value) {
            this.begin = begin;
            this.end = end;
            this.textAttribute = textAttribute;
            this.value = value;
        }

        /**
         * apply the stored attributes to as
         *
         * @param as
         */
        protected void apply(AttributedString as) {
            as.addAttribute(textAttribute,  value,  begin,  end);
        }
    }
}
