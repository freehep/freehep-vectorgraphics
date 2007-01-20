// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;

/**
 * EMF BitmapInfo
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/BitmapInfo.java 63c8d910ece7 2007/01/20 15:30:50 duns $
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
        return "  BitmapInfo\n" + header.toString();
    }

    public BitmapInfoHeader getHeader() {
        return header;
    }
}
