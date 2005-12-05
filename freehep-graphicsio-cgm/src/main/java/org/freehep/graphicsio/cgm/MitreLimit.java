// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * EdgeWidth TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/MitreLimit.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class MitreLimit extends CGMTag {

    private double limit;

    public MitreLimit() {
        super(3, 19, 1);
    }

    public MitreLimit(double limit) {
        this();
        this.limit = limit;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeReal(limit);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("MITRELIMIT ");
        cgm.writeReal(limit);
    }
}
