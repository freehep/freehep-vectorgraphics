// Copyright 2007 FreeHEP
package org.freehep.graphicsio.emf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.logging.Logger;

import org.freehep.graphicsio.emf.gdi.AbstractExtTextOut;
import org.freehep.graphicsio.emf.gdi.Arc;
import org.freehep.graphicsio.emf.gdi.ArcTo;
import org.freehep.graphicsio.emf.gdi.BeginPath;
import org.freehep.graphicsio.emf.gdi.Chord;
import org.freehep.graphicsio.emf.gdi.CloseFigure;
import org.freehep.graphicsio.emf.gdi.CreateBrushIndirect;
import org.freehep.graphicsio.emf.gdi.CreatePen;
import org.freehep.graphicsio.emf.gdi.DeleteObject;
import org.freehep.graphicsio.emf.gdi.EMFRectangle;
import org.freehep.graphicsio.emf.gdi.EOF;
import org.freehep.graphicsio.emf.gdi.Ellipse;
import org.freehep.graphicsio.emf.gdi.ExtCreateFontIndirectW;
import org.freehep.graphicsio.emf.gdi.ExtLogFontW;
import org.freehep.graphicsio.emf.gdi.GDIComment;
import org.freehep.graphicsio.emf.gdi.LineTo;
import org.freehep.graphicsio.emf.gdi.LogBrush32;
import org.freehep.graphicsio.emf.gdi.LogFontW;
import org.freehep.graphicsio.emf.gdi.LogPen;
import org.freehep.graphicsio.emf.gdi.MoveToEx;
import org.freehep.graphicsio.emf.gdi.Pie;
import org.freehep.graphicsio.emf.gdi.Polygon16;
import org.freehep.graphicsio.emf.gdi.SelectObject;
import org.freehep.graphicsio.emf.gdi.SetTextAlign;
import org.freehep.graphicsio.emf.gdi.SetTextColor;
import org.freehep.graphicsio.emf.gdi.SetWindowOrgEx;
import org.freehep.graphicsio.emf.gdi.StockObjects;
import org.freehep.graphicsio.emf.gdi.Text;
import org.freehep.util.io.Tag;

/**
 * Standalone EMF renderer.
 *
 * @author Daniel Noll (daniel@nuix.com)
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFRenderer.java 86ef08292548 2007/01/17 23:15:57 duns $
 */
public class EMFRenderer {
    private static final Logger logger = Logger.getLogger("org.freehep.graphicsio.emf");

    private EMFInputStream is;
    private EMFHeader header;

    private Point currentPosition;
    private AffineTransform initialTransform;

    private Object[] gdiObjects = new Object[256]; // TODO: Make this more flexible.

    // Rendering state.
    private Color currentTextColor = Color.BLACK;
    private Paint currentBrushPaint = new Color(0, 0, 0, 0);
    private Paint currentPenPaint = Color.BLACK;
    private Stroke currentPenStroke = new BasicStroke();
    private Point windowOrigin = new Point(0, 0);
    private int textAlignMode = 0;


    /**
     * Constructs the renderer.
     *
     * @param is the input stream to read the EMF records from.
     * @throws IOException if an error occurs reading the header.
     */
    public EMFRenderer(EMFInputStream is) throws IOException {
        this.is = is;
        this.header = is.readHeader();
    }

    /**
     * Gets the size of a canvas which would be required to render the EMF.
     *
     * @return the size.
     */
    public Dimension getSize() {
        return header.getBounds().getSize();
    }

