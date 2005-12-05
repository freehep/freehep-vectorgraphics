// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * CharacterSpacing TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CharacterSpacing.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CharacterSpacing extends CGMTag {

    private double spacing;

    public CharacterSpacing() {
        super(5, 13, 1);
    }

    public CharacterSpacing(double spacing) {
        this();
        this.spacing = spacing;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeReal(spacing);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("CHARSPACE ");
        cgm.writeReal(spacing);
    }
}
