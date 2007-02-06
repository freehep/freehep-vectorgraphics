// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Color;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * EMF ExtLogPen
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/ExtLogPen.java 10ec7516e3ce 2007/02/06 18:42:34 duns $
 */
public class ExtLogPen extends AbstractPen {

    private int penStyle;

    private int width;

    private int brushStyle;

    private Color color;

    private int hatch;

    private int[] style;

    public ExtLogPen(
        int penStyle,
        int width,
        int brushStyle,
        Color color,
        int hatch,
        int[] style) {

        this.penStyle = penStyle;
        this.width = width;
        this.brushStyle = brushStyle;
        this.color = color;
        this.hatch = hatch;
        this.style = style;
    }

    public ExtLogPen(EMFInputStream emf) throws IOException {
        penStyle = emf.readDWORD();
        width = emf.readDWORD();
        brushStyle = emf.readUINT();
        color = emf.readCOLORREF();
        hatch = emf.readULONG();
        int nStyle = emf.readDWORD();
        // it seems we always have to read one!
        if (nStyle == 0)
            emf.readDWORD();
        style = emf.readDWORD(nStyle);
    }

    public void write(EMFOutputStream emf) throws IOException {
        emf.writeDWORD(penStyle);
        emf.writeDWORD(width);
        emf.writeUINT(brushStyle);
        emf.writeCOLORREF(color);
        emf.writeULONG(hatch);
        emf.writeDWORD(style.length);
        // it seems we always have to write one!
        if (style.length == 0)
            emf.writeDWORD(0);
        emf.writeDWORD(style);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("  ExtLogPen\n");
        s.append("    penStyle: ");
        s.append(Integer.toHexString(penStyle));
        s.append("\n");
        s.append("    width: ");
        s.append(width);
        s.append("\n");
        s.append("    brushStyle: ");
        s.append(brushStyle);
        s.append("\n");
        s.append("    color: ");
        s.append(color);
        s.append("\n");
        s.append("    hatch: ");
        s.append(hatch);
        s.append("\n");
        for (int i = 0; i < style.length; i++) {
            s.append("      style[");
            s.append(i);
            s.append("]: ");
            s.append(style[i]);
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        renderer.setUseCreatePen(false);
        renderer.setPenPaint(color);
        renderer.setPenStroke(
            createStroke(renderer, penStyle, style, width));
    }
}
