// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * BeginFigure TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/BeginFigure.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class BeginFigure extends CGMTag {

    public BeginFigure() {
        super(0, 8, 2);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("BEGFIGURE");
        cgm.indent();
    }
}
