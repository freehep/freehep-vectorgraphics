// Copyright 2003-2005, FreeHEP.
package org.freehep.graphicsio.java;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TooManyListenersException;
import java.util.TreeSet;

import org.freehep.graphics2d.TagString;
import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.png.PNGEncoder;
import org.freehep.util.UserProperties;
import org.freehep.util.Value;
import org.freehep.util.io.IndentPrintWriter;
import org.freehep.util.io.LineNumberWriter;

/**
 * Exports the java calls made to Graphics2D as source code, with the associated
 * class, field and method definitions, resulting in a class which, when run
 * will produce the same display. Generating such source code may be helpful in
 * setting up test cases without a lot of machinery around it and in debugging
 * problems for different formats.
 * 
 * Due to size limitations in the bytecode, see Lindholm and Yellin, "The Java
 * Virtual Machine Specification", p. 136-137, 1997, the following was taken
 * into account:
 * <ul>
 * <li>Maximum size of bytecode is 65535 per method; we chain paint() methods
 * and limit them by number of lines (our best approximation for bytecode).
 * <li>Maximum number of constants is 65535 per class; we generate separate
 * inner classes for each paint method (see above) so that the number of
 * constants is limited.
 * </ul>
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-java/src/main/java/org/freehep/graphicsio/java/JAVAGraphics2D.java 35198018c7cc 2006/03/09 18:45:26 duns $
 */
