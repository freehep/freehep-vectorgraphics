// Copyright 2000-2006 FreeHEP
package org.freehep.graphicsio.svg;

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
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.zip.GZIPOutputStream;

import org.freehep.graphicsio.AbstractVectorGraphicsIO;
import org.freehep.graphicsio.FontConstants;
import org.freehep.graphicsio.ImageConstants;
import org.freehep.graphicsio.ImageGraphics2D;
import org.freehep.graphicsio.InfoConstants;
import org.freehep.graphicsio.PageConstants;
import org.freehep.util.ScientificFormat;
import org.freehep.util.UserProperties;
import org.freehep.util.Value;
import org.freehep.util.io.Base64OutputStream;
import org.freehep.util.io.WriterOutputStream;
import org.freehep.xml.util.XMLWriter;

/**
 * This class implements the Scalable Vector Graphics output. SVG specifications
 * can be found at http://www.w3c.org/Graphics/SVG/
 * 
 * The current implementation is based on REC-SVG11-20030114
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-svg/src/main/java/org/freehep/graphicsio/svg/SVGGraphics2D.java cbe5b99bb13b 2006/03/09 21:55:10 duns $
 */
public class SVGGraphics2D extends AbstractVectorGraphicsIO {

    public static final String VERSION_1_1 = "Version 1.1 (REC-SVG11-20030114)";

    private static final String rootKey = SVGGraphics2D.class.getName();

    public static final String TRANSPARENT = rootKey + "."
            + PageConstants.TRANSPARENT;

    public static final String BACKGROUND = rootKey + "."
            + PageConstants.BACKGROUND;

    public static final String BACKGROUND_COLOR = rootKey + "."
            + PageConstants.BACKGROUND_COLOR;

    public static final String VERSION = rootKey + ".Version";

    public static final String COMPRESS = rootKey + ".Binary";

    public static final String STYLABLE = rootKey + ".Stylable";

    public static final String IMAGE_SIZE = rootKey + "."
            + ImageConstants.IMAGE_SIZE;

    public static final String EXPORT_IMAGES = rootKey + ".ExportImages";

    public static final String EXPORT_SUFFIX = rootKey + ".ExportSuffix";

    public static final String WRITE_IMAGES_AS = rootKey + "."
            + ImageConstants.WRITE_IMAGES_AS;

    public static final String FOR = rootKey + "." + InfoConstants.FOR;

    public static final String TITLE = rootKey + "." + InfoConstants.TITLE;

    private BasicStroke defaultStroke = new BasicStroke();

    public static final String EMBED_FONTS = rootKey + "."
            + FontConstants.EMBED_FONTS;

    private SVGFontTable fontTable;

    private static final UserProperties defaultProperties = new UserProperties();
    static {
        defaultProperties.setProperty(TRANSPARENT, true);
        defaultProperties.setProperty(BACKGROUND, false);
        defaultProperties.setProperty(BACKGROUND_COLOR, Color.GRAY);

        defaultProperties.setProperty(VERSION, VERSION_1_1);
        defaultProperties.setProperty(COMPRESS, false);
        defaultProperties.setProperty(STYLABLE, true);
        defaultProperties.setProperty(IMAGE_SIZE, new Dimension(0, 0)); // ImageSize

        defaultProperties.setProperty(EXPORT_IMAGES, false);
        defaultProperties.setProperty(EXPORT_SUFFIX, "image");

        defaultProperties.setProperty(WRITE_IMAGES_AS, ImageConstants.SMALLEST);

        defaultProperties.setProperty(FOR, "");
        defaultProperties.setProperty(TITLE, "");

        defaultProperties.setProperty(CLIP, true);

        defaultProperties.setProperty(EMBED_FONTS, false);
    }

    public static Properties getDefaultProperties() {
        return defaultProperties;
    }

    public static void setDefaultProperties(Properties newProperties) {
        defaultProperties.setProperties(newProperties);
    }

    public static final String version = "$Revision$";

    // current filename including path
    private String filename;

    // The lowerleft and upper right points of the bounding box.
    private int bbx, bby, bbw, bbh;

    // The private writer used for this file.
    private OutputStream ros;

    private PrintWriter os;

    // table for gradients
    Hashtable gradients = new Hashtable();

