// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

import org.freehep.util.io.Tag;
import org.freehep.util.io.TaggedInputStream;
import org.freehep.util.io.TaggedOutputStream;

/**
 * SWF Tag, superclass of all SWF Tags.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SWFTag.java db861da05344 2005/12/05 00:59:43 duns $
 */
public abstract class SWFTag extends Tag implements SWFConstants {

    protected SWFTag(int tagID, int version) {
        super(tagID, version);
    }

    public Tag read(int tagID, TaggedInputStream input, int len)
            throws IOException {

        return read(tagID, (SWFInputStream) input, len);
    }

    public abstract SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException;

    public void write(int tagID, TaggedOutputStream output) throws IOException {

        write(tagID, (SWFOutputStream) output);
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {

        // empty
    }
}
