// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * TextPath TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/TextPath.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class TextPath extends CGMTag {

    public static final int RIGHT = 0;

    public static final int LEFT = 1;

    public static final int UP = 2;

    public static final int DOWN = 3;

    private int path;

    public TextPath() {
        super(5, 17, 1);
    }

    public TextPath(int path) {
        this();
        this.path = path;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeEnumerate(path);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("TEXTPATH ");
        switch (path) {
        default:
        case RIGHT:
            cgm.print("RIGHT");
            break;
        case LEFT:
            cgm.print("LEFT");
            break;
        case UP:
            cgm.print("UP");
            break;
        case DOWN:
            cgm.print("DOWN");
            break;
        }
    }
}
