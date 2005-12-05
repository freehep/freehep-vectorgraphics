// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * MetafileDefaultsReplacement TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/MetafileDefaultsReplacement.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class MetafileDefaultsReplacement extends CGMTag {

//    private int type;

    public MetafileDefaultsReplacement() {
        super(1, 12, 1);
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        throw new IOException("Not properly implemented.");
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        throw new IOException("Not properly implemented.");
    }
}