    // table for textures
    Hashtable textures = new Hashtable();

    private Stack closeTags = new Stack();

    private int imageNumber = 0;

    private Value clipNumber;

    private int width, height;

    /*
     * ================================================================================ |
     * 1. Constructors & Factory Methods
     * ================================================================================
     */
    public SVGGraphics2D(File file, Dimension size) throws IOException {
        this(new FileOutputStream(file), size);
        this.filename = file.getPath();
    }

    public SVGGraphics2D(File file, Component component) throws IOException {
        this(new FileOutputStream(file), component);
        this.filename = file.getPath();
    }

    public SVGGraphics2D(OutputStream os, Dimension size) {
        super(size, false);
        init(os);
        width = size.width;
        height = size.height;
    }

    public SVGGraphics2D(OutputStream os, Component component) {
        super(component, false);
        init(os);
        width = getSize().width;
        height = getSize().height;
    }

    private void init(OutputStream os) {
        this.ros = os;
        initProperties(getDefaultProperties());

        this.filename = null;

        this.clipNumber = new Value().set(0);
    }

    protected SVGGraphics2D(SVGGraphics2D graphics, boolean doRestoreOnDispose) {
        super(graphics, doRestoreOnDispose);
        // Now initialize the new object.
        filename = graphics.filename;
        os = graphics.os;
        bbx = graphics.bbx;
        bby = graphics.bby;
        bbw = graphics.bbw;
        bbh = graphics.bbh;
        gradients = graphics.gradients;
        textures = graphics.textures;
        clipNumber = graphics.clipNumber;
        fontTable = graphics.fontTable;
    }

    /*
     * ================================================================================ |
     * 2. Document Settings
     * ================================================================================
     */

    /**
     * Get the bounding box for this image.
     */
    public void setBoundingBox() {
        bbx = 0;
        bby = 0;

        Dimension size = getSize();
        bbw = size.width;
        bbh = size.height;
    }

    /*
     * ================================================================================ |
     * 3. Header, Trailer, Multipage & Comments
     * ================================================================================
     */

