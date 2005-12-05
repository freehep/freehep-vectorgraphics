// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * MetafileVersion TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/MetafileVersion.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class MetafileVersion extends CGMTag {

    private int version;

    public MetafileVersion() {
        super(1, 1, 1);
    }

    public MetafileVersion(int version) {
        this();
        this.version = version;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeInteger(version);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("MFVERSION ");
        cgm.writeInteger(version);
    }
}
