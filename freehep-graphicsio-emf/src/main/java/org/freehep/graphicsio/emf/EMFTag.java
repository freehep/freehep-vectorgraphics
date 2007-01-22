// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf;

import java.io.IOException;
import java.util.logging.Logger;

import org.freehep.util.io.Tag;
import org.freehep.util.io.TaggedInputStream;
import org.freehep.util.io.TaggedOutputStream;
import org.freehep.graphicsio.emf.gdi.GDIObject;

/**
 * EMF specific tag, from which all other EMF Tags inherit.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFTag.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public abstract class EMFTag extends Tag implements GDIObject {

    /**
     * logger for all instances
     */
    protected static final Logger logger = Logger.getLogger("org.freehep.graphicsio.emf");

    /**
     * Constructs a EMFTag.
     * 
     * @param id id of the element
     * @param version emf version in which this element was first supported
     */
    protected EMFTag(int id, int version) {
        super(id, version);
    }

    public Tag read(int tagID, TaggedInputStream input, int len)
            throws IOException {

        return read(tagID, (EMFInputStream) input, len);
    }

    public abstract EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException;

    public void write(int tagID, TaggedOutputStream output) throws IOException {
        write(tagID, (EMFOutputStream) output);
    }

    /**
     * Writes the extra tag information to the outputstream in binary format.
     * This implementation writes nothing, but concrete tags may override this
     * method. This method is called just after the TagHeader is written.
     * 
     * @param tagID id of the tag
     * @param emf Binary CGM output stream
     * @throws java.io.IOException thrown by EMFOutputStream
     */
    public void write(int tagID, EMFOutputStream emf) throws IOException {
        // empty
    }

    /**
     * @return a description of the tagName and tagID
     */
    public String toString() {
        return "EMFTag " + getName() + " (" + getTag() + ")";
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        logger.warning("EMF tag not supported: " + toString());
    }
}