    /*--------------------------------------------------------------------------------
     | 3.1 Header & Trailer
     *--------------------------------------------------------------------------------*/
    /**
     * Write out the header of this SVG file.
     */
    public void writeHeader() throws IOException {
        ros = new BufferedOutputStream(ros);
        if (isProperty(COMPRESS)) {
            ros = new GZIPOutputStream(ros);
        }

        os = new PrintWriter(ros, true);
        fontTable = new SVGFontTable();

        // Do the bounding box calculation.
        setBoundingBox();
        imageNumber = 0;

        os.println("<?xml version=\"1.0\" standalone=\"no\"?>");
        if (getProperty(VERSION).equals(VERSION_1_1)) {
            // no DTD anymore
        } else {
            // FIXME experimental version
        }
        os.println();

        int x = 0;
        int y = 0;
        Dimension size = getPropertyDimension(IMAGE_SIZE);
        int w = size.width;
        if (w <= 0)
            w = width;
        int h = size.height;
        if (h <= 0)
            h = height;

        os.println("<svg ");
        if (getProperty(VERSION).equals(VERSION_1_1)) {
            os.println("     version=\"1.1\"");
            os.println("     baseProfile=\"full\"");
            os.println("     xmlns=\"http://www.w3.org/2000/svg\"");
            os.println("     xmlns:xlink=\"http://www.w3.org/1999/xlink\"");
            os.println("     xmlns:ev=\"http://www.w3.org/2001/xml-events\"");
        }
        os.println("     x=\"" + x + "px\"");
        os.println("     y=\"" + y + "px\"");
        os.println("     width=\"" + w + "px\"");
        os.println("     height=\"" + h + "px\"");
        os.println("     viewBox=\"" + bbx + " " + bby + " " + bbw + " " + bbh
                + "\"");
        os.println("     >");
        closeTags.push("</svg> <!-- bounding box -->");

        os.println("<title>");
        os.println(XMLWriter.normalizeText(getProperty(TITLE)));
        os.println("</title>");

        String producer = getClass().getName();
        if (!isDeviceIndependent()) {
            producer += " " + version.substring(1, version.length() - 1);
        }

        os.println("<desc>");
        os.println("<Title>" + XMLWriter.normalizeText(getProperty(TITLE))
                + "</Title>");
        os.println("<Creator>" + XMLWriter.normalizeText(getCreator())
                + "</Creator>");
        os.println("<Producer>" + XMLWriter.normalizeText(producer)
                + "</Producer>");
        os.println("<Source>" + XMLWriter.normalizeText(getProperty(FOR))
                + "</Source>");
        if (!isDeviceIndependent()) {
            os.println("<Date>"
                    + DateFormat.getDateTimeInstance(DateFormat.FULL,
                            DateFormat.FULL).format(new Date()) + "</Date>");
        }
        os.println("</desc>");

        // write default stroke
        os
                .println("<g style=\"" + getStrokeString(defaultStroke, true)
                        + "\">");
        closeTags.push("</g> <!-- default stroke -->");
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

    /**
     * Writes the font definitions and calls {@link #writeGraphicsRestore()} to
     * close all open XML Tags
     * 
     * @throws IOException
     */
    public void writeTrailer() throws IOException {
        // write font definition
        if (isProperty(EMBED_FONTS)) {
            os.println("<defs>");
            os.println(fontTable.toString());
            os.println("</defs> <!-- font definitions -->");
        }

        // restor graphic
        writeGraphicsRestore();
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
        return new SVGGraphics2D(this, true);
    }

    public Graphics create(double x, double y, double width, double height) {
        try {
            writeGraphicsSave();
        } catch (IOException e) {
            handleException(e);
        }
        SVGGraphics2D graphics = new SVGGraphics2D(this, true);
        // FIXME: All other drivers have a translate(x,y), clip(0,0,w,h) here
        os.println("<svg x=\"" + fixedPrecision(x) + "\" " + "y=\""
                + fixedPrecision(y) + "\" " + "width=\""
                + fixedPrecision(width) + "\" " + "height=\""
                + fixedPrecision(height) + "\" " + ">");
        graphics.closeTags.push("</svg> <!-- graphics context -->");

        // write default stroke
        os.println("<g style=\""
                + getStrokeString(graphics.defaultStroke, true) + "\">");
        graphics.closeTags.push("</g> <!-- default stroke -->");

        return graphics;
    }

    protected void writeGraphicsSave() throws IOException {
        // not applicable
    }

    protected void writeGraphicsRestore() throws IOException {
        while (!closeTags.empty()) {
            os.println(closeTags.pop());
        }
    }

    /*
     * ================================================================================ |
     * 5. Drawing Methods
     * ================================================================================
     */
    /* 5.1 shapes */
    /* 5.1.4. shapes */
    public void draw(Shape shape) {
        draw(shape, false, true);
    }

    public void fill(Shape shape) {
        draw(shape, true, false);
    }

    private void draw(Shape s, boolean fill, boolean draw) {
        StringBuffer result = new StringBuffer();

        PathIterator path = s.getPathIterator(null);

        // define style (color and stroke)
        StringBuffer style = new StringBuffer();

        style.append(getPaintString(
        // when drawing use paint as "border"
                draw ? getPaint() : null,
                // when filling use paint as "filling"
                fill ? getPaint() : null));

        style.append(getStrokeString(getStroke(), false));

        if (path.getWindingRule() == PathIterator.WIND_EVEN_ODD) {
            style.append("fill-rule:evenodd;");
        } else {
            style.append("fill-rule:nonzero;");
        }

        // write style
        result.append("<g ");
        result.append(style(style.toString()));
        result.append(">\n  ");

        // draw shape
        result.append(getPath(path));

        // close style
        result.append("\n</g> <!-- drawing style -->");

        // write in a transformed context
        os.println(getTransformedString(getTransform(), getClippedString(result
                .toString())));
    }

    /* 5.2. Images */
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        writeWarning(getClass()
                + ": copyArea(int, int, int, int, int, int) not implemented.");
    }

