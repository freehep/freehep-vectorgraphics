// Copyright 2000-2007 FreeHEP
package org.freehep.graphicsio.emf;

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
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.freehep.graphics2d.PrintColor;
import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphics2d.font.FontEncoder;
import org.freehep.graphics2d.font.FontUtilities;
import org.freehep.graphicsbase.util.UserProperties;
import org.freehep.graphicsbase.util.images.ImageUtilities;
import org.freehep.graphicsio.AbstractVectorGraphicsIO;
import org.freehep.graphicsio.PageConstants;
import org.freehep.graphicsio.emf.gdi.AlphaBlend;
import org.freehep.graphicsio.emf.gdi.BeginPath;
import org.freehep.graphicsio.emf.gdi.CreateBrushIndirect;
import org.freehep.graphicsio.emf.gdi.DeleteObject;
import org.freehep.graphicsio.emf.gdi.EOF;
import org.freehep.graphicsio.emf.gdi.EndPath;
import org.freehep.graphicsio.emf.gdi.ExtCreateFontIndirectW;
import org.freehep.graphicsio.emf.gdi.ExtCreatePen;
import org.freehep.graphicsio.emf.gdi.ExtLogFontW;
import org.freehep.graphicsio.emf.gdi.ExtLogPen;
import org.freehep.graphicsio.emf.gdi.ExtTextOutW;
import org.freehep.graphicsio.emf.gdi.FillPath;
import org.freehep.graphicsio.emf.gdi.LogBrush32;
import org.freehep.graphicsio.emf.gdi.ModifyWorldTransform;
import org.freehep.graphicsio.emf.gdi.RestoreDC;
import org.freehep.graphicsio.emf.gdi.SaveDC;
import org.freehep.graphicsio.emf.gdi.SelectClipPath;
import org.freehep.graphicsio.emf.gdi.SelectObject;
import org.freehep.graphicsio.emf.gdi.SetBkMode;
import org.freehep.graphicsio.emf.gdi.SetMapMode;
import org.freehep.graphicsio.emf.gdi.SetMiterLimit;
import org.freehep.graphicsio.emf.gdi.SetPolyFillMode;
import org.freehep.graphicsio.emf.gdi.SetTextAlign;
import org.freehep.graphicsio.emf.gdi.SetTextColor;
import org.freehep.graphicsio.emf.gdi.SetViewportExtEx;
import org.freehep.graphicsio.emf.gdi.SetViewportOrgEx;
import org.freehep.graphicsio.emf.gdi.SetWindowExtEx;
import org.freehep.graphicsio.emf.gdi.SetWindowOrgEx;
import org.freehep.graphicsio.emf.gdi.SetWorldTransform;
import org.freehep.graphicsio.emf.gdi.StrokeAndFillPath;
import org.freehep.graphicsio.emf.gdi.StrokePath;
import org.freehep.graphicsio.emf.gdi.TextW;
import org.freehep.graphicsio.font.FontTable;

/**
 * Enhanced Metafile Format Graphics 2D driver.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFGraphics2D.java 59372df5e0d9 2007/02/06 21:11:19 duns $
 */
