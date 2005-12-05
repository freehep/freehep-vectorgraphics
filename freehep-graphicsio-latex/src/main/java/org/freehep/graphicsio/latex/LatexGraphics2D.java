// Copyright 2004, FreeHEP
package org.freehep.graphicsio.latex;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.text.*;
import java.util.*;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphics2d.TagString;
import org.freehep.graphicsio.AbstractVectorGraphicsIO;
import org.freehep.util.ScientificFormat;

/**
 * @author Andre Bach
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-latex/src/main/java/org/freehep/graphicsio/latex/LatexGraphics2D.java 937ca67e5f7c 2005/12/05 04:38:24 duns $
 */
public class LatexGraphics2D
    extends AbstractVectorGraphicsIO {
    /*================================================================================
     * Table of Contents:
     * ------------------
     * 1. Constructors & Factory Methods
     * 2. Document Settings
     * 3. Header, Trailer, Multipage & Comments
     *    3.1 Header & Trailer
     *    3.2 MultipageDocument methods
     * 4. Create & Dispose
     * 5. Drawing Methods
     *    5.1. shapes (draw/fill)
     *         5.1.1. lines, rectangles, round rectangles
     *         5.1.2. polylines, polygons
     *         5.1.3. ovals, arcs
     *         5.1.4. shapes
     *    5.2. Images
     *    5.3. Strings
     * 6. Transformations
     * 7. Clipping
     * 8. Graphics State / Settings
     *    8.1. stroke/linewidth
     *    8.2. paint/color
     *    8.3. font
     *    8.4. rendering hints
     * 9. Auxiliary
     * 10. Private/Utility Methos
     *================================================================================*/

    private PrintStream ps;
    private AffineTransform pageTrans = new AffineTransform();
    private double componentHeight;
    private static double pageHeight;
    private static double pageWidth;
    private int numberClips = 0;
    private float width = 1.f;
    private double[] dash;
    private Color savedColor;
    private float savedWidth;
    private double[] savedDash;

    /*================================================================================
     * 1. Constructors & Factory Methods
     *================================================================================*/
    public LatexGraphics2D(File file, Dimension size) throws FileNotFoundException {
        this(new FileOutputStream(file), size, true);
    }

    public LatexGraphics2D(File file, Component component) throws FileNotFoundException {
        this(new FileOutputStream(file), component, true);
    }

    public LatexGraphics2D(OutputStream os, Component component, boolean doRestoreOnDispose) {
        super(component, doRestoreOnDispose);
        init(os);
        componentHeight = component.getSize().getHeight();
        pageWidth = component.getSize().getWidth();
    }

    public LatexGraphics2D(OutputStream os, Dimension size, boolean doRestoreOnDispose) {
        super(size, doRestoreOnDispose);
        init(os);
    }

    private void init(OutputStream os) {
        ps = new PrintStream(os);
    }

    protected LatexGraphics2D(LatexGraphics2D graphics, boolean doRestoreOnDispose) {
        super(graphics, doRestoreOnDispose);
        ps = graphics.ps;
    }

    /*================================================================================
     | 2. Document Settings
     *================================================================================*/

    /*================================================================================
     | 3. Header, Trailer, Multipage & Comments
     *================================================================================*/
    /* 3.1 Header & Trailer */
    public void writeHeader() throws IOException {
        ps.println("\\documentclass{article}");
        ps.println("\\usepackage[noxcolor]{pstricks}");
        ps.println("\\usepackage{pst-grad}");
        ps.println("\\pagestyle{empty}");
        ps.println("\\begin{document}");
        ps.println("\\begin{pspicture}(5,20)(15,15)");
        ps.println("\\psset{unit=1pt}"); // One thing that should be changed for scaling.

        ps.println("% componentHeight = "+componentHeight);
        ps.println("% pageWidth = "+pageWidth);
        ps.println("% pageHeight = "+pageHeight);
        pageHeight = componentHeight;
        ps.println("% pageHeight = "+pageHeight);
        pageTrans.scale(1,-1);
        pageTrans.translate(0,-pageHeight);
        transform(pageTrans);
    }

    public void writeBackground() throws IOException {
//        writeWarning(getClass()+": writeBackground() not implemented.");
        // Write out the background to the output stream.
        ps.println("% writeBackground() called");
    }

    public void writeTrailer() throws IOException {
        ps.println("\\end{pspicture}");
        ps.println("\\end{document}");
    }

    public void closeStream() throws IOException {
        ps.close();
    }

    /* 3.2 MultipageDocument methods */

    /*================================================================================
     * 4. Create & Dispose
     *================================================================================*/

    public Graphics create() {
        ps.println("% create() called");
        LatexGraphics2D g = new LatexGraphics2D(this, true);
        try {
            g.writeGraphicsSave(getColor(),width,dash);
        } catch (IOException e) {
        }
        return g;
    }

    public Graphics create(double x, double y, double width, double height) {
        ps.println("% create(x,y,width,height) called");
        ps.println("% x      = "+x);
        ps.println("% y      = "+y);
        ps.println("% width  = "+width);
        ps.println("% height = "+height);
        LatexGraphics2D g = new LatexGraphics2D(this, true);
        try {
            g.writeGraphicsSave(getColor(),width,dash);
        } catch (IOException e) {
        }
        g.clipRect(x, y, width, height);
        return g;
    }
    
    protected void writeGraphicsSave(Color c, double w, double[] d) throws IOException {
        savedColor = c;
        savedWidth = (float) w;
        savedDash = d;
    }

    protected void writeGraphicsSave() throws IOException {
    }

    protected void writeGraphicsRestore() throws IOException {
        while(numberClips > 0) {
            ps.println("\\endpsclip");
            numberClips--;
        }
        // If the current color was written inside a clip statement, it will not be
        // remembered outside of it, so the restore must always be written.
        if (/*savedColor != getColor() &&*/ savedColor != null) writePaint(savedColor);
        /*if (savedWidth != width)*/ writeWidth(savedWidth);
        /*if (savedDash != dash)*/ writeDash(savedDash, 0.0);
    }

    /*================================================================================
     | 5. Drawing Methods
     *================================================================================*/
    /* 5.1.4. shapes */

    public void draw(Shape shape) {
        LatexPathConstructor pc = new LatexPathConstructor(ps);
        try {
            ps.println("\\pscustom[fillstyle=none]{");
            pc.addPath(shape, getTransform());
            ps.println("}");
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void fill(Shape shape) {
        LatexPathConstructor pc = new LatexPathConstructor(ps);
        try {
            ps.println("\\pscustom[linestyle=none]{");
            pc.addPath(shape, getTransform());
            ps.println("}");
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void fillAndDraw(Shape shape, Color fillColor) {
        LatexPathConstructor pc = new LatexPathConstructor(ps);
        try {
            ps.println("\\newrgbcolor{colorfill}{"+fixedPrecision(fillColor.getRed()/255.)+" "+fixedPrecision(fillColor.getGreen()/255.)+" "+fixedPrecision(fillColor.getBlue()/255.)+"}");
            ps.println("\\pscustom[fillstyle=solid,fillcolor=colorfill,linecolor=current]{");
            pc.addPath(shape, getTransform());
            ps.println("}");
        } catch (IOException e) {
            handleException(e);
        }
    }

    /* 5.2. Images */
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        // Mostly unimplemented.
        ps.println("% copyArea(x,y,width,height,dx,dy) called");
    }

    protected void writeImage(RenderedImage image, AffineTransform xform, Color bkg) throws IOException {
//        writeWarning(getClass()+": writeImage(RenderedImage, AffineTransform, Color) not implemented.");
        // Write out the image.
        ps.println("% writeImage(image,xform,bkg) called");
    }

    /* 5.3. Strings */
    protected void writeString(String str, double x, double y) throws IOException {
        ps.println("% string componentHeight = "+componentHeight);
        ps.println("% string pageHeight = "+pageHeight);
        y = -y + pageHeight;
        
        ps.print("\\put("+fixedPrecision(x)+","+fixedPrecision(y)+"){");
        ps.print("\\fontsize{"+getFont().getSize()+"}{"+fixedPrecision(1.2*getFont().getSize())+"}");
        ps.print("\\textcolor{current}{");

        if (getFont().getFamily()=="Serif")      ps.print("\\rmfamily ");
        if (getFont().getFamily()=="SansSerif")  ps.print("\\sffamily ");
        if (getFont().getFamily()=="Monospaced") ps.print("\\ttfamily ");

        switch (getFont().getStyle()) {
            case 0:
                ps.print("\\upshape ");
                break;
            case 1:
                ps.print("\\bfseries ");
                break;
            case 2:
                ps.print("\\itshape ");
                break;
            case 3:
                ps.print("\\bfseries \\itshape ");
                break;
            default:
        }

        ps.println(fixString(str)+"}}");
    }

    // Certain chars can only be printed in the LaTeX math environment, or
    // are commands and must be preceded by a backslash.
    protected String fixString(String str) {
        StringBuffer buffer = new StringBuffer(str);
        for (int i=0; i<buffer.length(); i++) {
            switch (buffer.charAt(i)) {
                default:
                    break;
                case '{': case '}': case '#': case '%': case '&': case '$': case '_': 
                    buffer.insert(i,'\\');
                    i++;
                    break;
                case '<': case '>':
                    buffer.insert(i,'$');
                    buffer.insert((i+2),'$');
                    i += 2;
                    break;
                case '\\':
                    buffer.delete(i,(i+1));
                    buffer.insert(i,"$\\backslash$");
                    i += 11;
                    break;
            }
        }
        return buffer.toString();
    }

    /*================================================================================
     | 6. Transformations
     *================================================================================*/

    protected void writeTransform(AffineTransform t) throws IOException {
        // Ignored, transforms are pre-calculated before written to LaTeX.
    }

    /*================================================================================
     | 7. Clipping
     *================================================================================*/
    protected void writeClip(Rectangle2D r2d) throws IOException {
        Shape s = r2d;
        writeClip(s);
    }

    protected void writeClip(Shape s) throws IOException {
        if (s == null) return;
        ps.println("\\psclip{");
        LatexPathConstructor pc = new LatexPathConstructor(ps);
        try {
            ps.println("\\pscustom[linestyle=none,fillstyle=none]{");
            pc.addPath(s, getTransform());
            ps.println("}");
        } catch (IOException e) {
            handleException(e);
        }
        ps.println("}");
        numberClips++;
    }

    /*================================================================================
     | 8. Graphics State
     *================================================================================*/
    /* 8.1. stroke/linewidth */
    protected void writeWidth(float width) throws IOException {
        ps.println("\\psset{linewidth="+width+"}");
        this.width = width;
    }

    protected void writeCap(int cap) throws IOException {
        // Not supported in LaTeX PSTricks when using lineto.
        ps.println("% writeCap(int cap) called, not supported");
        /*switch (cap) {
            case 0:
                ps.println("\\psset{arrows=-}");
                break;
            case 1:
                ps.println("\\psset{arrows=c-c}");
                break;
            case 2:
                ps.println("\\psset{arrows=C-C}");
                break;
            default:
        }*/
    }

    protected void writeJoin(int join) throws IOException {
        // Not supported in LaTeX PSTricks.
        ps.println("% writeJoin(int join) called, not supported");
    }

    protected void writeMiterLimit(float limit) throws IOException {
        // Not supported in LaTeX PSTricks.
        ps.println("% writeMiterLimit(float limit) called, not supported");
    }

    protected void writeDash(double[] dash, double phase) throws IOException {
        this.dash = dash;
        if (dash == null || dash.length==0) {
            ps.println("\\psset{linestyle=solid}");
            return;
        }

        // Dotted lines must be dealt with separately.
        if (dash[0]==0.0) {
            ps.println("\\psset{linestyle=dotted,dotsep="+fixedPrecision(dash[1])+"}");
            return;
        }

        // In more complex patterns, dots will appear as very thin dashes.
        ps.print("\\psset{linestyle=dashed,dash=");
        for (int i=0; i<dash.length-1; i++) {
            ps.print(fixedPrecision(dash[i])+" ");
        }
        ps.print(fixedPrecision(dash[dash.length-1]));
        ps.println("}");
    }

    /* 8.2. paint/color */
    public void setPaintMode() {
        // Mostly unimplemented.
        ps.println("% setPaintMode() called");
    }

    public void setXORMode(Color c1) {
        // Mostly unimplemented.
        ps.println("% setXORMode(Color c1) called");
    }

    protected void writePaint(Color p) throws IOException {
        ps.println("\\newrgbcolor{current}{"+fixedPrecision(p.getRed()/255.)+" "+fixedPrecision(p.getGreen()/255.)+" "+fixedPrecision(p.getBlue()/255.)+"}");
        ps.println("\\psset{linecolor=current,fillcolor=current,fillstyle=solid}");
    }

    protected void writePaint(GradientPaint p) throws IOException {
        double gradTheta = Math.atan( (p.getPoint1().getY()-p.getPoint2().getY())/(p.getPoint1().getX()-p.getPoint2().getX()) );
        ps.println("\\newrgbcolor{gradbegin}{"+fixedPrecision(p.getColor1().getRed()/255.)+" "+fixedPrecision(p.getColor1().getGreen()/255.)+" "+fixedPrecision(p.getColor1().getBlue()/255.)+"}");
        ps.println("\\newrgbcolor{gradend}{"+fixedPrecision(p.getColor2().getRed()/255.)+" "+fixedPrecision(p.getColor2().getGreen()/255.)+" "+fixedPrecision(p.getColor2().getBlue()/255.)+"}");
        ps.println("\\psset{gradmidpoint=1,gradangle="+Math.toDegrees(gradTheta)+",fillstyle=gradient}");
    }

    protected void writePaint(TexturePaint p) throws IOException {
        // Not supported in LaTeX PSTricks.
        ps.println("% writePaint(TexturePaint p) called, not supported, crosshatch substituted");
        ps.println("\\psset{fillstyle=crosshatch}");
    }

    protected void writePaint(Paint p) throws IOException {
        writeWarning(getClass()+": writePaint(Paint) not implemented for "+p.getClass());
        // Write out the paint.
        ps.println("% writePaint(Paint p) called");
    }

    /* 8.3. font */
    /* 8.4. rendering hints */

    /*================================================================================
     | 9. Auxiliary
     *================================================================================*/
    public GraphicsConfiguration getDeviceConfiguration() {
        // Mostly unimplemented
        return null;
    }

    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        // Mostly unimplemented
        return false;
    }

    public void writeComment(String comment) throws IOException {
        ps.println("% "+comment);
    }

    public void writeWarning(String msg) {
        System.out.println(msg);
    }

    public String toString() {
        return "LatexGraphics";
    }
    
    private static ScientificFormat scientific = new ScientificFormat(5, 100, false);
    public static String fixedPrecision(double d) {
        return scientific.format(d);
    }
}