    protected void writeImage(RenderedImage image, AffineTransform xform,
            Color bkg) throws IOException {

        StringBuffer result = new StringBuffer();

        result.append("<image x=\"0\" y=\"0\" " + "width=\"");
        result.append(image.getWidth());
        result.append("\" " + "height=\"");
        result.append(image.getHeight());
        result.append("\" " + "xlink:href=\"");

        String writeAs = getProperty(WRITE_IMAGES_AS);
        boolean isTransparent = image.getColorModel().hasAlpha()
                && (bkg == null);

        byte[] pngBytes = null;
        if (writeAs.equals(ImageConstants.PNG)
                || writeAs.equals(ImageConstants.SMALLEST) || isTransparent) {
            ByteArrayOutputStream png = new ByteArrayOutputStream();
            ImageGraphics2D.writeImage(image, "png", new Properties(), png);
            png.close();
            pngBytes = png.toByteArray();
        }

        byte[] jpgBytes = null;
        if ((writeAs.equals(ImageConstants.JPG) || writeAs
                .equals(ImageConstants.SMALLEST))
                && !isTransparent) {
            ByteArrayOutputStream jpg = new ByteArrayOutputStream();
            ImageGraphics2D.writeImage(image, "jpg", new Properties(), jpg);
            jpg.close();
            jpgBytes = jpg.toByteArray();
        }

        String encode;
        byte[] imageBytes;
        if (writeAs.equals(ImageConstants.PNG) || isTransparent) {
            encode = "png";
            imageBytes = pngBytes;
        } else if (writeAs.equals(ImageConstants.JPG)) {
            encode = "jpg";
            imageBytes = jpgBytes;
        } else {
            encode = (jpgBytes.length < 0.5 * pngBytes.length) ? "jpg" : "png";
            imageBytes = encode.equals("jpg") ? jpgBytes : pngBytes;
        }

        if (isProperty(EXPORT_IMAGES)) {
            imageNumber++;

            // create filenames
            if (filename == null) {
                writeWarning("SVG: cannot write embedded images, since SVGGraphics2D");
                writeWarning("     was created from an OutputStream rather than a File.");
                return;
            }
            int pos = filename.lastIndexOf(File.separatorChar);
            String dirName = (pos < 0) ? "" : filename.substring(0, pos + 1);
            String imageName = (pos < 0) ? filename : filename
                    .substring(pos + 1);
            imageName += "." + getProperty(EXPORT_SUFFIX) + "-" + imageNumber
                    + "." + encode;

            result.append(imageName);

            // write the image separately
            FileOutputStream imageStream = new FileOutputStream(dirName
                    + imageName);

            imageStream.write(imageBytes);
            imageStream.close();
        } else {
            result.append("data:image/");
            result.append(encode);
            result.append(";base64,");

            StringWriter writer = new StringWriter();
            Base64OutputStream b64 = new Base64OutputStream(
                    new WriterOutputStream(writer));
            b64.write(imageBytes);
            b64.finish();

            result.append(writer.toString());
        }

        result.append("\"/>");

        os.println(getTransformedString(getTransform(),
            getClippedString(getTransformedString(xform, result
                .toString()))));
    }

    /* 5.3. Strings */
    protected void writeString(String str, double x, double y)
            throws IOException {
        // str = FontEncoder.getEncodedString(str, getFont().getName());

        if (isProperty(EMBED_FONTS)) {
            fontTable.addGlyphs(str, getFont());
        }

        // font transformation should _not_ transform string position
        // so we draw at 0:0 and translate _before_ using getFont().getTransform()
        // we could not just translate before and reverse translation after
        // writing because the clipping area

        // create style
        StringBuffer style = new StringBuffer(getPaintString(null, getPaint()));
        style.append(getFontString(getFont()));
        style.append(getStrokeString(getStroke(), false));

        os.println(getTransformedString(
            // general transformation
            getTransform(),
            // general clip
            getClippedString(
                getTransformedString(
                    // text offset
                    new AffineTransform(1, 0, 0, 1, x, y),
                    getTransformedString(
                        // font transformation and text
                        getFont().getTransform(),
                        "<text "
                            // style
                            + style(style.toString())
                            // coordiantes
                            + " x=\"0\" y=\"0\">"
                            // text
                            + XMLWriter.normalizeText(str)
                            + "</text>")))));
    }