public class JAVAGraphics2D extends VectorGraphics implements
        LineNumberWriter.LineNumberListener {

    private static final int MAX_LINES_PER_METHOD = 200;

    private Map /* <VectorGraphics, Value> */vg = new HashMap();

    private SortedSet /*<String>*/ imports = new TreeSet();
    
    private Value vgIndex = new Value().set(0);

    private Value paintSequenceNo = new Value().set(0);

    private Value blockLevel = new Value().set(0);

    private UserProperties properties;

    private int width = 800;

    private int height = 600;

    private String className = "TemporaryName";

    private OutputStream os;
    
    private ByteArrayOutputStream bos;
    
    private LineNumberWriter lineWriter;

    private IndentPrintWriter out;

    private Color backgroundColor;

    private Color color = Color.WHITE;

    private Paint paint = Color.BLACK;

    private String creator = "FreeHEP JAVAGraphics2D";

    private int colorMode = 0; // CHECK

    private Stroke stroke = new BasicStroke(); // CHECK

    private AffineTransform transform = new AffineTransform();

    private RenderingHints hints = new RenderingHints(null);

    private boolean isDeviceIndependent;

    private Composite composite;

    private Shape clip;

    private Font font = new Font("Serif", Font.PLAIN, 12);

    private static final String rootKey = JAVAGraphics2D.class.getName();

    public static final String PACKAGE_NAME = rootKey + ".PackageName";

    private static final UserProperties defaultProperties = new UserProperties();
    static {
        defaultProperties.setProperty(PACKAGE_NAME, "");
    }

    public static Properties getDefaultProperties() {
        return defaultProperties;
    }

    public static void setDefaultProperties(Properties newProperties) {
        defaultProperties.setProperties(newProperties);
    }

    public final static String version = "$Revision$";

    public JAVAGraphics2D(File file, Dimension size)
            throws FileNotFoundException {
        this(new FileOutputStream(file), size);
        init(file);
    }

    public JAVAGraphics2D(File file, Component component)
            throws FileNotFoundException {
        this(new FileOutputStream(file), component);
        init(file);
    }

    public JAVAGraphics2D(OutputStream os, Dimension size) {
        super();
        init(os);
        width = size.width;
        height = size.height;
    }

    public JAVAGraphics2D(OutputStream os, Component component) {
        super();
        init(os);
        width = component.getWidth();
        height = component.getHeight();
        font = component.getFont();
        color = component.getForeground();
        paint = component.getForeground();
        backgroundColor = component.getBackground();
    }

    private void init(File file) {
        init();
        className = file.getName().substring(0,
                file.getName().lastIndexOf(".java"));
    }

    private void init(OutputStream os) {
        init();
        this.os = os;
        vg.put(this, new Integer(vgIndex.getInt()));
        bos = new ByteArrayOutputStream();
        lineWriter = new LineNumberWriter(new OutputStreamWriter(bos));
        lineWriter.setLineNumber(1);
        out = new IndentPrintWriter(lineWriter);
        out.setIndentString("    ");
        try {
            lineWriter.addLineNumberListener(this, MAX_LINES_PER_METHOD);
        } catch (TooManyListenersException tmle) {
            System.err.println(tmle);
        }
    }

    private void init() {
        properties = new UserProperties();
        isDeviceIndependent = false;
        composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
    }

    protected JAVAGraphics2D(JAVAGraphics2D graphics) {
        vg = graphics.vg;
        vgIndex = graphics.vgIndex;
        imports = graphics.imports;
        paintSequenceNo = graphics.paintSequenceNo;
        blockLevel = graphics.blockLevel;

        properties = graphics.properties;
        width = graphics.width;
        height = graphics.height;
        className = graphics.className;
        os = graphics.os;
        bos = graphics.bos;
        out = graphics.out;
        backgroundColor = graphics.backgroundColor;
        color = graphics.color;
        paint = graphics.paint;
        creator = graphics.creator;
        colorMode = graphics.colorMode;
        stroke = graphics.stroke;
        transform = graphics.transform;
        hints = graphics.hints;

        isDeviceIndependent = graphics.isDeviceIndependent;
        composite = graphics.composite;
        clip = graphics.clip;
        font = graphics.font;

        vgIndex.set(vgIndex.getInt() + 1);
        vg.put(this, new Integer(vgIndex.getInt()));
    }

    public void setProperties(Properties newProperties) {
        if (newProperties == null)
            return;
        properties.setProperties(newProperties);
    }

    protected void initProperties(Properties defaults) {
        properties = new UserProperties();
        properties.setProperties(defaults);
    }

    public Properties getProperties() {
        return properties;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public Color getPropertyColor(String key) {
        return properties.getPropertyColor(key);
    }

    public Rectangle getPropertyRectangle(String key) {
        return properties.getPropertyRectangle(key);
    }

    public Insets getPropertyInsets(String key) {
        return properties.getPropertyInsets(key);
    }

    public Dimension getPropertyDimension(String key) {
        return properties.getPropertyDimension(key);
    }

    public int getPropertyInt(String key) {
        return properties.getPropertyInt(key);
    }

    public double getPropertyDouble(String key) {
        return properties.getPropertyDouble(key);
    }

    public boolean isProperty(String key) {
        return properties.isProperty(key);
    }

    public void clearRect(int x, int y, int width, int height) {
        out.println(vg() + ".clearRect(" + x + ", " + y + ", " + width + ", "
                + height + ");");
    }

    public void clipRect(int x, int y, int width, int height) {
        out.println(vg() + ".clipRect(" + x + ", " + y + ", " + width + ", "
                + height + ");");
        clip = new Rectangle(x, y, width, height);
    }

    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        out.println(vg() + ".copyArea(" + x + ", " + y + ", " + width + ", "
                + height + ", " + dx + ", " + dy + ");");
    }

    public Graphics create() {
        JAVAGraphics2D g = new JAVAGraphics2D(this);
        out.println(g.vg() + " = (VectorGraphics)" + vg() + ".create();");
        return g;
    }

    public Graphics create(int x, int y, int width, int height) {
        JAVAGraphics2D g = new JAVAGraphics2D(this);
        out.println(g.vg() + " = (VectorGraphics)" + vg() + ".create(" + x
                + ", " + y + ", " + width + ", " + height + ");");
        return g;
    }

    public Graphics create(double x, double y, double width, double height) {
        JAVAGraphics2D g = new JAVAGraphics2D(this);
        out.println(g.vg() + " = (VectorGraphics)" + vg() + ".create(" + x
                + ", " + y + ", " + width + ", " + height + ");");
        return g;
    }

    public void dispose() {
        out.println(vg() + ".dispose();");
    }

    public void draw3DRect(int x, int y, int width, int height, boolean raised) {
        out.println(vg() + ".draw3DRect(" + x + ", " + y + ", " + width + ", "
                + height + ", " + raised + ");");
    }

    public void fill3DRect(int x, int y, int width, int height, boolean raised) {
        out.println(vg() + ".fill3DRect(" + x + ", " + y + ", " + width + ", "
                + height + ", " + raised + ");");
    }

    public void drawArc(int x, int y, int width, int height, int startAngle,
            int arcAngle) {
        out.println(vg() + ".drawArc(" + x + ", " + y + ", " + width + ", "
                + height + ", " + startAngle + ", " + arcAngle + ");");
    }

    public void fillArc(int x, int y, int width, int height, int startAngle,
            int arcAngle) {
        out.println(vg() + ".drawArc(" + x + ", " + y + ", " + width + ", "
                + height + ", " + startAngle + ", " + arcAngle + ");");
    }

    public void drawBytes(byte[] data, int offset, int length, int x, int y) {
        out.print(vg() + ".drawBytes(");
        write(data, offset + length);
        out.println(", " + offset + ", " + length + ", " + x + ", " + y + ");");
    }

    public void drawChars(char[] data, int offset, int length, int x, int y) {
        block();
        out.print(vg() + ".drawChars(");
        write(data, offset + length);
        unblock();
        out.println(", " + offset + ", " + length + ", " + x + ", " + y + ");");
    }

    public boolean drawImage(Image image, int x, int y, ImageObserver observer) {
        block();
        out.print(vg() + ".drawImage(");
        write(image);
        unblock();
        out.println(", " + x + ", " + y + ", " + "null" + ");");
        return true;
    }

    public boolean drawImage(Image image, int x, int y, int width, int height,
            ImageObserver observer) {
        block();
        out.print(vg() + ".drawImage(");
        write(image);
        unblock();
        out.println(", " + x + ", " + y + ", " + width + ", " + height + ", "
                + "null" + ");");
        return true;
    }

    public boolean drawImage(Image image, int x, int y, Color bgColor,
            ImageObserver observer) {
        block();
        out.print(vg() + ".drawImage(");
        write(image);
        out.print(", " + x + ", " + y + ", ");
        write(bgColor);
        unblock();
        out.println(", " + "null" + ");");
        return true;
    }

    public boolean drawImage(Image image, int x, int y, int width, int height,
            Color bgColor, ImageObserver observer) {
        block();
        out.print(vg() + ".drawImage(");
        write(image);
        out.print(", " + x + ", " + y + ", " + width + ", " + height + ", ");
        write(bgColor);
        unblock();
        out.println(", " + "null" + ");");
        return true;
    }

    public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2,
            int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        block();
        out.print(vg() + ".drawImage(");
        write(image);
        out.print(", " + dx1 + ", " + dy1 + ", " + dx2 + ", " + dy2);
        out.print(", " + sx1 + ", " + sy1 + ", " + sx2 + ", " + sy2);
        unblock();
        out.println(", " + "null" + ");");
        return true;
    }

    public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2,
            int sx1, int sy1, int sx2, int sy2, Color bgColor,
            ImageObserver observer) {
        block();
        out.print(vg() + ".drawImage(");
        write(image);
        out.print(", " + dx1 + ", " + dy1 + ", " + dx2 + ", " + dy2);
        out.print(", " + sx1 + ", " + sy1 + ", " + sx2 + ", " + sy2 + ", ");
        write(bgColor);
        unblock();
        out.println(", " + "null" + ");");
        return true;
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        out.println(vg() + ".drawLine(" + x1 + ", " + y1 + ", " + x2 + ", "
                + y2 + ");");
    }

    public void drawOval(int x, int y, int width, int height) {
        out.println(vg() + ".drawOval(" + x + ", " + y + ", " + width + ", "
                + height + ");");
    }

    public void fillOval(int x, int y, int width, int height) {
        out.println(vg() + ".fillOval(" + x + ", " + y + ", " + width + ", "
                + height + ");");
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        block();
        out.print(vg() + ".drawPolygon(");
        write(xPoints);
        out.print(", ");
        write(yPoints);
        unblock();
        out.println(", " + nPoints + ");");
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        block();
        out.print(vg() + ".fillPolygon(");
        write(xPoints);
        out.print(", ");
        write(yPoints);
        unblock();
        out.println(", " + nPoints + ");");
    }

    public void drawPolygon(Polygon p) {
        block();
        out.print(vg() + ".drawPolygon(");
        write(p);
        unblock();
        out.println(");");
    }

    public void fillPolygon(Polygon p) {
        block();
        out.print(vg() + ".fillPolygon(");
        write(p);
        unblock();
        out.println(");");
    }

    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        block();
        out.print(vg() + ".drawPolyline(");
        write(xPoints);
        out.print(", ");
        write(yPoints);
        unblock();
        out.println(", " + nPoints + ");");
    }

    public void drawRect(int x, int y, int width, int height) {
        out.println(vg() + ".drawRect(" + x + ", " + y + ", " + width + ", "
                + height + ");");
    }

    public void fillRect(int x, int y, int width, int height) {
        out.println(vg() + ".fillRect(" + x + ", " + y + ", " + width + ", "
                + height + ");");
    }

    public void drawRoundRect(int x, int y, int width, int height,
            int arcWidth, int arcHeight) {
        out.println(vg() + ".drawRoundRect(" + x + ", " + y + ", " + width
                + ", " + height + ", " + arcWidth + ", " + arcHeight + ");");
    }

    public void fillRoundRect(int x, int y, int width, int height,
            int arcWidth, int arcHeight) {
        out.println(vg() + ".fillRoundRect(" + x + ", " + y + ", " + width
                + ", " + height + ", " + arcWidth + ", " + arcHeight + ");");
    }

    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        out
                .println("System.err.println(\""
                        + getClass()
                        + ": drawString(AttributedCharacterIterator, int int) not implemented.\");");
    }

    public void drawString(String str, int x, int y) {
        block();
        out.print(vg() + ".drawString(");
        write(str);
        unblock();
        out.println(", " + x + ", " + y + ");");
    }

    public void finalize() {
        out.println(vg() + ".finalize();");
    }

    public Shape getClip() {
        return clip;
    }

    public Rectangle getClipBounds() {
        return (clip == null) ? null : clip.getBounds();
    }

    public Rectangle getClipBounds(Rectangle r) {
        Rectangle c = getClipBounds();
        if (c != null) {
            r.x = c.x;
            r.y = c.y;
            r.width = c.width;
            r.height = c.height;
        }
        return r;
    }

    /**
     * @deprecated probably forwards to getClipBounds()
     */
    // public Rectangle getClipRect() {
    // return null;
    // }
    public Color getColor() {
        return color;
    }

    public Font getFont() {
        return font;
    }

    public FontMetrics getFontMetrics() {
        return getFontMetrics(getFont());
    }
    
    /**
     * @param font
     * @deprecated
     */
    public FontMetrics getFontMetrics(Font font) {
        return Toolkit.getDefaultToolkit().getFontMetrics(font);
    }

    public boolean hitClip(int x, int y, int width, int height) {
        return clip.intersects(x, y, width, height);
    }

    public void setClip(int x, int y, int width, int height) {
        out.println(vg() + ".setClip(" + x + ", " + y + ", " + width + ", "
                + height + ");");
        clip = new Rectangle(x, y, width, height);
    }

    public void setClip(Shape clip) {
        block();
        out.print(vg() + ".setClip(");
        write(clip);
        unblock();
        out.println(");");
        this.clip = clip;
    }

    public void setColor(Color c) {
        block();
        out.print(vg() + ".setColor(");
        write(c);
        unblock();
        out.println(");");
        color = c;
    }

    public void setFont(Font font) {
        block();
        out.print(vg() + ".setFont(");
        write(font);
        unblock();
        out.println(");");
        this.font = font;
    }

    public void setPaintMode() {
        out.println(vg() + ".setPaintMode();");
    }

    public void setXORMode(Color c1) {
        block();
        out.print(vg() + ".setXORMode(");
        write(c1);
        unblock();
        out.println(");");
    }

    public String toString() {
        return "JavaGraphics2D";
    }

    public void translate(int x, int y) {
        out.println(vg() + ".translate(" + x + ", " + y + ");");
    }

    public void addRenderingHints(Map hints) {
        // FIXME call should be written out
        hints.putAll(hints);
    }

    public void clip(Shape s) {
        block();
        out.print(vg() + ".clip(");
        write(s);
        unblock();
        out.println(");");
        clip = s;
    }

    public void draw(Shape s) {
        block();
        out.print(vg() + ".draw(");
        write(s);
        unblock();
        out.println(");");
    }

    public void drawGlyphVector(GlyphVector g, float x, float y) {
        fill(g.getOutline(x, y));
    }

    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        out
                .println("System.err.println(\""
                        + getClass()
                        + ": drawImage(BufferedImage, BufferedImageOp, int, int) not implemented.\");");
    }

    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        block();
        out.print(vg() + ".drawImage(");
        write(img);
        out.print(", ");
        write(xform);
        unblock();
        out.println(", " + "null" + ");");
        return true;
    }

    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        out
                .println("System.err.println(\""
                        + getClass()
                        + ": drawRenderableImage(RenderableImage, AffineTransform) not implemented.\");");
    }

    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        out
                .println("System.err.println(\""
                        + getClass()
                        + ": drawRenderedImage(RenderedImage, AffineTransform) not implemented.\");");
    }

    public void drawString(AttributedCharacterIterator iterator, float x,
            float y) {
        // NOTE: ignores all attributes,
        // a better implementation can be found at:
        // http://cvs.sourceforge.net/viewcvs.py/itext/src/com/lowagie/text/pdf/PdfGraphics2D.java?rev=1.27&view=auto
        StringBuffer sb = new StringBuffer();
        for (char c = iterator.first(); c != AttributedCharacterIterator.DONE; c = iterator
                .next()) {
            sb.append(c);
        }
        drawString(sb.toString(), x, y);
    }

    public void drawString(String str, float x, float y) {
        block();
        out.print(vg() + ".drawString(");
        write(str);
        unblock();
        out.println(", " + x + ", " + y + ");");
    }

    public void fill(Shape s) {
        block();
        out.print(vg() + ".fill(");
        write(s);
        unblock();
        out.println(");");
    }

    public Color getBackground() {
        return backgroundColor;
    }

    public Composite getComposite() {
        return composite;
    }

    public GraphicsConfiguration getDeviceConfiguration() {
        // FIXME
        return null;
    }

    public FontRenderContext getFontRenderContext() {
        return new FontRenderContext(new AffineTransform(), false, false);
    }

    public Paint getPaint() {
        return paint;
    }

    public Object getRenderingHint(RenderingHints.Key key) {
        return hints.get(key);
    }

    public RenderingHints getRenderingHints() {
        return hints;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public AffineTransform getTransform() {
        return transform;
    }

    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        out.println("System.err.println(\"" + getClass()
                + ": hit(Rectangle, Shape, boolean) not implemented.\");");
        return false;
    }

    public void rotate(double theta) {
        out.println(vg() + ".rotate(" + theta + ");");
    }

    public void rotate(double theta, double x, double y) {
        out.println(vg() + ".rotate(" + theta + ", " + x + ", " + y + ");");
    }

    public void scale(double sx, double sy) {
        out.println(vg() + ".scale(" + sx + ", " + sy + ");");
    }

    public void setBackground(Color c) {
        block();
        out.print(vg() + ".setBackground(");
        write(c);
        unblock();
        out.println(");");
        backgroundColor = c;
    }

    public void setComposite(Composite c) {
        if (c instanceof AlphaComposite) {
            block();
            out.print(vg() + ".setComposite(");
            write((AlphaComposite) c);
            unblock();
            out.println(");");
        } else {
            out.println("System.err.println(\"" + getClass()
                    + ": setComposite(Composite) not implemented.\");");
        }
        composite = c;
    }

    public void setPaint(Paint p) {
        if (p instanceof Color) {
            block();
            out.print(vg() + ".setPaint(");
            write((Color) p);
            unblock();
            out.println(");");
        } else if (p instanceof GradientPaint) {
            block();
            out.print(vg() + ".setPaint(");
            write((GradientPaint) p);
            unblock();
            out.println(");");
        } else if (p instanceof TexturePaint) {
            block();
            out.print(vg() + ".setPaint(");
            write((TexturePaint) p);
            unblock();
            out.println(");");
        } else {
            out.println("System.err.println(\"" + getClass()
                    + ": setPaint(Paint) not implemented for class '"
                    + p.getClass() + "'.\");");
        }
        paint = p;
    }

    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
        // FIXME call should be written out
        if (hintValue != null) {
            hints.put(hintKey, hintValue);
        } else {
            hints.remove(hintKey);
        }
    }

    public void setRenderingHints(Map hints) {
        // FIXME call should be written out
        this.hints = new RenderingHints(hints);
    }

    public void setStroke(Stroke s) {
        if (s instanceof BasicStroke) {
            block();
            out.print(vg() + ".setStroke(");
            write((BasicStroke) s);
            unblock();
            out.println(");");
        } else {
            out.println("System.err.println(\"" + getClass()
                    + ": setStroke(Stroke) not implemented.\");");
        }
        stroke = s;
    }

    public void setTransform(AffineTransform xform) {
        block();
        out.print(vg() + ".setTransform(");
        write(xform);
        unblock();
        out.println(");");
    }

    public void shear(double shx, double shy) {
        out.println(vg() + ".shear(" + shx + ", " + shy + ");");
    }

    public void transform(AffineTransform xform) {
        block();
        out.print(vg() + ".transform(");
        write(xform);
        unblock();
        out.println(");");
    }

    public void translate(double tx, double ty) {
        out.println(vg() + ".translate(" + tx + ", " + ty + ");");
    }

    public void clearRect(double x, double y, double width, double height) {
        out.println(vg() + ".clearRect(" + x + ", " + y + ", " + width + ", "
                + height + ");");
    }

    public void clipRect(double x, double y, double width, double height) {
        out.println(vg() + ".clipRect(" + x + ", " + y + ", " + width + ", "
                + height + ");");
        clip = new Rectangle2D.Double(x, y, width, height);
    }

    public void drawArc(double x, double y, double width, double height,
            double startAngle, double arcAngle) {
        out.println(vg() + ".drawArc(" + x + ", " + y + ", " + width + ", "
                + height + ", " + startAngle + ", " + arcAngle + ");");
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        out.println(vg() + ".drawLine(" + x1 + ", " + y1 + ", " + x2 + ", "
                + y2 + ");");
    }

    public void drawOval(double x, double y, double width, double height) {
        out.println(vg() + ".drawOval(" + x + ", " + y + ", " + width + ", "
                + height + ");");
    }

    public void drawPolygon(double[] xPoints, double[] yPoints, int nPoints) {
        block();
        out.print(vg() + ".drawPolygon(");
        write(xPoints);
        out.print(", ");
        write(yPoints);
        unblock();
        out.println(", " + nPoints + ");");
    }

    public void drawPolyline(double[] xPoints, double[] yPoints, int nPoints) {
        block();
        out.print(vg() + ".drawPolyline(");
        write(xPoints);
        out.print(", ");
        write(yPoints);
        unblock();
        out.println(", " + nPoints + ");");
    }

    public void drawRect(double x, double y, double width, double height) {
        out.println(vg() + ".drawRect(" + x + ", " + y + ", " + width + ", "
                + height + ");");
    }

    public void drawRoundRect(double x, double y, double width, double height,
            double arcWidth, double arcHeight) {
        out.println(vg() + ".drawRoundRect(" + x + ", " + y + ", " + width
                + ", " + height + ", " + arcWidth + ", " + arcHeight + ");");
    }

    public void drawSymbol(int x, int y, int size, int symbol) {
        out.println(vg() + ".drawSymbol(" + x + ", " + y + ", " + size + ", "
                + symbol + ");");
    }

    public void fillAndDrawSymbol(int x, int y, int size, int symbol,
            Color fillColor) {
        // FIXME
    }

    public void drawSymbol(double x, double y, double size, int symbol) {
        out.println(vg() + ".drawSymbol(" + x + ", " + y + ", " + size + ", "
                + symbol + ");");
    }

    public void fillSymbol(int x, int y, int size, int symbol) {
        out.println(vg() + ".fillSymbol(" + x + ", " + y + ", " + size + ", "
                + symbol + ");");
    }

    public void fillSymbol(double x, double y, double size, int symbol) {
        out.println(vg() + ".fillSymbol(" + x + ", " + y + ", " + size + ", "
                + symbol + ");");
    }

    public void fillAndDrawSymbol(double x, double y, double size, int symbol,
            Color fillColor) {
        // FIXME
    }

    public void drawString(String str, double x, double y) {
        block();
        out.print(vg() + ".drawString(");
        write(str);
        unblock();
        out.println(", " + x + ", " + y + ");");
    }

    public void drawString(TagString str, double x, double y) {
        block();
        out.print(vg() + ".drawString(");
        write(str);
        unblock();
        out.println(", " + x + ", " + y + ");");
    }

    public void drawString(String str, double x, double y, int horizontal,
            int vertical) {
        block();
        out.print(vg() + ".drawString(");
        write(str);
        unblock();
        out.println(", " + x + ", " + y + ", " + horizontal + ", " + vertical
                + ");");
    }

    public void drawString(TagString str, double x, double y, int horizontal,
            int vertical) {
        block();
        out.print(vg() + ".drawString(");
        write(str);
        unblock();
        out.println(", " + x + ", " + y + ", " + horizontal + ", " + vertical
                + ");");
    }

    public void drawString(String str, double x, double y, int horizontal,
            int vertical, boolean framed, Color frameColor, double frameWidth,
            boolean banner, Color bannerColor) {
        block();
        out.print(vg() + ".drawString(");
        write(str);
        out.print(", " + x + ", " + y + ", " + horizontal + ", " + vertical
                + ", " + framed + ", ");
        write(frameColor);
        out.print(", " + frameWidth + ", " + banner + ", ");
        write(bannerColor);
        unblock();
        out.println(");");
    }

    public void drawString(TagString str, double x, double y, int horizontal,
            int vertical, boolean framed, Color frameColor, double frameWidth,
            boolean banner, Color bannerColor) {
        block();
        out.print(vg() + ".drawString(");
        write(str);
        out.print(", " + x + ", " + y + ", " + horizontal + ", " + vertical
                + ", " + framed + ", ");
        write(frameColor);
        out.print(", " + frameWidth + ", " + banner + ", ");
        write(bannerColor);
        unblock();
        out.println(");");
    }

    public void fillAndDraw(Shape s, Color fillColor) {
        block();
        out.print(vg() + ".fillAndDraw(");
        write(s);
        out.print(", ");
        write(fillColor);
        unblock();
        out.println(");");
    }

    public void fillArc(double x, double y, double width, double height,
            double startAngle, double arcAngle) {
        out.println(vg() + ".fillArc(" + x + ", " + y + ", " + width + ", "
                + height + ", " + startAngle + ", " + arcAngle + ");");
    }

    public void fillOval(double x, double y, double width, double height) {
        out.println(vg() + ".fillOval(" + x + ", " + y + ", " + width + ", "
                + height + ");");
    }

    public void fillPolygon(double[] xPoints, double[] yPoints, int nPoints) {
        block();
        out.print(vg() + ".fillPolygon(");
        write(xPoints);
        out.print(", ");
        write(yPoints);
        unblock();
        out.println(", " + nPoints + ");");
    }

    public void fillRect(double x, double y, double width, double height) {
        out.println(vg() + ".fillRect(" + x + ", " + y + ", " + width + ", "
                + height + ");");
    }

    public void fillRoundRect(double x, double y, double width, double height,
            double arcWidth, double arcHeight) {
        out.println(vg() + ".fillRoundRect(" + x + ", " + y + ", " + width
                + ", " + height + ", " + arcWidth + ", " + arcHeight + ");");
    }

    public int getColorMode() {
        return colorMode;
    }

    public String getCreator() {
        return creator;
    }

    public boolean isDeviceIndependent() {
        return false;
    }

    public void printComment(String comment) {
        block();
        out.print(vg() + ".printComment(");
        write(comment);
        unblock();
        out.println(");");
    }

    public void setClip(double x, double y, double width, double height) {
        out.println(vg() + ".setClip(" + x + ", " + y + ", " + width + ", "
                + height + ");");
        clip = new Rectangle2D.Double(x, y, width, height);
    }

    public void setColorMode(int colorMode) {
        out.println(vg() + ".setColorMode(" + colorMode + ");");
        this.colorMode = colorMode;
    }

    public void setCreator(String creator) {
        if (creator != null) {
            this.creator = creator;
        }
    }

    public void setDeviceIndependent(boolean isDeviceIndependent) {
        this.isDeviceIndependent = true;
    }

    public void setLineWidth(int width) {
        out.println(vg() + ".setLineWidth(" + width + ");");
    }

    public void setLineWidth(double width) {
        out.println(vg() + ".setLineWidth(" + width + ");");
    }

    public void startExport() {
        block();
        imports.add("java.awt.Graphics");
        imports.add("org.freehep.graphics2d.VectorGraphics");
        imports.add("org.freehep.graphicsio.test.TestingPanel");
                
        out.println("public class " + className + " extends TestingPanel {");
        out.println();
        out.indent();

        out.println("public " + className
                + "(String[] args) throws Exception {");
        out.indent();
        out.println("super(args);");
        out.print("setName(");
        write(className);
        out.println(");");
        out.outdent();
        out.println("} // contructor");

        out.println();
        out.println("public void paint(Graphics g) {");
        out.indent();
        out.println("vg[0] = VectorGraphics.create(g);");
        out.print("vg[0].setCreator(");
        write(creator);
        out.println(");");
        out.println("Paint0s" + paintSequenceNo.getInt() + ".paint(vg);");
        out.outdent();
        out.println("} // paint");
        out.println();

        startClass();
        unblock();
    }

    public void endExport() {
        block();
        endClass();

        out.println("private VectorGraphics vg[] = new VectorGraphics["
                + vg.size() + "];");
        out.println();

        out
                .println("public static void main(String[] args) throws Exception {");
        out.indent();
        out.println("new " + className + "(args).runTest(" + width + ", "
                + height + ");");
        out.outdent();
        out.println("}");
        out.outdent();
        out.println("} // class");
        out.close();
        unblock();

        // now write the header
        PrintWriter writer = new PrintWriter(os);
        
        writer.println("// AUTOMATICALLY GENERATED by " + creator);
        writer.println();
        if ((getProperty(PACKAGE_NAME) != null)
                && !getProperty(PACKAGE_NAME).equals("")) {
            writer.println("package " + getProperty(PACKAGE_NAME) + ";");
            writer.println();
        }
        for (Iterator i=imports.iterator(); i.hasNext(); ) {
            writer.println("import "+i.next()+";");
        }
        writer.println();    
        
        // and the buffer
        writer.print(bos.toString());
        
        writer.close();
    }

    public void lineNumberReached(LineNumberWriter.LineNumberEvent event) {
        if (!block()) {
            lineWriter.setLineNumber(1);

            out.println("Paint0s" + (paintSequenceNo.getInt() + 1)
                    + ".paint(vg);");
            endClass();

            paintSequenceNo.set(paintSequenceNo.getInt() + 1);
            startClass();
        }
        unblock();
    }

    private boolean block() {
        boolean blocked = blockLevel.getInt() > 0;
        blockLevel.set(blockLevel.getInt() + 1);
        return blocked;
    }

    private void unblock() {
        if (blockLevel.getInt() > 0)
            blockLevel.set(blockLevel.getInt() - 1);
    }

    private void startClass() {
        block();
        out.println("private static class Paint0s" + paintSequenceNo.getInt()
                + " {");
        out.indent();
        out.println("public static void paint(VectorGraphics[] vg) {");
        out.indent();
        unblock();
    }

    private void endClass() {
        block();
        out.outdent();
        out.println("} // paint");
        out.outdent();
        out.println("} // class Paint0s" + paintSequenceNo.getInt());
        out.println();
        unblock();
    }

    //
    // Private Methods to write objects
    //
    private void write(Color c) {
        if (c == null) {
            out.print("null");
            return;
        }

        imports.add("java.awt.Color");
        out.print("new Color(" + c.getRed() + ", " + c.getGreen() + ", "
                + c.getBlue() + ", " + c.getAlpha() + ")");
    }

    private void write(Font font) {
        if (font == null) {
            out.print("null");
            return;
        }

        imports.add("java.awt.Font");
        out.print("new Font(");
        write(font.getName());
        out.print(", " + font.getStyle() + ", " + font.getSize() + ")");
    }

    private void write(AffineTransform t) {
        if (t == null) {
            out.print("null");
            return;
        }

        double[] m = new double[6];
        t.getMatrix(m);
        imports.add("java.awt.geom.AffineTransform");
        out.print("new AffineTransform(" + m[0] + ", " + m[1] + ", " + m[2]
                + ", " + m[3] + ", " + m[4] + ", " + m[5] + ")");
    }

    private void write(Shape s) {
        if (s == null) {
            out.print("null");
            return;
        }

        PathIterator i = s.getPathIterator(null);
        imports.add("org.freehep.graphicsio.java.JAVAGeneralPath");
        out.println("new JAVAGeneralPath(" + i.getWindingRule()
                + ", new JAVAGeneralPath.PathElement[] {");
        out.indent();
        float[] c = new float[6];
        while (!i.isDone()) {
            int type = i.currentSegment(c);
            switch (type) {
            case PathIterator.SEG_MOVETO:
                out.print("new JAVAGeneralPath.MoveTo(" + c[0] + "f, " + c[1]
                        + "f)");
                break;
            case PathIterator.SEG_LINETO:
                out.print("new JAVAGeneralPath.LineTo(" + c[0] + "f, " + c[1]
                        + "f)");
                break;
            case PathIterator.SEG_CUBICTO:
                out.print("new JAVAGeneralPath.CurveTo(" + c[0] + "f, " + c[1]
                        + "f, " + c[2] + "f, " + c[3] + "f, " + c[4] + "f, "
                        + c[5] + "f)");
                break;
            case PathIterator.SEG_QUADTO:
                out.print("new JAVAGeneralPath.QuadTo(" + c[0] + "f, " + c[1]
                        + "f, " + c[2] + "f, " + c[3] + "f)");
                break;
            case PathIterator.SEG_CLOSE:
                out.print("new JAVAGeneralPath.ClosePath()");
                break;
            default:
                break;
            }
            i.next();
            out.println(i.isDone() ? "" : ",");
        }
        out.outdent();
        out.print("})");
    }

    private void write(double[] a) {
        if (a == null) {
            out.print("null");
            return;
        }
        write(a, a.length);
    }

    private void write(double[] a, int length) {
        if (a == null) {
            out.print("null");
            return;
        }

        out.println("new double[] {");
        out.indent();
        for (int i = 0; i < length; i++) {
            if (i != 0)
                out.print(", ");
            out.print(a[i]);
        }
        out.outdent();
        out.println();
        out.print("}");
    }

    private void write(float[] a) {
        if (a == null) {
            out.print("null");
            return;
        }
        write(a, a.length);
    }

    private void write(float[] a, int length) {
        if (a == null) {
            out.print("null");
            return;
        }

        out.println("new float[] {");
        out.indent();
        for (int i = 0; i < length; i++) {
            if (i != 0)
                out.print(", ");
            out.print(a[i] + "f");
        }
        out.outdent();
        out.println();
        out.print("}");
    }

    private void write(int[] a) {
        if (a == null) {
            out.print("null");
            return;
        }
        write(a, a.length);
    }

    private void write(int[] a, int length) {
        if (a == null) {
            out.print("null");
            return;
        }

        out.println("new int[] {");
        out.indent();
        for (int i = 0; i < length; i++) {
            if (i != 0)
                out.print(", ");
            out.print(a[i]);
        }
        out.outdent();
        out.println();
        out.print("}");
    }

