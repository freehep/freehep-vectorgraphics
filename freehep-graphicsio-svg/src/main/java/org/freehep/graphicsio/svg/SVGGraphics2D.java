// Copyright 2000-2005 FreeHEP
package org.freehep.graphicsio.svg;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;

import org.freehep.graphics2d.TagString;
import org.freehep.graphics2d.font.FontEncoder;
import org.freehep.graphicsio.AbstractVectorGraphicsIO;
import org.freehep.graphicsio.PageConstants;
import org.freehep.graphicsio.InfoConstants;
import org.freehep.graphicsio.ImageConstants;
import org.freehep.graphicsio.ImageGraphics2D;
import org.freehep.util.UserProperties;
import org.freehep.util.Value;
import org.freehep.util.io.Base64OutputStream;
import org.freehep.util.io.WriterOutputStream;
import org.freehep.util.ScientificFormat;
import org.freehep.xml.util.XMLWriter;

/**
 * This class implements the Scalable Vector Graphics output.
 * SVG specifications can be found at http://www.w3c.org/Graphics/SVG/
 *
 * The current implementation is based on REC-SVG-20010904
 * but can generate also files for the older specs CR-SVG-20000802, WD-SVG-20000303
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-svg/src/main/java/org/freehep/graphicsio/svg/SVGGraphics2D.java 02f873212b4c 2005/12/03 01:18:24 duns $
 */
