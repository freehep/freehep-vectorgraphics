// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * BeginPicture TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/BeginPicture.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class BeginPicture extends CGMTag {

    private String name;

    public BeginPicture() {
        super(0, 3, 1);
    }

    public BeginPicture(String name) {
        this();
        this.name = name;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeString(name);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("BEGPIC ");
        cgm.writeString(name);
        cgm.indent();
    }
}
