package org.freehep.graphicsio.emf.gdi;

import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;
import org.freehep.graphicsio.emf.EMFOutputStream;

import java.awt.Rectangle;
import java.io.IOException;

/**
 * Abstraction of commonality between the {@link ExtTextOutA} and {@link ExtTextOutW} tags.
 *
 * @author Daniel Noll (daniel@nuix.com)
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/AbstractExtTextOut.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public abstract class AbstractExtTextOut extends EMFTag implements EMFConstants {

    private Rectangle bounds;

    private int mode;

    private float xScale, yScale;

    /**
     * Constructs the tag.
     *
     * @param id id of the element
     * @param version emf version in which this element was first supported
     * @param bounds text boundary
     * @param mode text mode
     * @param xScale horizontal scale factor
     * @param yScale vertical scale factor
     */
    protected AbstractExtTextOut(
        int id,
        int version,
        Rectangle bounds,
        int mode,
        float xScale,
        float yScale) {

        super(id, version);
        this.bounds = bounds;
        this.mode = mode;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    protected abstract Text getText();

    public String toString() {
        return super.toString() +
            "\n  bounds: " + bounds +
            "\n  mode: " + mode +
            "\n  xScale: " + xScale +
            "\n  yScale: " + yScale +
            "\n" + getText().toString();
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
        emf.writeDWORD(mode);
        emf.writeFLOAT(xScale);
        emf.writeFLOAT(yScale);
        getText().write(emf);
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        Text text = getText();
        renderer.drawOrAppendText(
            text.getString(),
            text.getPos().getX(),
            text.getPos().getY());
    }
}
