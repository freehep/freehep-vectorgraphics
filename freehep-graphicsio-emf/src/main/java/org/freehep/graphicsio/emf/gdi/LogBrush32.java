// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Color;
import java.io.IOException;
import java.util.logging.Logger;

import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * EMF LogBrush32
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/LogBrush32.java cb17a8f71934 2007/01/23 15:44:34 duns $ see
 *          http://msdn.microsoft.com/library/default.asp?url=/library/en-us/gdi/brushes_8yk2.asp
 */
public class LogBrush32 implements EMFConstants, GDIObject {

    private int style;

    private Color color;

    private int hatch;

    public LogBrush32(int style, Color color, int hatch) {
        this.style = style;
        this.color = color;
        this.hatch = hatch;
    }

    public LogBrush32(EMFInputStream emf) throws IOException {
        style = emf.readUINT();
        color = emf.readCOLORREF();
        hatch = emf.readULONG();
    }

    public void write(EMFOutputStream emf) throws IOException {
        emf.writeUINT(style);
        emf.writeCOLORREF(color);
        emf.writeULONG(hatch);
    }

    public String toString() {
        return "  LogBrush32\n" + "    style: " + style +
            "\n    color: " + color +
            "\n    hatch: " + hatch;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        if (style == EMFConstants.BS_SOLID) {
            renderer.setBrushPaint(color);
        } else if (style == EMFConstants.BS_NULL) {
            // note: same value as BS_HOLLOW
            // Should probably do this by making a paint implementation that does nothing,
            // but a 100% transparent color works just as well for now.
            renderer.setBrushPaint(new Color(0, 0, 0, 0));

            // TODO: Support pattern types
            // TODO: Support hatching
            // TODO: Support DIB types
        } else {
            Logger logger = Logger.getLogger("org.freehep.graphicsio.emf");
            logger.warning("LogBrush32 style not supported: " + toString());
            renderer.setBrushPaint(color);
        }
    }
}