public class EMFGraphics2D extends AbstractVectorGraphicsIO implements
        EMFConstants {


    /**
     * The dash array definition used in the {@link #setStroke(Stroke)} and {@link #writeStroke(Stroke)} for a {@link EMFConstants#PS_DASH} pen.
     * Define it if you want to use {@link EMFConstants#PS_DASH} instead of {@link EMFConstants#PS_USERSTYLE}.
     */
    public static float[] dashArrayForDashStroke = null;

    /**
     * The dash array definition used in the {@link #setStroke(Stroke)} and {@link #writeStroke(Stroke)} for a {@link EMFConstants#PS_DOT} pen.
     * Define it if you want to use {@link EMFConstants#PS_DOT} instead of {@link EMFConstants#PS_USERSTYLE}.
     */
    public static float[] dashArrayForDotStroke = null;

    /**
     * The dash array definition used in the {@link #setStroke(Stroke)} and {@link #writeStroke(Stroke)} for a {@link EMFConstants#PS_DASHDOT} pen.
     * Define it if you want to use {@link EMFConstants#PS_DASHDOT} instead of {@link EMFConstants#PS_USERSTYLE}.
     */
    public static float[] dashArrayForDashDotStroke = null;

    /**
     * The dash array definition used in the {@link #setStroke(Stroke)} and {@link #writeStroke(Stroke)} for a {@link EMFConstants#PS_DASHDOTDOT} pen.
     * Define it if you want to use {@link EMFConstants#PS_DASHDOTDOT} instead of {@link EMFConstants#PS_USERSTYLE}.
     */
    public static float[] dashArrayForDashDotDotStroke = null;


    public static final String version = "$Revision$";

    private EMFHandleManager handleManager;

    private int penHandle;

    private int brushHandle;

    private Rectangle imageBounds;

    private OutputStream ros;

    private EMFOutputStream os;

    private Color textColor = null;

    private Color penColor = null;

    private Color brushColor = null;

    private Map<Font, Integer> fontTable; // java fonts

    private Map<Font, Font> unitFontTable; // windows fonts

    private EMFPathConstructor pathConstructor;

    private boolean evenOdd;

    private static final Rectangle dummy = new Rectangle(0, 0, 0, 0);

    /*
     * ================================================================================
     * Table of Contents: ------------------ 1. Constructors & Factory Methods
     * 2. Document Settings 3. Header, Trailer, Multipage & Comments 3.1 Header &
     * Trailer 3.2 MultipageDocument methods 4. Create & Dispose 5. Drawing
     * Methods 5.1. shapes (draw/fill) 5.1.1. lines, rectangles, round
     * rectangles 5.1.2. polylines, polygons 5.1.3. ovals, arcs 5.1.4. shapes
     * 5.2. Images 5.3. Strings 6. Transformations 7. Clipping 8. Graphics State /
     * Settings 8.1. stroke/linewidth 8.2. paint/color 8.3. font 8.4. rendering
     * hints 9. Auxiliary 10. Private/Utility Methos
     * ================================================================================
     */
    private static final String rootKey = EMFGraphics2D.class.getName();

    public static final String TRANSPARENT = rootKey + "."
            + PageConstants.TRANSPARENT;

    public static final String BACKGROUND = rootKey + "."
            + PageConstants.BACKGROUND;

    public static final String BACKGROUND_COLOR = rootKey + "."
            + PageConstants.BACKGROUND_COLOR;

    private static final UserProperties defaultProperties = new UserProperties();
    static {
        defaultProperties.setProperty(TRANSPARENT, true);
        defaultProperties.setProperty(BACKGROUND, false);
        defaultProperties.setProperty(BACKGROUND_COLOR, Color.GRAY);
        defaultProperties.setProperty(CLIP, true);
        // NOTE: using TEXT_AS_SHAPES makes the text shapes quite unreadable.
        defaultProperties.setProperty(TEXT_AS_SHAPES, false);
    }

    public static Properties getDefaultProperties() {
        return defaultProperties;
    }

    public static void setDefaultProperties(Properties newProperties) {
        defaultProperties.setProperties(newProperties);
    }

    /*
     * ================================================================================
     * 1. Constructors & Factory Methods
     * ================================================================================
     */
    public EMFGraphics2D(File file, Dimension size)
            throws FileNotFoundException {
        this(new FileOutputStream(file), size);
    }

    public EMFGraphics2D(File file, Component component)
            throws FileNotFoundException {
        this(new FileOutputStream(file), component);
    }

    public EMFGraphics2D(OutputStream os, Dimension size) {
        super(size, false);
        this.imageBounds = new Rectangle(0, 0, size.width, size.height);
        init(os);
    }

    public EMFGraphics2D(OutputStream os, Component component) {
        super(component, false);
        this.imageBounds = new Rectangle(0, 0, getSize().width,
                getSize().height);
        init(os);
    }

    private void init(OutputStream os) {
        fontTable = new HashMap<Font, Integer>();
        unitFontTable = new HashMap<Font, Font>();
        evenOdd = false;

        handleManager = new EMFHandleManager();
        ros = os;
        initProperties(defaultProperties);
    }

    protected EMFGraphics2D(EMFGraphics2D graphics, boolean doRestoreOnDispose) {
        super(graphics, doRestoreOnDispose);
        // Create a graphics context from a given graphics context.
        // This constructor is used by the system to clone a given graphics
        // context.
        // doRestoreOnDispose is used to call writeGraphicsRestore(),
        // when the graphics context is being disposed off.
        os = graphics.os;
        imageBounds = graphics.imageBounds;
        handleManager = graphics.handleManager;
        fontTable = graphics.fontTable;
        unitFontTable = graphics.unitFontTable;
        pathConstructor = graphics.pathConstructor;
        evenOdd = graphics.evenOdd;
        textColor = graphics.textColor;
        penColor = graphics.penColor;
        brushColor = graphics.brushColor;
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
    /* 3.1 Header & Trailer */
    public void writeHeader() throws IOException {
        ros = new BufferedOutputStream(ros);
        Dimension device = isDeviceIndependent() ? new Dimension(1024, 768)
                : Toolkit.getDefaultToolkit().getScreenSize();
        String producer = getClass().getName();
        if (!isDeviceIndependent()) {
            producer += " " + version.substring(1, version.length() - 1);
        }
        os = new EMFOutputStream(ros, imageBounds, handleManager, getCreator(),
                producer, device);
        pathConstructor = new EMFPathConstructor(os, imageBounds);

        Point orig = new Point(imageBounds.x, imageBounds.y);
        Dimension size = new Dimension(imageBounds.width, imageBounds.height);

        os.writeTag(new SetMapMode(MM_ANISOTROPIC));
        os.writeTag(new SetWindowOrgEx(orig));
        os.writeTag(new SetWindowExtEx(size));
        os.writeTag(new SetViewportOrgEx(orig));
        os.writeTag(new SetViewportExtEx(size));
        os.writeTag(new SetTextAlign(TA_BASELINE));
        os.writeTag(new SetTextColor(getColor()));
        os.writeTag(new SetPolyFillMode(EMFConstants.WINDING));

    }

    public void writeGraphicsState() throws IOException {
        super.writeGraphicsState();
        // write a special matrix here to scale all written coordinates by a
        // factor of TWIPS
        AffineTransform n = AffineTransform.getScaleInstance(1.0 / TWIPS,
                1.0 / TWIPS);
        os.writeTag(new SetWorldTransform(n));
    }

    public void writeBackground() throws IOException {
        if (isProperty(TRANSPARENT)) {
            setBackground(null);
            os.writeTag(new SetBkMode(BKG_TRANSPARENT));
        } else if (isProperty(BACKGROUND)) {
            os.writeTag(new SetBkMode(BKG_OPAQUE));
            setBackground(getPropertyColor(BACKGROUND_COLOR));
            clearRect(0.0, 0.0, getSize().width, getSize().height);
        } else {
            os.writeTag(new SetBkMode(BKG_OPAQUE));
            setBackground(getComponent() != null ? getComponent()
                    .getBackground() : Color.WHITE);
            clearRect(0.0, 0.0, getSize().width, getSize().height);
        }
    }

    public void writeTrailer() throws IOException {
        // delete any remaining objects
        for (;;) {
            int handle = handleManager.highestHandleInUse();
            if (handle < 0)
                break;
            os.writeTag(new DeleteObject(handle));
            handleManager.freeHandle(handle);
        }
        os.writeTag(new EOF());
    }

    public void closeStream() throws IOException {
        os.close();
    }

    /* 3.2 MultipageDocument methods */

    /*
     * ================================================================================
     * 4. Create & Dispose
     * ================================================================================
     */

    public Graphics create() {
        // Create a new graphics context from the current one.
        try {
            // Save the current context for restore later.
            writeGraphicsSave();
        } catch (IOException e) {
            handleException(e);
        }
        // The correct graphics context should be created.
        return new EMFGraphics2D(this, true);
    }

    public Graphics create(double x, double y, double width, double height) {
        // Create a new graphics context from the current one.
        try {
            // Save the current context for restore later.
            writeGraphicsSave();
        } catch (IOException e) {
            handleException(e);
        }
        // The correct graphics context should be created.
        VectorGraphics graphics = new EMFGraphics2D(this, true);
        graphics.translate(x, y);
        graphics.clipRect(0, 0, width, height);
        return graphics;
    }

    protected void writeGraphicsSave() throws IOException {
        os.writeTag(new SaveDC());
    }

    protected void writeGraphicsRestore() throws IOException {
        if (penHandle != 0)
            os.writeTag(new DeleteObject(handleManager.freeHandle(penHandle)));
        if (brushHandle != 0)
            os
                    .writeTag(new DeleteObject(handleManager
                            .freeHandle(brushHandle)));
        os.writeTag(new RestoreDC());
    }

    /*
     * ================================================================================ |
     * 5. Drawing Methods
     * ================================================================================
     */
    /* 5.1.4. shapes */
    public void draw(Shape shape) {
        try {
            if (getStroke() instanceof BasicStroke) {
                writePen((BasicStroke) getStroke(), getColor());
                writePath(shape);
                os.writeTag(new StrokePath(imageBounds));
            } else {
                writeBrush(getColor());
                writePath(getStroke().createStrokedShape(shape));
                os.writeTag(new FillPath(imageBounds));
            }
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void fill(Shape shape) {
        try {
            if (getPaint() instanceof Color) {
                writeBrush(getColor());
                writePath(shape);
                os.writeTag(new FillPath(imageBounds));
            } else {
                // draw paint as image
                fill(shape, getPaint());
            }
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void fillAndDraw(Shape shape, Color fillColor) {
        try {
            if (getPaint() instanceof Color) {
                writePen((BasicStroke) getStroke(), getColor());
                writeBrush(fillColor);
                writePath(shape);
                os.writeTag(new StrokeAndFillPath(imageBounds));
            } else {
                // draw paint as image
                fill(shape, getPaint());
                // draw shape
                draw(shape);
            }
        } catch (IOException e) {
            handleException(e);
        }
    }

    /* 5.2. Images */
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        writeWarning(getClass()
                + ": copyArea(int, int, int, int, int, int) not implemented.");
        // Mostly unimplemented.
    }

    // NOTE: does not use writeGraphicsSave and writeGraphicsRestore since these
    // delete pen and brush
    protected void writeImage(RenderedImage image, AffineTransform xform,
            Color bkg) throws IOException {
        os.writeTag(new SaveDC());

        AffineTransform imageTransform = new AffineTransform(
            1.0, 0.0, 0.0, -1.0, 0.0, image.getHeight());
        imageTransform.preConcatenate(xform);
        writeTransform(imageTransform);

        BufferedImage bufferedImage = ImageUtilities.createBufferedImage(
            image, null, null);
        AlphaBlend alphaBlend = new AlphaBlend(
            imageBounds,
            toUnit(0),
            toUnit(0),
            toUnit(image.getWidth()),
            toUnit(image.getHeight()),
            new AffineTransform(),
            bufferedImage,
            bkg);

        os.writeTag(alphaBlend);
        os.writeTag(new RestoreDC());
    }

    /* 5.3. Strings */
    public void writeString(String string, double x, double y)
            throws IOException {

        Color color;
        Paint paint = getPaint();
        if (paint instanceof Color) {
            color = (Color) paint;
        } else if (paint instanceof GradientPaint) {
            GradientPaint gp = (GradientPaint) paint;
            color = PrintColor.mixColor(gp.getColor1(), gp.getColor2());
        } else {
            Color bkg = getBackground();
            if (bkg == null) {
                color = Color.BLACK;
            } else {
                color = PrintColor.invert(bkg);
            }
        }
        if (!color.equals(textColor)) {
            textColor = color;
            os.writeTag(new SetTextColor(textColor));
        }

        // dialog.bold -> Dialog with TextAttribute.WEIGHT_BOLD
        Map<Attribute, Object> attributes = FontUtilities.getAttributes(getFont());
        FontTable.normalize(attributes);
        Font font = new Font(attributes);

        Font unitFont = unitFontTable.get(font);

        Integer fontIndex = fontTable.get(font);
        if (fontIndex == null) {
            // for special fonts (Symbol, ZapfDingbats) we choose a standard
            // font and
            // encode using unicode.
            String fontName = font.getName();
            string = FontEncoder.getEncodedString(string, fontName);

            String windowsFontName = FontUtilities
                .getWindowsFontName(fontName);

            unitFont = new Font(windowsFontName, font.getStyle(), font
                .getSize());
            unitFont = unitFont.deriveFont(font.getSize2D()
                * UNITS_PER_PIXEL * TWIPS);
            unitFontTable.put(font, unitFont);

            ExtLogFontW logFontW = new ExtLogFontW(unitFont);
            int handle = handleManager.getHandle();
            os.writeTag(new ExtCreateFontIndirectW(handle, logFontW));

            fontIndex = new Integer(handle);
            fontTable.put(font, fontIndex);
        }
        os.writeTag(new SelectObject(fontIndex.intValue()));

        int[] widths = new int[string.length()];
        for (int i = 0; i < widths.length; i++) {
            double w = unitFont.getStringBounds(string, i, i + 1,
                getFontRenderContext()).getWidth();
            widths[i] = (int) w;
        }

        // font transformation sould _not_ transform string position
        translate(x, y);

        // apply font transformation
        AffineTransform t = font.getTransform();
        if (!t.isIdentity()) {
            writeGraphicsSave();
            writeTransform(t);
        }

        TextW text = new TextW(new Point(0, 0), string, 0, dummy, widths);
        os.writeTag(new ExtTextOutW(imageBounds, EMFConstants.GM_ADVANCED, 1, 1, text));

        // revert font transformation
        if (!t.isIdentity()) {
            writeGraphicsRestore();
        }

        // translation for string position.
        translate(-x, -y);
    }

    /*
     * ================================================================================ |
     * 6. Transformations
     * ================================================================================
     */
    protected void writeTransform(AffineTransform t) throws IOException {
        AffineTransform n = new AffineTransform(t.getScaleX(), t.getShearY(), t
                .getShearX(), t.getScaleY(), t.getTranslateX()
                * UNITS_PER_PIXEL * TWIPS, t.getTranslateY() * UNITS_PER_PIXEL
                * TWIPS);
        os.writeTag(new ModifyWorldTransform(n, EMFConstants.MWT_LEFTMULTIPLY));
    }

    protected void writeSetTransform(AffineTransform t) throws IOException {
        // write a special matrix here to scale all written coordinates by a factor of TWIPS
        AffineTransform n = AffineTransform.getScaleInstance(1.0/TWIPS, 1.0/TWIPS);
        os.writeTag(new SetWorldTransform(n));
        // apply transform
        writeTransform(t);
    }

    /*
     * ================================================================================ |
     * 7. Clipping
     * ================================================================================
     */
    protected void writeSetClip(Shape s) throws IOException {
        if (!isProperty(CLIP)) {
            return;
        }

        // if s == null the clip is reset to the imageBounds
        if (s == null && imageBounds != null) {
            s = new Rectangle(imageBounds);
            AffineTransform at = getTransform();
            if (at != null) {
                s = at.createTransformedShape(s);
            }
        }

        writePath(s);
        os.writeTag(new SelectClipPath(EMFConstants.RGN_COPY));
    }

   protected void writeClip(Shape s) throws IOException {

       if (s == null || !isProperty(CLIP)) {
           return;
       }

       writePath(s);
       os.writeTag(new SelectClipPath(EMFConstants.RGN_AND));
   }

    /*
     * ================================================================================ |
     * 8. Graphics State
     * ================================================================================
     */
    public void writeStroke(Stroke stroke) throws IOException {
        if (stroke instanceof BasicStroke) {
            writePen((BasicStroke) stroke, getColor());
        }
    }

    /* 8.2. paint/color */
    public void setPaintMode() {
        writeWarning(getClass() + ": setPaintMode() not implemented.");
        // Mostly unimplemented.
    }

    public void setXORMode(Color c1) {
        writeWarning(getClass() + ": setXORMode(Color) not implemented.");
        // Mostly unimplemented.
    }

    protected void writePaint(Color p) throws IOException {
        // all color setting delayed
    }

    protected void writePaint(GradientPaint p) throws IOException {
        // all paint setting delayed
    }

    protected void writePaint(TexturePaint p) throws IOException {
        // all paint setting delayed
    }

    protected void writePaint(Paint p) throws IOException {
        // all paint setting delayed
    }

    /* 8.3. font */
    protected void writeFont(Font font) throws IOException {
    	// written when needed
    }

    /* 8.4. rendering hints */

    /*
     * ================================================================================ |
     * 9. Auxiliary
     * ================================================================================
     */
    public GraphicsConfiguration getDeviceConfiguration() {
        writeWarning(getClass() + ": getDeviceConfiguration() not implemented.");
        // Mostly unimplemented
        return null;
    }

    public void writeComment(String comment) throws IOException {
        writeWarning(getClass() + ": writeComment(String) not implemented.");
        // Write out the comment.
    }

    public String toString() {
        return "EMFGraphics2D";
    }

    /**
     * Implementation of createShape makes sure that the points are different by
     * at least one Unit.
     */
    protected Shape createShape(double[] xPoints, double[] yPoints,
            int nPoints, boolean close) {
        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        if (nPoints > 0) {
            path.moveTo((float) xPoints[0], (float) yPoints[0]);
            double lastX = xPoints[0];
            double lastY = yPoints[0];
            if (close && (Math.abs(xPoints[nPoints - 1] - lastX) < 1)
                    && (Math.abs(yPoints[nPoints - 1] - lastY) < 1)) {
                nPoints--;
            }
            for (int i = 1; i < nPoints; i++) {
                if ((Math.abs(xPoints[i] - lastX) > 1)
                        || (Math.abs(yPoints[i] - lastY) > 1)) {
                    path.lineTo((float) xPoints[i], (float) yPoints[i]);
                    lastX = xPoints[i];
                    lastY = yPoints[i];
                }
            }
            if (close)
                path.closePath();
        }
        return path;
    }

    /*
     * Private methods
     */
    private boolean writePath(Shape shape) throws IOException {
        boolean eo = EMFPathConstructor.isEvenOdd(shape);
        if (eo != evenOdd) {
            evenOdd = eo;
            os.writeTag(new SetPolyFillMode((evenOdd) ? EMFConstants.ALTERNATE
                    : EMFConstants.WINDING));
        }
        os.writeTag(new BeginPath());
        pathConstructor.addPath(shape);
        os.writeTag(new EndPath());
        return evenOdd;
    }

    private void writePen(BasicStroke stroke, Color color) throws IOException {
        if (color.equals(penColor) && stroke.equals(getStroke()))
            return;
        penColor = color;

        int style = EMFConstants.PS_GEOMETRIC;

        switch (stroke.getEndCap()) {
        case BasicStroke.CAP_BUTT:
            style |= EMFConstants.PS_ENDCAP_FLAT;
            break;
        case BasicStroke.CAP_ROUND:
            style |= EMFConstants.PS_ENDCAP_ROUND;
            break;
        case BasicStroke.CAP_SQUARE:
            style |= EMFConstants.PS_ENDCAP_SQUARE;
            break;
        }

        switch (stroke.getLineJoin()) {
        case BasicStroke.JOIN_MITER:
            style |= EMFConstants.PS_JOIN_MITER;
            break;
        case BasicStroke.JOIN_ROUND:
            style |= EMFConstants.PS_JOIN_ROUND;
            break;
        case BasicStroke.JOIN_BEVEL:
            style |= EMFConstants.PS_JOIN_BEVEL;
            break;
        }

        // FIXME int conversion
        // FIXME phase ignored
        float[] dashArray = stroke.getDashArray();
        int[] dash = new int[(dashArray != null) ? dashArray.length : 0];
        if (dashArrayForDashStroke != null && Arrays.equals(dashArray, dashArrayForDashStroke)) {
            dash = new int[0];
            style |= EMFConstants.PS_DASH;
        } else if (dashArrayForDotStroke != null && Arrays.equals(dashArray, dashArrayForDotStroke)) {
            dash = new int[0];
            style |= EMFConstants.PS_DOT;
        } else if (dashArrayForDashDotStroke != null && Arrays.equals(dashArray, dashArrayForDashDotStroke)) {
            dash = new int[0];
            style |= EMFConstants.PS_DASHDOT;
        }  else if (dashArrayForDashDotDotStroke != null && Arrays.equals(dashArray, dashArrayForDashDotDotStroke)) {
            dash = new int[0];
            style |= EMFConstants.PS_DASHDOTDOT;
        } else {
            style |= (dash.length == 0) ? EMFConstants.PS_SOLID : EMFConstants.PS_USERSTYLE;
            for (int i = 0; i < dash.length; i++) {
                dash[i] = toUnit(dashArray[i]);
            }
        }
        

        int brushStyle = (color.getAlpha() == 0) ? EMFConstants.BS_NULL
                : EMFConstants.BS_SOLID;

        ExtLogPen pen = new ExtLogPen(style, toUnit(stroke.getLineWidth()),
                brushStyle, getPrintColor(color), 0, dash);
        if (penHandle != 0) {
            os.writeTag(new DeleteObject(penHandle));
        } else {
            penHandle = handleManager.getHandle();
        }
        os.writeTag(new ExtCreatePen(penHandle, pen));
        os.writeTag(new SelectObject(penHandle));

        if (!(getStroke() instanceof BasicStroke)
                || (((BasicStroke) getStroke()).getMiterLimit() != stroke
                        .getMiterLimit())) {
            os.writeTag(new SetMiterLimit(toUnit(stroke.getMiterLimit())));
        }
    }

    private void writeBrush(Color color) throws IOException {
        if (color.equals(brushColor))
            return;
        brushColor = color;

        int brushStyle = (color.getAlpha() == 0) ? EMFConstants.BS_NULL
                : EMFConstants.BS_SOLID;

        LogBrush32 brush = new LogBrush32(brushStyle, getPrintColor(color), 0);
        if (brushHandle != 0) {
            os.writeTag(new DeleteObject(brushHandle));
        } else {
            brushHandle = handleManager.getHandle();
        }
        os.writeTag(new CreateBrushIndirect(brushHandle, brush));
        os.writeTag(new SelectObject(brushHandle));
    }

    private int toUnit(double d) {
        return (int) Math.floor(d * UNITS_PER_PIXEL * TWIPS);
    }
}
