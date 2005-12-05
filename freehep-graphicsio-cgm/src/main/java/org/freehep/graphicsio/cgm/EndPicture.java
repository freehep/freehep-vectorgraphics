// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * EndPicture TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/EndPicture.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class EndPicture extends CGMTag {

    public EndPicture() {
        super(0, 5, 1);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.outdent();
        cgm.print("ENDPIC");
    }
}