/*
    private void write(byte[] a) {
        if (a == null) {
            out.print("null");
            return;
        }
        write(a, a.length);
    }
*/
    private void write(byte[] a, int length) {
        if (a == null) {
            out.print("null");
            return;
        }

        out.println("new byte[] {");
        out.indent();
        for (int i = 0; i < length; i++) {
            if (i != 0)
                out.print(", ");
            out.print("(byte)0x" + Integer.toHexString(a[i]));
        }
        out.outdent();
        out.println();
        out.print("}");
    }

/*
    private void write(char[] a) {
        if (a == null) {
            out.print("null");
            return;
        }
        write(a, a.length);
    }
*/
    private void write(char[] a, int length) {
        if (a == null) {
            out.print("null");
            return;
        }

        out.println("new char[] {");
        out.indent();
        for (int i = 0; i < length; i++) {
            if (i != 0)
                out.print(", ");
            char c = (char) a[i];
            out.print("'");
            switch (c) {
            case '\b':
                out.print("\\b");
                break;
            case '\t':
                out.print("\\t");
                break;
            case '\n':
                out.print("\\n");
                break;
            case '\f':
                out.print("\\f");
                break;
            case '\r':
                out.print("\\r");
                break;
            case '\"':
                out.print("\\\"");
                break;
            case '\'':
                out.print("\\'");
                break;
            case '\\':
                out.print("\\\\");
                break;
            default:
                out.print(toUnicode(c));
                break;
            }
            out.print("'");
        }
        out.outdent();
        out.println();
        out.print("}");
    }

    private void write(Polygon p) {
        if (p == null) {
            out.print("null");
            return;
        }

        imports.add("java.awt.Polygon");
        out.println("new Polygon(");
        out.indent();
        write(p.xpoints, p.npoints);
        out.print(", ");
        write(p.ypoints, p.npoints);
        out.println(", " + p.npoints);
        out.outdent();
        out.print(")");
    }

    private void write(Image image) {
        if (image == null) {
            out.print("null");
            return;
        }

        PNGEncoder encoder = new PNGEncoder(image, true,
                PNGEncoder.FILTER_NONE, 9);

        /* byte[] data = */ encoder.pngEncode();
        // if (data == null) {
        imports.add("java.awt.image.BufferedImage");
        out.print("new BufferedImage(" + image.getWidth(null) + ", "
                + image.getHeight(null) + ", BufferedImage.TYPE_INT_ARGB)");
        return;
        // }
        /*
         * out.println("new ImageIcon("); out.indent(); System.out.println("png =
         * "+data.length+" "+image.getWidth(null)+" "+image.getHeight(null));
         * 
         * try { ByteArrayOutputStream ba = new ByteArrayOutputStream(); if
         * (javax.imageio.ImageIO.write((RenderedImage)image, "jpg", ba)) {
         * 
         * System.out.println("jpg = "+ba.size()); if (ba.size() < data.length) {
         * System.out.println("***** JPEG"); } } } catch (IOException ioe) {
         * System.out.println(ioe); }
         * 
         * write(data);
         * 
         * out.outdent(); out.println(").getImage()");
         */
    }

    private void write(GradientPaint gp) {
        if (gp == null) {
            out.print("null");
            return;
        }

        imports.add("java.awt.GradientPaint");
        out.println("new GradientPaint(");
        out.indent();
        write(gp.getPoint1());
        out.print(", ");
        write(gp.getColor1());
        out.println(", ");
        write(gp.getPoint2());
        out.print(", ");
        write(gp.getColor2());
        out.println(", " + gp.isCyclic());
        out.outdent();
        out.print(")");
    }

    private void write(TexturePaint tp) {
        if (tp == null) {
            out.print("null");
            return;
        }

        imports.add("java.awt.TexturePaint");
        out.println("new TexturePaint(");
        out.indent();
        out.println("(BufferedImage)");
        write(tp.getImage());
        out.print(", ");
        write(tp.getAnchorRect());
        out.outdent();
        out.print(")");
    }

    private void write(BasicStroke bs) {
        if (bs == null) {
            out.print("null");
            return;
        }

        imports.add("java.awt.BasicStroke");
        out.println("new BasicStroke(");
        out.indent();
        out.println(bs.getLineWidth() + "f, " + bs.getEndCap() + ", ");
        out.println(bs.getLineJoin() + ", " + bs.getMiterLimit() + "f, ");
        write(bs.getDashArray());
        out.println(", " + bs.getDashPhase() + "f");
        out.outdent();
        out.print(")");
    }

    private void write(AlphaComposite ac) {
        if (ac == null) {
            out.print("null");
            return;
        }

        imports.add("java.awt.AlphaComposite");
        out.print("AlphaComposite.getInstance(" + ac.getRule() + ", "
                + ac.getAlpha() + ")");
    }

    private void write(Point2D p) {
        if (p == null) {
            out.print("null");
            return;
        }

        imports.add("java.awt.geom.Point2D");
        out.print("new Point2D.Double(" + p.getX() + ", " + p.getY() + ")");
    }

    private void write(Rectangle2D r) {
        if (r == null) {
            out.print("null");
            return;
        }

        imports.add("java.awt.geom.Rectangle2D");
        out.print("new Rectangle2D.Double(" + r.getX() + ", " + r.getY() + ", "
                + r.getWidth() + ", " + r.getHeight() + ")");
    }

    private void write(String s) {
        if (s == null) {
            out.print("(String)null");
            return;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
            case '\\':
                sb.append("\\\\");
                break;
            case '"':
                sb.append("\\\"");
                break;
            default:
                sb.append(toUnicode(c));
                break;
            }
        }
        out.print("\"" + sb.toString() + "\"");
    }

    private void write(TagString s) {
        if (s == null) {
            out.print("null");
            return;
        }

        imports.add("org.freehep.graphics2d.TagString");
        out.print("new TagString(");
        write(s.toString());
        out.print(")");
    }

    private String vg() {
        Integer index = (Integer) vg.get(this);
        return "vg["
                + ((index != null) ? String.valueOf(index.intValue()) : "null")
                + "]";
    }

    private String toUnicode(char c) {
        if ((c <= 0x1f) || (0x7f <= c)) {
            String unicode = "0000" + Integer.toHexString(c);
            return "\\u" + unicode.substring(unicode.length() - 4);
        } else {
            return String.valueOf(c);
        }
    }
}
