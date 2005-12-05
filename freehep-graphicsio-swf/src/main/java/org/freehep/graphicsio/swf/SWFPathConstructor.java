// Copyright 2001-2004 FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;
import java.util.Vector;

import org.freehep.graphicsio.CubicToQuadPathConstructor;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SWFPathConstructor.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class SWFPathConstructor extends CubicToQuadPathConstructor implements
        SWFConstants {
    private Vector path;

    // to avoid rounding errors we keep all values in TWIPS.
    private int x0, y0;

    private int xc, yc;

    private int stroke, fill0, fill1;

    public SWFPathConstructor(Vector path, int stroke, int fill0, int fill1) {
        // resolution equals .5 twips
        this(path, stroke, fill0, fill1, 0.5 / TWIPS);
    }

    public SWFPathConstructor(Vector path, int stroke, int fill0, int fill1,
            double resolution) {
        super(resolution);
        this.path = path;
        this.stroke = stroke;
        this.fill0 = fill0;
        this.fill1 = fill1;
    }

    public void move(double x, double y) throws IOException {
        x0 = toInt(x);
        y0 = toInt(y);
        xc = x0;
        yc = y0;
        path.add(new SWFShape.ShapeRecord(true, xc, yc, fill0, fill1, stroke));
        super.move(x, y);
    }

    public void line(double x, double y) throws IOException {
        int dx = toInt(x) - xc;
        int dy = toInt(y) - yc;
        path.add(new SWFShape.EdgeRecord(dx, dy));
        xc += dx;
        yc += dy;
        super.line(x, y);
    }

    public void quad(double x1, double y1, double x2, double y2)
            throws IOException {
        int cdx = toInt(x1) - xc;
        int cdy = toInt(y1) - yc;
        int dx = toInt(x2 - x1);
        int dy = toInt(y2 - y1);
        path.add(new SWFShape.EdgeRecord(cdx, cdy, dx, dy));
        xc += dx + cdx;
        yc += dy + cdy;

        // important for cubic method
        currentX = x2;
        currentY = y2;
    }

    public void closePath(double xd0, double yd0) throws IOException {
        if ((xc != x0) || (yc != y0)) {
            // due to rounding we may miss the closing, so we do it by hand
            path.add(new SWFShape.EdgeRecord(x0 - xc, y0 - yc));
        }
        super.closePath(xd0, yd0);
    }

    private int toInt(double d) {
        return (int) (d * TWIPS);
    }
}
