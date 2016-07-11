// Copyright 2007 FreeHEP
package org.freehep.graphicsio.ps;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.freehep.graphics2d.TagString;
import org.freehep.graphics2d.font.FontUtilities;
import org.freehep.graphicsbase.util.ScientificFormat;
import org.freehep.graphicsbase.util.UserProperties;
import org.freehep.graphicsbase.util.images.ImageUtilities;
import org.freehep.graphicsio.AbstractVectorGraphicsIO;
import org.freehep.graphicsio.FontConstants;
import org.freehep.graphicsio.ImageConstants;
import org.freehep.graphicsio.ImageGraphics2D;
import org.freehep.graphicsio.InfoConstants;
import org.freehep.graphicsio.PageConstants;

/**
 * 
 * @author duns
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/AbstractPSGraphics2D.java bed5e3a39f35 2007/09/10 18:13:00 duns $
 */
public abstract class AbstractPSGraphics2D extends AbstractVectorGraphicsIO implements FontUtilities.ShowString {
    private static final String rootKey = PSGraphics2D.class.getName();

    public static final String BACKGROUND = rootKey + "."
            + PageConstants.BACKGROUND;

    public static final String BACKGROUND_COLOR = rootKey + "."
            + PageConstants.BACKGROUND_COLOR;

    /**
     * Property name for setting the size of the pages output by this {@code AbstractPSGraphics2D}. 
     * <p>
     * To set a pre-defined page size, set this property to one of the page size constants defined
     * in {@link PageConstants} - for instance, {@linkplain PageConstants#A4},
     * {@linkplain PageConstants#LETTER}.
     * <p>
     * To set a custom page size, set this property to the value {@link CUSTOM_PAGE_SIZE}.  Then,
     * set the {@link CUSTOM_PAGE_SIZE} property to a {@link Dimension} object.
     * @see UserProperties
     */
    public static final String PAGE_SIZE = rootKey + "."
            + PageConstants.PAGE_SIZE;

    /**
     * Property name (and value!) for setting a custom page size.
     * <p>
     * If the {@link PAGE_SIZE} property is set to THIS VALUE, then the properties are consulted
     * for the value of THIS KEY.  The value of this key should be a {@link Dimension} object,
     * in the "user coordinates" of Java2D.
     * 
     */
    public static final String CUSTOM_PAGE_SIZE = rootKey + "."
            + PageConstants.CUSTOM_PAGE_SIZE;

    /**
     * Set this to an {@link Insets} object to set the page margins.
     */
    public static final String PAGE_MARGINS = rootKey + "."
            + PageConstants.PAGE_MARGINS;

    public static final String ORIENTATION = rootKey + "."
            + PageConstants.ORIENTATION;

    public static final String FIT_TO_PAGE = rootKey + "."
            + PageConstants.FIT_TO_PAGE;

    public static final String EMBED_FONTS = rootKey + "."
            + FontConstants.EMBED_FONTS;

    public static final String EMBED_FONTS_AS = rootKey + "."
            + FontConstants.EMBED_FONTS_AS;

    public static final String FOR = rootKey + "." + InfoConstants.FOR;

    public static final String TITLE = rootKey + "." + InfoConstants.TITLE;

    public static final String PREVIEW = rootKey + ".Preview";

    public static final String PREVIEW_BITS = rootKey + ".PreviewBits";

    public static final String WRITE_IMAGES_AS = rootKey + "."
            + ImageConstants.WRITE_IMAGES_AS;

    private static final UserProperties defaultProperties = new UserProperties();
    static {
        defaultProperties.setProperty(BACKGROUND, false);
        defaultProperties.setProperty(BACKGROUND_COLOR, Color.GRAY);

        defaultProperties.setProperty(PAGE_SIZE, PageConstants.INTERNATIONAL);
        defaultProperties.setProperty(PAGE_MARGINS, PageConstants
                .getMargins(PageConstants.SMALL));
        defaultProperties.setProperty(ORIENTATION, PageConstants.PORTRAIT);
        defaultProperties.setProperty(FIT_TO_PAGE, true);
        defaultProperties.setProperty(EMBED_FONTS, false);
        defaultProperties.setProperty(TEXT_AS_SHAPES, false);
        defaultProperties.setProperty(EMBED_FONTS_AS,
                FontConstants.EMBED_FONTS_TYPE3);

        defaultProperties.setProperty(FOR, "");
        defaultProperties.setProperty(TITLE, "");

        defaultProperties.setProperty(PREVIEW, false);
        defaultProperties.setProperty(PREVIEW_BITS, 8);

        defaultProperties.setProperty(WRITE_IMAGES_AS, ImageConstants.SMALLEST);

        defaultProperties.setProperty(CLIP, true);
        defaultProperties.setProperty(TEXT_AS_SHAPES, true);
    }

