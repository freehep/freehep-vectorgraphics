package org.freehep.graphicsio.emf;

import java.io.IOException;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Paint;
import java.awt.Color;
import java.awt.Shape;
import java.awt.AlphaComposite;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Area;
import java.util.logging.Logger;
import java.util.Stack;
import java.util.Vector;
import java.util.Map;

import org.freehep.util.io.Tag;
import org.freehep.graphicsio.emf.gdi.*;

/**
 * Standalone EMF renderer.
 *
 * @author Daniel Noll (daniel@nuix.com)
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFRenderer.java 63c8d910ece7 2007/01/20 15:30:50 duns $
 */
public class EMFRenderer {
    private static final Logger logger = Logger.getLogger("org.freehep.graphicsio.emf");

    private EMFHeader header;

    /**
     * affect by all XXXTo methods, e.g. LinTo. ExtMoveTo creates the
     * starting point. CloseFigure closes the currentFigure.
     */
    private GeneralPath currentFigure = null;

    /**
     * AffineTransform which is the base for all rendering
     * operations.
     */
    private AffineTransform initialTransform;

    /**
     * origin of the emf window, set by SetWindowOrgEx
     */
    private Point windowOrigin = null;

    /**
     * origin of the emf viewport, set By SetViewportOrgEx
     */
    private Point viewportOrigin = null;

    /**
     * size of the emf window, set by SetWindowExtEx
     */
    private Dimension windowSize = null;

    /**
     * size of the emf viewport, set by SetViewportExtEx
     */
    private Dimension viewportSize = null;

    /**
     * The MM_ISOTROPIC mode ensures a 1:1 aspect ratio.
     *  The MM_ANISOTROPIC mode allows the x-coordinates
     * and y-coordinates to be adjusted independently.
     */
    private boolean mapModeIsotropic = false;

    /**
     * AffineTransform defined by SetMapMode. Used for
     * resizing the emf to propper device bounds.
     */
    private AffineTransform  mapModeTransform =
        AffineTransform.getScaleInstance(twipScale, twipScale);


    /**
     * clipping area which is the base for all rendering
     * operations.
     */
    private Shape initialClip;

    private Object[] gdiObjects = new Object[256]; // TODO: Make this more flexible.

    // Rendering state.
    private Color currentTextColor = Color.BLACK;
    private Paint currentBrushPaint = new Color(0, 0, 0, 0);
    private Paint currentPenPaint = Color.BLACK;
    private Stroke currentPenStroke = new BasicStroke();
    private int textAlignMode = 0;

    /**
     * written by {@link #map(org.freehep.util.io.Tag, java.awt.Graphics2D)}
     * if a SetPolyFillMode is read, used by
     * the same method to draw a PolyPolygon16
     */
    private int currentWindingRule = GeneralPath.WIND_EVEN_ODD;

    /**
     * {@link #map(org.freehep.util.io.Tag, java.awt.Graphics2D)} stores
     * an Instance of DC if saveDC is read. RestoreDC pops an object.
     */
    private Stack dcStack = new Stack();

    /**
     * Defined by SetBkModes, either {@link EMFConstants#BKG_OPAQUE} or
     * {@link EMFConstants#BKG_TRANSPARENT}. Used in
     * {@link #fillAndDrawOrAppend(java.awt.Graphics2D, java.awt.Shape)}
     */
    private int currentBkMode = EMFConstants.BKG_OPAQUE;

    /**
     * The SetBkMode function affects the line styles for lines drawn using a
     * pen created by the CreatePen function. SetBkMode does not affect lines
     * drawn using a pen created by the ExtCreatePen function.
     */
    private boolean useCreatePen = true;

    /**
     * The miter length is defined as the distance from the intersection
     * of the line walls on the inside of the join to the intersection of
     * the line walls on the outside of the join. The miter limit is the
     * maximum allowed ratio of the miter length to the line width.
     */
    private int currentMeterLimit = 10;

    /**
     * The SetROP2 function sets the current foreground mix mode.
     * Default is to use the pen.
     */
    private int currentROP2 = EMFConstants.R2_COPYPEN;

    /**
     * e.g. {@link Image#SCALE_SMOOTH} for rendering images
     */
    private int currentScaleMode = Image.SCALE_SMOOTH;

    /**
     * The brush origin is a pair of coordinates specifying the location of one
     *pixel in the bitmap. The default brush origin coordinates are (0,0). For 
     * horizontal coordinates, the value 0 corresponds to the leftmost column
     * of pixels; the width corresponds to the rightmost column. For vertical
     * coordinates, the value 0 corresponds to the uppermost row of pixels;
     * the height corresponds to the lowermost row.
     */
    private Point currentBrushOrigin = new Point(0, 0);

    /**
     * Class the encapsulate the state of a Graphics2D object.
     * Instances are store in dcStack by
     * {@link org.freehep.graphicsio.emf.EMFRenderer#map(org.freehep.util.io.Tag, java.awt.Graphics2D)}
     */
    private class DC {
        private Paint paint;
        private Stroke stroke;
        private AffineTransform transform;
        private Shape clip;
        public GeneralPath path;
        public int bkMode;
        public int windingRule;
        public int meterLimit;
        public boolean useCreatePen;
        public int scaleMode;
    }

    /**
     * stores the parsed tags. Filled by the constructor. Read by
     * {@link #paint(java.awt.Graphics2D)}
     */
    private Vector tags = new Vector(0);

    /**
     * Created by BeginPath and closed by EndPath. 
     */
    private GeneralPath currentPath = null;

    /**
     * The transformations set by ModifyWorldTransform are redirected to
     * that AffineTransform. They do not affect the current paint context,
     * after BeginPath is called. Only the figures appended to currentPath
     * are transformed by this AffineTransform.
     * BeginPath clears the transformation, ModifyWorldTransform changes ist.
     */
    private AffineTransform currentPathTransform = new AffineTransform();

    /**
     * Constructs the renderer.
     *
     * @param is the input stream to read the EMF records from.
     * @throws IOException if an error occurs reading the header.
     */
    public EMFRenderer(EMFInputStream is) throws IOException {
        this.header = is.readHeader();

        // read all tags
        Tag tag;
        while ((tag = is.readTag()) != null) {
            tags.add(tag);
        }
        is.close();
    }

    /**
     * Each logical unit is mapped to one twentieth of a
     * printer's point (1/1440 inch, also called a twip).
     */
    private static double twipScale = 1d / 1440 * 254;

    /**
     * Gets the size of a canvas which would be required to render the EMF.
     *
     * @return the size.
     */
    public Dimension getSize() {
        Dimension bounds = header.getBounds().getSize();
        return new Dimension(
            (int)Math.ceil(bounds.width * twipScale),
            (int)Math.ceil(bounds.height * twipScale));
    }

