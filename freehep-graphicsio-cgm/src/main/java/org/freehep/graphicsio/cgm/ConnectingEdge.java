// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * ConnectingEdge TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/ConnectingEdge.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class ConnectingEdge extends CGMTag {

    public ConnectingEdge() {
        super(5, 21, 1);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("CONNEDGE");
    }
}
