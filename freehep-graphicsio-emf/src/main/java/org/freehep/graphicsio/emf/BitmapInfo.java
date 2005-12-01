// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * EMF BitmapInfo
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/BitmapInfo.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class BitmapInfo {

    private BitmapInfoHeader header;

    public BitmapInfo(BitmapInfoHeader header) {
        this.header = header;
    }

    public BitmapInfo(EMFInputStream emf) throws IOException {
        header = new BitmapInfoHeader(emf);
        // colormap not necessary for true color image
    }

    public void write(EMFOutputStream emf) throws IOException {
        header.write(emf);
        // colormap not necessary for true color image
    }
    
    public String toString() {
        return "  BitmapInfo\n"+
               header.toString();
    }
}