    /**
     * Paints the EMF onto the provided graphics context.
     *
     * @param g2 the graphics context to paint onto.
     */
    public void paint(Graphics2D g2) {
        // store at leat clip and transformation
        Shape clip = g2.getClip();
        AffineTransform at = g2.getTransform();
        Map hints = g2.getRenderingHints();

        // some quality settings
        g2.setRenderingHint(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(
            RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // used by SetWorldTransform to reset transformation
        initialTransform = g2.getTransform();

        // set the initial value, defaults for EMF
        currentPath = null;
        currentFigure = null;
        currentMeterLimit = 10;
        currentWindingRule = GeneralPath.WIND_EVEN_ODD;
        currentBkMode = EMFConstants.BKG_OPAQUE;
        useCreatePen = true;
        currentScaleMode = Image.SCALE_SMOOTH;

        windowOrigin = null;
        viewportOrigin = null;
        windowSize = null;
        viewportSize = null;

        mapModeIsotropic = false;
        mapModeTransform = AffineTransform.getScaleInstance(
            twipScale, twipScale);

        // apply all default settings
        resetTransformation(g2);
        
        // determin initial clip after all basic transformations
        initialClip = g2.getClip();

        for (int i = 0; i < tags.size(); i++) {
            map((Tag) tags.get(i), g2);
        }

        // reset Transform and clip
        g2.setRenderingHints(hints);
        g2.setTransform(at);
        g2.setClip(clip);
    }

    /**
     * set the initial transform, the windowOrigin and viewportOrigin,
     * scales by viewportSize and windowSize
     * @param g2 Context to apply transformations
     */
    private void resetTransformation(Graphics2D g2) {
        // rest to device configuration
        if (initialTransform != null) {
            g2.setTransform(initialTransform);
        } else {
            g2.setTransform(new AffineTransform());
        }
        
        if (mapModeTransform != null) {
            g2.transform(mapModeTransform);
        }

        // move to window origin
        if (windowOrigin != null) {
            g2.translate(
                - windowOrigin.getX(),
                - windowOrigin.getY());
        }
        // move to window origin
        if (viewportOrigin != null) {
            g2.translate(
                - viewportOrigin.getX(),
                - viewportOrigin.getY());
        }

        // twipScale by windo and viewport size
        if (viewportSize != null && windowSize != null) {
            double scaleX =
                viewportSize.getWidth() /
                windowSize.getWidth();
            double scaleY =
                viewportSize.getHeight() /
                windowSize.getHeight();
            g2.scale(scaleX,  scaleY);
        }
    }

    /**
     * The mapping function EMF tags -> java2d methods
     *
     * @param tag the tag being rendered/mapped.
     * @param g2 the graphics context to draw onto.
     */
    private void map(Tag tag, Graphics2D g2) {
        if (tag instanceof SetMapMode) {
            int mode = ((SetMapMode)tag).getMode();

            // MM_ANISOTROPIC 	Logical units are mapped to arbitrary
            // units with arbitrarily scaled axes. Use the SetWindowExtEx
            // and SetViewportExtEx functions to specify the units,
            // orientation, and scaling.
            if (mode == EMFConstants.MM_ANISOTROPIC) {
                mapModeIsotropic = false;
            }

            // MM_HIENGLISH 	Each logical unit is mapped to 0.001 inch.
            // Positive x is to the right; positive y is up.
            else if (mode == EMFConstants.MM_HIENGLISH) {
                // TODO not sure
                double scale = 0.001 * 25.4;
                mapModeTransform = AffineTransform.getScaleInstance(
                    scale, scale);
            }

            // MM_HIMETRIC 	Each logical unit is mapped to 0.01 millimeter.
            // Positive x is to the right; positive y is up.
            else if (mode == EMFConstants.MM_HIENGLISH) {
                // TODO not sure
                double scale = 0.01;
                mapModeTransform = AffineTransform.getScaleInstance(
                    scale, scale);
            }

            // MM_ISOTROPIC 	Logical units are mapped to arbitrary units
            // with equally scaled axes; that is, one unit along the x-axis
            // is equal to one unit along the y-axis. Use the SetWindowExtEx
            // and SetViewportExtEx functions to specify the units and the
            // orientation of the axes. Graphics device interface (GDI) makes
            // adjustments as necessary to ensure the x and y units remain
            // the same size (When the window extent is set, the viewport will
            // be adjusted to keep the units isotropic).
            else if (mode == EMFConstants.MM_ISOTROPIC) {
                mapModeIsotropic = true;
                fixViewportSize();
            }

            // MM_LOENGLISH 	Each logical unit is mapped to 0.01 inch.
            // Positive x is to the right; positive y is up.
            else if (mode == EMFConstants.MM_HIENGLISH) {
                // TODO not sure
                double scale = 0.01 * 25.4;
                mapModeTransform = AffineTransform.getScaleInstance(
                    scale, scale);
            }

            // MM_LOMETRIC 	Each logical unit is mapped to 0.1 millimeter.
            // Positive x is to the right; positive y is up.
            else if (mode == EMFConstants.MM_LOMETRIC) {
                // TODO not sure
                double scale = 0.1;
                mapModeTransform = AffineTransform.getScaleInstance(
                    scale, scale);
            }

            // MM_TEXT 	Each logical unit is mapped to one device pixel. Positive
            // x is to the right; positive y is down.
            else if (mode == EMFConstants.MM_TEXT) {
                mapModeTransform = AffineTransform.getScaleInstance(1, -1);
            }

            // MM_TWIPS 	Each logical unit is mapped to one twentieth of a
            // printer's point (1/1440 inch, also called a twip). Positive x
            // is to the right; positive y is up.
            else if (mode == EMFConstants.MM_TWIPS) {
                mapModeTransform = AffineTransform.getScaleInstance(twipScale, twipScale);
            }

            
        } if (tag instanceof SetViewportExtEx) {
            // The SetViewportExtEx function sets the horizontal and vertical
            // extents of the viewport for a device context by using the specified values.

            // The viewport refers to the device coordinate system of the device space.
            // The extent is the maximum value of an axis. This function sets the maximum
            // values for the horizontal and vertical axes of the viewport in device
            // coordinates (or pixels). When mapping between page space and device space,
            // SetWindowExtEx and SetViewportExtEx determine the scaling factor between
            // the window and the viewport.
            viewportSize = ((SetViewportExtEx)tag).getSize();
            fixViewportSize();
            resetTransformation(g2);
        } else if (tag instanceof SetWindowExtEx) {
            windowSize = ((SetWindowExtEx)tag).getSize();
            fixViewportSize();
            resetTransformation(g2);
        } else if (tag instanceof SetViewportOrgEx) {
            // The SetViewportOrgEx function specifies which device point maps
            // to the viewport origin (0,0).

            // This function (along with SetViewportExtEx and SetWindowExtEx) helps
            // define the mapping from the logical coordinate space (also known as a
            // window) to the device coordinate space (the viewport). SetViewportOrgEx
            // specifies which device point maps to the logical point (0,0). It has the
            // effect of shifting the axes so that the logical point (0,0) no longer
            // refers to the upper-left corner.
            viewportOrigin = ((SetViewportOrgEx) tag).getPoint();
            resetTransformation(g2);
        } else if (tag instanceof SetWindowOrgEx) {
            // The SetWindowOrgEx function specifies which window point maps to
            // the window origin (0,0).
            windowOrigin = ((SetWindowOrgEx) tag).getPoint();
            resetTransformation(g2);
        } else if (tag instanceof SetTextAlign) {
            textAlignMode = ((SetTextAlign) tag).getMode();
        } else if (tag instanceof Arc) {
            // The Arc function draws an elliptical arc.
            //
            // BOOL Arc(
            // HDC hdc, // handle to device context
            // int nLeftRect, // x-coord of rectangle's upper-left corner
            // int nTopRect, // y-coord of rectangle's upper-left corner
            // int nRightRect, // x-coord of rectangle's lower-right corner
            // int nBottomRect, // y-coord of rectangle's lower-right corner
            // int nXStartArc, // x-coord of first radial ending point
            // int nYStartArc, // y-coord of first radial ending point
            // int nXEndArc, // x-coord of second radial ending point
            // int nYEndArc // y-coord of second radial ending point
            // );
            // The points (nLeftRect, nTopRect) and (nRightRect, nBottomRect)
            // specify the bounding rectangle.
            // An ellipse formed by the specified bounding rectangle defines the
            // curve of the arc.
            // The arc extends in the current drawing direction from the point
            // where it intersects the
            // radial from the center of the bounding rectangle to the
            // (nXStartArc, nYStartArc) point.
            // The arc ends where it intersects the radial from the center of
            // the bounding rectangle to
            // the (nXEndArc, nYEndArc) point. If the starting point and ending
            // point are the same,
            // a complete ellipse is drawn.
            Arc arc = (Arc) tag;

            // normalize start and end point to a circle
            double nx0 = arc.getStart().getX() / arc.getBounds().width;

            // double ny0 = arc.getStart().y / arc.getBounds().height;
            double nx1 = arc.getEnd().getX() / arc.getBounds().width;

            // double ny1 = arc.getEnd().y / arc.getBounds().height;
            // calculate angle of start point
            double alpha0 = Math.acos(nx0);
            double alpha1 = Math.acos(nx1);

            Arc2D arc2d = new Arc2D.Double(
                arc.getStart().x,
                arc.getStart().y,
                arc.getBounds().width,
                arc.getBounds().height,
                alpha0,
                alpha1 - alpha0,
                Arc2D.OPEN);
            drawOrAppend(g2, arc2d);
        } else if (tag instanceof ArcTo) {
            // The ArcTo function draws an elliptical arc.
            //
            // BOOL ArcTo(
            // HDC hdc, // handle to device context
            // int nLeftRect, // x-coord of rectangle's upper-left corner
            // int nTopRect, // y-coord of rectangle's upper-left corner
            // int nRightRect, // x-coord of rectangle's lower-right corner
            // int nBottomRect, // y-coord of rectangle's lower-right corner
            // int nXRadial1, // x-coord of first radial ending point
            // int nYRadial1, // y-coord of first radial ending point
            // int nXRadial2, // x-coord of second radial ending point
            // int nYRadial2 // y-coord of second radial ending point
            // );
            // ArcTo is similar to the Arc function, except that the current
            // position is updated.
            //
            // The points (nLeftRect, nTopRect) and (nRightRect, nBottomRect)
            // specify the bounding rectangle.
            // An ellipse formed by the specified bounding rectangle defines the
            // curve of the arc. The arc extends
            // counterclockwise from the point where it intersects the radial
            // line from the center of the bounding
            // rectangle to the (nXRadial1, nYRadial1) point. The arc ends where
            // it intersects the radial line from
            // the center of the bounding rectangle to the (nXRadial2,
            // nYRadial2) point. If the starting point and
            // ending point are the same, a complete ellipse is drawn.
            //
            // A line is drawn from the current position to the starting point
            // of the arc.
            // If no error occurs, the current position is set to the ending
            // point of the arc.
            //
            // The arc is drawn using the current pen; it is not filled.
            ArcTo arc = (ArcTo) tag;

            // normalize start and end point to a circle
            double nx0 = arc.getStart().x / arc.getBounds().width;

            // double ny0 = arc.getStart().y / arc.getBounds().height;
            double nx1 = arc.getEnd().x / arc.getBounds().width;

            // double ny1 = arc.getEnd().y / arc.getBounds().height;
            // calculate angle of start point
            double alpha0 = Math.acos(nx0);
            double alpha1 = Math.acos(nx1);

            Arc2D arc2d = new Arc2D.Double(
                arc.getStart().x,
                arc.getStart().y,
                arc.getBounds().width,
                arc.getBounds().height,
                alpha0,
                alpha1 - alpha0,
                Arc2D.OPEN);
            currentFigure.append(arc2d, true);
        } else if (tag instanceof BeginPath) {
            // The BeginPath function opens a path bracket in the specified
            // device context.
            currentPath = new GeneralPath(currentWindingRule);
            currentPathTransform = new AffineTransform();
            // currentPathTransform = new AffineTransform(g2.getTransform());
        } else if (tag instanceof EndPath) {
            // TODO: fix EMFGraphics2D?
            // this happens only when EMF is created by EMFGraphics2D
            // there could be an open figure (created with LineTo, PolylineTo etc.)
            // that is not closed and therefore not written to the currentPath
            closeCurrentFigure();

            // The EndPath function closes a path bracket and selects the path
            // defined by the bracket into the specified device context.
            if (currentPath != null && currentPath.getCurrentPoint() != null) {
                currentPath.closePath();
            }
        } else if (tag instanceof WidenPath) {
            // The WidenPath function redefines the current path as the area
            // that would be painted if the path were stroked using the pen
            // currently selected into the given device context.
            if (currentPath != null && currentPenStroke != null) {
                GeneralPath newPath = new GeneralPath(currentWindingRule);
                newPath.append(currentPenStroke.createStrokedShape(currentPath), false);
                currentPath = newPath;
            }
        } else if (tag instanceof StrokeAndFillPath) {
            // fills the current path
            if (currentPath != null) {
                fillShape(g2, currentPath);
                drawShape(g2, currentPath);
                currentPath = null;
            }
        } else if (tag instanceof FillPath) {
            // fills the current path
            if (currentPath != null) {
                fillShape(g2, currentPath);
                currentPath = null;
            }
        } else if (tag instanceof StrokePath) {
            // fills the current path
            if (currentPath != null) {
                drawShape(g2, currentPath);
                currentPath = null;
            }
        } else if (tag instanceof SelectClipPath) {
            int mode = ((SelectClipPath)tag).getMode();
            if (currentPath != null) {
                // The new clipping region includes the intersection
                // (overlapping areas) of the current clipping region and the current path.
                if (mode == EMFConstants.RGN_AND) {
                    g2.clip(currentPath);
                }
                // The new clipping region is the current path
                else if (mode == EMFConstants.RGN_COPY) {
                    // rest the clip ...
                    AffineTransform at = g2.getTransform();
                    // temporarly switch to the base transformation to
                    // aplly the base clipping area
                    resetTransformation(g2);
                    // set the clip
                    g2.setClip(initialClip);
                    g2.setTransform(at);
                    g2.clip(currentPath);
                }
                // The new clipping region includes the areas of the
                // current clipping region with those of the current path excluded.
                else if (mode == EMFConstants.RGN_DIFF) {
                    Shape clip = g2.getClip();
                    if (clip != null) {
                        Area a = new Area(currentPath);
                        a.subtract(new Area(clip));
                        g2.setClip(a);
                    } else {
                        g2.setClip(currentPath);
                    }
                }
                // The new clipping region includes the union (combined areas)
                // of the current clipping region and the current path.
                else if(mode == EMFConstants.RGN_OR) {
                    Shape clip = g2.getClip();
                    if (clip != null) {
                        currentPath.append(clip, false);
                    }
                    g2.setClip(currentPath);
                }
                // The new clipping region includes the union of the current
                // clipping region and the current path but without the overlapping areas.
                else if(mode == EMFConstants.RGN_XOR) {
                    Shape clip = g2.getClip();
                    if (clip != null) {
                        Area a = new Area(currentPath);
                        a.exclusiveOr(new Area(clip));
                        g2.setClip(a);
                    } else {
                        g2.setClip(currentPath);
                    }
                }
            }

            // delete the current path
            currentPath = null;
        } else if (tag instanceof Chord) {
            // The Chord function draws a chord (a region bounded by the
            // intersection of an
            // ellipse and a line segment, called a secant). The chord is
            // outlined by using the
            // current pen and filled by using the current brush.
            Chord arc = (Chord) tag;

            // normalize start and end point to a circle
            double nx0 = arc.getStart().getX() / arc.getBounds().width;

            // double ny0 = arc.getStart().y / arc.getBounds().height;
            double nx1 = arc.getEnd().getX() / arc.getBounds().width;

            // double ny1 = arc.getEnd().y / arc.getBounds().height;
            // calculate angle of start point
            double alpha0 = Math.acos(nx0);
            double alpha1 = Math.acos(nx1);

            Arc2D arc2d = new Arc2D.Double(
                arc.getStart().getX(),
                arc.getStart().getY(),
                arc.getBounds().getWidth(),
                arc.getBounds().getHeight(),
                alpha0,
                alpha1 - alpha0,
                Arc2D.CHORD);
            fillAndDrawOrAppend(g2, arc2d);
        } else if (tag instanceof EMFRectangle) {
            Rectangle rect = ((EMFRectangle) tag).getBounds();
            fillAndDrawOrAppend(g2, rect);
        } else if (tag instanceof CloseFigure) {
            // The CloseFigure function closes an open figure in a path.
            closeCurrentFigure();
        } else if (tag instanceof CreatePen) {
            // CreatePen
            //
            // The CreatePen function creates a logical pen that has the
            // specified style, width, and color.
            // The pen can subsequently be selected into a device context and
            // used to draw lines and curves.
            //
            // HPEN CreatePen(
            // int fnPenStyle, // pen style
            // int nWidth, // pen width
            // COLORREF crColor // pen color
            // );
            CreatePen cpen = (CreatePen) tag;
            gdiObjects[cpen.getIndex()] = cpen.getPen();
        } else if (tag instanceof ExtCreatePen) {
            // ExtCreatePen
            //
            // The ExtCreatePen function creates a logical cosmetic or
            // geometric pen that has the specified style, width,
            // and brush attributes.
            //
            // HPEN ExtCreatePen(
            //  DWORD dwPenStyle,      // pen style
            //  DWORD dwWidth,         // pen width
            //  CONST LOGBRUSH *lplb,  // brush attributes
            //  DWORD dwStyleCount,    // length of custom style array
            //  CONST DWORD *lpStyle   // custom style array
            //);
            ExtCreatePen cpen = (ExtCreatePen) tag;
            gdiObjects[cpen.getIndex()] = cpen.getPen();
        } else if (tag instanceof CreateBrushIndirect) {
            // CreateBrushIndirect
            //
            // The CreateBrushIndirect function creates a logical brush that has the
            // specified style, color, and pattern.
            //
            // HBRUSH CreateBrushIndirect(
            //   CONST LOGBRUSH *lplb   // brush information
            // );

            CreateBrushIndirect cbrush = (CreateBrushIndirect) tag;
            gdiObjects[cbrush.getIndex()] = cbrush.getBrush();
        } else if (tag instanceof ExtCreateFontIndirectW) {
            ExtCreateFontIndirectW cfont = (ExtCreateFontIndirectW) tag;
            gdiObjects[cfont.getIndex()] = cfont.getFont();
        } else if (tag instanceof SelectPalette) {
            // The SelectPalette function selects the specified
            // logical palette into a device context.
            
            // TODO needs CreatePalette and CreatePalletteIndex to work
        } else if (tag instanceof SelectObject) {
            int gdiIndex = ((SelectObject) tag).getIndex();
            Object gdiObject;

            if (gdiIndex < 0) {
                gdiObject = StockObjects.getStockObject(gdiIndex);
            } else {
                gdiObject = gdiObjects[gdiIndex];
            }

            // TODO: Other object types.
            if (gdiObject instanceof LogPen) {
                LogPen lpen = (LogPen) gdiObject;

                useCreatePen = true;
                currentPenPaint = lpen.getColor();
                currentPenStroke = new BasicStroke(
                    lpen.getWidth(),
                    getCap(lpen.getPenStyle()),
                    getJoin(lpen.getPenStyle()),
                    currentMeterLimit,
                    getDash(lpen.getPenStyle(), null),
                    0);
                
            } else if (gdiObject instanceof ExtLogPen) {
                ExtLogPen extLogPen = (ExtLogPen)gdiObject;

                useCreatePen = false;
                currentPenPaint = extLogPen.getColor();
                currentPenStroke = new BasicStroke(
                    extLogPen.getWidth(),
                    getCap(extLogPen.getPenStyle()),
                    getJoin(extLogPen.getPenStyle()),
                    currentMeterLimit,
                    getDash(
                        extLogPen.getPenStyle(),
                        extLogPen.getStyle()),
                    0);
            } else if (gdiObject instanceof LogBrush32) {
                LogBrush32 lbrush = (LogBrush32) gdiObject;

                Paint paint = lbrush.getColor();

                switch (lbrush.getStyle())
                {
                    case EMFConstants.BS_SOLID:
                        paint = lbrush.getColor();
                        break;
                    case EMFConstants.BS_NULL: // note: same value as BS_HOLLOW
                        // Should probably do this by making a paint implementation that does nothing,
                        // but a 100% transparent color works just as well for now.
                        paint = new Color(0, 0, 0, 0);
                        break;

                    // TODO: Support pattern types
                    // TODO: Support hatching
                    // TODO: Support DIB types

                    default:
                        logger.warning("got unsupported brush style " + lbrush.getStyle());
                        break;
                }

                currentBrushPaint = paint;
            } else if (gdiObject instanceof LogFontW) {
                g2.setFont(((LogFontW) gdiObject).getFont()); // TODO: See if this ever happens.
            } else if (gdiObject instanceof ExtLogFontW) {
                g2.setFont(((ExtLogFontW) gdiObject).getFont().getFont());
            }
        } else if (tag instanceof SetROP2) {
            // The SetROP2 function sets the current foreground mix mode.
            // GDI uses the foreground mix mode to combine pens and interiors
            // of filled objects with the colors already on the screen. The
            // foreground mix mode defines how colors from the brush or pen
            // and the colors in the existing image are to be combined.
            currentROP2 = ((SetROP2)tag).getMode();
        } else if (tag instanceof SetBkMode) {
            // The SetBkMode function affects the line styles for lines drawn using a
            // pen created by the CreatePen function. SetBkMode does not affect lines
            // drawn using a pen created by the ExtCreatePen function.
            currentBkMode = ((SetBkMode)tag).getMode();
        } else if (tag instanceof DeleteObject) {
            int gdiIndex = ((DeleteObject) tag).getIndex();
            gdiObjects[gdiIndex] = null;
        } else if (tag instanceof SetTextColor) {
            currentTextColor = ((SetTextColor) tag).getColor();
        } else if (tag instanceof Ellipse) {
            // The Ellipse function draws an ellipse. The center of the ellipse
            // is the center of the specified bounding rectangle.
            // The ellipse is outlined by using the current pen and is filled by
            // using the current brush.
            // The current position is neither used nor updated by Ellipse.
            Ellipse el = (Ellipse) tag;

            Ellipse2D el2 = new Ellipse2D.Double(
                el.getBounds().getX(),
                el.getBounds().getY(),
                el.getBounds().getWidth(),
                el.getBounds().getHeight());
            fillAndDrawOrAppend(g2, el2);
        } else if (tag instanceof LineTo) {
            // The LineTo function draws a line from the current position up to,
            // but not including, the specified point.
            // The line is drawn by using the current pen and, if the pen is a
            // geometric pen, the current brush.
            Point destination = ((LineTo) tag).getPoint();
            currentFigure.lineTo(
                (float)destination.getX(),
                (float)destination.getY());
        } else if (tag instanceof MoveToEx) {
            // The MoveToEx function updates the current position to the
            // specified point
            // and optionally returns the previous position.
            Point p = ((MoveToEx) tag).getPoint();
            currentFigure = new GeneralPath(currentWindingRule);
            currentFigure.setWindingRule(currentWindingRule);
            currentFigure.moveTo((float) p.getX(), (float) p.getY());
        } else if (tag instanceof Pie) {
            Pie arc = (Pie) tag;

            // normalize start and end point to a circle
            double nx0 = arc.getStart().x / arc.getBounds().width;

            // double ny0 = arc.getStart().y / arc.getBounds().height;
            double nx1 = arc.getEnd().x / arc.getBounds().width;

            // double ny1 = arc.getEnd().y / arc.getBounds().height;
            // calculate angle of start point
            double alpha0 = Math.acos(nx0);
            double alpha1 = Math.acos(nx1);

            Arc2D arc2d = new Arc2D.Double(
                arc.getStart().x,
                arc.getStart().y,
                arc.getBounds().width,
                arc.getBounds().height,
                alpha0,
                alpha1 - alpha0,
                Arc2D.PIE);
            fillAndDrawOrAppend(g2, arc2d);
        } else if (tag instanceof AbstractExtTextOut) {
            Text text = ((AbstractExtTextOut) tag).getText();

            String str = text.getString();
            Point pos = text.getPos();
            int x = pos.x;
            int y = pos.y;

            // TODO: Use explicit widths to pixel-position each character, if present.

            // TODO: Implement alignment properly.  What we have already seems to work well enough.
//            FontRenderContext frc = g2.getFontRenderContext();
//            TextLayout layout = new TextLayout(str, g2.getFont(), frc);
//            if ((textAlignMode & EMFConstants.TA_CENTER) != 0) {
//                layout.draw(g2, x + (width - textWidth) / 2, y);
//            } else if ((textAlignMode & EMFConstants.TA_RIGHT) != 0) {
//                layout.draw(g2, x + width - textWidth, y);
//            } else {
//                layout.draw(g2, x, y);
//            }

            if (currentPath != null) {
                // do not use g2.drawString(str, x, y) to be aware of currentPath
                TextLayout tl = new TextLayout(str, g2.getFont(), g2.getFontRenderContext());
                currentPath.append(tl.getOutline(null), false);
            } else {
                g2.setPaint(currentTextColor);
                g2.drawString(str, x, y);
            }
        } else if (tag instanceof AlphaBlend) {
            // This function displays bitmaps that have transparent or semitransparent pixels.
            AlphaBlend alphaBlend = ((AlphaBlend)tag);
            g2.drawImage(
                alphaBlend.getImage(),
                alphaBlend.getX(),
                alphaBlend.getY(),
                alphaBlend.getWidth(),
                alphaBlend.getHeight(),
                null);
        } else if (tag instanceof StretchDIBits) {
            // The StretchDIBits function copies the color data for a rectangle of pixels in a
            // DIB to the specified destination rectangle. If the destination rectangle is larger
            // than the source rectangle, this function stretches the rows and columns of color
            // data to fit the destination rectangle. If the destination rectangle is smaller
            // than the source rectangle, this function compresses the rows and columns by using
            // the specified raster operation.
            StretchDIBits diBits = ((StretchDIBits)tag);
            g2.drawImage(
                diBits.getImage(),
                diBits.getX(),
                diBits.getY(),
                diBits.getWidthSrc(),
                diBits.getHeightSrc(),
                null);
        } else if (tag instanceof SetStretchBltMode) {
            // The stretching mode defines how the system combines rows or columns of a
            // bitmap with existing pixels on a display device when an application calls
            // the StretchBlt function.
            currentScaleMode = getScaleMode((SetStretchBltMode) tag);
        } else if (tag instanceof SetBrushOrgEx) {
            // The SetBrushOrgEx function sets the brush origin that GDI assigns to
            // the next (only to the next!) brush an application selects into the specified
            // device context.
            currentBrushOrigin = ((SetBrushOrgEx)tag).getPoint();
        } else if (tag instanceof SaveDC) {
            // create a DC instance with current settings
            DC dc = new DC();
            dc.paint = g2.getPaint();
            dc.stroke = g2.getStroke();
            dc.transform = g2.getTransform();
            dc.clip = g2.getClip();
            dc.path = currentPath;
            dc.meterLimit = currentMeterLimit;
            dc.windingRule = currentWindingRule;
            dc.bkMode = currentBkMode;
            dc.useCreatePen = useCreatePen;
            dc.scaleMode = currentScaleMode;
            // push it on top of the stack
            dcStack.push(dc);
        } else if (tag instanceof RestoreDC) {
            // is somethoing stored?
            if (!dcStack.empty()) {
                // read it
                DC dc = (DC) dcStack.pop();

                // use it
                currentMeterLimit = dc.meterLimit;
                currentWindingRule = dc.windingRule;
                currentPath = dc.path;
                currentBkMode = dc.bkMode;
                useCreatePen = dc.useCreatePen;
                currentScaleMode = dc.scaleMode;
                g2.setPaint(dc.paint);
                g2.setStroke(dc.stroke);
                g2.setTransform(dc.transform);
                g2.setClip(dc.clip);
            } else {
                // set the default values
            }
        } else if (tag instanceof SetBkColor) {
            // This function fills the gaps between styled lines drawn using a
            // pen created by the CreatePen function; it does not fill the gaps
            // between styled lines drawn using a pen created by the ExtCreatePen
            // function. The SetBKColor function also sets the background colors
            // for TextOut and ExtTextOut.

            // If the background mode is OPAQUE, the background color is used to
            // fill gaps between styled lines, gaps between hatched lines in brushes,
            // and character cells. The background color is also used when converting
            // bitmaps from color to monochrome and vice versa.

            // TODO: affects TextOut and ExtTextOut, CreatePen
        } else if (tag instanceof SetMetaRgn) {
            // The SetMetaRgn function intersects the current clipping region
            // for the specified device context with the current metaregion and
            // saves the combined region as the new metaregion for the specified
            // device context. The clipping region is reset to a null region.

            // TODO: what ist the current metaregion?
        } else if (tag instanceof SetMiterLimit) {
            // The SetMiterLimit function sets the limit for the length of miter
            // joins for the specified device context.
            // The miter length is defined as the distance from the intersection
            // of the line walls on the inside of the join to the intersection of
            // the line walls on the outside of the join. The miter limit is the
            // maximum allowed ratio of the miter length to the line width.

            // The default miter limit is 10.0.
            currentMeterLimit = ((SetMiterLimit)tag).getMiterLimit();
        } else if (tag instanceof Polygon16) {
            Point[] points = ((Polygon16) tag).getPoints();

            // Safety check.
            if (points.length > 1) {
                GeneralPath path = new GeneralPath(currentWindingRule);
                path.moveTo((float)points[0].getX(), (float)points[0].getY());
                for (int i = 1; i < points.length; i++) {
                    path.lineTo((float)points[i].getX(), (float)points[i].getY());
                }
                path.closePath();
                fillAndDrawOrAppend(g2, path);
            }
        } else if (tag instanceof PolyBezier16) {
            Point[] points = ((PolyBezier16)tag).getPoints();
            int numberOfPoints = ((PolyBezier16)tag).getNumberOfPoints();
            if (points != null && points.length > 0) {
                GeneralPath gp = new GeneralPath(currentWindingRule);
                Point p = points[0];
                gp.moveTo((float) p.getX(),  (float)p.getY());

                for (int point = 1; point < numberOfPoints; point = point + 3) {
                    // add a point to gp
                    Point p1 = points[point];
                    Point p2 = points[point + 1];
                    Point p3 = points[point + 2];
                    if (point > 0) {
                        gp.curveTo(
                            (float)p1.getX(), (float)p1.getY(),
                            (float)p2.getX(), (float)p2.getY(),
                            (float)p3.getX(), (float)p3.getY());
                    }
                }
                fillAndDrawOrAppend(g2, gp);
            }
        } else if (tag instanceof PolyBezierTo) {
            polyBezierTo(
                ((PolyBezierTo)tag).getPoints(),
                ((PolyBezierTo)tag).getNumberOfPoints());
        } else if (tag instanceof PolyBezierTo16) {
            polyBezierTo(
                ((PolyBezierTo16)tag).getPoints(),
                ((PolyBezierTo16)tag).getNumberOfPoints());
        } else if (tag instanceof Polyline16) {
            Point[] points = ((Polyline16)tag).getPoints();
            int numberOfPoints = ((Polyline16)tag).getNumberOfPoints();
            if (points != null && points.length > 0) {
                GeneralPath gp = new GeneralPath(currentWindingRule);
                Point p;
                for (int point = 0; point < numberOfPoints; point ++) {
                    // add a point to gp
                    p = points[point];
                    if (point > 0) {
                        gp.lineTo((float) p.getX(),  (float)p.getY());
                    } else {
                        gp.moveTo((float) p.getX(),  (float)p.getY());
                    }
                }
                drawOrAppend(g2, gp);
            }
        } else if (tag instanceof PolylineTo) {
            polylineTo(
                ((PolylineTo)tag).getPoints(),
                ((PolylineTo)tag).getNumberOfPoints());
        } else if (tag instanceof PolylineTo16) {
            polylineTo(
                ((PolylineTo16)tag).getPoints(),
                ((PolylineTo16)tag).getNumberOfPoints());
        } else if (tag instanceof PolyPolygon16) {
            PolyPolygon16 polyPolygon16 = (PolyPolygon16)tag;
            // read values from polyPolygon16
            int numberOfPolygons = polyPolygon16.getNumberOfPolys();
            int[] numberOfPoints = polyPolygon16.getNumberOfPoints();
            Point[][] points = polyPolygon16.getPoints();

            // create a GeneralPath containing GeneralPathes
            GeneralPath path = new GeneralPath(currentWindingRule);

            // iterate the polgons
            Point p;
            for (int polygon = 0; polygon < numberOfPolygons; polygon++) {

                // create a new member of path
                GeneralPath gp = new GeneralPath(currentWindingRule);
                for (int point = 0; point < numberOfPoints[polygon]; point ++) {
                    // add a point to gp
                    p = points[polygon][point];
                    if (point > 0) {
                        gp.lineTo((float) p.getX(),  (float)p.getY());
                    } else {
                        gp.moveTo((float) p.getX(),  (float)p.getY());
                    }
                }

                // close the member, add it to path
                gp.closePath();
                path.append(gp, false);
            }

            // draw the complete path
            fillAndDrawOrAppend(g2, path);
        } else if (tag instanceof BitBlt) {
            BitBlt bitBlt = (BitBlt)tag;
            g2.drawImage(
                bitBlt.getImage(),
                bitBlt.getTransform(),
                null);
        } else if (tag instanceof SetPolyFillMode) {
            currentWindingRule = getWindingRule((SetPolyFillMode) tag);
        } else if (tag instanceof ModifyWorldTransform) {
            AffineTransform at = ((ModifyWorldTransform)tag).getTransform();
            if (currentPath != null) {
                currentPathTransform.concatenate(at);
                g2.transform(at);
            } else {
                g2.transform(at);
            }
        } else if (tag instanceof SetWorldTransform) {
            AffineTransform at = ((SetWorldTransform)tag).getTransform();
            if (currentPath != null) {
                currentPathTransform = at;
            } else {
                resetTransformation(g2);
                g2.transform(at);
            }
        } else if (tag instanceof GDIComment || tag instanceof EOF) {
            // TODO: GDIComments contain image informations too.
        } else if (tag instanceof SetICMMode) {
            // Ignore SetICMMode, it is used for restricted color contexts
        } else {
            logger.warning("EMF tag not supported: " + tag);
        }
    }

    /**
     * closes and appends the current open figure to the
     * currentPath
     */
    private void closeCurrentFigure() {
        if (currentFigure == null) {
            return;
        }

        try {
            currentFigure.closePath();
            appendToCurrentPath(currentFigure);
            currentFigure = null;
        } catch (java.awt.geom.IllegalPathStateException e) {
            logger.warning("no figure to close");
        }
    }

    /**
     * Logical units are mapped to arbitrary units with equally scaled axes;
     * that is, one unit along the x-axis is equal to one unit along the y-axis.
     * Use the SetWindowExtEx and SetViewportExtEx functions to specify the
     * units and the orientation of the axes. Graphics device interface (GDI)
     * makes adjustments as necessary to ensure the x and y units remain the
     * same size (When the window extent is set, the viewport will be adjusted
     * to keep the units isotropic).
     */
    private void fixViewportSize() {
        if (mapModeIsotropic && (windowSize != null && viewportSize != null)) {
            viewportSize.setSize(
                viewportSize.getWidth(),
                viewportSize.getWidth() *
                    (windowSize.getHeight() / windowSize.getWidth())
            );
        }
    }

    /**
     * used by PolylineTo and PolylineTo16
     * @param points array of points
     * @param numberOfPoints number of points in the array
     */
    private void polylineTo(Point[] points, int numberOfPoints) {
        if (points != null && points.length > 0) {
            for (int point = 0; point < numberOfPoints; point ++) {
                // add a point to gp
                currentFigure.lineTo(
                    (float) points[point].getX(),
                    (float) points[point].getY());
            }
        }
    }

    /**
     * used by PolyBezierTo and PolyBezierTo16
     * @param points array of points
     * @param numberOfPoints number of points in the array
     */
    private void polyBezierTo(Point[] points, int numberOfPoints) {
        if (points != null && points.length > 0) {

            Point p1, p2, p3;
            for (int point = 0; point < numberOfPoints; point = point + 3) {
                // add a point to gp
                p1 = points[point];
                p2 = points[point + 1];
                p3 = points[point + 2];
                currentFigure.curveTo(
                    (float)p1.getX(), (float)p1.getY(),
                    (float)p2.getX(), (float)p2.getY(),
                    (float)p3.getX(), (float)p3.getY());
            }
        }
    }

    /**
     * gets a winding rule for GeneralPath creation based on
     * EMF SetPolyFillMode.
     *
     * @param polyFillMode SetPolyFillMode to convert
     * @return winding rule
     */
    private int getWindingRule(SetPolyFillMode polyFillMode) {
        if (polyFillMode != null) {
            if (polyFillMode.getMode() == EMFConstants.WINDING) {
                return GeneralPath.WIND_EVEN_ODD;
            } else if (polyFillMode.getMode() == EMFConstants.ALTERNATE) {
                return GeneralPath.WIND_NON_ZERO;
            }
        }
        return GeneralPath.WIND_EVEN_ODD;
    }

    /**
     * returns a BasicStroke JOIN for an EMF pen style
     * @param penStyle penstyle
     * @return e.g. {@link BasicStroke#JOIN_MITER}
     */
    private int getJoin(int penStyle) {
        switch (penStyle & 0xF000) {
            case EMFConstants.PS_JOIN_ROUND:
                return BasicStroke.JOIN_ROUND;
            case EMFConstants.PS_JOIN_BEVEL:
                return BasicStroke.JOIN_BEVEL;
            case EMFConstants.PS_JOIN_MITER:
                return BasicStroke.JOIN_MITER;
            default:
                logger.warning("got unsupported pen style " + penStyle);
                return BasicStroke.JOIN_ROUND;
        }
    }

    /**
     * returns a BasicStroke JOIN for an EMF pen style
     * @param penStyle Style to convert
     * @return asicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE, BasicStroke.CAP_BUTT
     */
    private int getCap(int penStyle) {
        switch (penStyle & 0xF00) {
            case EMFConstants.PS_ENDCAP_ROUND:
                return BasicStroke.CAP_ROUND;
            case EMFConstants.PS_ENDCAP_SQUARE:
                return BasicStroke.CAP_SQUARE;
            case EMFConstants.PS_ENDCAP_FLAT:
                return BasicStroke.CAP_BUTT;
            default:
                logger.warning("got unsupported pen style " + penStyle);
                return BasicStroke.CAP_ROUND;
        }
    }

    /**
     * returns a Dash for an EMF pen style
     * @param penStyle Style to convert
     * @param style used if EMFConstants#PS_USERSTYLE is set
     * @return float[] representing a dash
     */
    private float[] getDash(int penStyle, int[] style) {
        switch (penStyle & 0xFF) {
            case EMFConstants.PS_SOLID:
                // do not use float[] { 1 }
                // it's _slow_
                return null;
            case EMFConstants.PS_DASH:
                return new float[] { 5, 5 };
            case EMFConstants.PS_DOT:
                return new float[] { 1, 2 };
            case EMFConstants.PS_DASHDOT:
                return new float[] { 5, 2, 1, 2 };
            case EMFConstants.PS_DASHDOTDOT:
                return new float[] { 5, 2, 1, 2, 1, 2 };
            case EMFConstants.PS_NULL:
                // do not use float[] { 1 }
                // it's _slow_
                return null;
            case EMFConstants.PS_USERSTYLE:
                if (style != null && style.length > 0) {
                    float[] result = new float[style.length];
                    for (int i = 0; i < style.length; i++) {
                        result[i] = style[i];
                    }
                    return result;
                } else {
                    return null;
                }
            default:
                logger.warning("got unsupported pen style " + penStyle);
                // do not use float[] { 1 }
                // it's _slow_
                return null;
        }
    }

    /**
     * fills a shape using the currentBrushPaint,  currentPenPaint and currentPenStroke
     * @param g2 Painting context
     * @param s Shape to fill with current brush
     */
    private void fillAndDrawOrAppend(Graphics2D g2, Shape s) {
        // don't draw, just append the shape if BeginPath
        // has opened the currentPath
        if (!appendToCurrentPath(s)) {
            // The SetBkMode function affects the line styles for lines drawn using a
            // pen created by the CreatePen function. SetBkMode does not affect lines
            // drawn using a pen created by the ExtCreatePen function.
            if (useCreatePen) {
                // OPAQUE 	Background is filled with the current background
                // color before the text, hatched brush, or pen is drawn.
                if (currentBkMode == EMFConstants.BKG_OPAQUE) {
                    fillShape(g2, s);
                } else {
                    // TRANSPARENT 	Background remains untouched.
                    // TODO: if we really do nothing some drawings are incomplete
                    // this needs definitly a fix
                    fillShape(g2, s);
                }
            } else {
                // always fill the background if ExtCreatePen is set
                fillShape(g2, s);
            }
            drawShape(g2, s);
        }
    }

    /**
     * draws a shape using the currentPenPaint and currentPenStroke
     * @param g2 Painting context
     * @param s Shape to draw with current paen
     */
    private void drawOrAppend(Graphics2D g2, Shape s) {
        // don't draw, just append the shape if BeginPath
        // opens a GeneralPath
        if (!appendToCurrentPath(s)) {
            drawShape(g2, s);
        }
    }

    /**
     * Append the shape to the current path
     *
     * @param s Shape to fill with current brush
     * @return true, if currentPath was changed
     */
    private boolean appendToCurrentPath(Shape s) {
        // don't draw, just append the shape if BeginPath
        // opens a GeneralPath
        if (currentPath != null) {
            // aplly transformation if set
            if (currentPathTransform != null) {
                s = currentPathTransform.createTransformedShape(s);
            }
            // append the shape
            currentPath.append(s, false);
            // current path set
            return false;
        }
        // current path not set
        return false;
    }

    /**
     * fills a shape using the currentBrushPaint,  currentPenPaint and currentPenStroke.
     * This method should only be called for path painting. It doesn't check for a
     * current path.
     *
     * @param g2 Painting context
     * @param s Shape to fill with current brush
     */
    private void fillShape(Graphics2D g2, Shape s) {
        g2.setPaint(currentBrushPaint);
        g2.fill(s);
    }

    /**
     * draws a shape using the currentPenPaint and currentPenStroke
     * This method should only be called for path drawing. It doesn't check for a
     * current path.
     *
     * @param g2 Painting context
     * @param s Shape to draw with current paen
     */
    private void drawShape(Graphics2D g2, Shape s) {
        g2.setStroke(currentPenStroke);

        // R2_BLACK 	Pixel is always 0.
        if (currentROP2 == EMFConstants.R2_BLACK) {
            g2.setComposite(AlphaComposite.SrcOver);
            g2.setPaint(Color.black);
        }
        // R2_COPYPEN 	Pixel is the pen color.
        else if (currentROP2 == EMFConstants.R2_COPYPEN) {
            g2.setComposite(AlphaComposite.SrcOver);
            g2.setPaint(currentPenPaint);
        }
        // R2_NOP 	Pixel remains unchanged.
        else if (currentROP2 == EMFConstants.R2_NOP) {
            g2.setComposite(AlphaComposite.SrcOver);
            g2.setPaint(currentPenPaint);
        }
        // R2_WHITE 	Pixel is always 1.
        else if (currentROP2 == EMFConstants.R2_WHITE) {
            g2.setComposite(AlphaComposite.SrcOver);
            g2.setPaint(Color.white);
        }
        // R2_NOTCOPYPEN 	Pixel is the inverse of the pen color.
        else if (currentROP2 == EMFConstants.R2_NOTCOPYPEN) {
            g2.setComposite(AlphaComposite.SrcOver);
            // TODO: set at least inverted color if paint is a color
        }
        // R2_XORPEN 	Pixel is a combination of the colors
        // in the pen and in the screen, but not in both.
        else if (currentROP2 == EMFConstants.R2_XORPEN) {
            g2.setComposite(AlphaComposite.Xor);
        } else {
            logger.warning("got unsupported ROP" + currentROP2);
            // TODO:
            //R2_MASKNOTPEN 	Pixel is a combination of the colors common to both the screen and the inverse of the pen.
            //R2_MASKPEN 	Pixel is a combination of the colors common to both the pen and the screen.
            //R2_MASKPENNOT 	Pixel is a combination of the colors common to both the pen and the inverse of the screen.
            //R2_MERGENOTPEN 	Pixel is a combination of the screen color and the inverse of the pen color.
            //R2_MERGEPEN 	Pixel is a combination of the pen color and the screen color.
            //R2_MERGEPENNOT 	Pixel is a combination of the pen color and the inverse of the screen color.
            //R2_NOT 	Pixel is the inverse of the screen color.
            //R2_NOTCOPYPEN 	Pixel is the inverse of the pen color.
            //R2_NOTMASKPEN 	Pixel is the inverse of the R2_MASKPEN color.
            //R2_NOTMERGEPEN 	Pixel is the inverse of the R2_MERGEPEN color.
            //R2_NOTXORPEN 	Pixel is the inverse of the R2_XORPEN color.
        }

        g2.draw(s);
    }

    /**
     * converts a SetStretchBltMode to a twipScale constat of class Image
     * @return e.g. {@link Image#SCALE_FAST}
     * @param stretchBltMode EMFTag SetStretchBltMode
     */
    private int getScaleMode(SetStretchBltMode stretchBltMode) {
        //     COLORONCOLOR 	Deletes the pixels. This mode deletes all
        // eliminated lines of pixels without trying to preserve their information.
        if (
            stretchBltMode.getMode() == EMFConstants.COLORONCOLOR ||
            stretchBltMode.getMode() == EMFConstants.STRETCH_DELETESCANS) {
            return Image.SCALE_FAST;
        }
        //     HALFTONE 	Maps pixels from the source rectangle into blocks
        // of pixels in the destination rectangle. The average color over the
        // destination block of pixels approximates the color of the source pixels.
        else if (
            stretchBltMode.getMode() == EMFConstants.HALFTONE ||
            stretchBltMode.getMode() == EMFConstants.STRETCH_HALFTONE) {
            return Image.SCALE_SMOOTH;
        }
        //     BLACKONWHITE 	Performs a Boolean AND operation using the
        // color values for the eliminated and existing pixels. If the bitmap
        // is a monochrome bitmap, this mode preserves black pixels at the
        // expense of white pixels.
        else if (
            stretchBltMode.getMode() == EMFConstants.BLACKONWHITE ||
            stretchBltMode.getMode() == EMFConstants.STRETCH_ANDSCANS) {
            // TODO not sure
            return Image.SCALE_REPLICATE;
        }
        //     WHITEONBLACK 	Performs a Boolean OR operation using the
        // color values for the eliminated and existing pixels. If the bitmap
        // is a monochrome bitmap, this mode preserves white pixels at the
        // expense of black pixels.
        else if (
            stretchBltMode.getMode() == EMFConstants.WHITEONBLACK ||
            stretchBltMode.getMode() == EMFConstants.STRETCH_ORSCANS) {
            // TODO not sure
            return Image.SCALE_REPLICATE;
        } else {
            logger.warning("got unsupported SetStretchBltMode " + stretchBltMode);
            return Image.SCALE_DEFAULT;
        }
    }
}