    /**
     * Creates a tag list for the given font.
     * Family, size, bold italic, underline and strikethrough are converted.
     * {@link java.awt.font.TextAttribute#SUPERSCRIPT}
     * is handled by {@link java.awt.Font#getTransform()}
     *
     * @return correspondig svg tags for the font
     */
    private String getFontString(Font font) {
        StringBuffer svgFont = new StringBuffer();

        svgFont.append("font-family:");

        if (isProperty(EMBED_FONTS)) {
            svgFont.append(fontTable.getEmbeddedFontName(getFont()));
        } else {
            svgFont.append(getFont().getFamily());
        }

        if (getFont().isBold()) {
            svgFont.append(";font-weight:bold");
        } else {
            svgFont.append(";font-weight:normal");
        }

        if (getFont().isItalic()) {
            svgFont.append(";font-style:italic");
        } else {
            svgFont.append(";font-style:normal");
        }

        Object ul = font.getAttributes().get(TextAttribute.UNDERLINE);
        if (ul != null) {
            // underline style, only supported by CSS 3
            if (TextAttribute.UNDERLINE_LOW_DOTTED.equals(ul)) {
                svgFont.append(";text-underline-style:dotted");
            } else if (TextAttribute.UNDERLINE_LOW_DASHED.equals(ul)) {
                svgFont.append(";text-underline-style:dashed");
            } else if (TextAttribute.UNDERLINE_ON.equals(ul)) {
                svgFont.append(";text-underline-style:solid");
            }

            // the underline itself, supported by CSS 2
            svgFont.append(";text-decoration:underline");
        }

        if (font.getAttributes().get(TextAttribute.STRIKETHROUGH) != null) {
            // is the property allready witten?
            if  (ul == null) {
                svgFont.append(";text-decoration:");
            } else {
                svgFont.append(" ");
            }
            svgFont.append("line-through");
        }

        int size = getFont().getSize();
        svgFont.append(";font-size:");
        svgFont.append(size);
        svgFont.append(";");

        return svgFont.toString();
    }

    /*
     * ================================================================================ |
     * 6. Transformations
     * ================================================================================
     */
    protected void writeTransform(AffineTransform transform) throws IOException {
        // written when needed
    }

    protected void writeSetTransform(AffineTransform transform)
            throws IOException {
        // written when needed
    }

    /*
     * ================================================================================ |
     * 7. Clipping
     * ================================================================================
     */
    protected void writeClip(Shape s) throws IOException {
        // written when needed
    }

    protected void writeSetClip(Shape s) throws IOException {
        // written when needed
    }

    /*
     * ================================================================================ |
     * 8. Graphics State
     * ================================================================================
     */
    /* 8.1. stroke/linewidth */
    protected void writeWidth(float width) throws IOException {
        // written when needed
    }

    protected void writeCap(int cap) throws IOException {
        // Written when needed
    }

    protected void writeJoin(int join) throws IOException {
        // written when needed
    }

    protected void writeMiterLimit(float limit) throws IOException {
        // written when needed
    }

    protected void writeDash(float[] dash, float phase) throws IOException {
        // written when needed
    }

