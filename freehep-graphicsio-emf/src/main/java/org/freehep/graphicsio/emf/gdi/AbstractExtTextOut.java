package org.freehep.graphicsio.emf.gdi;

import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFConstants;

/**
 * Abstraction of commonality between the {@link ExtTextOutA} and {@link ExtTextOutW} tags.
 *
 * @author Daniel Noll (daniel@nuix.com)
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/AbstractExtTextOut.java 11783e27e55b 2007/01/15 16:30:03 duns $
 */
public abstract class AbstractExtTextOut extends EMFTag implements EMFConstants {

    /**
     * Constructs the tag.
     *
     * @param id id of the element
     * @param version emf version in which this element was first supported
     */
    protected AbstractExtTextOut(int id, int version) {
        super(id, version);
    }

    public abstract Text getText();
}
