// Copyright 2001-2004 FreeHEP.
package org.freehep.graphicsio.ps;

import java.io.IOException;
import java.io.PrintStream;

import org.freehep.graphicsio.QuadToCubicPathConstructor;
import org.freehep.util.ScientificFormat;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/PSPathConstructor.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class PSPathConstructor extends QuadToCubicPathConstructor {
    private PrintStream os;

    private boolean intPrecision;

    private String moveto, lineto, curveto, close;

    private ScientificFormat scientific = new ScientificFormat(6, 9, false);

    public PSPathConstructor(PrintStream os, boolean useProlog,
            boolean intPrecision) {
        super();
        this.os = os;
        this.intPrecision = intPrecision;
        if (useProlog) {
            moveto = "m";
            lineto = "l";
            curveto = "c";
            close = "h";
        } else {
            moveto = "moveto";
            lineto = "lineto";
            curveto = "curveto";
            close = "closepath";
        }
    }

    public void move(double x, double y) throws IOException {
        os.println(fixedPrecision(x) + " " + fixedPrecision(y) + " " + moveto);
        super.move(x, y);
    }

    public void line(double x, double y) throws IOException {
        os.println(fixedPrecision(x) + " " + fixedPrecision(y) + " " + lineto);
        super.line(x, y);
    }

    public void cubic(double x1, double y1, double x2, double y2, double x3,
            double y3) throws IOException {
        os.println(fixedPrecision(x1) + " " + fixedPrecision(y1) + " "
                + fixedPrecision(x2) + " " + fixedPrecision(y2) + " "
                + fixedPrecision(x3) + " " + fixedPrecision(y3) + " " + " "
                + curveto);
        super.cubic(x1, y1, x2, y2, x3, y3);
    }

    public void closePath(double x0, double y0) throws IOException {
        os.println(close);
        super.closePath(x0, y0);
    }

    protected String fixedPrecision(double d) {
        if (intPrecision) {
            return Integer.toString((int) d);
        } else {
            return scientific.format(d);

        }
    }
}
