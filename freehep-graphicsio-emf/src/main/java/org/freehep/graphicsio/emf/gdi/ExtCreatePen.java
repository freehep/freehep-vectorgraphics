// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * ExtCreatePen TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/ExtCreatePen.java 63c8d910ece7 2007/01/20 15:30:50 duns $
 */
public class ExtCreatePen extends EMFTag {

    private int index;

    private ExtLogPen pen;

    public ExtCreatePen() {
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

    public int getIndex() {
        return index;
    }

    public ExtLogPen getPen() {
        return pen;
    }
}