    /**
     * Paints the EMF onto the provided graphics context.
     *
     * @param g2 the graphics context to paint onto.
     */
    public void paint(Graphics2D g2) {
        initialTransform = g2.getTransform();
        rebuildTransform(g2);

        try {
            Tag tag;
            while ((tag = is.readTag()) != null) {
                map(tag, g2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rebuilds the transform for the given graphics context.
     *
     * @param g2 the graphics context.
     */
    private void rebuildTransform(Graphics2D g2) {
        g2.setTransform(initialTransform);
        g2.translate(-windowOrigin.getX(), -windowOrigin.getY());
    }

    /**
     * The mapping function EMF tags -> java2d methods
     *
     * @param tag the tag being rendered/mapped.
     * @param g2 the graphics context to draw onto.
     */
    private void map(Tag tag, Graphics2D g2) {
        if (tag instanceof SetWindowOrgEx) {
            windowOrigin = ((SetWindowOrgEx) tag).getPoint();
            rebuildTransform(g2);
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
            double nx0 = arc.getStart().x / arc.getBounds().width;

            // double ny0 = arc.getStart().y / arc.getBounds().height;
            double nx1 = arc.getEnd().x / arc.getBounds().width;

            // double ny1 = arc.getEnd().y / arc.getBounds().height;
            // calculate angle of start point
            double alpha0 = Math.acos(nx0);
            double alpha1 = Math.acos(nx1);

            Arc2D arc2d = new Arc2D.Double(arc.getStart().x, arc.getStart().y,
                                           arc.getBounds().width, arc.getBounds().height, alpha0,
                                           alpha1 - alpha0, Arc2D.OPEN);
//            currentShape = arc2d;
            g2.setPaint(currentPenPaint);
            g2.setStroke(currentPenStroke);
            g2.draw(arc2d);
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

            // update currentPosition
            currentPosition = arc.getEnd();

            Arc2D arc2d = new Arc2D.Double(arc.getStart().x, arc.getStart().y,
                                           arc.getBounds().width, arc.getBounds().height, alpha0,
                                           alpha1 - alpha0, Arc2D.OPEN);
//            currentShape = arc2d;
            g2.setPaint(currentPenPaint);
            g2.setStroke(currentPenStroke);
            g2.draw(arc2d);
        } else if (tag instanceof BeginPath) {
            // The BeginPath function opens a path bracket in the specified
            // device context.
//            currentShape = null;
        } else if (tag instanceof Chord) {
            // The Chord function draws a chord (a region bounded by the
            // intersection of an
            // ellipse and a line segment, called a secant). The chord is
            // outlined by using the
            // current pen and filled by using the current brush.
            Chord arc = (Chord) tag;

            // normalize start and end point to a circle
            double nx0 = arc.getStart().x / arc.getBounds().width;

            // double ny0 = arc.getStart().y / arc.getBounds().height;
            double nx1 = arc.getEnd().x / arc.getBounds().width;

            // double ny1 = arc.getEnd().y / arc.getBounds().height;
            // calculate angle of start point
            double alpha0 = Math.acos(nx0);
            double alpha1 = Math.acos(nx1);

            // update currentPosition
            currentPosition = arc.getEnd();

            Arc2D arc2d = new Arc2D.Double(arc.getStart().x, arc.getStart().y,
                                           arc.getBounds().width, arc.getBounds().height, alpha0,
                                           alpha1 - alpha0, Arc2D.CHORD);

            g2.setPaint(currentBrushPaint);
            g2.fill(arc2d);

//            currentShape = arc2d;
            g2.setPaint(currentPenPaint);
            g2.setStroke(currentPenStroke);
            g2.draw(arc2d);
        } else if (tag instanceof EMFRectangle) {
            Rectangle rect = ((EMFRectangle) tag).getBounds();

            g2.setPaint(currentBrushPaint);
            g2.fill(rect);

            g2.setPaint(currentPenPaint);
            g2.setStroke(currentPenStroke);
            g2.draw(rect);
        } else if (tag instanceof Polygon16) {
            Point[] points = ((Polygon16) tag).getPoints();

            // Safety check.
            if (points.length > 1) {
                GeneralPath path = new GeneralPath();
                path.moveTo(points[0].x, points[0].y);
                for (int i = 1; i < points.length; i++)
                {
                    path.lineTo(points[i].x, points[i].y);
                }
                path.closePath();

                g2.setPaint(currentBrushPaint);
                g2.fill(path);

                g2.setPaint(currentPenPaint);
                g2.setStroke(currentPenStroke);
                g2.draw(path);
            }
        } else if (tag instanceof CloseFigure) {
            // The CloseFigure function closes an open figure in a path.
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
        } else if (tag instanceof SelectObject) {
            int gdiIndex = ((SelectObject) tag).getIndex();
            Object gdiObject;

            if (gdiIndex < 0)
            {
                gdiObject = StockObjects.getStockObject(gdiIndex);
            }
            else
            {
                gdiObject = gdiObjects[gdiIndex];
            }

            // TODO: Other object types.
            if (gdiObject instanceof LogPen) {
                LogPen lpen = (LogPen) gdiObject;

                // Stroking styles
                float[] dash = new float[] { 1 };
                Paint paint = lpen.getColor();
                switch (lpen.getPenStyle() & 0xFF) {
                    case EMFConstants.PS_SOLID:
                        break;
                    case EMFConstants.PS_DASH:
                        dash = new float[] { 5, 5 };
                        break;
                    case EMFConstants.PS_DOT:
                        dash = new float[] { 1, 2 };
                        break;
                    case EMFConstants.PS_DASHDOT:
                        dash = new float[] { 5, 2, 1, 2 };
                        break;
                    case EMFConstants.PS_DASHDOTDOT:
                        dash = new float[] { 5, 2, 1, 2, 1, 2 };
                        break;
                    case EMFConstants.PS_NULL:
                        paint = new Color(0, 0, 0, 0);
                        break;
                    default:
                        logger.warning("got unsupported pen style " + lpen.getPenStyle());
                }

                // End cap styles
                int cap = BasicStroke.CAP_ROUND;
                switch (lpen.getPenStyle() & 0xF00)
                {
                    case EMFConstants.PS_ENDCAP_ROUND:
                        cap = BasicStroke.CAP_ROUND;
                        break;
                    case EMFConstants.PS_ENDCAP_SQUARE:
                        cap = BasicStroke.CAP_SQUARE;
                        break;
                    case EMFConstants.PS_ENDCAP_FLAT:
                        cap = BasicStroke.CAP_BUTT;
                        break;
                    default:
                        logger.warning("got unsupported pen style " + lpen.getPenStyle());
                }

                // Join styles
                int join = BasicStroke.JOIN_ROUND;
                switch (lpen.getPenStyle() & 0xF000)
                {
                    case EMFConstants.PS_JOIN_ROUND:
                        join = BasicStroke.JOIN_ROUND;
                        break;
                    case EMFConstants.PS_JOIN_BEVEL:
                        join = BasicStroke.JOIN_BEVEL;
                        break;
                    case EMFConstants.PS_JOIN_MITER:
                        join = BasicStroke.JOIN_MITER;
                        break;
                    default:
                        logger.warning("got unsupported pen style " + lpen.getPenStyle());
                }

                currentPenPaint = paint;
                currentPenStroke = new BasicStroke(lpen.getWidth(), cap, join, 1, dash, 0);
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

            Ellipse2D el2 = new Ellipse2D.Double(el.getBounds().getX(), el
                    .getBounds().getY(), el.getBounds().getWidth(), el
                    .getBounds().getHeight());
//            currentShape = el2;

            g2.setPaint(currentBrushPaint);
            g2.fill(el2);

            g2.setPaint(currentPenPaint);
            g2.setStroke(currentPenStroke);
            g2.draw(el2);
        } else if (tag instanceof LineTo) {
            // The LineTo function draws a line from the current position up to,
            // but not including, the specified point.
            // The line is drawn by using the current pen and, if the pen is a
            // geometric pen, the current brush.
            LineTo lineTo = (LineTo) tag;
            Line2D l2 = new Line2D.Double(currentPosition, lineTo.getPoint());

            g2.setPaint(currentPenPaint);
            g2.setStroke(currentPenStroke);
            g2.draw(l2);
//            currentShape = l2;
        } else if (tag instanceof MoveToEx) {
            // The MoveToEx function updates the current position to the
            // specified point
            // and optionally returns the previous position.
            MoveToEx mte = (MoveToEx) tag;
            currentPosition = mte.getPoint();
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

            Arc2D arc2d = new Arc2D.Double(arc.getStart().x, arc.getStart().y,
                                           arc.getBounds().width, arc.getBounds().height, alpha0,
                                           alpha1 - alpha0, Arc2D.PIE);
//            currentShape = arc2d;
            g2.setPaint(currentBrushPaint);
            g2.fill(arc2d);

            g2.setPaint(currentPenPaint);
            g2.setStroke(currentPenStroke);
            g2.draw(arc2d);
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

            g2.setPaint(currentTextColor);
            g2.drawString(str, x, y);
        } else if (tag instanceof GDIComment || tag instanceof EOF) {
            // Ignore these, they aren't useful for rendering.
        } else {
            logger.warning("EMF tag not supported: " + tag);
        }
    }
}
