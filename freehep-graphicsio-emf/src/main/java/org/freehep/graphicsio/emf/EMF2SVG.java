package org.freehep.graphicsio.emf;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.freehep.graphicsio.emf.gdi.Arc;
import org.freehep.graphicsio.emf.gdi.BeginPath;
import org.freehep.graphicsio.emf.gdi.CreateBrushIndirect;
import org.freehep.graphicsio.emf.gdi.CreatePen;
import org.freehep.graphicsio.emf.gdi.DeleteObject;
import org.freehep.graphicsio.emf.gdi.EndPath;
import org.freehep.graphicsio.emf.gdi.FillPath;
import org.freehep.graphicsio.emf.gdi.LineTo;
import org.freehep.graphicsio.emf.gdi.LogBrush32;
import org.freehep.graphicsio.emf.gdi.LogPen;
import org.freehep.graphicsio.emf.gdi.MoveToEx;
import org.freehep.graphicsio.emf.gdi.SelectObject;
import org.freehep.util.io.Tag;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * @author Carsten Zerbst carsten.zerbst@atlantec-es.com
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMF2SVG.java f2f1115939ae 2006/12/07 07:50:41 duns $
 */
public class EMF2SVG {
    private EMFInputStream is;

    private Element svg;

    private Element currentPath;

    private Element group;

    private Point currentPosition;

    private Object currentObject;

    private LogPen currentPen;

    private HashMap objects = new HashMap();

    public EMF2SVG(EMFInputStream is) {
        this.is = is;
    }

    public String getSVG() {
        try {
            EMFHeader header = is.readHeader();
            System.out.println("header " + header);

            svg = new Element("svg");

            svg.setAttribute("version", "1.1");

            svg.setAttribute("width", "" + header.getBounds().width / 10);
            svg.setAttribute("height", "" + header.getBounds().height / 10);

            group = new Element("g");
            group.setAttribute("transform", "scale(0.1)");
            svg.addContent(group);

            Element g = new Element("g");
            g.setAttribute("transform", "translate( "
                    + (-1 * header.getBounds().x) + " "
                    + (-1 * header.getBounds().y) + ")");

            group.addContent(g);
            group = g;

            Tag tag = is.readTag();
            while (tag != null) {
                System.out.println(tag);
                map(tag);
                tag = is.readTag();
            }
        } catch (IOException ioexp) {
            ioexp.printStackTrace();
        }

        String retval = null;
        try {
            org.jdom.output.XMLOutputter output = new org.jdom.output.XMLOutputter(
                    org.jdom.output.Format.getPrettyFormat());
            retval = output.outputString(svg);
        } catch (Exception exp) {
        }

        return retval;
    }

    /**
     * the mapping function EMF tags -> svg elements
     */
    private void map(Tag tag) {
        if (tag instanceof Arc) {
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

            Element path = new Element("path");
            StringBuffer def = new StringBuffer();
            def.append("M" + arc.getStart().x + "," + arc.getStart().y);
            def.append(" ");
            def.append("a" + (arc.getBounds().width / 2) + ","
                    + (arc.getBounds().height / 2));
            def.append(" 0 ");
            def.append(" 0,0");
            def.append(arc.getEnd().x + "," + arc.getEnd().y);

            path.setAttribute("d", def.toString());

            path.setAttribute("stroke", "blue");
            path.setAttribute("stroke-width", "1");

            group.addContent(path);
        } else if (tag instanceof BeginPath) {
            beginPath();
        } else if (tag instanceof CreateBrushIndirect) {
            CreateBrushIndirect cbi = (CreateBrushIndirect) tag;
            objects.put(new Integer(cbi.getIndex()), cbi.getBrush());
        } else if (tag instanceof CreatePen) {
            CreatePen cpen = (CreatePen) tag;
            currentPen = cpen.getPen();
        } else if (tag instanceof DeleteObject) {
            objects.remove(new Integer(((DeleteObject) tag).getIndex()));
        } else if (tag instanceof EndPath) {
            // nothing done here, wait for fillPath ...
        } else if (tag instanceof FillPath) {
            if (currentObject instanceof LogBrush32) {
                LogBrush32 brush = (LogBrush32) currentObject;
                currentPath.setAttribute("fill", printColor(brush.getColor()));
                currentPath.setAttribute("fill-rule", "evenodd");

                // } else if ( currentObject instanceof LogPen ) {
            } else {
                System.out.println("unsupported object " + currentObject
                        + " for FillPath");
            }
        } else if (tag instanceof LineTo) {
            LineTo lineTo = (LineTo) tag;

            StringBuffer def = new StringBuffer();
            def.append(currentPath.getAttributeValue("d"));

            def.append("L " + lineTo.getPoint().x + " " + lineTo.getPoint().y
                    + " ");

            Attribute attr = currentPath.getAttribute("d");
            attr.setValue(def.toString());
        } else if (tag instanceof MoveToEx) {
            if (currentPath == null) {
                beginPath();
            }

            MoveToEx mte = (MoveToEx) tag;
            currentPosition = mte.getPoint();

            StringBuffer def = new StringBuffer();
            Attribute d = currentPath.getAttribute("d");
            def.append(d.getValue());
            def
                    .append("M " + currentPosition.x + " " + currentPosition.y
                            + " ");

            d.setValue(def.toString());
        } else if (tag instanceof SelectObject) {
            currentObject = objects.get(new Integer(((SelectObject) tag)
                    .getIndex()));
        } else {
            System.out.println("tag " + tag + " not supported");
        }
    }

    private void beginPath() {
        if (currentPath != null) {
            closePath();
        }

        currentPath = new Element("path");
        currentPath.setAttribute("d", "");
        if (currentPen != null) {
            currentPath.setAttribute("stroke",
                    printColor(currentPen.getColor()));
            currentPath
                    .setAttribute("stroke-width", "" + currentPen.getWidth());
        }

        group.addContent(currentPath);
    }

    private void closePath() {
    }

    private String printColor(Color color) {
        StringBuffer buff = new StringBuffer();
        buff.append("#");
        buff.append(Integer.toHexString(color.getRed()));
        buff.append(Integer.toHexString(color.getGreen()));
        buff.append(Integer.toHexString(color.getBlue()));

        String retval = buff.toString();
        if ("#00ff".equals(retval)) {
            retval = "#00f";
        }

        return retval.toString();
    }

    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream(args[0]);
            EMFInputStream emf = new EMFInputStream(fis);

            EMF2SVG mapper = new EMF2SVG(emf);

            File f = new File("test.svg");
            PrintWriter out = new PrintWriter(new FileWriter(f));

            out.print(mapper.getSVG());
            out.close();
        } catch (IOException ioexp) {
            ioexp.printStackTrace();
        }
    }
}
