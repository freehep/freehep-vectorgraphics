// Copyright 2000-2003 FreeHEP
package org.freehep.graphicsio.cgm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.freehep.graphics2d.PrintColor;
import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.AbstractVectorGraphicsIO;
import org.freehep.graphicsio.PageConstants;
import org.freehep.util.UserProperties;
import org.freehep.util.io.TaggedOutput;

/**
 * Implementation of <tt>VectorGraphics</tt> that writes the output to a CGM
 * file. Users of this class have to generate a <tt>TagWriter</tt> and create
 * an instance by invoking the factory method or the constructor. Document
 * specific settings like page size can then be made by the appropriate setter
 * methods. Before starting to draw, <tt>startExport()</tt> must be called.
 * When drawing is finished, call <tt>endExport()</tt>.
 * 
 * @author Mark Donszelmann
 * @author Ian Graham - fixed constructor bug, added writeImage for raster
 *         support, and refactored for reuse
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CGMGraphics2D.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CGMGraphics2D extends AbstractVectorGraphicsIO {

    /*
     * ================================================================================
     * Table of Contents: ------------------ 1. Constructors & Factory Methods
     * 2. Document Settings 3. Header, Trailer, Multipage & Comments 3.1 Header &
     * Trailer 3.3 comments 4. Create 5. Drawing Methods 5.1. shapes (draw/fill)
     * 5.1.1. lines, rectangles, round rectangles 5.1.2. polylines, polygons
     * 5.1.3. ovals, arcs 5.1.4. shapes 5.2. Images 5.3. Strings 6.
     * Transformations 7. Clipping 8. Graphics State / Settings 8.1.
     * stroke/linewidth 8.2. paint/color 8.3. font 8.4. rendering hints 9.
     * Private/Utility Methos 9.1. drawing, shape creation 9.2. font, strings
     * 9.3. images 9.4. transformations 11. Todo List
     * ================================================================================
     */

    private static final String rootKey = CGMGraphics2D.class.getName();

    private static final Color DEFAULT_TRANSPARENT_CELL_COLOR = new Color(1, 2,
            3); // unlikely in real use

    public static final String BACKGROUND = rootKey + "."
            + PageConstants.BACKGROUND;

    public static final String BACKGROUND_COLOR = rootKey + "."
            + PageConstants.BACKGROUND_COLOR;

    public static final String BINARY = rootKey + ".Binary";

    public static final String AUTHOR = rootKey + ".Author";

    public static final String TITLE = rootKey + ".Title";

    public static final String SUBJECT = rootKey + ".Subject";

    public static final String KEYWORDS = rootKey + ".Keywords";

    private static final UserProperties defaultProperties = new UserProperties();
    static {
        defaultProperties.setProperty(BACKGROUND, false);
        defaultProperties.setProperty(BACKGROUND_COLOR, Color.GRAY);

        defaultProperties.setProperty(BINARY, true);
        defaultProperties.setProperty(AUTHOR, "");
        defaultProperties.setProperty(TITLE, "");
        defaultProperties.setProperty(SUBJECT, "");
        defaultProperties.setProperty(KEYWORDS, "");
    }

    public static Properties getDefaultProperties() {
        return defaultProperties;
    }

    public static void setDefaultProperties(Properties newProperties) {
        defaultProperties.setProperties(newProperties);
    }

    public final static String version = "$Revision$";

    private static final int CGM_VERSION = 3;

    private static final double FONTSIZE_CORRECTION = 0.8;

    private static final String[] STANDARD_FONT = { "Courier", "Courier-Bold",
            "Courier-Italic", "Courier-BoldItalic", "Helvetica",
            "Helvetica-Bold", "Helvetica-Italic", "Helvetica-BoldItalic",
            "TimesRoman", "TimesRoman-Bold", "TimesRoman-Italic",
            "TimesRoman-BoldItalic", "Symbol", "ZapfDingbats" };

    // output
    private OutputStream ros;

    private TaggedOutput os;

    private Color transparentCellColor = null;

    /*
     * ================================================================================ |
     * 1. Constructors & Factory Methods
     * ================================================================================
     */

    public CGMGraphics2D(File file, Dimension size)
            throws FileNotFoundException {
        this(new FileOutputStream(file), size);
    }

    public CGMGraphics2D(File file, Component component)
            throws FileNotFoundException {
        this(new FileOutputStream(file), component);
    }

    public CGMGraphics2D(OutputStream os, Dimension size) {
        super(size, false);
        init(os);
    }

    public CGMGraphics2D(OutputStream os, Component component) {
        super(component, false);
        init(os);
    }

    private void init(OutputStream os) {
        this.ros = os;
        initProperties(defaultProperties);
    }

    /** Cloneconstructor */
    protected CGMGraphics2D(CGMGraphics2D graphics) {
        super(graphics, false);

        os = graphics.os;
        transparentCellColor = graphics.transparentCellColor;
    }

    /*
     * ================================================================================ |
     * 2. Document Settings
     * ================================================================================
     */

    /*
     * ================================================================================ |
     * 3. Header, Trailer, Multipage & Comments
     * ================================================================================
     */

    /*--------------------------------------------------------------------------------
     | 3.1 Header & Trailer
     *--------------------------------------------------------------------------------*/

    /**
     * Writes the catalog, docinfo, preferences, and (as we use only single page
     * output the page tree. Written as a template method to enable reuse.
     */
    public void writeHeader() throws IOException {
        if (isProperty(BINARY)) {
            os = new CGMOutputStream(new BufferedOutputStream(ros), CGM_VERSION);
            ((CGMOutputStream) os).setRealPrecision(false, true);
        } else {
            os = new CGMWriter(new BufferedWriter(new OutputStreamWriter(ros)),
                    CGM_VERSION);
            ((CGMWriter) os).setRealPrecision(false, true);
        }

        os.writeTag(new BeginMetafile(
                "Generated by CGMGraphics2D, FreeHEP Graphics2D Driver"));
        writeMetafileDescriptor();

        os.writeTag(new BeginPicture("Picture"));
        writePictureDescriptor();

        writeBeginningOfPictureBody();

        // FIXME
        if (getClip() != null) {
            setClip(getClip());
        }
    }

    protected void writeMetafileDescriptor() throws IOException {
        String producer = getClass().getName();
        if (!isDeviceIndependent()) {
            producer += " " + version.substring(1, version.length() - 1);
        }
        os.writeTag(new MetafileVersion(CGM_VERSION));
        os.writeTag(new MetafileDescription("Producer: " + producer));
        os.writeTag(new MetafileDescription("Creator: " + getCreator()));
        // info
        os.writeTag(new MetafileDescription("Title: " + getProperty(TITLE)));
        os.writeTag(new MetafileDescription("Author: " + getProperty(AUTHOR)));
        os
                .writeTag(new MetafileDescription("Subject: "
                        + getProperty(SUBJECT)));
        os.writeTag(new MetafileDescription("Info: " + getProperty(KEYWORDS)));

        if (!isDeviceIndependent()) {
            os.writeTag(new MetafileDescription("CreationDate: "
                    + (new SimpleDateFormat().format(new Date()))));
        }
        os.writeTag(new MetafileElementList(MetafileElementList.VERSION_3_SET));
        os.writeTag(new VDCType(VDCType.REAL));
        // os.writeTag(new VDCRealPrecision()); // PowerPoint 10 does not accept
        os.writeTag(new FontList(STANDARD_FONT));
        os.writeTag(new ClipIndicator(true));
    }

    protected void writePictureDescriptor() throws IOException {
        os.writeTag(new ColorSelectionMode(ColorSelectionMode.DIRECT));
        os.writeTag(new EdgeWidthSpecificationMode(
                EdgeWidthSpecificationMode.ABSOLUTE));
        os.writeTag(new LineWidthSpecificationMode(
                LineWidthSpecificationMode.ABSOLUTE));
        Dimension size = getSize();
        os.writeTag(new VDCExtent(new Point2D.Double(0, size.height),
                new Point2D.Double(size.width, 0)));
    }

    protected void writeBeginningOfPictureBody() throws IOException {
        setTransparentCellColor(DEFAULT_TRANSPARENT_CELL_COLOR);
        os.writeTag(new BeginPictureBody());
        os.writeTag(new EdgeVisibility(false));
        os.writeTag(new EdgeCap(EdgeCap.BUTT));
        os.writeTag(new LineCap(LineCap.BUTT));
        os.writeTag(new EdgeColor(Color.black));
        os.writeTag(new FillColor(Color.black));
        os.writeTag(new LineColor(Color.black));
        os.writeTag(new TextColor(Color.black));
        // os.writeTag(new TransparentCellColour(true, new Color(0, 0, 0)));
        os.writeTag(new EdgeJoin(EdgeJoin.MITRE));
        os.writeTag(new LineJoin(LineJoin.MITRE));
        os.writeTag(new EdgeType(EdgeType.SOLID));
        os.writeTag(new LineType(LineType.SOLID));
        os.writeTag(new EdgeWidth(1));
        os.writeTag(new LineWidth(1));
        os.writeTag(new InteriorStyle(InteriorStyle.SOLID));
        os.writeTag(new CharacterHeight(getFont().getSize()));
        os.writeTag(new TextFontIndex(fontIndex(getFont())));
    }

    public void writeBackground() throws IOException {
        // since CGM is non-transparent, write current background
        if (isProperty(BACKGROUND)) {
            setBackground(getPropertyColor(BACKGROUND_COLOR));
        } else {
            setBackground(getComponent() != null ? getComponent()
                    .getBackground() : Color.WHITE);
        }
        clearRect(0.0, 0.0, getSize().width, getSize().height);
    }

    public void writeTrailer() throws IOException {
        os.writeTag(new EndPicture());
        os.writeTag(new EndMetafile());
    }

    public void closeStream() throws IOException {
        os.close();
    }

    /*
     * ================================================================================ |
     * 4. Create
     * ================================================================================
     */

    public Graphics create() {
        try {
            writeGraphicsSave();
        } catch (IOException e) {
            handleException(e);
        }
        return new CGMGraphics2D(this);
    }

    public Graphics create(double x, double y, double width, double height) {
        try {
            writeGraphicsSave();
        } catch (IOException e) {
            handleException(e);
        }
        VectorGraphics graphics = new CGMGraphics2D(this);
        graphics.translate(x, y);
        graphics.clipRect(0, 0, width, height);
        return graphics;
    }

    protected void writeGraphicsSave() throws IOException {
        // not applicable
    }

    protected void writeGraphicsRestore() throws IOException {
        // not applicable
    }

    /*
     * ================================================================================ |
     * 5. Drawing Methods
     * ================================================================================
     */
    /* 5.1.4. shapes */
    public void draw(Shape s) {
        try {
            if (getStroke() instanceof BasicStroke) {
                // in this case we've already handled the stroke
                drawPath(s, false);
            } else {
                // otherwise handle it now
                drawPath(getStroke().createStrokedShape(s), true);
            }
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void fill(Shape s) {
        try {
            drawPath(s, true);
        } catch (IOException e) {
            handleException(e);
        }
    }

    // TODO: Does not use current stroke yet
    public void fillAndDraw(Shape s, Color fillColor) {
        try {
            setNonStrokeColor(fillColor);
            drawPath(s, true);
            setNonStrokeColor(getColor());
            drawPath(s, false);
        } catch (IOException e) {
            handleException(e);
        }
    }

    private void drawPath(Shape s, boolean fill) throws IOException {
        // use a resolution compatible with the current transform
        AffineTransform t = getTransform();
        double resolution = 0.5 / (20 * Math.min(t.getScaleX(), t.getScaleY()));

        CGMPathConstructor path = new CGMPathConstructor(os, fill,
                getTransform(), resolution);
        boolean eo = path.addPath(s);
        path.writePolyline();
        if (!eo) {
            writeWarning(getClass()
                    + ": cannot fill using non-zero winding rule.");
        }
    }

    /* 5.2. Images */
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        writeWarning(getClass()
                + ": copyArea(int, int, int, int, int, int) not implemented.");
    }

    protected void writeImage(RenderedImage image, AffineTransform xform,
            Color bkg) throws IOException {

        int originX = image.getMinX();
        int originY = image.getMinY();
        int width = image.getWidth();
        int height = image.getHeight();
        Point untransformedP = new Point(originX, originY);
        Point untransformedQ = new Point(originX + width, originY + height);
        Point untransformedR = new Point(originX + width, 0);
        AffineTransform compoundTransform = getTransform();
        compoundTransform.concatenate(xform);
        Point2D p = compoundTransform.transform(untransformedP, null);
        Point2D q = compoundTransform.transform(untransformedQ, null);
        Point2D r = compoundTransform.transform(untransformedR, null);

        ColorModel colorModel = image.getColorModel();
        Raster raster = image.getData();
        Object currentDataElements = null;
        Color[][] colorArray = new Color[height][width];
        for (int xIndex = 0; xIndex < width; xIndex++) {
            for (int yIndex = 0; yIndex < height; yIndex++) {
                Color color;
                currentDataElements = raster.getDataElements(xIndex, yIndex,
                        currentDataElements);
                int alphaComponent = colorModel.getAlpha(currentDataElements);
                if (alphaComponent > 0 || transparentCellColor == null) {
                    int redComponent = colorModel.getRed(currentDataElements);
                    int greenComponent = colorModel
                            .getGreen(currentDataElements);
                    int blueComponent = colorModel.getBlue(currentDataElements);
                    color = new Color(redComponent, greenComponent,
                            blueComponent, alphaComponent);
                } else {
                    color = transparentCellColor;
                }
                colorArray[yIndex][xIndex] = color;
            }
        }

        CellArray cgmCellArray = new CellArray(p, q, r, colorArray);
        os.writeTag(cgmCellArray);
    }

    /* 5.3. Strings */
    protected void writeString(String str, double x, double y)
            throws IOException {
        Point2D point = new Point2D.Double(x, y);
        AffineTransform currentTransform = new AffineTransform(getTransform());
        currentTransform.concatenate(getFont().getTransform());
        os.writeTag(new CharacterOrientation(-currentTransform.getShearX(),
                -currentTransform.getScaleY(), currentTransform.getScaleX(),
                currentTransform.getShearY()));
        os.writeTag(new Text(currentTransform.transform(point, point), str));
    }

    /*
     * ================================================================================ |
     * 6. Transformations
     * ================================================================================
     */
    protected void writeTransform(AffineTransform t) throws IOException {
        // ignored, coordinates are pre-calculated before written to CGM
    }

    /*
     * ================================================================================ |
     * 7. Clipping
     * ================================================================================
     */
    protected void writeClip(Rectangle2D r2d) throws IOException {
        AffineTransform currentTransform = getTransform();
        Point2D xy = currentTransform.transform(new Point2D.Double(r2d.getX(),
                r2d.getY()), null);
        Point2D wh = currentTransform.deltaTransform(new Point2D.Double(r2d
                .getWidth(), r2d.getHeight()), null);
        r2d = new Rectangle2D.Double(xy.getX(), xy.getY(), wh.getX(), wh.getY());
        os.writeTag(new ClipRectangle(r2d));
    }

    protected void writeClip(Shape s) throws IOException {
        writeWarning(getClass() + ": writeClip(Shape) not implemented.");
    }

    /*
     * ================================================================================ |
     * 8. Graphics State
     * ================================================================================
     */
    /* 8.1. stroke/linewidth */
    protected void writeWidth(float width) throws IOException {
        os.writeTag(new EdgeWidth(width));
        os.writeTag(new LineWidth(width));
    }

    protected void writeCap(int cap) throws IOException {
        switch (cap) {
        case BasicStroke.CAP_BUTT:
            os.writeTag(new EdgeCap(EdgeCap.BUTT));
            os.writeTag(new LineCap(LineCap.BUTT));
            break;
        case BasicStroke.CAP_ROUND:
            os.writeTag(new EdgeCap(EdgeCap.ROUND));
            os.writeTag(new LineCap(LineCap.ROUND));
            break;
        case BasicStroke.CAP_SQUARE:
            os.writeTag(new EdgeCap(EdgeCap.SQUARE));
            os.writeTag(new LineCap(LineCap.SQUARE));
            break;
        }
    }

    protected void writeJoin(int join) throws IOException {
        switch (join) {
        default:
        case BasicStroke.JOIN_MITER:
            os.writeTag(new EdgeJoin(EdgeJoin.MITRE));
            os.writeTag(new LineJoin(LineJoin.MITRE));
            break;
        case BasicStroke.JOIN_ROUND:
            os.writeTag(new EdgeJoin(EdgeJoin.ROUND));
            os.writeTag(new LineJoin(LineJoin.ROUND));
            break;
        case BasicStroke.JOIN_BEVEL:
            os.writeTag(new EdgeJoin(EdgeJoin.BEVEL));
            os.writeTag(new LineJoin(LineJoin.BEVEL));
            break;
        }
    }

    protected void writeMiterLimit(float limit) throws IOException {
        os.writeTag(new MitreLimit(limit));
    }

    protected void writeDash(double[] dash, double phase) throws IOException {
        // NOTE: patterns are ignored, a guess is made
        switch (dash.length) {
        case 0:
            os.writeTag(new EdgeType(EdgeType.SOLID));
            os.writeTag(new LineType(LineType.SOLID));
            break;
        default:
        case 1:
            os.writeTag(new EdgeType(EdgeType.DASH));
            os.writeTag(new LineType(LineType.DASH));
            break;
        case 2:
            os.writeTag(new EdgeType(EdgeType.DOT));
            os.writeTag(new LineType(LineType.DOT));
            break;
        case 3:
            os.writeTag(new EdgeType(EdgeType.DASH_DOT));
            os.writeTag(new LineType(LineType.DASH_DOT));
            break;
        case 4:
            os.writeTag(new EdgeType(EdgeType.DASH_DOT_DOT));
            os.writeTag(new LineType(LineType.DASH_DOT_DOT));
            break;
        }
    }

    /* 8.2. paint/color */
    public void setPaintMode() {
        writeWarning(getClass() + ": setPaintMode() not implemented.");
    }

    public void setXORMode(Color c1) {
        writeWarning(getClass() + ": setXORMode(Color) not implemented.");
    }

    public void setTransparentCellColor(Color color) throws IOException {
        if (color != null) {
            os.writeTag(new TransparentCellColour(true, color));
        } else {
            os.writeTag(new TransparentCellColour(false, null));
        }
        transparentCellColor = color;
    }

    protected void writePaint(Color c) throws IOException {
        os.writeTag(new LineColor(c));
        os.writeTag(new EdgeColor(c));
        os.writeTag(new FillColor(c));
        os.writeTag(new TextColor(c));
    }

    protected void writePaint(GradientPaint paint) throws IOException {
        // NOTE: CGM does not support gradients, so we average the colors
        setColor(PrintColor.mixColor(paint.getColor1(), paint.getColor2()));
    }

    protected void writePaint(TexturePaint p) throws IOException {
        writeWarning(getClass() + ": writePaint(TexturePaint) not implemented.");
    }

    protected void writePaint(Paint p) throws IOException {
        writeWarning(getClass() + ": writePaint(Paint) not implemented for "
                + p.getClass());
    }

    /* 8.3. font */

    public void setFont(Font font) {
        super.setFont(font);
        try {
            os.writeTag(new TextFontIndex(fontIndex(font)));
            os.writeTag(new CharacterHeight(font.getSize()
                    * FONTSIZE_CORRECTION));
        } catch (IOException e) {
            handleException(e);
        }
    }

    /*
     * ================================================================================ |
     * 9. Auxiliary
     * ================================================================================
     */
    public GraphicsConfiguration getDeviceConfiguration() {
        writeWarning(getClass() + ": getDeviceConfiguration() not implemented.");
        return null;
    }

    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        writeWarning(getClass()
                + ": hit(Rectangle, Shape, boolean) not implemented.");
        return false;
    }

    public void writeComment(String comment) throws IOException {
        // comments are ignored and disabled, because they confuse compressed
        // streams
    }

    public String toString() {
        return "CGMGraphics2D";
    }

    /*
     * ================================================================================ |
     * 10. Private/Utility Methos
     * ================================================================================
     */
    private static int fontIndex(Font font) {
        StringBuffer fontName = new StringBuffer(font.getName());
        if (font.isBold() || font.isItalic())
            fontName.append("-");
        if (font.isBold())
            fontName.append("Bold");
        if (font.isItalic())
            fontName.append("Italic");
        String name = fontName.toString();
        for (int i = 0; i < STANDARD_FONT.length; i++) {
            if (name.equals(STANDARD_FONT[i]))
                return i + 1;
        }
        return 5; // Helvetica
    }

    private void setNonStrokeColor(Color c) throws IOException {
        os.writeTag(new FillColor(getPrintColor(c)));
    }

    // private void setStrokeColor(Color c) throws IOException {
    // os.writeTag(new LineColor(getPrintColor(c)));
    // }

    /**
     * Return the tagged output stream to which to write tags.
     * 
     * @return TaggedOutput
     */
    protected TaggedOutput getTaggedOutput() {
        return os;
    }

}
