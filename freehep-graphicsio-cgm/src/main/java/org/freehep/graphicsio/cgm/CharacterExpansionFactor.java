// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * CharacterExpansionFactor TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CharacterExpansionFactor.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CharacterExpansionFactor extends CGMTag {

    private double factor;

    public CharacterExpansionFactor() {
        super(5, 12, 1);
    }

    public CharacterExpansionFactor(double factor) {
        this();
        this.factor = factor;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeReal(factor);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("CHAREXPAN ");
        cgm.writeReal(factor);
    }
}
