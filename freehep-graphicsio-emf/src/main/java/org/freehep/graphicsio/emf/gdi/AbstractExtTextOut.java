package org.freehep.graphicsio.emf.gdi;

import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * Abstraction of commonality between the {@link ExtTextOutA} and {@link ExtTextOutW} tags.
 *
 * @author Daniel Noll (daniel@nuix.com)
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/AbstractExtTextOut.java 86ef08292548 2007/01/17 23:15:57 duns $
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
