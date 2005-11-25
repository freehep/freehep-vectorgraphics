// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.font.truetype;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * GLYPH Table.
 *
 *  @author Simon Fischer
 *  @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/font/truetype/TTFGlyfTable.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class TTFGlyfTable extends TTFVersionTable {

    /** If this variable is set to false then the glyphs will not be read
     *  until they are retrieved with <tt>getGlyph(int)</tt>. */
    private static final boolean READ_GLYPHS = false;

    public abstract class Glyph {

	public int xMin, yMin, xMax, yMax;

	public abstract String getType();
	public abstract GeneralPath getShape();

	public void read() throws IOException {
	    xMin = ttf.readFWord();
	    yMin = ttf.readFWord();
	    xMax = ttf.readFWord();
	    yMax = ttf.readFWord();
	}

	public Rectangle getBBox() {
	    return new Rectangle(xMin, yMin, xMax-xMin, yMax-yMin);
	}

	public String toString() {
	    return "["+getType()+"] ("+xMin+","+yMin+"):("+xMax+","+yMax+")";
	}

	public String toDetailedString() { return toString(); }
    }
    
    // --------------------------------------------------------------------------------

    public class SimpleGlyph extends Glyph {

	private static final int ON_CURVE    = 0;
	private static final int X_SHORT     = 1;
	private static final int Y_SHORT     = 2;
	private static final int REPEAT_FLAG = 3;
	private static final int X_SAME      = 4;
	private static final int Y_SAME      = 5;
	private static final int X_POSITIVE  = 4;
	private static final int Y_POSITIVE  = 5;

	public int numberOfContours;
	public int[] endPtsOfContours;
	public int[] instructions;
	public int[] flags;
	public int[] xCoordinates, yCoordinates;
	public boolean[] onCurve;
	public GeneralPath shape;

	public SimpleGlyph(int numberOfContours) {
	    this.numberOfContours = numberOfContours;
	    this.endPtsOfContours = new int[numberOfContours];
	}

	public String getType() { return "Simple Glyph"; }

	public void read() throws IOException {
	    super.read();

	    for (int i = 0; i < endPtsOfContours.length; i++)
		endPtsOfContours[i] = ttf.readUShort();

	    instructions = new int[ttf.readUShort()];
	    for (int i = 0; i < instructions.length; i++)
		instructions[i] = ttf.readByte();

	    int numberOfPoints = endPtsOfContours[endPtsOfContours.length-1]+1;
	    flags        = new int[numberOfPoints];
	    xCoordinates = new int[numberOfPoints];
	    yCoordinates = new int[numberOfPoints];
	    onCurve      = new boolean[numberOfPoints];
	    int repeatCount = 0;
	    int repeatFlag  = 0;
	    for (int i = 0; i < numberOfPoints; i++) {
		if (repeatCount > 0) {
		    flags[i] = repeatFlag;
		    repeatCount--;
		} else {
		    flags[i] = ttf.readRawByte();
		    if (TTFInput.flagBit(flags[i], REPEAT_FLAG)) {
			repeatCount = ttf.readByte();
			repeatFlag = flags[i];
		    }
		}
		TTFInput.checkZeroBit(flags[i], 6, "flags");
		TTFInput.checkZeroBit(flags[i], 7, "flags");
		onCurve[i] = TTFInput.flagBit(flags[i], ON_CURVE);
	    }

	    int last = 0;
	    for (int i = 0; i < numberOfPoints; i++) {		
		if (TTFInput.flagBit(flags[i], X_SHORT)) {
		    if (TTFInput.flagBit(flags[i], X_POSITIVE)) {
			last = xCoordinates[i] = last+ttf.readByte();
		    } else {
			last = xCoordinates[i] = last-ttf.readByte();
		    }
		} else {
		    if (TTFInput.flagBit(flags[i], X_SAME)) {
			last = xCoordinates[i] = last;
		    } else {
			last = xCoordinates[i] = last+ttf.readShort();
		    }
		}
	    }

	    last = 0;
	    for (int i = 0; i < numberOfPoints; i++) {
		if (TTFInput.flagBit(flags[i], Y_SHORT)) {
		    if (TTFInput.flagBit(flags[i], Y_POSITIVE)) {
			last = yCoordinates[i] = last+ttf.readByte();
		    } else {
			last = yCoordinates[i] = last-ttf.readByte();
		    }
		} else {
		    if (TTFInput.flagBit(flags[i], Y_SAME)) {
			last = yCoordinates[i] = last;
		    } else {
			last = yCoordinates[i] = last+ttf.readShort();
		    }
		}
	    }
	}

	public String toString() { 
	    String str = super.toString()+ ", "+numberOfContours+" contours, endPts={";
	    for (int i = 0; i < numberOfContours; i++)
		str += (i==0?"":",")+endPtsOfContours[i];
	    str += "}, " + instructions.length + " instructions";
	    return str;
	}

	public String toDetailedString() { 
	    String str = toString() + "\n  instructions = {";
	    for (int i = 0; i < instructions.length; i++) {
		str += Integer.toHexString(instructions[i]) + " ";
	    }
	    return str + "}";
	}

	public GeneralPath getShape() {
	    if (shape != null) return shape;

	    shape = new GeneralPath(GeneralPath.WIND_NON_ZERO);
	    int p = 0;
	    int x = 0;
	    int y = 0;
	    for (int i = 0; i < endPtsOfContours.length; i++) {
		boolean first = true;		
		while (p <= endPtsOfContours[i]) { 
		    x = xCoordinates[p];
		    y = yCoordinates[p];
		    //System.out.print(p+": ("+x+","+y+")");
		    if (first) {
			shape.moveTo(x, y);
			//System.out.println(" m");
			if (!onCurve[p]) 
			    System.err.println("First point of contour not on curve!");
		    } else if (onCurve[p]) {
			shape.lineTo(x, y);
			//System.out.println(" l");
		    } else {
			int pIndex = 0;
			// when we are at the end of a contour
			if (p == endPtsOfContours[i])
			    // look for the endpoint of the curve at the beginning
			    if (i>0) pIndex = endPtsOfContours[i-1]+1;
			    else pIndex = 0;			    
			else pIndex = ++p; // else take the next point
			int x1 = xCoordinates[pIndex];
			int y1 = yCoordinates[pIndex];
			//System.out.print("("+x1+","+y1+")");
			if (onCurve[p]) {
			    shape.quadTo(x, y, x1, y1);
			    //System.out.println(" q");
			} else {
			    if (p == endPtsOfContours[i])
				if (i>0) pIndex = endPtsOfContours[i-1]+1;
				else pIndex = 0;			    
			    else pIndex = ++p;
			    int x2 = xCoordinates[pIndex];
			    int y2 = yCoordinates[pIndex];
			    //System.out.println("("+y2+","+y2+") c");
			    shape.curveTo(x, y, x1, y1, x2, y2);

			    // FIXME: Find out how to construct a cubic or quadratic 
			    // Bezier curve from a Bezier spline with an arbitrary number
			    // of off-curve points
			    if (!onCurve[p]) 
				System.err.println("Three points in a row not on curve!");
			}
		    }
		    first = false;
		    p++;
		}
		shape.closePath();
		//System.out.println(".");
	    }
	    return shape;
	}

    }

    // --------------------------------------------------------------------------------

    public class CompositeGlyph extends Glyph {

	private static final int ARGS_WORDS      = 0;
	private static final int ARGS_XY         = 1;
	private static final int SCALE           = 3;
	private static final int XY_SCALE        = 6;
	private static final int TWO_BY_TWO      = 7;
	private static final int MORE_COMPONENTS = 5;

	private GeneralPath shape;
	private int noComponents;

	public String getType() { return "Composite Glyph"; }
	
	public GeneralPath getShape() { return shape; }

	public void read() throws IOException {
	    super.read();
	    shape = new GeneralPath();

	    noComponents = 0;
	    boolean more = true;
	    while (more) {
		noComponents++;
		ttf.readUShortFlags();
		more = ttf.flagBit(MORE_COMPONENTS);
		int glyphIndex = ttf.readUShort();
		int arg1, arg2;
		if (ttf.flagBit(ARGS_WORDS)) {
		    arg1 = ttf.readShort();
		    arg2 = ttf.readShort();
		} else {
		    arg1 = ttf.readChar();
		    arg2 = ttf.readChar();
		}
		AffineTransform t = new AffineTransform();
		if (ttf.flagBit(ARGS_XY)) {
		    t.translate(arg1, arg2);
		} else {
		    System.err.println("TTFGlyfTable: ARGS_ARE_POINTS not implemented.");
		}

		if (ttf.flagBit(SCALE)) {
		    double scale = ttf.readF2Dot14();
		    t.scale(scale, scale);
		} else if (ttf.flagBit(XY_SCALE)) {
		    double scaleX = ttf.readF2Dot14();
		    double scaleY = ttf.readF2Dot14();
		    t.scale(scaleX, scaleY);
		} else if (ttf.flagBit(TWO_BY_TWO)) {
		    System.err.println("TTFGlyfTable: WE_HAVE_A_TWO_BY_TWO not implemented.");
		}

		GeneralPath appendGlyph = (GeneralPath)getGlyph(glyphIndex).getShape().clone();
		appendGlyph.transform(t);
		shape.append(appendGlyph, false);
	    }
	}

	public String toString() { 
	    return super.toString() + ", "+noComponents + " components";
	}

    }

    // --------------------------------------------------------------------------------
    
    public Glyph[] glyphs;

    private long[] offsets;

    public String getTag() { return "glyf"; }

    public void readTable() throws IOException {
	glyphs = new Glyph[((TTFMaxPTable)getTable("maxp")).numGlyphs];
	offsets = ((TTFLocaTable)getTable("loca")).offset;

	if (READ_GLYPHS) {
	    for (int i = 0; i < glyphs.length; i++) {
		if ((i > 0) && (offsets[i-1] == offsets[i])) {
		    glyphs[i] = glyphs[i-1];
		} else {
		    try {
			getGlyph(i);
		    } catch (IOException e) {
			System.err.println("While reading glyph #"+i+" (offset "+offsets[i]+"):");
			e.printStackTrace();
		    }
		}
	    }
	}
	
    }

    public Glyph getGlyph(int i) throws IOException {
	if (glyphs[i] != null) {
	    return glyphs[i];
	} else {
	    ttf.pushPos();
	    ttf.seek(offsets[i]);
	    int numberOfContours = ttf.readShort();
	    if (numberOfContours >= 0) glyphs[i] = new SimpleGlyph(numberOfContours);
	    else glyphs[i] = new CompositeGlyph();
	    glyphs[i].read();
	    //System.out.println(i+": "+offsets[i]+"-"+ttf.getPointer());
	    ttf.popPos();
	    return glyphs[i];
	}
    }

    public String toString() {
	String str = super.toString();
	for (int i = 0; i < glyphs.length; i++)
	    str += "\n  #"+i+": "+glyphs[i];
	return str;
    }
}
