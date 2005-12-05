// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.geom.Rectangle2D;
import java.io.IOException;

/**
 * DefineShape2 TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineShape2.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineShape2 extends DefineShape {

    public DefineShape2(int id, Rectangle2D bounds, FillStyleArray fillStyles,
            LineStyleArray lineStyles, SWFShape shape) {
        this();
        character = id;
        this.bounds = bounds;
        this.fillStyles = fillStyles;
        this.lineStyles = lineStyles;
        this.shape = shape;
    }

    public DefineShape2() {
        super(22, 2);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {
        DefineShape2 tag = new DefineShape2();
        tag.read(tagID, swf, false);
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        write(swf, true);
    }
}
