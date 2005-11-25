// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphics2d;

import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;
import java.util.*;

/**
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphics2d/src/main/java/org/freehep/graphics2d/GenericTagHandler.java f5b43d67642f 2005/11/25 23:10:27 duns $
 */
public class GenericTagHandler extends TagHandler {

    private static final float scriptShiftRatio = 50.0f/100.0f;
    private static final float scriptSizeFactor = 2.0f/3.0f;
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
    private Stack fontStack;

    public GenericTagHandler(Graphics2D graphics) {
        super();
        this.graphics = graphics;
    }

    public void print(TagString s, double x, double y) {
        this.x = (float)x;
        this.y = (float)y;
        miny = this.y;
        maxy = this.y;
        fontStack = new Stack();
        print = true;
        vertical = false;
        buffered = 0;
        parse(s);
    }

    private float stringWidth(TagString s) {
        x = 0;
        y = 0;
        miny = y;
        maxy = y;
        fontStack = new Stack();
        print = false;
        vertical = false;
        buffered = 0;
        parse(s);
        return x;
    }

    public Rectangle2D stringSize(TagString s) {
        x = 0;
        y = 0;
        miny = y;
        maxy = y;
        fontStack = new Stack();
        print = false;
        vertical = false;
        buffered = 0;
        parse(s);
        return new Rectangle2D.Float(0,miny,x,maxy-miny);
    }

    /**
     * handles bold <b>, italic <i>, superscript <sup>, subscript <sub>, vertical <v>, overline <over> and typewriter <tt>
     */
// FIXME: check if we can support overline and vertical?
    protected String openTag(String tag) {
        Font font = graphics.getFont();
        FontRenderContext fontContext = graphics.getFontRenderContext();
        if (tag.equalsIgnoreCase("b")) {
            if (!font.isBold()) {
                graphics.setFont(font.deriveFont(Font.BOLD));
            }
        } else if (tag.equalsIgnoreCase("i")) {
            if (!font.isItalic()) {
                graphics.setFont(font.deriveFont(Font.ITALIC));
            }
        } else if (tag.equalsIgnoreCase("tt")) {
            fontStack.push(font);
	        graphics.setFont(new Font("Courier", Font.PLAIN, font.getSize()));
    	} else if (tag.equalsIgnoreCase("v")) {
            vertical = true;
        } else if (tag.equalsIgnoreCase("over")) {
            overlineStart = x;
        } else if (tag.equalsIgnoreCase("sup")) {
            y -= font.getLineMetrics(mM, fontContext).getAscent()*scriptShiftRatio;
            fontStack.push(font);
            graphics.setFont(font.deriveFont(font.getSize2D()*scriptSizeFactor));
        } else if (tag.equalsIgnoreCase("sub")) {
            y += font.getLineMetrics(mM, fontContext).getAscent()*scriptShiftRatio;
            fontStack.push(font);
            graphics.setFont(font.deriveFont(font.getSize2D()*scriptSizeFactor));
        } else {
            return super.openTag(tag);
        }
        return "";
    }

    protected String closeTag(String tag) {
        if (tag.equalsIgnoreCase("b")) {
            Font font = graphics.getFont();
            if (font.isBold()) {
                graphics.setFont(font.deriveFont(font.getStyle() - Font.BOLD));
            }
        } else if (tag.equalsIgnoreCase("i")) {
            Font font = graphics.getFont();
            if (font.isItalic()) {
                graphics.setFont(font.deriveFont(font.getStyle() - Font.ITALIC));
            }
        } else if (tag.equalsIgnoreCase("tt")) {
            graphics.setFont((Font)fontStack.pop());
        } else if (tag.equalsIgnoreCase("v")) {
            vertical = false;
        } else if (tag.equalsIgnoreCase("over")) {
            if (print) {
                LineMetrics metrics = graphics.getFont().getLineMetrics(mM, graphics.getFontRenderContext());
                float ascent = metrics.getAscent();
                GeneralPath path = new GeneralPath();
                path.moveTo(overlineStart, y-ascent);
                path.lineTo(overlineEnd, y-ascent);
                graphics.draw(path);
            }
        } else if (tag.equalsIgnoreCase("sup")) {
            graphics.setFont((Font)fontStack.pop());
            y += graphics.getFont().getLineMetrics(mM, graphics.getFontRenderContext()).getAscent()*scriptShiftRatio;
        } else if (tag.equalsIgnoreCase("sub")) {
            graphics.setFont((Font)fontStack.pop());
            y -= graphics.getFont().getLineMetrics(mM, graphics.getFontRenderContext()).getAscent()*scriptShiftRatio;
        } else {
            return super.closeTag(tag);
        }
        return "";
    }

    protected String text(String text) {
        Font font = graphics.getFont();
        FontRenderContext fontContext = graphics.getFontRenderContext();
        LineMetrics metrics = graphics.getFont().getLineMetrics(text, fontContext);
        float width = (float)font.getStringBounds(text, fontContext).getWidth();
        float descent = metrics.getDescent();
        float ascent = metrics.getAscent();

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
        miny = Math.min(miny,y-ascent);
        maxy = Math.max(maxy,y+descent);

        return text;
    }
}
