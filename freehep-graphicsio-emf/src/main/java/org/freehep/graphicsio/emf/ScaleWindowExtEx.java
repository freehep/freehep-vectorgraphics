// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;

/**
 * ScaleWindowExtEx TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/ScaleWindowExtEx.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class ScaleWindowExtEx extends EMFTag {

    private int xNum, xDenom, yNum, yDenom;

    ScaleWindowExtEx() {
        super(32, 1);
    }

    public ScaleWindowExtEx(int xNum, int xDenom, int yNum, int yDenom) {
        this();
        this.xNum = xNum;
        this.xDenom = xDenom;
        this.yNum = yNum;
        this.yDenom = yDenom;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        ScaleWindowExtEx tag = new ScaleWindowExtEx(emf.readLONG(), emf
                .readLONG(), emf.readLONG(), emf.readLONG());
        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeLONG(xNum);
        emf.writeLONG(xDenom);
        emf.writeLONG(yNum);
        emf.writeLONG(yDenom);
    }

    public String toString() {
        return super.toString() + "\n" + "  xNum: " + xNum + "\n"
                + "  xDenom: " + xDenom + "\n" + "  yNum: " + yNum + "\n"
                + "  yDenom: " + yDenom;
    }
}
