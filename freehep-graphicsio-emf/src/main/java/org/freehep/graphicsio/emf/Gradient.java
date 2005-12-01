// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Color;
import java.io.IOException;

/**
 * EMF Gradient
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/Gradient.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public abstract class Gradient {

    public Gradient() {
    }

    public abstract void write(EMFOutputStream emf) throws IOException;
}

