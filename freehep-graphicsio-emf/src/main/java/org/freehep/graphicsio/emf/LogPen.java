// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Color;
import java.io.IOException;

/**
 * EMF LogPen
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/LogPen.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class LogPen implements EMFConstants {

    private int penStyle;

    private int width;

    private Color color;

    public LogPen(int penStyle, int width, Color color) {
        this.penStyle = penStyle;
        this.width = width;
        this.color = color;
    }

    LogPen(EMFInputStream emf) throws IOException {
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
        return "  LogPen\n" + "    penstyle: " + penStyle + "\n"
                + "    width: " + width + "\n" + "    color: " + color;
    }

    public int getPenStyle() {
        return penStyle;
    }

    public int getWidth() {
        return width;
    }

    public Color getColor() {
        return color;
    }
}