    public static Properties getDefaultProperties() {
        return defaultProperties;
    }

    public static final String version = "$Revision: 12753 $";

    public static final int LEVEL_2 = 2;

    public static final int LEVEL_3 = 3;

    // remember which fonts are used
    private PSFontTable fontTable;

    // The private writer used for this file.
    protected OutputStream ros;

    protected PrintStream os;

    private int postscriptLevel = LEVEL_3;

	public AbstractPSGraphics2D(Dimension size, boolean doRestoreOnDispose) {
		super(size, doRestoreOnDispose);
	}

	public AbstractPSGraphics2D(Component component, boolean doRestoreOnDispose) {
		super(component, doRestoreOnDispose);
	}

    /**
     * This protected method is used by the create() methods to create a clone
     * of the given graphics object.
     *
     * @param graphics Parent graphics to take attributes from
     * @param doRestoreOnDispose if true writeGraphicsRestore() is called on dispose()
     */
    protected AbstractPSGraphics2D(AbstractPSGraphics2D graphics, boolean doRestoreOnDispose) {

        super(graphics, doRestoreOnDispose);

        // Now initialize the new object.
        ros = graphics.ros;
        os = graphics.os;
        fontTable = graphics.fontTable;
    }

    protected void init(OutputStream os) {
        ros = new BufferedOutputStream(os);
        initProperties(defaultProperties);
        fontTable = new PSFontTable(ros, getFontRenderContext());
    }

    /**
     * Set the clipping enabled flag. This will affect all output operations
     * after this call completes.
     *
     * @param enabled true enables clipping
     */
    public static void setClipEnabled(boolean enabled) {
        defaultProperties.setProperty(CLIP, enabled);
    }

