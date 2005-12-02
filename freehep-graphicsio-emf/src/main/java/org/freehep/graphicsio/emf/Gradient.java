// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * EMF Gradient
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/Gradient.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public abstract class Gradient {

    public Gradient() {
    }

    public abstract void write(EMFOutputStream emf) throws IOException;
}
