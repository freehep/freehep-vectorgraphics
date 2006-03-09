// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphics2d;

import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.Stack;
import java.util.Map;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphics2d/src/main/java/org/freehep/graphics2d/GenericTagHandler.java cbe5b99bb13b 2006/03/09 21:55:10 duns $
 */
public class GenericTagHandler extends TagHandler {

    private static final String mM = "mM";

    private Graphics2D graphics;

    private boolean vertical;

    private float buffered;

    private float overlineStart;

    private float overlineEnd;

    private float x;

    private float y;

    private float miny;

    private float maxy;

    private boolean print = false;

    /**
     * store the names of font families before they are changed by openTag()
     */
    private Stack/*<String>*/ fontFamilyStack;

    /**
     * creates a tag handler for printing text and calculating its size
     *
     * @param graphics stores the font for calculations
     */
    public GenericTagHandler(Graphics2D graphics) {
        super();
        this.graphics = graphics;
    }

    /**
     * prints the tagged string at x:y
     *
     * @param s string to print using the stored graphics
     * @param x coordinate for drawing
     * @param y coordinate for drawing
     */
    public void print(TagString s, double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
        miny = this.y;
        maxy = this.y;
        fontFamilyStack = new Stack();
        print = true;
        vertical = false;
        buffered = 0;
        parse(s);
    }

    /**
     * calculates the string bounds using the current font of {@link #graphics}.
     *
     * @param s string to calculate
     * @return bouding box after parsing s
     */
    public Rectangle2D stringSize(TagString s) {
        x = 0;
        y = 0;
        miny = y;
        maxy = y;
        fontFamilyStack = new Stack();
        print = false;
        vertical = false;
        buffered = 0;
        parse(s);
        return new Rectangle2D.Float(0, miny, x, maxy - miny);
    }

    /**
     * handles bold <b>, italic <i>, superscript <sup>, subscript <sub>,
     * vertical <v>, overline <over>, underline <u>, strikethrough <s>,
     * underline dashed <udash>, underline dotted <udot> and typewriter <tt>
     *
     * @param tag one of the known tags, otherwise the overloaded methode is called
     * @return empty string or the result of the overloaded method
     */
    // FIXME: check if we can support overline and vertical?
    protected String openTag(String tag) {
        // clone the font attribute for a derived font
        Map attributes = graphics.getFont().getAttributes();

        // change attributes
        if (tag.equalsIgnoreCase("b")) {
            attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        } else if (tag.equalsIgnoreCase("i")) {
            attributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
        } else if (tag.equalsIgnoreCase("s") || tag.equalsIgnoreCase("strike")) {
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        } else if (tag.equalsIgnoreCase("udash")) {
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DASHED);
        } else if (tag.equalsIgnoreCase("udot")) {
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_DOTTED);
        } else if (tag.equalsIgnoreCase("u")) {
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        } else if (tag.equalsIgnoreCase("tt")) {
            fontFamilyStack.push(attributes.get(TextAttribute.FAMILY));
            attributes.put(TextAttribute.FAMILY, "Courier");
        } else if (tag.equalsIgnoreCase("v")) {
            vertical = true;
        } else if (tag.equalsIgnoreCase("over")) {
            overlineStart = x;
        } else if (tag.equalsIgnoreCase("sup")) {
            attributes.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
        } else if (tag.equalsIgnoreCase("sub")) {
            attributes.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
        } else {
            return super.openTag(tag);
        }

        // set the font
        graphics.setFont(new Font(attributes));
        return "";
    }

    /**
     * closes the given html tag. It doesn't matter, if that one was opened, so </udot>
     * closes a <udash> too, because the use the same {@link TextAttribute.UNDERLINE}.
     *
     * @param tag to close
     * @return empty string or the result of the overloaded method
     */
    protected String closeTag(String tag) {
        // clone the font attribute for a derived font
        Map attributes = graphics.getFont().getAttributes();

        // change attributes
        if (tag.equalsIgnoreCase("b")) {
            attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
        } else if (tag.equalsIgnoreCase("i")) {
            attributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_REGULAR);
        } else if (tag.equalsIgnoreCase("s") || tag.equalsIgnoreCase("strike")) {
            attributes.remove(TextAttribute.STRIKETHROUGH);
        } else if (tag.equalsIgnoreCase("udash")) {
            attributes.remove(TextAttribute.UNDERLINE);
        } else if (tag.equalsIgnoreCase("udot")) {
            attributes.remove(TextAttribute.UNDERLINE);
        } else if (tag.equalsIgnoreCase("u")) {
            attributes.remove(TextAttribute.UNDERLINE);
        } else if (tag.equalsIgnoreCase("tt")) {
            attributes.put(TextAttribute.FAMILY, fontFamilyStack.pop());
        } else if (tag.equalsIgnoreCase("v")) {
            vertical = false;
        } else if (tag.equalsIgnoreCase("over")) {
            if (print) {
                LineMetrics metrics = graphics.getFont().getLineMetrics(mM,
                        graphics.getFontRenderContext());
                float ascent = metrics.getAscent();
                GeneralPath path = new GeneralPath();
                path.moveTo(overlineStart, y - ascent);
                path.lineTo(overlineEnd, y - ascent);
                graphics.draw(path);
            }
        } else if (tag.equalsIgnoreCase("sup")) {
            attributes.remove(TextAttribute.SUPERSCRIPT);
        } else if (tag.equalsIgnoreCase("sub")) {
            attributes.remove(TextAttribute.SUPERSCRIPT);
        } else {
            return super.closeTag(tag);
        }

        // set the font
        graphics.setFont(new Font(attributes));
        return "";
    }

    /**
     * calculates miny und maxy for {@link #stringSize(TagString)}. If
     * {@link #print} is set, text is drawed using
     * {@link Graphics2D#drawString(String, float, float)} of
     * {@link #graphics}
     *
     * @param text text to draw
     * @return unmodified text parameter
     */
    protected String text(String text) {
        Font font = graphics.getFont();

        // calculate the "real" width, works for italic fonts too
        TextLayout layout = new TextLayout(text, font, graphics.getFontRenderContext());
        float width = Math.max(
            layout.getAdvance(),
            (float) layout.getBounds().getWidth());

        // move X position in case we had something vertical before
        if ((!vertical) && (buffered > 0)) {
            x += buffered;
            buffered = 0;
        }

        if (print) {
            graphics.drawString(text, x, y);
        }

        if (vertical) {
            buffered = Math.max(buffered, width);
            overlineEnd = x + width;
        } else {
            x += width;
            overlineEnd = x;
        }

        // store the minimum values for stringSize(TagString)
        miny = Math.min(miny, y - layout.getAscent());
        maxy = Math.max(maxy, y + layout.getDescent());

        return text;
    }
}