    /**
     * Write out the header of this EPS file.
     * @param prolog name of the Java resource containing the 
     * prolog. Must be reachable by the ClassLoader.
     */
    public void writeHeader(String prolog) throws IOException {
        String producer = getClass().getName();
        if (!isDeviceIndependent()) {
            producer += " " + version.substring(1, version.length() - 1);
        }
        os.println("%%Creator: " + getCreator());
        os.println("%%Producer: " + producer);
        os.println("%%For: " + getProperty(FOR));
        os.println("%%Title: " + getProperty(TITLE));
        if (!isDeviceIndependent()) {
            os.println("%%CreationDate: "
                    + DateFormat.getDateTimeInstance(DateFormat.FULL,
                            DateFormat.FULL).format(new Date()));
        }
        os.println("%%LanguageLevel: " + postscriptLevel);
        os.println("%%EndComments");

        // write preview if possible
        if (isProperty(PREVIEW) && (getComponent() != null)) {
            Rectangle size = getComponent().getBounds();
            BufferedImage image = new BufferedImage(size.width, size.height,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics imageGraphics = image.getGraphics();
            getComponent().print(imageGraphics);

            EPSIEncoder encoder = new EPSIEncoder(image, ros,
                    getPropertyInt(PREVIEW_BITS), getProperty(ORIENTATION)
                            .equals(PageConstants.PORTRAIT));
            encoder.encode();
        }

        // The prolog is provided as a Java resource.  It is 
        // simply copied into the output file.
        os.println("%%BeginProlog");
        copyResourceTo(this, prolog, os);
        os.println("%%EndProlog");
        os.println();
    }

    /**
     * Write out the header of this EPS file. Use the default 
     * PSProlog.txt file provided with this package.
     */
    public void writeHeader() throws IOException {
        // the default prolog is kept in a file PSProlog.txt in the 
        // same area as this class definition.
        writeHeader("PSProlog.txt");
    }

    public void writeBackground() throws IOException {
        // since PS is non-transparent, write current background
        if (isProperty(BACKGROUND)) {
            setBackground(getPropertyColor(BACKGROUND_COLOR));
            clearRect(0.0, 0.0, getSize().width, getSize().height);
        } else {
            setBackground(getComponent() != null ? getComponent()
                    .getBackground() : Color.WHITE);
            clearRect(0.0, 0.0, getSize().width, getSize().height);
        }
    }

    public void writeTrailer() throws IOException {
        os.println();
        os.println("%%Trailer");
    }

    public void closeStream() throws IOException {
        ros.close();
        os.close();
    }


    /**
     * Reads the PAGE_SIZE Property. If A4 .. A0 is found,
     * PageConstants will determine the size. If CUSTOM_PAGE_SIZE
     * is found, CUSTOM_PAGE_SIZE is used as a Property key for
     * a dimension object.
     *
     * @return Size of page
     */
    protected Dimension getPageSize() {
        // A4 ... A0 or PageConstants.CUSTOM_PAGE_SIZE expected
        // if PageConstants.CUSTOM_PAGE_SIZE is found,
        // PageConstants.CUSTOM_PAGE_SIZE is used as Key too
        String pageSizeProperty = getProperty(PAGE_SIZE);

        // determine page size
        Dimension result = CUSTOM_PAGE_SIZE.equals(pageSizeProperty) ?
            getPropertyDimension(CUSTOM_PAGE_SIZE) :
            PageConstants.getSize(getProperty(PAGE_SIZE));

        // set a default value
        if (result == null) {
            result = PageConstants.getSize(PageConstants.A4);
        }

        return result;
    }

    protected void openPage(Dimension size, String title, Component component) {
        if (size == null) {
            size = component.getSize();
        }
        resetClip(new Rectangle(0, 0, size.width, size.height));

        // Our PS Header has internal page orientation mode, all sizes given in
        // portrait
        Dimension pageSize = getPageSize();
        Insets margins = getPropertyInsets(PAGE_MARGINS);

        os.println("save");
        os.println("procDict begin");
        os.println("printColorMap begin");
        os.println(pageSize.width + " " + pageSize.height + " setpagesize");
        os.println(margins.left + " " + margins.bottom + " " + margins.top
                + " " + margins.right + " setmargins");
        os.println("0 0 setorigin");
        os.println(size.width + " " + size.height + " setsize");
        os.println(isProperty(FIT_TO_PAGE) ? "fittopage" : "naturalsize");
        os.println(getProperty(ORIENTATION).equals(PageConstants.PORTRAIT) ? "portrait" : "landscape");
        os.println("imagescale");
        os.println("cliptobounds");
        os.println("setbasematrix");
        os.println("/Helvetica 10 sf");
        os.println("defaultGraphicsState");
    }

    public void setHeader(Font font, TagString left, TagString center,
            TagString right, int underlineThickness) {
    }

    public void setFooter(Font font, TagString left, TagString center,
            TagString right, int underlineThickness) {
    }

    /**
     * Embed a gsave in the PostScript file.
     */
    protected void writeGraphicsSave() throws IOException {
        os.println("q");
    }

    /**
     * Embed a grestore in the PostScript file.
     */
    protected void writeGraphicsRestore() throws IOException {
        os.println("Q");
    }

    public void draw(Shape shape) {
        try {
            if (getStroke() instanceof BasicStroke) {
                writePath(shape);
                os.println("S");
            } else {
                boolean eofill = writePath(getStroke().createStrokedShape(shape));
                os.println(((eofill) ? "f*" : "f"));
            }
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void fill(Shape shape) {
        try {
            if (getPaint() instanceof Color || (getPaint() instanceof GradientPaint && postscriptLevel >= LEVEL_3)) {
                boolean eofill = writePath(shape);
                os.println(((eofill) ? "f*" : "f"));
            } else {
                // setting the color seems to be needed by PS
                writePaint(Color.black);
                fill(shape, getPaint());
            }
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void fillAndDraw(Shape shape, Color fillColor) {
        try {
            if (getPaint() instanceof Color || (getPaint() instanceof GradientPaint && postscriptLevel >= LEVEL_3)) {
                setPSColor(fillColor, true);
                boolean eofill = writePath(shape);
                os.println(((eofill) ? "B*" : "B"));
            } else {
                // setting the color seems to be needed by PS
                writePaint(Color.black);
                fill(shape, getPaint());
            }
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        writeWarning(getClass()
                + ": copyArea(int, int, int, int, int, int) not implemented.");
    }

    protected void writeImage(RenderedImage image, AffineTransform xform,
            Color bkg) throws IOException {

        // save complete gstate, not only color space
        writeGraphicsSave();

        // FIXME FVG-31

        if (bkg == null) {
            bkg = getBackground();
        }
        image = ImageUtilities.createRenderedImage(image, bkg);

        // Write out the PostScript code to start an image
        // definition.
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        AffineTransform imageTransform = new AffineTransform(
            imageWidth, 0.0, 0.0, imageHeight, 0.0, 0.0);
        xform.concatenate(imageTransform);

        writeTransform(xform);
        os.println("<<");
        os.println("/ImageType 1");
        os.println("/Width " + imageWidth + "  /Height " + imageHeight);
        os.println("/BitsPerComponent 8");
        os.println("/Decode [0 1 0 1 0 1]");
        os.println("/ImageMatrix [" + imageWidth + " 0 0 " + imageHeight
                + " 0 0]");

        // read image format
        String writeAs = getProperty(WRITE_IMAGES_AS);

        // used for creating
        //    /DataSource currentfile /ASCII85Decode filter /"encode"Decode filter
        //    >> image "imageBytes"
        String encode;
        byte[] imageBytes;

        // write as RAW with ZIP compression
        if (ImageConstants.ZLIB.equalsIgnoreCase(writeAs)) {
            encode = ImageConstants.ENCODING_FLATE;
            imageBytes = ImageGraphics2D.toByteArray(
                image,
                ImageConstants.RAW,
                ImageConstants.ENCODING_FLATE_ASCII85,
                ImageGraphics2D.getRAWProperties(bkg, ImageConstants.COLOR_MODEL_RGB));
        }

        // write as JPEG
        else if (ImageConstants.JPG.equalsIgnoreCase(writeAs)) {
            encode = ImageConstants.ENCODING_DCT;
            imageBytes = ImageGraphics2D.toByteArray(
                image,
                ImageConstants.JPG,
                ImageConstants.ENCODING_ASCII85,
                null);
        }

        // write SMALLEST (JPEG or RAW)
        else {
            // zip-compressed raw image
            byte[] flateBytes = ImageGraphics2D.toByteArray(
                image,
                ImageConstants.RAW,
                ImageConstants.ENCODING_FLATE_ASCII85,
                ImageGraphics2D.getRAWProperties(bkg, ImageConstants.COLOR_MODEL_RGB));

            // jpeg image DCT encoded
            byte[] jpgBytes = ImageGraphics2D.toByteArray(
                image,
                ImageConstants.JPG,
                ImageConstants.ENCODING_ASCII85,
                null);

            // define encoding and imagebytes
            if (jpgBytes.length < 0.5 * flateBytes.length) {
                encode = ImageConstants.ENCODING_DCT;
                imageBytes = jpgBytes;
            } else {
                encode = ImageConstants.ENCODING_FLATE;
                imageBytes = flateBytes;
            }
        }

        os.println("/DataSource currentfile " + "/ASCII85Decode filter " + "/"
                + encode + "Decode filter ");
        os.println(">> image");

        os.write(imageBytes);

        os.println("");

        writeGraphicsRestore();
    }

    protected void writeString(String str, double x, double y)
            throws IOException {
        showCharacterCodes(str, x, y);
    }

    protected void writeTransform(AffineTransform tx) throws IOException {
        os.println("[ " + fixedPrecision(tx.getScaleX()) + " "
                + fixedPrecision(tx.getShearY()) + " "
                + fixedPrecision(tx.getShearX()) + " "
                + fixedPrecision(tx.getScaleY()) + " "
                + fixedPrecision(tx.getTranslateX()) + " "
                + fixedPrecision(tx.getTranslateY()) + " ] concat");
    }

    protected void writeSetTransform(AffineTransform tx) throws IOException {
        os.println("[ " + fixedPrecision(tx.getScaleX()) + " "
                + fixedPrecision(tx.getShearY()) + " "
                + fixedPrecision(tx.getShearX()) + " "
                + fixedPrecision(tx.getScaleY()) + " "
                + fixedPrecision(tx.getTranslateX()) + " "
                + fixedPrecision(tx.getTranslateY())
                + " ] defaultmatrix matrix concatmatrix setmatrix");
    }

    protected void writeClip(Shape s) throws IOException {
        if (s == null || !isProperty(CLIP)) {
            return;
        }
        if (s instanceof Rectangle) {
            os.println(
                    ((Rectangle)s).x+" "+
                    ((Rectangle)s).y+" "+
                    ((Rectangle)s).width+" "+
                    ((Rectangle)s).height+" rc");
        } else if (s instanceof Rectangle2D) {
            os.println(
                    fixedPrecision(((Rectangle2D)s).getX())+" "+
                    fixedPrecision(((Rectangle2D)s).getY())+" "+
                    fixedPrecision(((Rectangle2D)s).getWidth())+" "+
                    fixedPrecision(((Rectangle2D)s).getHeight())+" rc");
        } else {
            boolean eofill = writePath(s);
            os.println(((eofill) ? "W*" : "W"));
        }
    }

    protected void writeSetClip(Shape s) throws IOException {
        os.println("cliprestore");
        writeClip(s);
    }

    /**
     * Write the path of the current shape to the output file. Return a boolean
     * indicating whether or not the even-odd rule for filling should be used.
     *
     * @return path by PSPathConstructor
     * @param s Shape to convert
     * @throws java.io.IOException thrown by created path
     */
    private boolean writePath(Shape s) throws IOException {
        os.println("newpath");
        PSPathConstructor path = new PSPathConstructor(os, true, false);
        return path.addPath(s);
    }

    protected void writeWidth(float width) throws IOException {
        os.println(fixedPrecision(width) + " w");
    }

    protected void writeCap(int cap) throws IOException {
        switch (cap) {
        default:
        case BasicStroke.CAP_BUTT:
            os.println("0 J");
            break;
        case BasicStroke.CAP_ROUND:
            os.println("1 J");
            break;
        case BasicStroke.CAP_SQUARE:
            os.println("2 J");
            break;
        }
    }

    protected void writeJoin(int join) throws IOException {
        switch (join) {
        default:
        case BasicStroke.JOIN_MITER:
            os.println("0 j");
            break;
        case BasicStroke.JOIN_ROUND:
            os.println("1 j");
            break;
        case BasicStroke.JOIN_BEVEL:
            os.println("2 j");
            break;
        }
    }

    protected void writeMiterLimit(float limit) throws IOException {
        os.println(fixedPrecision(limit) + " M");
    }

    protected void writeDash(float[] dash, float phase) throws IOException {
        os.print("[ ");
        for (int i = 0; i < dash.length; i++) {
            os.print(fixedPrecision(dash[i]) + " ");
        }
        os.println("] " + fixedPrecision(phase) + " d");
    }

    public void setPaintMode() {
        writeWarning(getClass() + ": setPaintMode() not implemented.");
    }

    public void setXORMode(Color c1) {
        writeWarning(getClass() + ": setXORMode(Color) not implemented.");
    }

    protected void writePaint(Color c) throws IOException {
        setPSColor(c, false);
    }

    protected void writePaint(GradientPaint paint) throws IOException {
        if (postscriptLevel >= LEVEL_3) {
            float[] rgb1 = paint.getColor1().getRGBColorComponents(null);
            float[] rgb2 = paint.getColor2().getRGBColorComponents(null);
            Point2D p1 = paint.getPoint1();
            Point2D p2 = paint.getPoint2();
            os.println("<< /PatternType 2");
            os.println("   /Shading");
            os.println("   << /ShadingType 2");
            os.println("      /ColorSpace /DeviceRGB");
            os.println("      /Coords [" + p1.getX() + " " + p1.getY() + " "
                    + p2.getX() + " " + p2.getY() + "]");
            os.println("      /Function");
            os.println("      << /FunctionType 2");
            os.println("         /Domain [0 1]");
            os.println("         /Range [0 1 0 1 0 1]");
            os.println("         /C0 [" + rgb1[0] + " " + rgb1[1] + " "
                    + rgb1[2] + "]");
            os.println("         /C1 [" + rgb2[0] + " " + rgb2[1] + " "
                    + rgb2[2] + "]");
            os.println("         /N 1");
            os.println("      >>");
            os.println("      /Extend [true true]");
            os.println("   >>");
            os.println(">>");
            os.println("matrix makepattern setpattern");
        }
    }

    protected void writePaint(TexturePaint paint) throws IOException {
        // written when filled
    }

    protected void writePaint(Paint p) throws IOException {
        // written when filled
    }

    protected void writeFont(Font font) {
	    // written when needed
    }

    public GraphicsConfiguration getDeviceConfiguration() {
        writeWarning(getClass() + ": getDeviceConfiguration() not implemented.");
        return null;
    }

    /**
     * Embed a PostScript comment into the output file.
     */
    public void writeComment(String s) throws IOException {
        os.println("% " + s);
    }

    public String toString() {
        return "PSGraphics2D";
    }

    /**
     * Write the string <code>str</code> the the stream. Method is used by
     * {@link FontUtilities#showString(java.awt.Font, String,
     * org.freehep.graphics2d.font.CharTable,
     * org.freehep.graphics2d.font.FontUtilities.ShowString)} or
     * {@link #showCharacterCodes(String, double, double)} depending on the
     * settings font embedding.
     *
     * @param font font to use
     * @param str string to draw
     */
    public void showString(Font font, String str) {
        StringBuffer result = new StringBuffer();
        Map /*<TextAttribute,?>*/<Attribute, Object> attributes = FontUtilities.getAttributes(font);
        PSFontTable.normalize(attributes);
        // write font name
        String fontName = fontTable.fontReference(
            font,
            isProperty(EMBED_FONTS),
            getProperty(EMBED_FONTS_AS));
        result.append("/");
        result.append(fontName);
        result.append(" findfont ");
        result.append(font.getSize());
        result.append(" scalefont setfont");

        // use embeded fonts or draw string directly
        if (isProperty(EMBED_FONTS)) {
            // write string directly
            result.append("\n");
            result.append(PSStringStyler.getEscaped(str));
            result.append(" show");
        } else {
            // write string as styled unicode char sequence
            result.append(" ");
            result.append(PSStringStyler.getStyledString(attributes, str));
            result.append(" recshow");
        }

        // write the result
        os.println(result.toString());
    }

    /**
     * Draws <code>str</code> to the stream. Uses the font transformation and depending on
     * {@link PSGraphics2D#EMBED_FONTS} the method {@link #showString(java.awt.Font, String)}
     * or {@link FontUtilities#showString(java.awt.Font, String, org.freehep.graphics2d.font.CharTable,
     * org.freehep.graphics2d.font.FontUtilities.ShowString)} ;
     *
     * @param str string to draw
     * @param x coordinate for drawing
     * @param y coordinate for drawing
     * @throws java.io.IOException thrown by write operations
     */
    private void showCharacterCodes(String str, double x, double y) throws IOException {
        // push a copy of the current graphics state on the graphics state stack
        writeGraphicsSave();
        // move to string position. This is done by transformation because
        // after a "moveto" command all translations by a transformation
        // are ignored and font transformation should be used after the
        // general one.
        AffineTransform at = new AffineTransform(1, 0, 0, 1, x, y);
        // aplly font transformation, e.g. vertical offset and scaling
        // for TextAttribut.SUPERSUBSCRIPT
        at.concatenate(getFont().getTransform());
        // flip vertically
        at.scale(1, -1);
        // write transformation
        writeTransform(at);
        // move to drawing position, nedded to open a drawing path
        os.println(fixedPrecision(0) + " " + fixedPrecision(0) + " moveto");
        // embed font or draw string directly
        if (isProperty(EMBED_FONTS)) {
            // use embedded font encodings for unicode to PS
            FontUtilities.showString(
                getFont(),
                str,
                fontTable.getEncodingTable(),
                this);
        } else {
            // use PS header for unicode to composite font encoding
            showString(getFont(), str);
        }

        // reset the current graphics state from the one on the top
        // of the graphics state stack and pop the graphics state stack
        writeGraphicsRestore();
    }

    /**
     * A utility function which actually sets the color in the PS. We use the
     * stroke color as the PS current color and a special saved variable for the
     * fill color.
     *
     * @param c Color
     * @param fillColor true writes a color for fillings
     */
    private void setPSColor(Color c, boolean fillColor) {
        if (c != null) {
            if (c instanceof MappedColor) {
                MappedColor mc = (MappedColor) c;
                if (!fillColor) {
                    if (mc.getBrightness() == 0) {
                        os.println(mc.getColorTag() + " vg&C");
                    } else {
                        os.println(mc.getColorTag() + " " + mc.getBrightness()
                                + " darken vg&C");
                    }
                } else {
                    if (mc.getBrightness() == 0) {
                        os.println(mc.getColorTag() + " vg&DFC");
                    } else {
                        os.println(mc.getColorTag() + " " + mc.getBrightness()
                                + " darken vg&DFC");
                    }
                }
            } else {
                Color pc = getPrintColor(c);
                double red = ((double) pc.getRed()) / 255.;
                double green = ((double) pc.getGreen()) / 255.;
                double blue = ((double) pc.getBlue()) / 255.;
                os.print(fixedPrecision(red) + " " + fixedPrecision(green)
                        + " " + fixedPrecision(blue) + " ");
                os.println(((fillColor) ? "rg" : "RG"));
            }
        }
    }
    
    private ScientificFormat scientific = new ScientificFormat(6, 9, false);

    public String fixedPrecision(double d) {
        return scientific.format(d);
    }
    
}
