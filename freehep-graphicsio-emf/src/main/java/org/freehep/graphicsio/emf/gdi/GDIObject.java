package org.freehep.graphicsio.emf.gdi;

import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * A GDIObject uses a {@link org.freehep.graphicsio.emf.EMFRenderer}
 * to render itself to a Graphics2D object.
 *
 * @author Steffen Greiffenberg
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/GDIObject.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public interface GDIObject {

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer);
}