public class SVGGraphics2D
    extends AbstractVectorGraphicsIO {

    public static final String VERSION_1_0 = "Version 1.0 (REC-SVG-20010904)";
    public static final String VERSION_1_1 = "Version 1.1 (REC-SVG11-20030114)";

    private static final String rootKey = SVGGraphics2D.class.getName();

    public static final String TRANSPARENT          = rootKey+"."+PageConstants.TRANSPARENT;
    public static final String BACKGROUND           = rootKey+"."+PageConstants.BACKGROUND;
    public static final String BACKGROUND_COLOR     = rootKey+"."+PageConstants.BACKGROUND_COLOR;

    public static final String VERSION              = rootKey+".Version";
    public static final String COMPRESS             = rootKey+".Binary";
    public static final String STYLABLE             = rootKey+".Stylable";
    public static final String IMAGE_SIZE           = rootKey+"."+ImageConstants.IMAGE_SIZE;

    public static final String EXPORT_IMAGES        = rootKey+".ExportImages";
    public static final String EXPORT_SUFFIX        = rootKey+".ExportSuffix";

    public static final String WRITE_IMAGES_AS      = rootKey+"."+ImageConstants.WRITE_IMAGES_AS;

    public static final String FOR                  = rootKey+"."+InfoConstants.FOR;
    public static final String TITLE                = rootKey+"."+InfoConstants.TITLE;

    private static final UserProperties defaultProperties = new UserProperties();
    static {
        defaultProperties.setProperty(TRANSPARENT,      true);
        defaultProperties.setProperty(BACKGROUND,       false);
        defaultProperties.setProperty(BACKGROUND_COLOR, Color.GRAY);

        defaultProperties.setProperty(VERSION,          VERSION_1_0);
        defaultProperties.setProperty(COMPRESS,         true);
        defaultProperties.setProperty(STYLABLE,         true);
        defaultProperties.setProperty(IMAGE_SIZE,       new Dimension(0, 0));   // ImageSize

        defaultProperties.setProperty(EXPORT_IMAGES,    false);
        defaultProperties.setProperty(EXPORT_SUFFIX,    "image");

        defaultProperties.setProperty(WRITE_IMAGES_AS,  ImageConstants.SMALLEST);

        defaultProperties.setProperty(FOR,              "");
        defaultProperties.setProperty(TITLE,            "");
    }

    public static Properties getDefaultProperties() {
        return defaultProperties;
    }

    public static void setDefaultProperties(Properties newProperties) {
        defaultProperties.setProperties(newProperties);
    }

    public static final String version = "$Revision$";

    // shift to make draw routines draw in the middle
    private static final double bias = 0.5;

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
    private int currentClipNumber;

    private int width, height;

    /*================================================================================
     | 1. Constructors & Factory Methods
     *================================================================================*/
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
        this.currentClipNumber = -1;
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
        currentClipNumber = -1;
    }

    /*================================================================================
     | 2. Document Settings
     *================================================================================*/

    /**
     * Get the bounding box for this image. */
    public void setBoundingBox() {
        bbx = 0;
        bby = 0;

        Dimension size = getSize();
        bbw = size.width;
        bbh = size.height;
    }

    /*================================================================================
     | 3. Header, Trailer, Multipage & Comments
     *================================================================================*/

    /*--------------------------------------------------------------------------------
     | 3.1 Header & Trailer
     *--------------------------------------------------------------------------------*/
    /**
     * Write out the header of this SVG file. */
    public void writeHeader() throws IOException {
        ros = new BufferedOutputStream(ros);
        if (isProperty(COMPRESS)) ros = new GZIPOutputStream(ros);
        os = new PrintWriter(ros, true);

        // Do the bounding box calculation.
        setBoundingBox();
        imageNumber = 0;

        os.println("<?xml version=\"1.0\" standalone=\"no\"?>");
        if (getProperty(VERSION).equals(VERSION_1_0)) {
            os.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\"");
            os.println("  \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg-20010904.dtd\">");
        } else if (getProperty(VERSION).equals(VERSION_1_1)) {
            // FIXME disabled for now
        } else {
            // FIXME experimental version
        }
        os.println();

        int x = 0;
        int y = 0;
        Dimension size = getPropertyDimension(IMAGE_SIZE);
        int w = size.width;
        if (w <= 0) w = width;
        int h = size.height;
        if (h <= 0) h = height;

        os.println("<svg x=\""+x+"px\" "+
                        "y=\""+y+"px\" "+
                        "width=\""+w+"px\" "+
                        "height=\""+h+"px\" "+
                        "viewBox=\""+bbx+" "+bby+" "+bbw+" "+bbh+"\" "+
                        defaultStyle()+
                        ">");
        closeTags.push("</svg> <!-- bounding box -->");

        os.println("<title>");
        os.println(XMLWriter.normalizeText(getProperty(TITLE)));
        os.println("</title>");

        String producer = getClass().getName();
        if (!isDeviceIndependent()) {
            producer += " "+version.substring(1,version.length()-1);
        }

        os.println("<desc>");
        os.println("<Title>"+XMLWriter.normalizeText(getProperty(TITLE))+"</Title>");
        os.println("<Creator>"+XMLWriter.normalizeText(getCreator())+"</Creator>");
        os.println("<Producer>"+XMLWriter.normalizeText(producer)+"</Producer>");
        os.println("<Source>"+XMLWriter.normalizeText(getProperty(FOR))+"</Source>");
        if (!isDeviceIndependent()) {
            os.println("<Date>"+
                          DateFormat.
                          getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).
                          format(new Date())+"</Date>");
        }
        os.println("</desc>");

        writeDefs();
        writeSetup();
    }

    private void writeDefs() throws IOException {
        // The defs are kept in a file SVGDefs.txt in the same area
        // as this class definition.  It is simply copied into the
        // output file.
        os.println("<defs>");
        copyResourceTo(this,"SVGDefs.txt", os);
        if (isProperty(STYLABLE)) {
            copyResourceTo(this, "SVGDefs-stylable.txt", os);
        } else {
            copyResourceTo(this, "SVGDefs-stylable.txt", os);
        }
        os.println("</defs>\n");
    }

    private void writeSetup() throws IOException {
        os.println("<g "+defaultStyle()+">");
        setFont(getFont());

        closeTags.push("</g> <!-- top-level -->");
    }

    public void writeBackground() throws IOException {
        if (isProperty(TRANSPARENT)) {
            setBackground(null);
        } else if (isProperty(BACKGROUND)) {
            setBackground(getPropertyColor(BACKGROUND_COLOR));
            clearRect(0.0, 0.0, getSize().width, getSize().height);
        } else {
            setBackground(getComponent() != null ? getComponent().getBackground() : Color.WHITE);
            clearRect(0.0, 0.0, getSize().width, getSize().height);
        }
    }

    public void writeTrailer() throws IOException {
        writeGraphicsRestore();
    }

    public void closeStream() throws IOException {
        os.close();
    }

    /*================================================================================
     | 4. Create
     *================================================================================*/

    public Graphics create() {
        try {
            writeGraphicsSave();
        } catch (IOException e) {
            handleException(e);
        }
        SVGGraphics2D tempGraphics = new SVGGraphics2D(this, true);
//      os.println("<g "+defaultStyle()+">");
        return tempGraphics;
    }

    public Graphics create(double x, double y, double width, double height) {
        try {
            writeGraphicsSave();
        } catch (IOException e) {
            handleException(e);
        }
        SVGGraphics2D graphics = new SVGGraphics2D(this, true);
        // FIXME: All other drivers have a translate(x,y), clip(0,0,w,h) here
        os.println("<svg x=\""+fixedPrecision(x)+"\" "+
                        "y=\""+fixedPrecision(y)+"\" "+
                        "width=\""+fixedPrecision(width)+"\" "+
                        "height=\""+fixedPrecision(height)+"\" "+
                         ">");
//                       defaultStyle()+">");
        graphics.closeTags.push("</svg> <!-- graphics context -->");
        return graphics;
    }

    protected void writeGraphicsSave() throws IOException {
        // not applicable
    }

    protected void writeGraphicsRestore() throws IOException {
        while(!closeTags.empty()) {
            os.println(closeTags.pop());
        }
    }

    /*================================================================================
     | 5. Drawing Methods
     *================================================================================*/
    /* 5.1 shapes */
    /* 5.1.1. lines, rectangles, round rectangles */
    public void drawLine(double x1, double y1, double x2, double y2) {
        os.println("<line "+style(color(getPaint(), null))+" x1=\""+fixedPrecision(x1+bias)+"\" y1=\""+fixedPrecision(y1+bias)+"\" x2=\""+fixedPrecision(x2+bias)+"\" y2=\""+fixedPrecision(y2+bias)+"\" />");
    }

    public void drawRect(double x, double y, double width, double height) {
        os.println("<rect "+style(color(getPaint(), null))+" x=\""+fixedPrecision(x+bias)+"\" y=\""+fixedPrecision(y+bias)+"\" width=\""+fixedPrecision(width)+"\" height=\""+fixedPrecision(height)+"\"/>");
    }

    public void fillRect(double x, double y, double width, double height) {
        os.println("<rect "+style(color(null, getPaint()))+
                   " x=\""+fixedPrecision(x)+"\" y=\""+fixedPrecision(y)+"\" width=\""+fixedPrecision(width)+"\" height=\""+fixedPrecision(height)+"\"/>");
    }

    public void drawRoundRect(double x, double y, double width, double height,
                              double arcWidth, double arcHeight) {
        os.println("<rect "+style(color(getPaint(), null))+
                            " x=\""+fixedPrecision(x)+"\" y=\""+fixedPrecision(y)+"\" width=\""+fixedPrecision(width)+"\" height=\""+fixedPrecision(height)+"\" "+
                             "rx=\""+fixedPrecision(arcWidth/2)+"\" ry=\""+fixedPrecision(arcHeight/2)+"\" />");
    }

    public void fillRoundRect(double x, double y, double width, double height,
                              double arcWidth, double arcHeight) {
        os.println("<rect "+style(color(null, getPaint()))+
                         " x=\""+fixedPrecision(x)+"\" y=\""+fixedPrecision(y)+"\" width=\""+fixedPrecision(width)+"\" height=\""+fixedPrecision(height)+"\" "+
                             "rx=\""+fixedPrecision(arcWidth/2)+"\" ry=\""+fixedPrecision(arcHeight/2)+"\" />");
    }

    /* 5.1.2. polylines, polygons */
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        if (nPoints>1) {
            os.print("<polyline "+style(color(getPaint(), null))+" points=\"");
            for (int i=0; i<nPoints; i++) {
                os.print((xPoints[i]+bias)+","+(yPoints[i]+bias)+" ");
            }
            os.println("\" />");
        }
    }

    public void drawPolyline(double[] xPoints, double[] yPoints, int nPoints) {
        if (nPoints>1) {
            os.print("<polyline "+style(color(getPaint(), null))+" points=\"");
            for (int i=0; i<nPoints; i++) {
                os.print(fixedPrecision(xPoints[i]+bias)+","+fixedPrecision(yPoints[i]+bias)+" ");
            }
            os.println("\" />");
        }
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        if (nPoints>1) {
            os.print("<polygon "+style(color(getPaint(), null))+" points=\"");
            for (int i=0; i<nPoints; i++) {
                os.print((xPoints[i]+bias)+","+(yPoints[i]+bias)+" ");
            }
            os.println("\" />");
        }
    }

    public void drawPolygon(double[] xPoints, double[] yPoints, int nPoints) {
        if (nPoints>1) {
            os.print("<polygon "+style(color(getPaint(), null))+" points=\"");
            for (int i=0; i<nPoints; i++) {
                os.print(fixedPrecision(xPoints[i]+bias)+","+fixedPrecision(yPoints[i]+bias)+" ");
            }
            os.println("\" />");
        }
    }

    public void drawPolygon(Polygon p) {
        drawPolygon(p.xpoints,p.ypoints,p.npoints);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        if (nPoints>1) {
            os.print("<polygon "+style(color(null, getPaint())+"fill-rule:evenodd")+" points=\"");
            for (int i=0; i<nPoints; i++) {
                os.print(xPoints[i]+","+yPoints[i]+" ");
            }
            os.println("\" />");
        }
    }

    public void fillPolygon(double[] xPoints, double[] yPoints, int nPoints) {
        if (nPoints>1) {
            os.print("<polygon "+style(color(null, getPaint())+"fill-rule:evenodd")+" points=\"");
            for (int i=0; i<nPoints; i++) {
                os.print(fixedPrecision(xPoints[i])+","+fixedPrecision(yPoints[i])+" ");
            }
            os.println("\" />");
        }
    }

    public void fillPolygon(Polygon p) {
        fillPolygon(p.xpoints,p.ypoints,p.npoints);
    }

    /* 5.1.3. ovals, arcs */
    public void drawArc(double x, double y,
                        double width, double height,
                        double startAngle, double arcAngle) {
        double sa = startAngle * Math.PI / 180;
        double aa = arcAngle * Math.PI / 180;
        double rx = width/2;
        double ry = height/2;
        double x1 = x + bias + rx + rx*Math.cos(sa);
        double y1 = y + bias + ry - ry*Math.sin(sa);
        double x2 = x + bias + rx + rx*Math.cos(sa+aa);
        double y2 = y + bias + ry - ry*Math.sin(sa+aa);
        int large = (Math.abs(arcAngle) <= 180) ? 0 : 1;
        int sweep = (arcAngle > 0) ? 0 : 1;
        os.println("<path "+style(color(getPaint(), null))+
                            " d=\"M "+fixedPrecision(x1)+" "+fixedPrecision(y1)+
                            " A "+fixedPrecision(rx)+","+fixedPrecision(ry)+
                            " 0 "+large+" "+sweep+" "+fixedPrecision(x2)+","+fixedPrecision(y2)+"\"/>");
    }

    public void fillArc(double x, double y, double width, double height,
                        double startAngle, double arcAngle) {
        double sa = startAngle * Math.PI / 180;
        double aa = arcAngle * Math.PI / 180;
        double rx = width/2;
        double ry = height/2;
        double x1 = x + rx + rx*Math.cos(sa);
        double y1 = y + ry - ry*Math.sin(sa);
        double x2 = x + rx + rx*Math.cos(sa+aa);
        double y2 = y + ry - ry*Math.sin(sa+aa);
        int large = (Math.abs(arcAngle) <= 180) ? 0 : 1;
        int sweep = (arcAngle > 0) ? 0 : 1;
        os.println("<path "+style(color(null, getPaint()))+
                       " d=\"M "+fixedPrecision(x1)+" "+fixedPrecision(y1)+
                            " A "+fixedPrecision(rx)+","+fixedPrecision(ry)+
                            " 0 "+large+" "+sweep+" "+fixedPrecision(x2)+","+fixedPrecision(y2)+
                            " L "+fixedPrecision(x+rx)+" "+fixedPrecision(y+ry)+" z\"/>");
    }

    public void drawOval(double x, double y, double width, double height) {
        os.println("<ellipse "+style(color(getPaint(), null))+
                             " cx=\""+fixedPrecision(x+bias+width/2)+"\" cy=\""+fixedPrecision(y+bias+height/2)+"\" "+
                             "rx=\""+fixedPrecision(width/2)+"\" ry=\""+fixedPrecision(height/2)+"\" />");
    }

    public void fillOval(double x, double y, double width, double height) {
        os.println("<ellipse "+style(color(null, getPaint()))+
                             " cx=\""+fixedPrecision(x+width/2)+"\" cy=\""+fixedPrecision(y+height/2)+"\" "+
                             "rx=\""+fixedPrecision(width/2)+"\" ry=\""+fixedPrecision(height/2)+"\" />");
    }


    /*
    public void drawSymbol(double x, double y, double size, int symbol) {
        if (size>0) {
            // FIXME: the current viewer (Adobe beta2 4/00) ignores the ViewBox in the symbols
            // so we go for fixed size (100) symbols and do the scaling and translation ourselves...
            // we also leave out any placement!
            os.print("<use "+ // "x=\""+fixedPrecision(x+bias)+"\" y=\""+fixedPrecision(y+bias)+"\" "+
                     "transform=\"translate("+fixedPrecision(x+bias-size/2)+","+fixedPrecision(y+bias-size/2)+")scale("+fixedPrecision(size/600)+")\" "+
                     "xlink:href=\"#");
            switch (symbol) {
            case SYMBOL_VLINE:
                        os.print("vline");
                        break;
            case SYMBOL_HLINE:
                        os.print("hline");
                        break;
            case SYMBOL_PLUS:
                        os.print("plus");
                        break;
                    case SYMBOL_CROSS:
                        os.print("cross");
                        break;
            case SYMBOL_STAR:
                        os.print("star");
                        break;
            case SYMBOL_CIRCLE:
                        os.print("dot");
                        break;
            case SYMBOL_FILLED_CIRCLE:
                        os.print("fdot");
                        break;
            case SYMBOL_BOX:
                        os.print("box");
                        break;
            case SYMBOL_FILLED_BOX:
                        os.print("fbox");
                        break;
            case SYMBOL_UP_TRIANGLE:
                        os.print("triup");
                        break;
            case SYMBOL_FILLED_UP_TRIANGLE:
                        os.print("ftriup");
                        break;
            case SYMBOL_DN_TRIANGLE:
                        os.print("tridn");
                        break;
            case SYMBOL_FILLED_DN_TRIANGLE:
                        os.print("ftridn");
                        break;
            case SYMBOL_DIAMOND:
                        os.print("diamond");
                        break;
            case SYMBOL_FILLED_DIAMOND:
                        os.print("fdiamond");
                        break;
            }
            os.println("\"/>");
        }
    }
    */

    /* 5.1.4. shapes */
    public void draw(Shape shape) {
        PathIterator path = shape.getPathIterator(null);

        os.println("<g "+style(color(getPaint(), null))+">");
        writePath(path);
        os.println("</g> <!-- draw -->");
    }

    public void fill(Shape shape) {

        PathIterator path = shape.getPathIterator(null);
        StringBuffer s = new StringBuffer();
        s.append(color(null, getPaint()));
        if (path.getWindingRule()==PathIterator.WIND_EVEN_ODD) {
            s.append("fill-rule:evenodd;");
        } else {
            s.append("fill-rule:nonzero;");
        }

        os.println("<g "+style(s.toString())+">");
        writePath(path);
        os.println("</g> <!-- fill -->");
    }

    public void fillAndDraw(Shape shape, Color fillColor) {
        PathIterator path = shape.getPathIterator(null);
        StringBuffer s = new StringBuffer();
        if (fillColor != null) {
            s.append(color(getPaint(), fillColor));
            if (path.getWindingRule()==PathIterator.WIND_EVEN_ODD) {
                s.append("fill-rule:evenodd;");
            } else {
                s.append("fill-rule:nonzero;");
            }
        }

        os.println("<g "+style(s.toString())+">");
        writePath(path);
        os.println("</g> <!-- fillAndDraw -->");
    }

    /* 5.2. Images */
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        writeWarning(getClass()+": copyArea(int, int, int, int, int, int) not implemented.");
    }

    protected void writeImage(RenderedImage image, AffineTransform xform, Color bkg) throws IOException {

        if ((xform != null) && !xform.isIdentity()) {
            os.println("<g transform=\"matrix("+fixedPrecision(xform.getScaleX())+", "+
                                                fixedPrecision(xform.getShearY())+", "+
                                                fixedPrecision(xform.getShearX())+", "+
                                                fixedPrecision(xform.getScaleY())+", "+
                                                fixedPrecision(xform.getTranslateX())+", "+
                                                fixedPrecision(xform.getTranslateY())+")\">");
        }
        os.print("<image x=\"0\" y=\"0\" "+
                        "width=\""+image.getWidth()+"\" "+
                        "height=\""+image.getHeight()+"\" "+
                        "xlink:href=\"");

        String writeAs = getProperty(WRITE_IMAGES_AS);
        boolean isTransparent = image.getColorModel().hasAlpha() && (bkg == null);

        byte[] pngBytes = null;
        if (writeAs.equals(ImageConstants.PNG) ||
            writeAs.equals(ImageConstants.SMALLEST) ||
            isTransparent) {
            ByteArrayOutputStream png = new ByteArrayOutputStream();
            ImageGraphics2D.writeImage(image, "png", new Properties(), png);
            png.close();
            pngBytes = png.toByteArray();
        }

        byte[] jpgBytes = null;
        if ((writeAs.equals(ImageConstants.JPG) ||
             writeAs.equals(ImageConstants.SMALLEST)) &&
            !isTransparent) {
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
            encode = (jpgBytes.length < 0.5*pngBytes.length) ? "jpg" : "png";
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
            String dirName = (pos < 0) ? "" : filename.substring(0,pos+1);
            String imageName = (pos < 0) ? filename : filename.substring(pos+1);
            imageName += "."+getProperty(EXPORT_SUFFIX)+"-"+imageNumber+"."+encode;

            os.print(imageName);

            // write the image separately
            FileOutputStream imageStream = new FileOutputStream(dirName + imageName);

            imageStream.write(imageBytes);
            imageStream.close();
        } else {
            os.println("data:image/"+encode+";base64,");
            Base64OutputStream b64 = new Base64OutputStream(new WriterOutputStream(os));
            b64.write(imageBytes);
            b64.finish();
        }

        os.println("\"/>");
        if ((xform != null) && !xform.isIdentity()) {
            os.println("</g> <!-- transform -->");
        }
    }

    /* 5.3. Strings */
    protected void writeString(String str, double x, double y) throws IOException {
        str = FontEncoder.getEncodedString(str, getFont().getName());
        AffineTransform t = getFont().getTransform();
        if (!t.isIdentity()) {
            os.println("<g transform=\"matrix("+fixedPrecision(t.getScaleX())+", "+
                                                fixedPrecision(t.getShearY())+", "+
                                                fixedPrecision(t.getShearX())+", "+
                                                fixedPrecision(t.getScaleY())+", "+
                                                fixedPrecision(t.getTranslateX())+", "+
                                                fixedPrecision(t.getTranslateY())+")\">");
        }
        os.println("<text "+style(color(null, getPaint()))+
                   " x=\""+fixedPrecision(x)+"\" y=\""+fixedPrecision(y)+"\">");
        os.println(XMLWriter.normalizeText(str));
        os.println("</text>");
        if (!t.isIdentity()) {
            os.println("</g> <!-- transform -->");
        }
    }

    public void drawString(String str, double x, double y, int horizontal, int vertical,
                           boolean framed, Color frameColor, double frameWidth,
                           boolean banner, Color bannerColor) {

        str = FontEncoder.getEncodedString(str, getFont().getName());

        LineMetrics metrics = getFont().getLineMetrics(str, getFontRenderContext());
        double w = getFont().getStringBounds(str, getFontRenderContext()).getWidth();
        double h = metrics.getHeight();
        double d = metrics.getDescent();
        double adjustment = (getFont().getSize2D()*2)/10;

        double ny = getYalignment(y, h, d, vertical);
        double nx = getXalignment(x, w, horizontal);

        // Calculate the box size for the banner.
        double rx = nx-adjustment;
        double ry = ny-h+d-adjustment;
        double rw = w+2*adjustment;
        double rh = h+2*adjustment;

        if (banner) {
            Color color = getColor();
            setColor(bannerColor);
            fillRect(rx, ry, rw, rh);
            setColor(color);
        }

        if (framed) {
            Color color = getColor();
            setColor(frameColor);
            Stroke s = getStroke();
            setLineWidth(frameWidth);
            drawRect(rx, ry, rw, rh);
            setColor(color);
            setStroke(s);
        }

        AffineTransform t = getFont().getTransform();
        if (!t.isIdentity()) {
            os.println("<g transform=\"matrix("+fixedPrecision(t.getScaleX())+", "+
                                                fixedPrecision(t.getShearY())+", "+
                                                fixedPrecision(t.getShearX())+", "+
                                                fixedPrecision(t.getScaleY())+", "+
                                                fixedPrecision(t.getTranslateX())+", "+
                                                fixedPrecision(t.getTranslateY())+")\">");
        }
        os.println("<text "+style(color(null, getPaint())+getAlignmentString(horizontal, vertical, metrics))+
                   " x=\""+fixedPrecision(x)+"\" y=\""+fixedPrecision(y)+"\">");
        os.println(XMLWriter.normalizeText(str));
        os.println("</text>");
        if (!t.isIdentity()) {
            os.println("</g> <!-- transform -->");
        }
    }

    public void drawString(TagString str, double x, double y, int horizontal, int vertical,
                           boolean framed, Color frameColor, double frameWidth,
                           boolean banner, Color bannerColor) {
        SVGTagHandler tagHandler = new SVGTagHandler(isProperty(STYLABLE), getFont(), getFontRenderContext());

        double nx = getXalignment(x, tagHandler.stringWidth(str), horizontal);
        LineMetrics metrics = getFont().getLineMetrics(str.toString(), getFontRenderContext());
        double w = tagHandler.stringWidth(str);
        double h = metrics.getHeight();
        double d = metrics.getDescent();
        double adjustment = (getFont().getSize2D()*2)/10;

        double ny = getYalignment(y, h, d, vertical);

        // Calculate the box size for the banner.
        double rx = nx-adjustment;
        double ry =  ny-h+d-adjustment;
        double rw = w+2*adjustment;
        double rh = h+2*adjustment;

        if (banner) {
            Color color = getColor();
            setColor(bannerColor);
            fillRect(rx, ry, rw, rh);
            setColor(color);
        }

        if (framed) {
            Color color = getColor();
            setColor(frameColor);
            Stroke s = getStroke();
            setLineWidth(frameWidth);
            drawRect(rx, ry, rw, rh);
            setColor(color);
            setStroke(s);
        }

        String string = tagHandler.parse(str);
        string = FontEncoder.getEncodedString(string, getFont().getName());

        AffineTransform t = getFont().getTransform();
        if (!t.isIdentity()) {
            os.println("<g transform=\"matrix("+fixedPrecision(t.getScaleX())+", "+
                                                fixedPrecision(t.getShearY())+", "+
                                                fixedPrecision(t.getShearX())+", "+
                                                fixedPrecision(t.getScaleY())+", "+
                                                fixedPrecision(t.getTranslateX())+", "+
                                                fixedPrecision(t.getTranslateY())+")\">");
        }
        os.println("<text "+style(color(null, getPaint())+getAlignmentString(horizontal, vertical, metrics))+
                   " x=\""+fixedPrecision(x)+"\" y=\""+fixedPrecision(y)+"\">");
        os.println(string);
        os.println("</text>");
        if (!t.isIdentity()) {
            os.println("</g> <!-- transform -->");
        }
    }

    /*================================================================================
     | 6. Transformations
     *================================================================================*/
    protected void writeTransform(AffineTransform transform) throws IOException {
        os.println("<g transform=\"matrix("+
                   fixedPrecision(transform.getScaleX())+","+
                   fixedPrecision(transform.getShearY())+","+
                   fixedPrecision(transform.getShearX())+","+
                   fixedPrecision(transform.getScaleY())+","+
                   fixedPrecision(transform.getTranslateX())+","+
                   fixedPrecision(transform.getTranslateY())+")\">");
        closeTags.push("</g> <!-- transform -->");
    }

    /*================================================================================
     | 7. Clipping
     *================================================================================*/
    protected void writeClip(Rectangle2D r2d) throws IOException {
        writeClip((Shape)r2d);
    }

    protected void writeClip(Shape s) throws IOException {
        if (s == null) {
            currentClipNumber = -1;
            return;
        }

        PathIterator path = s.getPathIterator(null);

        currentClipNumber = clipNumber.getInt();
        clipNumber.set(currentClipNumber+1);
        os.println("<clipPath id=\"clip"+currentClipNumber+"\">");
        writePath(path);
        os.println("</clipPath>");
        os.println("<g clip-path=\"url(#clip"+currentClipNumber+")\">");
        closeTags.push("</g> <!-- clip -->");
    }

    /*================================================================================
     | 8. Graphics State
     *================================================================================*/
    /* 8.1. stroke/linewidth */
    protected void writeWidth(float width) throws IOException {
        // width of 0 means thinnest line, which does not exist in SVG
        if (width == 0) width = 0.000001f;
        os.println("<g "+style("stroke-width:"+fixedPrecision(width))+">");
        closeTags.push("</g> <!-- stroke width -->");
    }

    protected void writeCap(int cap) throws IOException {
        os.print("<g ");
        switch (cap) {
            default:
            case BasicStroke.CAP_BUTT:
                os.print(style("stroke-linecap:butt"));
                break;
            case BasicStroke.CAP_ROUND:
                os.print(style("stroke-linecap:round"));
                break;
            case BasicStroke.CAP_SQUARE:
                os.print(style("stroke-linecap:square"));
                break;
        }
        os.println(">");
        closeTags.push("</g> <!-- stroke cap -->");
    }

    protected void writeJoin(int join) throws IOException {
        os.print("<g ");
        switch (join) {
            default:
            case BasicStroke.JOIN_MITER:
                os.print(style("stroke-linejoin:miter"));
                break;
            case BasicStroke.JOIN_ROUND:
                os.print(style("stroke-linejoin:round"));
                break;
            case BasicStroke.JOIN_BEVEL:
                os.print(style("stroke-linejoin:bevel"));
                break;
        }
        os.println(">");
        closeTags.push("</g> <!-- stroke join -->");
    }

    protected void writeMiterLimit(float limit) throws IOException {
        os.println("<g "+style("stroke-miterlimit:"+fixedPrecision(limit))+">");
        closeTags.push("</g> <!-- stroke limit -->");
    }

    protected void writeDash(double[] dash, double phase) throws IOException {
        os.print("<g ");
        StringBuffer s = new StringBuffer();
        s.append("stroke-dasharray:");
        if (dash.length > 0) {
            for (int i=0; i<dash.length; i++) {
                if (i > 0) s.append(",");
                s.append(fixedPrecision(dash[i]));
            }
            s.append(";");
        } else {
            s.append("none;");
        }
        s.append("stroke-dashoffset:"+fixedPrecision(phase));
        os.println(style(s.toString())+">");
        closeTags.push("</g> <!-- stroke dash -->");
    }

    /* 8.2. paint/color */
    public void setPaintMode() {
        writeWarning(getClass()+": setPaintMode() not implemented.");
    }

    public void setXORMode(Color c1) {
        writeWarning(getClass()+": setXORMode(Color) not implemented.");
    }

    protected void writePaint(Color c) throws IOException {
        // written with every draw
    }

    protected void writePaint(GradientPaint paint) throws IOException {
        if (gradients.get(paint) == null) {
            String name = "gradient-"+gradients.size();
            gradients.put(paint, name);
            GradientPaint gp = (GradientPaint)paint;
            Point2D p1 = gp.getPoint1();
            Point2D p2 = gp.getPoint2();
            os.println("<defs>");
            os.print("  <linearGradient id=\""+name+"\" ");
            os.print("x1=\""+fixedPrecision(p1.getX())+"\" ");
            os.print("y1=\""+fixedPrecision(p1.getY())+"\" ");
            os.print("x2=\""+fixedPrecision(p2.getX())+"\" ");
            os.print("y2=\""+fixedPrecision(p2.getY())+"\" ");
            os.print("gradientUnits=\"userSpaceOnUse\" ");
            os.print("spreadMethod=\""+((gp.isCyclic()) ? "reflect" : "pad")+"\" ");
            os.println(">");
            os.println("    <stop offset=\"0\" stop-color=\""+hexColor(gp.getColor1())+"\" "+
                                              "opacity-stop=\""+alphaColor(gp.getColor1())+"\" />");
            os.println("    <stop offset=\"1\" stop-color=\""+hexColor(gp.getColor2())+"\" "+
                                              "opacity-stop=\""+alphaColor(gp.getColor2())+"\" />");
            os.println("  </linearGradient>");
            os.println("</defs>");
        }
        os.println("<g "+style("stroke:"+hexColor(getPaint()))+">");
        closeTags.push("</g> <!-- color -->");
    }

    protected void writePaint(TexturePaint paint) throws IOException {
        if (textures.get(paint) == null) {
            String name = "texture-"+textures.size();
            textures.put(paint, name);
            TexturePaint tp = (TexturePaint)paint;
            BufferedImage image = tp.getImage();
            Rectangle2D rect = tp.getAnchorRect();
            os.println("<defs>");
            os.print("  <pattern id=\""+name+"\" ");
            os.print("x=\"0\" ");
            os.print("y=\"0\" ");
            os.print("width=\""+fixedPrecision(image.getWidth())+"\" ");
            os.print("height=\""+fixedPrecision(image.getHeight())+"\" ");
            os.print("patternUnits=\"userSpaceOnUse\" ");
            os.print("patternTransform=\"matrix("+
                               fixedPrecision(rect.getWidth()/image.getWidth())+","+
                               "0.0,0.0,"+
                               fixedPrecision(rect.getHeight()/image.getHeight())+","+
                               fixedPrecision(rect.getX())+","+
                               fixedPrecision(rect.getY())+
                     ")\" ");
            os.println(">");
            writeImage(image, null, null);
            os.println("  </pattern>");
            os.println("</defs>");
        }
        os.println("<g "+style("stroke:"+hexColor(getPaint()))+">");
        closeTags.push("</g> <!-- color -->");
    }

    protected void writePaint(Paint p) throws IOException {
        writeWarning(getClass()+": writePaint(Paint) not implemented for "+p.getClass());
    }

    private static final Properties replaceFonts = new Properties();
    static {
        replaceFonts.setProperty("Dialog",          "sans-serif");
        replaceFonts.setProperty("DialogInput",     "sans-serif");
        replaceFonts.setProperty("Serif",           "serif");
        replaceFonts.setProperty("SansSerif",       "sans-serif");
        replaceFonts.setProperty("Monospaced",      "monospace");
        replaceFonts.setProperty("Symbol",          "serif");
        replaceFonts.setProperty("ZapfDingbats",    "serif");

        replaceFonts.setProperty("TimesRoman",      "serif");
        replaceFonts.setProperty("Helvetica",       "sans-serif");
        replaceFonts.setProperty("Courier",         "monospace");
    }

    /* 8.3. font */
    /**
     * Method sets the current font.  This method makes a reasonable
     * guess for the desired SVG font since the names of the
     * actual SVG fonts is implementation dependent.
     * Currently, this tries to identify Helvetica, Times, Courier,
     * Symbol, and ZapfDingbats fonts.  If all else fails, Helvetica
     * is used. */
    public void setFont(Font font) {
        super.setFont(font);

        StringBuffer svgFont = new StringBuffer();

        svgFont.append("font-family:");

        String fontName = font.getName();
        svgFont.append(replaceFonts.getProperty(fontName, fontName));

        if (font.isBold()) {
            svgFont.append(";font-weight:bold");
        } else {
            svgFont.append(";font-weight:normal");
        }

        if (font.isItalic()) {
            svgFont.append(";font-style:italic");
        } else {
            svgFont.append(";font-style:normal");
        }

        int size = font.getSize();
        svgFont.append(";font-size:"+size);
        os.println("<g "+style(svgFont.toString())+">");
        closeTags.push("</g> <!-- font -->");
    }

    /*================================================================================
     | 9. Auxiliary
     *================================================================================*/
    public GraphicsConfiguration getDeviceConfiguration() {
        writeWarning(getClass()+": getDeviceConfiguration() not implemented.");
        return null;
    }

    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        writeWarning(getClass()+": hit(Rectangle, Shape, boolean) not implemented.");
        return false;
    }

    public void writeComment(String s) throws IOException {
        os.println("<!-- "+s+" -->");
    }

    public String toString() {
        return "SVGGraphics2D";
    }

    /*================================================================================
     | 10. Private/Utility Methos
     *================================================================================*/
    private String color(Paint stroke, Paint fill) {
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
            return (float)(getPrintColor((Color)p).getAlpha()/255.0);
        } else if (p instanceof GradientPaint) {
            return 1.0f;
        } else if (p instanceof TexturePaint) {
            return 1.0f;
        }
        writeWarning(getClass()+": alphaColor() not implemented for "+p.getClass()+".");
        return 1.0f;
    }

    private String hexColor(Paint p) {
        if (p instanceof Color) {
            return hexColor(getPrintColor((Color)p));
        } else if (p instanceof GradientPaint) {
            return hexColor((GradientPaint)p);
        } else if (p instanceof TexturePaint) {
            return hexColor((TexturePaint)p);
        }
        writeWarning(getClass()+": hexColor() not implemented for "+p.getClass()+".");
        return "#000000";
    }

    private String hexColor(Color c) {
        String s1 = Integer.toHexString(c.getRed());
        s1 = (s1.length() != 2) ? "0"+s1 : s1;

        String s2 = Integer.toHexString(c.getGreen());
        s2 = (s2.length() != 2) ? "0"+s2 : s2;

        String s3 = Integer.toHexString(c.getBlue());
        s3 = (s3.length() != 2) ? "0"+s3 : s3;

        return "#"+s1+s2+s3;
    }

    private String hexColor(GradientPaint p) {
        return "url(#"+gradients.get(p)+")";
    }

    private String hexColor(TexturePaint p) {
        return "url(#"+textures.get(p)+")";
    }

    /**
     * Write the path to the output file.
     */
    private void writePath(PathIterator path) {

        double[] coords = new double[6];
        double currentX = 0.;
        double currentY = 0.;
        os.print("<path d=\"");
        while (!path.isDone()) {
            int segType = path.currentSegment(coords);

            switch (segType) {
            case PathIterator.SEG_MOVETO:
                os.print("M "+fixedPrecision(coords[0])+" "+
                              fixedPrecision(coords[1])+" ");
                currentX = coords[0];
                currentY = coords[1];
                break;
            case PathIterator.SEG_LINETO:
                os.print("L "+fixedPrecision(coords[0])+" "+
                              fixedPrecision(coords[1])+" ");
                currentX = coords[0];
                currentY = coords[1];
                break;
            case PathIterator.SEG_CUBICTO:
                os.print("C "+fixedPrecision(coords[0])+" "+
                              fixedPrecision(coords[1])+" "+
                              fixedPrecision(coords[2])+" "+
                              fixedPrecision(coords[3])+" "+
                              fixedPrecision(coords[4])+" "+
                              fixedPrecision(coords[5])+" ");
                currentX = coords[4];
                currentY = coords[5];
                break;
            case PathIterator.SEG_QUADTO:
                os.print("Q "+fixedPrecision(coords[0])+" "+
                              fixedPrecision(coords[1])+" "+
                              fixedPrecision(coords[2])+" "+
                              fixedPrecision(coords[3])+" ");
                currentX = coords[2];
                currentY = coords[3];
                break;
            case PathIterator.SEG_CLOSE:
                os.print("z ");
                currentX = 0.;
                currentY = 0.;
                break;
            }

            // Move to the next segment.
            path.next();
        }
        os.println("\"/>");
    }

    private String defaultStyle() {
        // FIXME: should be currentWidth...
        return style(color(getPaint(), null)+
                     "stroke-width:1;"+
                     "stroke-linecap:square");
    }

    private String style(String stylableString) {
        return style(isProperty(STYLABLE), stylableString);
    }

    static String style(boolean stylable, String stylableString) {
        if ((stylableString == null) || (stylableString.equals(""))) return "";

        if (stylable) return "style=\""+stylableString+"\"";

        StringBuffer r = new StringBuffer();
        StringTokenizer st1 = new StringTokenizer(stylableString, ";");
        while (st1.hasMoreTokens()) {
            String s = st1.nextToken();
            int colon = s.indexOf(':');
            if (colon >= 0) {
                r.append(s.substring(0,colon));
                r.append("=\"");
                r.append(s.substring(colon+1));
                r.append("\" ");
            }
        }
        return r.toString();
    }

    private String getAlignmentString(int horizontal, int vertical, LineMetrics metrics) {
        String textAnchor;
        switch(horizontal) {
            case TEXT_CENTER:
                textAnchor = "middle";
                break;
            case TEXT_RIGHT:
                textAnchor = "end";
                break;
            case TEXT_LEFT:
            default:
                textAnchor = "start";
                break;
        }

    // FIXME not a very good job yet. For tagstrings this does not work very well.
        double alignmentBaseline;
        switch(vertical) {
            case TEXT_TOP:
                alignmentBaseline = -100*(metrics.getAscent()+metrics.getLeading())/metrics.getHeight();
                break;
            case TEXT_CENTER:
                alignmentBaseline = -50*metrics.getAscent()/metrics.getHeight();
                break;
            case TEXT_BOTTOM:
                alignmentBaseline = metrics.getDescent()/metrics.getHeight();
                break;
            case TEXT_BASELINE:
            default:
                alignmentBaseline = 0;
                break;
        }
        return "text-anchor:"+textAnchor+";"+"baseline-shift:"+fixedPrecision(alignmentBaseline)+"%";
    }

    private static ScientificFormat scientific = new ScientificFormat(5, 8, false);
    public static String fixedPrecision(double d) {
        return scientific.format(d);
    }
}
