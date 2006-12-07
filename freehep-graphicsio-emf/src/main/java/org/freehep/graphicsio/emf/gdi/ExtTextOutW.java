// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.awt.Rectangle;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * ExtTextOutW TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/ExtTextOutW.java f2f1115939ae 2006/12/07 07:50:41 duns $
 */
public class ExtTextOutW extends EMFTag implements EMFConstants {

    private Rectangle bounds;

    private int mode;

    private float xScale, yScale;

    private TextW text;

    public ExtTextOutW() {
        super(84, 1);
    }

    public ExtTextOutW(Rectangle bounds, int mode, float xScale, float yScale,
            TextW text) {
        this();
        this.bounds = bounds;
        this.mode = mode;
        this.xScale = xScale;
        this.yScale = yScale;
        this.text = text;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        ExtTextOutW tag = new ExtTextOutW(emf.readRECTL(), emf.readDWORD(), emf
                .readFLOAT(), emf.readFLOAT(), new TextW(emf));
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
        emf.writeDWORD(mode);
        emf.writeFLOAT(xScale);
        emf.writeFLOAT(yScale);
        text.write(emf);
    }

    public String toString() {
        return super.toString() + "\n" + "  bounds: " + bounds + "\n"
                + "  mode: " + mode + "\n" + "  xScale: " + xScale + "\n"
                + "  yScale: " + yScale + "\n" + text.toString();
    }
}
