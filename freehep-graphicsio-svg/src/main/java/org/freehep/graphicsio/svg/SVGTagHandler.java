// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphicsio.svg;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.util.*;

import org.freehep.graphics2d.TagHandler;
import org.freehep.graphics2d.TagString;

/**
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-svg/src/main/java/org/freehep/graphicsio/svg/SVGTagHandler.java 02f873212b4c 2005/12/03 01:18:24 duns $
 */
public class SVGTagHandler extends TagHandler {

    private static final float scriptShiftRatio = 50.0f/100.0f;
    private static final float scriptSizeFactor = 2.0f/3.0f;
    private static final String mM = "mM";

    private float x;
    private float y;
    private boolean print;
    private boolean stylable;
    private Font font;
    private Stack fontStack;
    private FontRenderContext fontContext;

    public SVGTagHandler(boolean stylable, Font font, FontRenderContext fontContext) {
        super();
        this.stylable = stylable;
        this.font = font;
        this.fontContext = fontContext;
    }

    public int stringWidth(TagString s) {
        x = 0;
        y = 0;
        fontStack = new Stack();
        print = false;
        parse(s);
        print = true;
        return (int)x;
    }

    /**
     * handles bold <b>, italic <i>, superscript <sup>, subscript <sub>, vertical <v>, overline <over>
     */
    protected String openTag(String tag) {
        String tagString;
        if (tag.equalsIgnoreCase("b")) {
            tagString = "font-weight:bold";
        } else if (tag.equalsIgnoreCase("i")) {
            tagString = "font-style:italic";
        } else if (tag.equalsIgnoreCase("v")) {
// FIXME: vertical can be supported after Adobe implements the glyphRun tag.
            tagString = "";
        } else if (tag.equalsIgnoreCase("over")) {
            tagString = "text-decoration:overline";
        } else if (tag.equalsIgnoreCase("sup")) {
            y -= font.getLineMetrics(mM, fontContext).getAscent()*scriptShiftRatio;
            fontStack.push(font);
            font = font.deriveFont(font.getSize2D()*scriptSizeFactor);
            tagString = "baseline-shift:super;font-size:"+font.getSize2D();
        } else if (tag.equalsIgnoreCase("sub")) {
            y += font.getLineMetrics(mM, fontContext).getAscent()*scriptShiftRatio;
            fontStack.push(font);
            font = font.deriveFont(font.getSize2D()*scriptSizeFactor);
            tagString = "baseline-shift:sub;font-size:"+font.getSize2D();
        } else {
            return super.openTag(tag);
        }

        return "<tspan "+SVGGraphics2D.style(stylable, tagString)+">";
    }

    protected String closeTag(String tag) {
        if (tag.equalsIgnoreCase("b")) {
            // close tspan
        } else if (tag.equalsIgnoreCase("i")) {
            // close tspan
        } else if (tag.equalsIgnoreCase("v")) {
// FIXME: vertical can be supported after Adobe implements the glyphRun tag.
        } else if (tag.equalsIgnoreCase("over")) {
            // close tspan
        } else if (tag.equalsIgnoreCase("sup")) {
            font = (Font)fontStack.pop();
            y += font.getLineMetrics(mM, fontContext).getAscent()*scriptShiftRatio;
        } else if (tag.equalsIgnoreCase("sub")) {
            font = (Font)fontStack.pop();
            y -= font.getLineMetrics(mM, fontContext).getAscent()*scriptShiftRatio;
        } else {
            return super.closeTag(tag);
        }

        return "</tspan>";
    }

    /**
     * Keep encoded strings for SVG
     */
    protected String defaultEntity(String entity) {
        if (print) {
            return "&"+entity+";";
        } else {
            return super.defaultEntity(entity);
        }
    }

    protected String entity(String entity) {
        if (print) {
            return "&"+entity+";";
        } else {
            // No other way, we use a default 'm' character here
            return "m";
        }
    }

    protected String text(String text) {
        double width = font.getStringBounds(text, fontContext).getWidth();

        x += width;

        return text;
    }

    public static void main(String[] args) {
  		String text = "&lt;Vector<sup><b>Graphics</b></sup> &amp; Card<i><sub>Adapter</sub></i>&gt;";

  		TagString s = new TagString(text);
  		SVGTagHandler handler = new SVGTagHandler(true,
  		            new Font("Helvetica", 18, Font.PLAIN),
  		            new FontRenderContext(new AffineTransform(1,0,0,-1,0,0), true, true));
  		System.out.println("\""+s+"\"");
  		System.out.println(handler.parse(s));
    }

}