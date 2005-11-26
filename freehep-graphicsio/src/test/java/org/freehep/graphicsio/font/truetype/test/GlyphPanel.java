// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.font.truetype.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.freehep.graphicsio.font.truetype.TTFGlyfTable;

/**
 * @author Simon Fischer
 * @version $Id: freehep-graphicsio/src/test/java/org/freehep/graphicsio/font/truetype/test/GlyphPanel.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class GlyphPanel extends JPanel {

    private TTFGlyfTable.Glyph glyph;

    private Rectangle maxCharBounds;

    private final static AffineTransform transform = new AffineTransform();

    static {
        transform.scale(0.2, -0.2);
        transform.translate(1500, -2250);
    }

    public GlyphPanel(TTFGlyfTable.Glyph glyph, Rectangle maxCharBounds) {
        this.glyph = glyph;
        this.maxCharBounds = maxCharBounds;

        this.setBackground(Color.white);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setBackground(Color.white);
        g2d.clearRect(0, 0, 1000, 700);

        g2d.setTransform(transform);

        g2d.setColor(new Color(200, 200, 255));
        g2d.fill(maxCharBounds);

        Rectangle bbox = glyph.getShape().getBounds();
        g2d.setColor(Color.lightGray);
        g2d.fill(bbox);

        g2d.setColor(Color.black);
        g2d.drawLine(-10000, glyph.yMin, 10000, glyph.yMin);
        g2d.drawLine(glyph.xMin, -10000, glyph.xMin, 10000);
        g2d.drawLine(-10000, glyph.yMax, 10000, glyph.yMax);
        g2d.drawLine(glyph.xMax, -10000, glyph.xMax, 10000);

        g2d.setFont(new Font("Helvetica", Font.PLAIN, 60));
        drawString(g2d, "yMin=" + glyph.yMin, glyph.xMin - 325, glyph.yMin);
        drawString(g2d, "yMax=" + glyph.yMax, glyph.xMin - 325, glyph.yMax);
        drawString(g2d, "xMin=" + glyph.xMin, glyph.xMin + 20, glyph.yMin - 75);
        drawString(g2d, "xMax=" + glyph.xMax, glyph.xMax + 20, glyph.yMin - 75);

        g2d.fillOval(-10, -10, 21, 21);

        g2d.fill(glyph.getShape());

        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.red);
        g2d.draw(glyph.getShape());
    }

    private void drawString(Graphics2D g2d, String str, int x, int y) {
        AffineTransform tx = g2d.getTransform();
        g2d.translate(0, y);
        g2d.scale(1, -1);
        g2d.drawString(str, x, 0);
        g2d.setTransform(tx);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1000, 700);
    }
}
