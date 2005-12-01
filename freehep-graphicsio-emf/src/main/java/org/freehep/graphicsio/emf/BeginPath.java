// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

import org.freehep.util.io.Tag;

/**
 * BeginPath TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/BeginPath.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class BeginPath
    extends EMFTag {

    public BeginPath() {
        super(59, 1);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) 
        throws IOException {
    
        return this;
    }
}
