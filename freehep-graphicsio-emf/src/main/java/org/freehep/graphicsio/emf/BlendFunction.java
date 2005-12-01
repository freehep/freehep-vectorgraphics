// Copyright 2002-2003, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * EMF BitmapInfoHeader
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/BlendFunction.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class BlendFunction implements EMFConstants {

    public static final int size = 4;
    private int blendOp;
    private int blendFlags;
    private int sourceConstantAlpha;
    private int alphaFormat;

    public BlendFunction(int blendOp, int blendFlags, int sourceConstantAlpha, int alphaFormat) {
        this.blendOp = blendOp;
        this.blendFlags = blendFlags;
        this.sourceConstantAlpha = sourceConstantAlpha;
        this.alphaFormat = alphaFormat;
    }

    public BlendFunction(EMFInputStream emf) throws IOException {
        blendOp = emf.readBYTE();
        blendFlags = emf.readBYTE();
        sourceConstantAlpha = emf.readBYTE();
        alphaFormat = emf.readBYTE();
    }

    public void write(EMFOutputStream emf) throws IOException {
        emf.writeBYTE(blendOp);
        emf.writeBYTE(blendFlags);
        emf.writeBYTE(sourceConstantAlpha);
        emf.writeBYTE(alphaFormat);
    }

    public String toString() {
        return "BlendFunction";
    }
}


