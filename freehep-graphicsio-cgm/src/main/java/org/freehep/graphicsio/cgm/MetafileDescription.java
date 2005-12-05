// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * MetafileDescription TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/MetafileDescription.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class MetafileDescription extends CGMTag {

    private String description;

    public MetafileDescription() {
        super(1, 2, 1);
    }

    public MetafileDescription(String description) {
        this();
        this.description = description;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeString(description);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("MFDESC ");
        cgm.writeString(description);
    }
}