    /**
     * return the style tag for the stroke
     * 
     * @param s
     *            Stroke to convert
     * @param all
     *            all attributes (not only the differences to defaultStroke) are
     *            handled
     * @return corresponding style string
     */
    private String getStrokeString(Stroke s, boolean all) {
        // only BasisStrokes are written
        if (!(s instanceof BasicStroke)) {
            return "";
        }

        BasicStroke stroke = (BasicStroke) s;
        StringBuffer result = new StringBuffer();

        // append linecap
        if (all || (stroke.getEndCap() != defaultStroke.getEndCap())) {
            result.append("stroke-linecap:");
            // append cap
            switch (stroke.getEndCap()) {
                default:
                case BasicStroke.CAP_BUTT:
                    result.append("butt");
                    break;
                case BasicStroke.CAP_ROUND:
                    result.append("round");
                    break;
                case BasicStroke.CAP_SQUARE:
                    result.append("square");
                    break;
            }
            result.append(";");
        }

        // append dasharray
        if (all
                || !Arrays.equals(stroke.getDashArray(), defaultStroke
                        .getDashArray())) {
            result.append("stroke-dasharray:");
            if (stroke.getDashArray() != null
                    && stroke.getDashArray().length > 0) {
                for (int i = 0; i < stroke.getDashArray().length; i++) {
                    if (i > 0) {
                        result.append(",");
                    }
                    result.append(fixedPrecision(stroke.getDashArray()[i]));
                }
            } else {
                result.append("none");
            }
            result.append(";");
        }

        if (all || (stroke.getDashPhase() != defaultStroke.getDashPhase())) {
            result.append("stroke-dashoffset:");
            result.append(fixedPrecision(stroke.getDashPhase()));
            result.append(";");
        }

        // append meter limit
        if (all || (stroke.getMiterLimit() != defaultStroke.getMiterLimit())) {
            result.append("stroke-miterlimit:");
            result.append(fixedPrecision(stroke.getMiterLimit()));
            result.append(";");
        }

        // append join
        if (all || (stroke.getLineJoin() != defaultStroke.getLineJoin())) {
            result.append("stroke-linejoin:");
            switch (stroke.getLineJoin()) {
                default:
                case BasicStroke.JOIN_MITER:
                    result.append("miter");
                    break;
                case BasicStroke.JOIN_ROUND:
                    result.append("round");
                    break;
                case BasicStroke.JOIN_BEVEL:
                    result.append("bevel");
                    break;
            }
            result.append(";");
        }

        // append linewidth
        if (all || (stroke.getLineWidth() != defaultStroke.getLineWidth())) {
            result.append("stroke-width:");
            // width of 0 means thinnest line, which does not exist in SVG
            if (stroke.getLineWidth() == 0) {
                result.append(fixedPrecision(0.000001f));
            } else {
                result.append(fixedPrecision(stroke.getLineWidth()));
            }
            result.append(";");
        }

        return result.toString();
    }

    /* 8.2. paint/color */
    public void setPaintMode() {
        writeWarning(getClass() + ": setPaintMode() not implemented.");
    }

    public void setXORMode(Color c1) {
        writeWarning(getClass() + ": setXORMode(Color) not implemented.");
    }

    protected void writePaint(Color c) throws IOException {
        // written with every draw
    }

    protected void writePaint(GradientPaint paint) throws IOException {
        if (gradients.get(paint) == null) {
            String name = "gradient-" + gradients.size();
            gradients.put(paint, name);
            Point2D p1 = paint.getPoint1();
            Point2D p2 = paint.getPoint2();
            os.println("<defs>");
            os.print("  <linearGradient id=\"" + name + "\" ");
            os.print("x1=\"" + fixedPrecision(p1.getX()) + "\" ");
            os.print("y1=\"" + fixedPrecision(p1.getY()) + "\" ");
            os.print("x2=\"" + fixedPrecision(p2.getX()) + "\" ");
            os.print("y2=\"" + fixedPrecision(p2.getY()) + "\" ");
            os.print("gradientUnits=\"userSpaceOnUse\" ");
            os.print("spreadMethod=\""
                    + ((paint.isCyclic()) ? "reflect" : "pad") + "\" ");
            os.println(">");
            os.println("    <stop offset=\"0\" stop-color=\""
                    + hexColor(paint.getColor1()) + "\" " + "opacity-stop=\""
                    + alphaColor(paint.getColor1()) + "\" />");
            os.println("    <stop offset=\"1\" stop-color=\""
                    + hexColor(paint.getColor2()) + "\" " + "opacity-stop=\""
                    + alphaColor(paint.getColor2()) + "\" />");
            os.println("  </linearGradient>");
            os.println("</defs>");
        }
        os.println("<g " + style("stroke:" + hexColor(getPaint())) + ">");
        closeTags.push("</g> <!-- color -->");
    }

    protected void writePaint(TexturePaint paint) throws IOException {
        if (textures.get(paint) == null) {
            String name = "texture-" + textures.size();
            textures.put(paint, name);
            BufferedImage image = paint.getImage();
            Rectangle2D rect = paint.getAnchorRect();
            os.println("<defs>");
            os.print("  <pattern id=\"" + name + "\" ");
            os.print("x=\"0\" ");
            os.print("y=\"0\" ");
            os.print("width=\"" + fixedPrecision(image.getWidth()) + "\" ");
            os.print("height=\"" + fixedPrecision(image.getHeight()) + "\" ");
            os.print("patternUnits=\"userSpaceOnUse\" ");
            os.print("patternTransform=\"matrix("
                    + fixedPrecision(rect.getWidth() / image.getWidth()) + ","
                    + "0.0,0.0,"
                    + fixedPrecision(rect.getHeight() / image.getHeight())
                    + "," + fixedPrecision(rect.getX()) + ","
                    + fixedPrecision(rect.getY()) + ")\" ");
            os.println(">");
            writeImage(image, null, null);
            os.println("  </pattern>");
            os.println("</defs>");
        }
        os.println("<g " + style("stroke:" + hexColor(getPaint())) + ">");
        closeTags.push("</g> <!-- color -->");
    }

