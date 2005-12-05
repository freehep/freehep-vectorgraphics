// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * EndSegment TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/EndSegment.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class EndSegment extends CGMTag {

    public EndSegment() {
        super(0, 7, 2);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.outdent();
        cgm.print("ENDSEG");
    }
}
