// Copyright 2000-2006 FreeHEP
package org.freehep.graphicsio.swf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import org.freehep.graphics2d.PrintColor;
import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphics2d.font.FontEncoder;
import org.freehep.graphicsio.AbstractVectorGraphicsIO;
import org.freehep.graphicsio.ImageConstants;
import org.freehep.graphicsio.PageConstants;
import org.freehep.util.UserProperties;
import org.freehep.util.Value;

/**
 * SWF Graphics 2D driver.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SWFGraphics2D.java 35e0dc167f2a 2006/11/13 23:44:09 duns $
 */
public class SWFGraphics2D extends AbstractVectorGraphicsIO implements
        SWFConstants {

    private static final String rootKey = SWFGraphics2D.class.getName();

    public static final String TRANSPARENT = rootKey + "."
            + PageConstants.TRANSPARENT;

    public static final String BACKGROUND = rootKey + "."
            + PageConstants.BACKGROUND;

    public static final String BACKGROUND_COLOR = rootKey + "."
            + PageConstants.BACKGROUND_COLOR;

    public static final String WRITE_IMAGES_AS = rootKey + "."
            + ImageConstants.WRITE_IMAGES_AS;

    private static final UserProperties defaultProperties = new UserProperties();
    static {
        defaultProperties.setProperty(TRANSPARENT, true);
        defaultProperties.setProperty(BACKGROUND, false);
        defaultProperties.setProperty(BACKGROUND_COLOR, Color.GRAY);
        defaultProperties.setProperty(WRITE_IMAGES_AS, ImageConstants.SMALLEST);
        // Seems not to work yet...
        defaultProperties.setProperty(CLIP, false);
    }

    public static Properties getDefaultProperties() {
        return defaultProperties;
    }

    public static void setDefaultProperties(Properties newProperties) {
        defaultProperties.setProperties(newProperties);
    }

    public final static String version = "$Revision$";

    private OutputStream ros;

    private SWFOutputStream os;

    private Value id;

    private Value depth;

    private static final float frameRate = 20.0f;

    private boolean compress = true;

    private LineStyleArray lineStyles;

    private FillStyleArray fillStyles;

    // keeps the color for text, which cannot be a paint
    private Color textColor;

    // keeps the last clip which has not been written yet, its ID and depth
    private Shape unwrittenClip = null;

    private AffineTransform clipTransform = null;

    private int clipID, clipDepthID, showClipID, showClipDepthID;

    // for rendering shapes
    boolean fillStroke;

    // for debugging
    private static final boolean showBounds = false;

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

    /*
     * ================================================================================
     * 1. Constructors & Factory Methods
     * ================================================================================
     */
    public SWFGraphics2D(File file, Dimension size)
            throws FileNotFoundException {
        this(new FileOutputStream(file), size);
    }

    public SWFGraphics2D(File file, Component component)
            throws FileNotFoundException {
        this(new FileOutputStream(file), component);
    }

    public SWFGraphics2D(OutputStream os, Dimension size) {
        super(size, true);
        init(os);
    }

    public SWFGraphics2D(OutputStream os, Component component) {
        super(component, true);
        init(os);
    }

    private void init(OutputStream os) {
        initProperties(getDefaultProperties());
        ros = os;
        id = new Value().set(1);
        depth = new Value().set(1);
        textColor = getColor();
        fillStroke = false;
    }

    protected SWFGraphics2D(SWFGraphics2D graphics, boolean doRestoreOnDispose) {
        super(graphics, doRestoreOnDispose);
        // Create a graphics context from a given graphics context.
        // This constructor is used by the system to clone a given graphics
        // context.
        // doRestoreOnDispose is used to call writeGraphicsRestore(),
        // when the graphics context is being disposed off.
        os = graphics.os;
        id = graphics.id;
        depth = graphics.depth;
        lineStyles = new LineStyleArray();
        lineStyles.add(graphics.lineStyles.get(0));
        if (showBounds || isProperty(CLIP)) {
            lineStyles.add(graphics.lineStyles.get(1));
            lineStyles.add(graphics.lineStyles.get(2));
        }
        fillStyles = new FillStyleArray();
        fillStyles.add(graphics.fillStyles.get(0));
        textColor = graphics.textColor;
        fillStroke = graphics.fillStroke;
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
        os = new SWFOutputStream(new BufferedOutputStream(ros), getSize(),
                frameRate, compress);

        String description = getCreator() + ":" + getClass().getName();
        if (!isDeviceIndependent()) {
            description += " " + version.substring(1, version.length() - 1);
        }
        // FIXME no way to write out a comment...
    }

    public void writeBackground() throws IOException {
        if (isProperty(TRANSPARENT)) {
            setBackground(null);
        } else if (isProperty(BACKGROUND)) {
            setBackground(getPropertyColor(BACKGROUND_COLOR));
            clearRect(0.0, 0.0, getSize().width, getSize().height);
        } else {
            setBackground(getComponent() != null ? getComponent()
                    .getBackground() : Color.WHITE);
            clearRect(0.0, 0.0, getSize().width, getSize().height);
        }
    }

    public void writeTrailer() throws IOException {
        os.writeTag(new ShowFrame());
        os.writeTag(new End());
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
        return new SWFGraphics2D(this, true);
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
        VectorGraphics graphics = new SWFGraphics2D(this, true);
        graphics.translate(x, y);
        graphics.clipRect(0, 0, width, height);
        return graphics;
    }

    protected void writeGraphicsSave() throws IOException {
    }

    protected void writeGraphicsRestore() throws IOException {
        popStreamAndWriteClip();
    }

    /*
     * ================================================================================ |
     * 5. Drawing Methods
     * ================================================================================
     */
    /* 5.1.4. shapes */

    public void draw(Shape shape) {
        Shape strokedShape = getStroke().createStrokedShape(shape);
        if (fillStroke) {
            // do this for dashed lines and non-round linejoins and linecaps
            fill(new Area(strokedShape));
            return;
        }

        try {
            Rectangle2D bounds = strokedShape.getBounds2D();
            SWFShape swfShape = createShape(shape, 1, 0, -1);
            os.writeTag(new DefineShape3(id.getInt(), bounds, fillStyles,
                    lineStyles, swfShape));
            os.writeTag(new PlaceObject(id.getInt(), depth.getInt(),
                    getTransform()));
            id.set(id.getInt() + 1);
            depth.set(depth.getInt() + 1);

            if (showBounds) {
                SWFShape swfBounds = createShape(bounds, 2, 0, -1);
                os.writeTag(new DefineShape3(id.getInt(), bounds, fillStyles,
                        lineStyles, swfBounds));
                os.writeTag(new PlaceObject(id.getInt(), depth.getInt(),
                        getTransform()));
                id.set(id.getInt() + 1);
                depth.set(depth.getInt() + 1);
            }

        } catch (IOException e) {
            handleException(e);
        }
    }

    public void fill(Shape shape) {
        try {
            Rectangle2D bounds = new BasicStroke().createStrokedShape(shape)
                    .getBounds2D();
            SWFShape swfShape = createShape(shape, 0, 1, -1);
            os.writeTag(new DefineShape3(id.getInt(), bounds, fillStyles,
                    lineStyles, swfShape));
            os.writeTag(new PlaceObject(id.getInt(), depth.getInt(),
                    getTransform()));
            id.set(id.getInt() + 1);
            depth.set(depth.getInt() + 1);

            if (showBounds) {
                SWFShape swfBounds = createShape(bounds, 2, 0, -1);
                os.writeTag(new DefineShape3(id.getInt(), bounds, fillStyles,
                        lineStyles, swfBounds));
                os.writeTag(new PlaceObject(id.getInt(), depth.getInt(),
                        getTransform()));
                id.set(id.getInt() + 1);
                depth.set(depth.getInt() + 1);
            }

            boolean eo = SWFPathConstructor.isEvenOdd(shape);
            if (!eo) {
                writeWarning(getClass()
                        + ": cannot fill using non-zero winding rule, used even-odd instead.");
            }
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void fillAndDraw(Shape shape, Color fillColor) {
        try {
            setFillColor(fillColor);
            fill(shape);
            setFillColor(getColor());
            draw(shape);
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

    protected void writeImage(RenderedImage image, AffineTransform xform,
            Color bkg) throws IOException {

        // define image
        int imageID = id.getInt();
        os.writeTag(getImageTag(imageID, image, bkg));
        id.set(id.getInt() + 1);

        // define shape for image
        Shape shape = xform.createTransformedShape(new Rectangle(0, 0, image
                .getWidth(), image.getHeight()));

        AffineTransform imageTransform = new AffineTransform(TWIPS, 0, 0,
                TWIPS, 0, 0);
        xform.concatenate(imageTransform);

        // create fill from shape
        SWFShape imageShape = createShape(shape, 0, 0, 1);
        FillStyleArray imageFill = new FillStyleArray();
        imageFill.add(new FillStyle(imageID, false, xform));

        LineStyleArray imageLine = new LineStyleArray();
        Rectangle bounds = shape.getBounds();
        os.writeTag(new DefineShape3(id.getInt(), bounds, imageFill, imageLine,
                imageShape));

        // place image
        os.writeTag(new PlaceObject2(id.getInt(), depth.getInt(),
                getTransform()));
        id.set(id.getInt() + 1);
        depth.set(depth.getInt() + 1);

        if (showBounds) {
            SWFShape swfBounds = createShape(bounds, 2, 0, -1);
            os.writeTag(new DefineShape3(id.getInt(), bounds, fillStyles,
                    lineStyles, swfBounds));
            os.writeTag(new PlaceObject(id.getInt(), depth.getInt(),
                    getTransform()));
            id.set(id.getInt() + 1);
            depth.set(depth.getInt() + 1);
        }
    }

    private final static Properties replaceFonts = new Properties();
    static {
        replaceFonts.setProperty("Symbol", "Serif");
        replaceFonts.setProperty("ZapfDingbats", "Serif");
    }

    /* 5.3. Strings */
    protected void writeString(String string, double x, double y)
            throws IOException {
        // for special fonts (Symbol, ZapfDingbats) we choose a standard font
        // and
        // encode using unicode.
        String fontName = getFont().getName();
        string = FontEncoder.getEncodedString(string, fontName);
        fontName = replaceFonts.getProperty(fontName, null);

        Font font = (fontName == null) ? getFont() : new Font(fontName,
                getFont().getStyle(), getFont().getSize());
        Font font1024 = font.deriveFont((float) (1024.0 / TWIPS));

        GlyphVector glyphs = font1024.createGlyphVector(getFontRenderContext(),
                string);
        Rectangle2D bounds = font.createGlyphVector(getFontRenderContext(),
                string).getVisualBounds();
        bounds.setRect(bounds.getX() + x, bounds.getY() + y, bounds.getWidth(),
                bounds.getHeight());

        // define the Glyphs
        int fontID = id.getInt();
        DefineFont swfFont = new DefineFont(fontID);
        id.set(id.getInt() + 1);

        // define Text
        Vector text = new Vector();
        DefineText2.RecordType1 record1 = new DefineText2.RecordType1(fontID,
                textColor, (int) (x * TWIPS), (int) (y * TWIPS), (int) (font
                        .getSize2D() * TWIPS));
        DefineText2.RecordType0 record0 = new DefineText2.RecordType0();
        text.add(record1);
        text.add(record0);
        int textID = id.getInt();
        DefineText2 swfText = new DefineText2(textID, bounds,
                new AffineTransform(), text);
        id.set(id.getInt() + 1);

        // NOTE: SWF 6 does not seem to support Text filled with gradients.
        // if fillStyle 1 is a Gradient, replace it with Black
        FillStyle nonSolidFillStyle = null;
        if (fillStyles.get(0).getType() != FillStyle.SOLID) {
            nonSolidFillStyle = fillStyles.get(0);
            fillStyles = new FillStyleArray();
            fillStyles.add(new FillStyle(Color.BLACK));
        }

        // hook font and text
        for (int i = 0; i < glyphs.getNumGlyphs(); i++) {
            // add filled shapes to font
            swfFont.add(createShape(glyphs.getGlyphOutline(i), -1, 1, -1));

            // add glyphs to text
            // float advance = glyphs.getGlyphMetrics(i).getAdvance();
            record0.add(new DefineText2.GlyphEntry(i, 0)); // (int)(advance*TWIPS)));
        }

        // write font and text
        os.writeTag(swfFont);
        os.writeTag(swfText);

        // place String
        os.writeTag(new PlaceObject2(textID, depth.getInt(), getTransform()));
        depth.set(depth.getInt() + 1);

        // Special case for Gradient: put original fill style back
        if (nonSolidFillStyle != null) {
            fillStyles = new FillStyleArray();
            fillStyles.add(nonSolidFillStyle);
        }

        if (showBounds) {
            SWFShape swfBounds = createShape(bounds, 2, 0, -1);
            os.writeTag(new DefineShape3(id.getInt(), bounds, fillStyles,
                    lineStyles, swfBounds));
            os.writeTag(new PlaceObject(id.getInt(), depth.getInt(),
                    getTransform()));
            id.set(id.getInt() + 1);
            depth.set(depth.getInt() + 1);
        }
    }

    /*
     * ================================================================================ |
     * 6. Transformations
     * ================================================================================
     */

    protected void writeTransform(AffineTransform t) throws IOException {
        // Transforms written when needed
    }

    protected void writeSetTransform(AffineTransform t) throws IOException {
        // Transforms written when needed
    }

    /*
     * ================================================================================ |
     * 7. Clipping
     * ================================================================================
     */
    protected void writeSetClip(Shape s) throws IOException {
        writeClip(s);
    }

    protected void writeClip(Shape s) throws IOException {
        // we assume we can write nested clips
        popStreamAndWriteClip();

        unwrittenClip = s;
        clipTransform = (unwrittenClip != null) ? new AffineTransform(
                getTransform()) : null;

        if (unwrittenClip != null) {
            // reserve IDs
            clipID = id.getInt();
            id.set(id.getInt() + 1);
            clipDepthID = depth.getInt();
            depth.set(depth.getInt() + 1);

            if (isProperty(CLIP)) {
                showClipID = id.getInt();
                id.set(id.getInt() + 1);
                showClipDepthID = depth.getInt();
                depth.set(depth.getInt() + 1);
            }

            os.pushBuffer();
        } else {
            clipID = 0;
            clipDepthID = 0;

            if (isProperty(CLIP)) {
                showClipID = 0;
                showClipDepthID = 0;
            }
        }
    }

    /*
     * ================================================================================ |
     * 8. Graphics State
     * ================================================================================
     */
    protected void writeStroke(Stroke stroke) throws IOException {
        fillStroke = true;
        if (stroke instanceof BasicStroke) {
            BasicStroke bs = (BasicStroke) stroke;
            setPen(bs, getColor());
            if ((bs.getLineJoin() == BasicStroke.JOIN_ROUND)
                    && (bs.getEndCap() == BasicStroke.CAP_ROUND)
                    && ((bs.getDashArray() == null) || (bs.getDashArray().length <= 1))) {
                fillStroke = false;
            }
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
        setPen((BasicStroke) getStroke(), p);
        setFillColor(p);
        textColor = PrintColor.createPrintColor(p);
    }

    protected void writePaint(GradientPaint p) throws IOException {
        Gradient[] gradient = new Gradient[2];
        gradient[0] = new Gradient(0, p.getColor1());
        gradient[1] = new Gradient(255, p.getColor2());
        double x0 = p.getPoint1().getX();
        double y0 = p.getPoint1().getY();
        double dx = p.getPoint2().getX() - x0;
        double dy = p.getPoint2().getY() - y0;
        double scale = p.getPoint1().distance(p.getPoint2()) * TWIPS / 32768;
        double angle = Math.atan2(dy, dx);
        AffineTransform transform = new AffineTransform(scale, 0, 0, scale, dx
                / 2 + x0, dy / 2 + y0);
        transform.rotate(angle);

        fillStyles = new FillStyleArray();
        fillStyles.add(new FillStyle(gradient, true, transform));

        textColor = PrintColor.mixColor(p.getColor1(), p.getColor2());
    }

    protected void writePaint(TexturePaint p) throws IOException {
        // define image
        BufferedImage image = p.getImage();
        int imageID = id.getInt();
        os.writeTag(getImageTag(imageID, image, null));
        id.set(id.getInt() + 1);

        // setup image as fill
        Rectangle2D a = p.getAnchorRect();
        double sx = a.getWidth() / image.getWidth();
        double sy = a.getHeight() / image.getHeight();
        fillStyles = new FillStyleArray();
        fillStyles.add(new FillStyle(imageID, true, new AffineTransform(sx
                * TWIPS, 0, 0, sy * TWIPS, a.getX(), a.getY())));
        textColor = PrintColor.black;
    }

    protected void writePaint(Paint p) throws IOException {
        writeWarning(getClass() + ": writePaint(Paint) not implemented for "
                + p.getClass());
        // Write out the paint.
        writePaint(Color.BLACK);
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

    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        writeWarning(getClass()
                + ": hit(Rectangle, Shape, boolean) not implemented.");
        // Mostly unimplemented
        return false;
    }

    public void writeComment(String comment) throws IOException {
        writeWarning(getClass() + ": writeComment(String) not implemented.");
        // Write out the comment.
    }

    public String toString() {
        return "SWFGraphics2D";
    }

    /*
     * Private methods
     */
    SWFShape createShape(Shape shape, int stroke, int fill0, int fill1)
            throws IOException {
        // use a resolution compatible with the current transform
        AffineTransform t = getTransform();
        double resolution = 0.5 / (TWIPS * Math.min(t.getScaleX(), t
                .getScaleY()));

        Vector path = new Vector();
        SWFPathConstructor pc = new SWFPathConstructor(path, stroke, fill0,
                fill1, resolution);
        pc.addPath(shape);
        return new SWFShape(path);
    }

    private void setPen(BasicStroke stroke, Color color) throws IOException {
    	System.err.println("LW: "+stroke.getLineWidth());
        lineStyles = new LineStyleArray();
        lineStyles.add(new LineStyle((int) (stroke.getLineWidth() * TWIPS),
                getPrintColor(color)));

        if (showBounds || isProperty(CLIP)) {
            lineStyles.add(new LineStyle(TWIPS, Color.cyan));
            lineStyles.add(new LineStyle(TWIPS, Color.orange));
        }
    }

    private void setFillColor(Color color) throws IOException {
        fillStyles = new FillStyleArray();
        fillStyles.add(new FillStyle(getPrintColor(color)));
    }

    private void popStreamAndWriteClip() throws IOException {
        if (unwrittenClip == null)
            return;

        if ((clipID == 0) || (clipDepthID == 0)) {
            System.err
                    .println("SWFGraphics2D: internal error, invalid clipID or clipDepthID");
            return;
        }

        // pop buffer
        os.popBuffer();

        Rectangle2D bounds = unwrittenClip.getBounds2D();
        SWFShape clipShape = createShape(unwrittenClip, 0, 1, -1);
        os.writeTag(new DefineShape3(clipID, bounds, fillStyles, lineStyles,
                clipShape));

        int clipDepth = depth.getInt() - 1;
        os.writeTag(new PlaceObject2(clipID, clipDepthID, clipTransform,
                clipDepth));

        // System.out.println("Clip "+clipID+" at depth "+clipDepthID+" for
        // clipDepth "+clipDepth+": "+unwrittenClip);

        if (isProperty(CLIP)) {
            if ((showClipID == 0) || (showClipDepthID == 0)) {
                System.err
                        .println("SWFGraphics2D: internal error, invalid showClipID or showClipDepthID");
                return;
            }

            SWFShape swfBounds = createShape(bounds, 3, 0, -1);
            os.writeTag(new DefineShape3(showClipID, bounds, fillStyles,
                    lineStyles, swfBounds));
            os.writeTag(new PlaceObject(showClipID, showClipDepthID,
                    clipTransform));
        }

        // append popped buffer;
        os.append();
    }

    private SWFTag getImageTag(int imageID, RenderedImage image, Color bkg)
            throws IOException {
        String writeAs = getProperty(WRITE_IMAGES_AS);

        DefineBitsLossless2 flateTag = null;
        if (writeAs.equals(ImageConstants.ZLIB)
                || writeAs.equals(ImageConstants.SMALLEST)) {
            flateTag = new DefineBitsLossless2(imageID, image, bkg);
        }

        DefineBitsJPEG3 jpgTag = null;
        if (writeAs.equals(ImageConstants.JPG)
                || writeAs.equals(ImageConstants.SMALLEST)) {
            jpgTag = new DefineBitsJPEG3(imageID, image, bkg, new Properties());
        }

        SWFTag imageTag;
        if (writeAs.equals(ImageConstants.ZLIB)) {
            imageTag = flateTag;
        } else if (writeAs.equals(ImageConstants.JPG)) {
            imageTag = jpgTag;
        } else {
            imageTag = (jpgTag.getLength() < 0.5 * flateTag.getLength()) ? (SWFTag) jpgTag
                    : (SWFTag) flateTag;
        }

        return imageTag;
    }
}
