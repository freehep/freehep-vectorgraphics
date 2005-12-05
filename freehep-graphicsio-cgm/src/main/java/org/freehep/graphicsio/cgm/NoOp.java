// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * NoOp TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/NoOp.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class NoOp extends CGMTag {

    private int n;

    public NoOp() {
        super(0, 0, 1);
    }

    public NoOp(int n) {
        this();
        this.n = n;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        for (int i = 0; i < n; i++) {
            cgm.writeUnsignedByte(0);
        }
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
    }
}
