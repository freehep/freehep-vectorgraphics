// Copyright 2007 FreeHEP
package org.freehep.graphicsio.emf;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * simple panel which displays an EMF image using the {@link EMFRenderer}
 *
 * @author Steffen Greiffenberg
 * @version $id$
 */
public class EMFPanel extends JComponent {

    /**
     * factor used for zoom in an out
     */
    private static double SCALE_FACTOR = 2;

    /**
     * renders an EMF image to a Graphics2D instance
     */
    private EMFRenderer renderer;

    /**
     * used for zooming
     */
    private double scale = 1;

    /**
     * defines a white background
     */
    public EMFPanel() {
        setBackground(Color.white);
    }

    /**
     * sets the renderer an resets size
     * @param renderer
     */
    public void setRenderer(EMFRenderer renderer) {
        this.renderer = renderer;
        scale = 1;
        setSize(getPreferredSize());
    }

    /**
     * @return {@link EMFRenderer#getSize()}
     */
    public Dimension getPreferredSize() {
        Dimension bounds = renderer.getSize();
        return new Dimension(
            (int)Math.ceil(bounds.width * scale),
            (int)Math.ceil(bounds.height * scale));
    }

    /**
     * paints using the renderer
     * @param g Context of the component
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = ((Graphics2D)g);

        super.paintComponent(g2);

        // to restore AffineTransform
        AffineTransform at = g2.getTransform();

        // apply the scale factor
        g2.scale(scale, scale);

        // render the emf
        renderer.paint(g2);

        // rest the AffineTransform
        g2.setTransform(at);
    }

    /**
     * scale = scale * 2
     */
    public void zoomIn() {
        scale = scale * SCALE_FACTOR;
        setSize(getPreferredSize());
        repaint();
    }

    /**
     * scale = scale / 2;
     */
    public void zoomOut() {
        scale = scale / SCALE_FACTOR;
        setSize(getPreferredSize());
        repaint();
    }
}
