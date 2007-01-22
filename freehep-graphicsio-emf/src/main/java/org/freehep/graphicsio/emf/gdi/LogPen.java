// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Color;
import java.awt.BasicStroke;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * EMF LogPen
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/LogPen.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class LogPen extends AbstractPen {

    private int penStyle;

    private int width;

    private Color color;

    public LogPen(int penStyle, int width, Color color) {
        this.penStyle = penStyle;
        this.width = width;
        this.color = color;
    }

    public LogPen(EMFInputStream emf) throws IOException {
        penStyle = emf.readDWORD();
        width = emf.readDWORD();
        /* int y = */ emf.readDWORD();
        color = emf.readCOLORREF();
    }

    public void write(EMFOutputStream emf) throws IOException {
        emf.writeDWORD(penStyle);
        emf.writeDWORD(width);
        emf.writeDWORD(0);
        emf.writeCOLORREF(color);
    }

    public String toString() {
        return "  LogPen\n" + "    penstyle: " + penStyle +
            "\n    width: " + width +
            "\n    color: " + color;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        renderer.setUseCreatePen(true);
        renderer.setPenPaint(color);
        renderer.setPenStroke(new BasicStroke(
            width,
            getCap(penStyle),
            getJoin(penStyle),
            renderer.getMeterLimit(),
            getDash(penStyle, null),
            0));
    }
}
