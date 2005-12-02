// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * CreatePen TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/CreatePen.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class CreatePen extends EMFTag {

    private int index;

    private LogPen pen;

    CreatePen() {
        super(38, 1);
    }

    public CreatePen(int index, LogPen pen) {
        this();
        this.index = index;
        this.pen = pen;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        CreatePen tag = new CreatePen(emf.readDWORD(), new LogPen(emf));
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(index);
        pen.write(emf);
    }

    public String toString() {
        return super.toString() + "\n" + "  index: 0x"
                + Integer.toHexString(index) + "\n" + pen.toString();
    }

    public int getIndex() {
        return index;
    }

    public LogPen getPen() {
        return pen;
    }

}
