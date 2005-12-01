// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * EMF GradientRectangle
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/GradientRectangle.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class GradientRectangle extends Gradient {

    private int upperLeft, lowerRight;

    public GradientRectangle(int upperLeft, int lowerRight) {
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
    }

    GradientRectangle(EMFInputStream emf) throws IOException {
        upperLeft = emf.readULONG();
        lowerRight = emf.readULONG();
    }
    
    public void write(EMFOutputStream emf) throws IOException {
        emf.writeULONG(upperLeft);
        emf.writeULONG(lowerRight);
    }

    public String toString() {
        return "  GradientRectangle: "+upperLeft+", "+lowerRight;
    }   
}

