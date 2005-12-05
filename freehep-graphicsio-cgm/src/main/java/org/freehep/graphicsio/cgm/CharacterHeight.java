// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * CharacterHeight TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CharacterHeight.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CharacterHeight extends CGMTag {

    private double height;

    public CharacterHeight() {
        super(5, 15, 1);
    }

    public CharacterHeight(double height) {
        this();
        this.height = height;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeVDC(height);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("CHARHEIGHT ");
        cgm.writeVDC(height);
    }
}
