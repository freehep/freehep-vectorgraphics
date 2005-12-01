// Copyright 2001-2005 FreeHEP
package org.freehep.graphicsio.ps;

import java.io.IOException;
import java.awt.Shape;
import java.awt.Font;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.font.GlyphMetrics;
import java.awt.font.FontRenderContext;
import java.io.PrintStream;

import org.freehep.graphics2d.font.CharTable;
import org.freehep.graphicsio.font.FontEmbedder;

/**
 *  Type 3 Font Embedder class for Postscript.
 *  @author Sami Kama
 *  @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/PSFontEmbedder.java 2689041eec29 2005/12/01 22:37:27 duns $
 */
public class PSFontEmbedder extends FontEmbedder {

    protected int dictSize = 9;
    protected PrintStream os;
    private PSPathConstructor pc;

    public PSFontEmbedder(FontRenderContext context, PrintStream os) {
        super(context);
        this.os = os;
        pc = new PSPathConstructor(os, false, true);
    }


/**
 *  Writes Glyph definition of given glyph.
 */
    protected void writeGlyph(String unicodeName, Shape glyph,
                GlyphMetrics glyphMetrics) throws IOException {

        double[] points = new double[6];
        double[] lastMove = new double[2];
        double[] lastPoint = new double[2];
        double[] controlPoint = new double[4];

        PathIterator pIter = glyph.getPathIterator(null);
        os.println("\t/"+unicodeName);
        os.println("\t\t{");

        while (!pIter.isDone()) {
            switch(pIter.currentSegment(points)) {
                case PathIterator.SEG_MOVETO:
                    pc.move(points[0], points[1]);
                    lastMove[0] = lastPoint[0] = points[0];
                    lastMove[1] = lastPoint[1] = points[1];
                    break;

                case PathIterator.SEG_LINETO :
                    pc.line(points[0], points[1]);
                    lastPoint[0] = points[0];
                    lastPoint[1] = points[1];
                    break;

                case PathIterator.SEG_QUADTO :
                    controlPoint[0] = points[0]+(lastPoint[0]-points[0])/3.;
                    controlPoint[1] = points[1]+(lastPoint[1]-points[1])/3.;
                    controlPoint[2] = points[0]+(points[2]-points[0])/3.;
                    controlPoint[3] = points[1]+(points[3]-points[1])/3.;
                    pc.cubic(controlPoint[0], controlPoint[1], controlPoint[2], controlPoint[3],
                        points[2], points[3]);
                    lastPoint[0] = points[2];
                    lastPoint[1] = points[3];
                    break;

                case PathIterator.SEG_CUBICTO :
                    pc.cubic(points[0], points[1], points[2], points[3],
                        points[4], points[5]);
                    lastPoint[0] = points[4];
                    lastPoint[1] = points[5];
                    break;

                case PathIterator.SEG_CLOSE :
                    pc.closePath(lastMove[0], lastMove[1]);
                    lastPoint[0] = 0.;
                    lastPoint[1] = 0.;
                    break;
            }
            pIter.next();
        }
//        System.out.println("done once");
        os.println("fill");
        os.println("\t\t} def");
        os.println();
    }

/** writes metric dictionary for the font. */

    protected void writeWidths(double[] widths) throws IOException {
        os.println("\t/Metrics "+(getNODefinedChars()+1)+" dict def");
        os.println("\t\tMetrics begin");
        os.println("\t\t/"+NOTDEF+" "+(int)getUndefinedWidth()+" def");
        for (int i=1; i<256; i++) {
            if (getCharName(i) != null) {
                os.println("\t\t/"+getCharName(i)+" "+widths[i]+" def");
            }

        }
        os.println("\tend");
    }

/** writes encoding array.*/

    protected void writeEncoding(CharTable charTable) throws IOException{
        os.println("\t/Encoding 256 array def");
        os.println("\t\t\t0 1 255 {Encoding exch /.notdef put}for");
        for (int i=1; i<256; i++) {
            String name = charTable.toName(i);
            if (name != null){
                os.println("\t\tEncoding "+i+" /"+name+" put");
            }
        }
    }
/** Writes initial parts of the font dictionary. */
    protected void openIncludeFont() throws IOException {
        Rectangle2D boundingBox = getFontBBox();
        double llx = boundingBox.getX();
        double lly = -boundingBox.getY()-boundingBox.getHeight();
        double urx = boundingBox.getX()+boundingBox.getWidth();
        double ury = -boundingBox.getY();
        os.println("9 dict begin");
        os.println("/FontType 3 def");
        os.println("/FontMatrix ["+(1/FONT_SIZE)+" 0 0 "+(1/FONT_SIZE)+" 0 0]def");
        os.println("/FontBBox ["+(int)llx+" "+(int)lly+
            " "+(int)urx+" "+(int)ury+" ] def");
//          os.println("\t /"+getFontName()+" "+(getNODefinedChars()+1)+" dict def");

    }

/** Closes font dictionary.*/
    protected void closeIncludeFont() throws IOException {
        this.writeBuildProcs();

    }
/*
 *  Writes <tt>BuildGlyph</tt> and <tt>BuildChar</tt> procedures of font.
 */
    protected void writeBuildProcs() throws IOException {
        Rectangle2D boundingBox = getFontBBox();
        double llx = boundingBox.getX();
        double lly = -boundingBox.getY()-boundingBox.getHeight();
        double urx = boundingBox.getX()+boundingBox.getWidth();
        double ury = -boundingBox.getY();

        os.println("\t/BuildGlyph");
        os.println("\t\t{ 2 copy exch /Metrics get exch ");
        os.println("\t\t\t2 copy known {get}{pop pop "+getUndefinedWidth()+"} ifelse");
        os.println("\t\t\t0");
        os.println("\t\t\t"+(int)llx+" "+(int)lly+" "+(int)urx+" "+
            (int)ury);
        os.println("\t\t\tsetcachedevice");
        // ????? os.println("\t\t\texch /"+getFontName()+" get exch");
        os.println("\t\t\texch /CharProcs get exch");
        os.println("\t\t\t2 copy known not");
        os.println("\t\t\t\t\t{pop /.notdef}");
        os.println("\t\t\t\tif");
        os.println("\t\t\tget exec");
        os.println("\t\t} bind def");
        os.println();
        os.println("\t/BuildChar");
        os.println("\t\t{ 1 index /Encoding get exch get");
        os.println("\t\t  1 index /BuildGlyph get exec");
        os.println("\t  } bind def");
    }

/**
 *  Writes necessary commands to find <tt>Glyphs</tt> dictionary from font dictionary
 *  and puts it on the stack, opens dictionary to write glyph definitions.
 */
    protected void openGlyphs() throws IOException {
            // ??????? os.println("\t/"+getFontName()+" "+(getNODefinedChars()+1)+" dict def");
        os.println("\t/CharProcs "+(getNODefinedChars()+1)+" dict def");
        os.println("\tCharProcs begin");
//            os.print("dup /"+getFontName()+" get begin");
        os.println("\t\t\t%define Glyph dictionary and start filling");
    }
/** Closes Glyphs dictionary.*/
    protected void closeGlyphs() throws IOException {

        os.print("\tend");
        os.println("\t\t\t\t% close glyph dict. ");
    }
/** Closes font dictionary.*/
    protected void closeEmbedFont() throws IOException {
        os.print("\tcurrentdict");
        os.println("\t\t\t% actually put dict on the stack");
        os.print("\tend");
        os.println("\t\t\t% close the dictionary now");
        os.println("\t/"+getFontName()+" exch definefont pop");
        os.flush();
    }

}
