// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * BeginPictureBody TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/BeginPictureBody.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class BeginPictureBody extends CGMTag {

    public BeginPictureBody() {
        super(0, 4, 1);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.outdent();
        cgm.print("BEGPICBODY");
        cgm.indent();
    }
}
