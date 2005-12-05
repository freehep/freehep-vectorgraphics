// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * ShowFrame TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/ShowFrame.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class ShowFrame extends ControlTag {

    public ShowFrame() {
        super(1, 1);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {
        return this;
    }
}
