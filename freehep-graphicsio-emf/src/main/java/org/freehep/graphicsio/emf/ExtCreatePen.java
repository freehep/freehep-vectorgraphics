// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * ExtCreatePen TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/ExtCreatePen.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class ExtCreatePen extends EMFTag {

    private int index;

    private ExtLogPen pen;

    ExtCreatePen() {
        super(95, 1);
    }

    public ExtCreatePen(int index, ExtLogPen pen) {
        this();
        this.index = index;
        this.pen = pen;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        int index = emf.readDWORD();
        /* int bmiOffset = */ emf.readDWORD();
        /* int bmiSize = */ emf.readDWORD();
        /* int brushOffset = */ emf.readDWORD();
        /* int brushSize = */ emf.readDWORD();
        ExtCreatePen tag = new ExtCreatePen(index, new ExtLogPen(emf));
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(index);
        emf.writeDWORD(0); // offset to bmi
        emf.writeDWORD(0); // size of bmi
        emf.writeDWORD(0); // offset to brush bitmap
        emf.writeDWORD(0); // size of brush bitmap
        pen.write(emf);
    }

    public String toString() {
        return super.toString() + "\n" + "  index: 0x"
                + Integer.toHexString(index) + "\n" + pen.toString();
    }
}
