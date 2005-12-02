// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Point;
import java.io.IOException;

/**
 * LineTo TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/LineTo.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class LineTo extends EMFTag {

    private Point point;

    LineTo() {
        super(54, 1);
    }

    public LineTo(Point point) {
        this();
        this.point = point;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        LineTo tag = new LineTo(emf.readPOINTL());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writePOINTL(point);
    }

    public String toString() {
        return super.toString() + "\n" + "  point: " + point;
    }

    public Point getPoint() {
        return point;
    }

}
