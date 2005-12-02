// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * CloseFigure TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/CloseFigure.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class CloseFigure extends EMFTag {

    public CloseFigure() {
        super(61, 1);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return this;
    }

}
