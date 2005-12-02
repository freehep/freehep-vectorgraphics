// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Point;
import java.io.IOException;

/**
 * MoveToEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/MoveToEx.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class MoveToEx extends EMFTag {

    private Point point;

    MoveToEx() {
        super(27, 1);
    }

    public MoveToEx(Point point) {
        this();
        this.point = point;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        MoveToEx tag = new MoveToEx(emf.readPOINTL());
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