    protected void writePaint(Paint p) throws IOException {
        writeWarning(getClass() + ": writePaint(Paint) not implemented for "
                + p.getClass());
    }

    /* 8.3. font */
    protected void writeFont(Font font) throws IOException {
        // written when needed
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

    public void writeComment(String s) throws IOException {
        os.println("<!-- " + s + " -->");
    }

    public String toString() {
        return "SVGGraphics2D";
    }

    /*
     * ================================================================================ |
     * 10. Private/Utility Methos
     * ================================================================================
     */

    /**
     * Encapsulates a SVG-Tag by the given transformation matrix
     * 
     * @param t
     *            Transformation
     * @param s
     *            SVG-Tag
     */
    private String getTransformedString(AffineTransform t, String s) {
        StringBuffer result = new StringBuffer();

        if (t != null && !t.isIdentity()) {
            result.append("<g transform=\"matrix(");
            result.append(fixedPrecision(t.getScaleX()));
            result.append(", ");
            result.append(fixedPrecision(t.getShearY()));
            result.append(", ");
            result.append(fixedPrecision(t.getShearX()));
            result.append(", ");
            result.append(fixedPrecision(t.getScaleY()));
            result.append(", ");
            result.append(fixedPrecision(t.getTranslateX()));
            result.append(", ");
            result.append(fixedPrecision(t.getTranslateY()));
            result.append(")\">\n");
        }

        result.append(s);

        if (t != null && !t.isIdentity()) {
            result.append("\n</g> <!-- transform -->");
        }

        return result.toString();
    }

    /**
     * Encapsulates a SVG-Tag by the current clipping area matrix
     * 
     * @param s
     *            SVG-Tag
     */
    private String getClippedString(String s) {
        StringBuffer result = new StringBuffer();

        // clipping
        if (getClip() != null && isProperty(CLIP)) {
            // SVG uses unique lip numbers, don't reset allways increment them
            clipNumber.set(clipNumber.getInt() + 1);

            // define clip
            result.append("<clipPath id=\"clip");
            result.append(clipNumber.getInt());
            result.append("\">\n  ");
            result.append(getPath(getClip().getPathIterator(null)));
            result.append("\n</clipPath>\n");

            // use clip
            result.append("<g clip-path=\"url(#clip");
            result.append(clipNumber.getInt());
            result.append(")\">\n");
        }

        // append the string
        result.append(s);

        // close clipping
        if (getClip() != null && isProperty(CLIP)) {
            result.append("\n</g> <!-- clip");
            result.append(clipNumber.getInt());
            result.append(" -->");
        }

        return result.toString();
    }

    private String getPaintString(Paint stroke, Paint fill) {
        // stroke color
        StringBuffer s = new StringBuffer();
        s.append("stroke:");
        if (stroke != null) {
            s.append(hexColor(stroke));
            s.append(";stroke-opacity:");
            s.append(alphaColor(stroke));
        } else {
            s.append("none");
        }
        s.append(";");

        s.append("fill:");
        if (fill != null) {
            s.append(hexColor(fill));
            s.append(";fill-opacity:");
            s.append(alphaColor(fill));
        } else {
            s.append("none");
        }
        s.append(";");

        return s.toString();
    }

    private float alphaColor(Paint p) {
        if (p instanceof Color) {
            return (float) (getPrintColor((Color) p).getAlpha() / 255.0);
        } else if (p instanceof GradientPaint) {
            return 1.0f;
        } else if (p instanceof TexturePaint) {
            return 1.0f;
        }
        writeWarning(getClass() + ": alphaColor() not implemented for "
                + p.getClass() + ".");
        return 1.0f;
    }

    private String hexColor(Paint p) {
        if (p instanceof Color) {
            return hexColor(getPrintColor((Color) p));
        } else if (p instanceof GradientPaint) {
            return hexColor((GradientPaint) p);
        } else if (p instanceof TexturePaint) {
            return hexColor((TexturePaint) p);
        }
        writeWarning(getClass() + ": hexColor() not implemented for "
                + p.getClass() + ".");
        return "#000000";
    }

    private String hexColor(Color c) {
        String s1 = Integer.toHexString(c.getRed());
        s1 = (s1.length() != 2) ? "0" + s1 : s1;

        String s2 = Integer.toHexString(c.getGreen());
        s2 = (s2.length() != 2) ? "0" + s2 : s2;

        String s3 = Integer.toHexString(c.getBlue());
        s3 = (s3.length() != 2) ? "0" + s3 : s3;

        return "#" + s1 + s2 + s3;
    }

    private String hexColor(GradientPaint p) {
        return "url(#" + gradients.get(p) + ")";
    }

    private String hexColor(TexturePaint p) {
        return "url(#" + textures.get(p) + ")";
    }

    protected static String getPathContent(PathIterator path) {
        StringBuffer result = new StringBuffer();

        double[] coords = new double[6];
        result.append("d=\"");
        while (!path.isDone()) {
            int segType = path.currentSegment(coords);

            switch (segType) {
                case PathIterator.SEG_MOVETO:
                    result.append("M ");
                    result.append(fixedPrecision(coords[0]));
                    result.append(" ");
                    result.append(fixedPrecision(coords[1]));
                    break;
                case PathIterator.SEG_LINETO:
                    result.append("L ");
                    result.append(fixedPrecision(coords[0]));
                    result.append(" ");
                    result.append(fixedPrecision(coords[1]));
                    break;
                case PathIterator.SEG_CUBICTO:
                    result.append("C ");
                    result.append(fixedPrecision(coords[0]));
                    result.append(" ");
                    result.append(fixedPrecision(coords[1]));
                    result.append(" ");
                    result.append(fixedPrecision(coords[2]));
                    result.append(" ");
                    result.append(fixedPrecision(coords[3]));
                    result.append(" ");
                    result.append(fixedPrecision(coords[4]));
                    result.append(" ");
                    result.append(fixedPrecision(coords[5]));
                    break;
                case PathIterator.SEG_QUADTO:
                    result.append("Q ");
                    result.append(fixedPrecision(coords[0]));
                    result.append(" ");
                    result.append(fixedPrecision(coords[1]));
                    result.append(" ");
                    result.append(fixedPrecision(coords[2]));
                    result.append(" ");
                    result.append(fixedPrecision(coords[3]));
                    break;
                case PathIterator.SEG_CLOSE:
                    result.append("z");
                    break;
            }

            // Move to the next segment.
            path.next();

            // Not needed but makes the output readable
            if (!path.isDone()) {
                result.append(" ");
            }
        }
        result.append("\"");

        return result.toString();
    }

    protected String getPath(PathIterator path) {
        StringBuffer result = new StringBuffer();

        result.append("<path ");
        result.append(getPathContent(path));
        result.append("/>");

        return result.toString();
    }

    private String style(String stylableString) {
        return style(isProperty(STYLABLE), stylableString);
    }

    static String style(boolean stylable, String stylableString) {
        if ((stylableString == null) || (stylableString.equals(""))) {
            return "";
        }

        if (stylable) {
            return "style=\"" + stylableString + "\"";
        }

        StringBuffer r = new StringBuffer();
        StringTokenizer st1 = new StringTokenizer(stylableString, ";");
        while (st1.hasMoreTokens()) {
            String s = st1.nextToken();
            int colon = s.indexOf(':');
            if (colon >= 0) {
                r.append(s.substring(0, colon));
                r.append("=\"");
                r.append(s.substring(colon + 1));
                r.append("\" ");
            }
        }
        return r.toString();
    }

    private static ScientificFormat scientific = new ScientificFormat(5, 8,
            false);

    public static String fixedPrecision(double d) {
        return scientific.format(d);
    }

    protected PrintWriter getOutputStream() {
        return os;
    }
}
