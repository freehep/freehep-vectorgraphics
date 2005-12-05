// Copyright 2001-2003, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

import org.freehep.util.io.Tag;
import org.freehep.util.io.TaggedInputStream;
import org.freehep.util.io.TaggedOutputStream;

/**
 * CGM specific tag, from which all other CGM Tags inherit.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CGMTag.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public abstract class CGMTag extends Tag {

    /**
     * Constructs a CGMTag.
     * 
     * @param elementClass class of the element
     * @param id id of the element
     * @param version cgm version in which this element was first supported
     */
    protected CGMTag(int elementClass, int id, int version) {
        super(((elementClass & 0x0f) << 7) + id, version);
    }

    public Tag read(int tagID, TaggedInputStream input, int len)
            throws IOException {

        return read(tagID, (CGMInputStream) input, len);
    }

    public CGMTag read(int tagID, CGMInputStream cgm, int len)
            throws IOException {
        return new CGMDummyTag(tagID, cgm, len);
    }

    public void write(int tagID, TaggedOutputStream output) throws IOException {
        write(tagID, (CGMOutputStream) output);
    }

    /**
     * Writes the extra tag information to the outputstream in binary format.
     * This implementation writes nothing, but concrete tags may override this
     * method. This method is called just after the TagHeader is written.
     * 
     * @param tagID id of the tag
     * @param cgm Binary CGM output stream
     */
    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        // empty
    }

    /**
     * Writes the full tag to the Writer in Clear Text format.
     * 
     * @param tagID id of the tag
     * @param cgm Clear Text CGM Writer
     */
    public abstract void write(int tagID, CGMWriter cgm) throws IOException;

    /**
     * @return a description of the tagName and tagID
     */
    public String toString() {
        return "CGMTag " + getName() + " (" + getTag() + ")";
    }
}
