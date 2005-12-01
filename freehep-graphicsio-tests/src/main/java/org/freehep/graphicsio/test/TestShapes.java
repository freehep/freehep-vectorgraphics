// Copyright 2002, SLAC, Stanford University, U.S.A.
package org.freehep.graphicsio.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import org.freehep.graphics2d.VectorGraphics;

/**
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestShapes.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestShapes extends TestingPanel {

    final static int maxCharHeight = 15;

    final static Color bg = Color.white;

    final static Color fg = Color.black;

    final static Color red = Color.red;

    final static Color white = Color.white;

    Dimension totalSize;

    public TestShapes(String[] args) throws Exception {
        super(args);
        setName("Shapes");
        // Initialize drawing colors
        setBackground(bg);
        setForeground(fg);
    }

    public void paintComponent(Graphics g) {
        VectorGraphics vg = VectorGraphics.create(g);
        if (vg != null)
            drawComponent(vg);
    }

    protected void drawComponent(VectorGraphics g) {

        // Fill in the background.
        Dimension d = getSize();

        g.setColor(bg);
        g.fillRect(0, 0, d.width, d.height);

        int gridWidth = d.width / 6;
        int gridHeight = d.height / 8;

        int x = 5;
        int y = 7;
        int rectWidth = gridWidth - 2 * x;
        int rectHeight = gridHeight - 2 * y;

        drawShapes(g, x, y, rectWidth, rectHeight, gridWidth);
        x = 5;
        y += gridHeight;

        drawFilledShapes(g, x, y, rectWidth, rectHeight, gridWidth);
        x = 5;
        y += gridHeight;

        g.setLineWidth(5.0);
        drawShapes(g, x, y, rectWidth, rectHeight, gridWidth);
        x = 5;
        y += gridHeight;

        drawFilledShapes(g, x, y, rectWidth, rectHeight, gridWidth);
        x = 5;
        y += gridHeight;

        int xp = x;
        int yp = y;

        String saying = "The quick brown fox jumped over the lazy dog.";

        Font thisFont = new Font("SansSerif", Font.PLAIN, 14);
        g.setFont(thisFont);
        g.drawString("SansSerif: " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("SansSerif", Font.BOLD, 14);
        g.setFont(thisFont);
        g.drawString("SansSerif (bold): " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("SansSerif", Font.ITALIC, 14);
        g.setFont(thisFont);
        g.drawString("SansSerif (italic): " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("Serif", Font.PLAIN, 14);
        g.setFont(thisFont);
        g.drawString("Serif: " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("Serif", Font.BOLD, 14);
        g.setFont(thisFont);
        g.drawString("Serif (bold): " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("Serif", Font.ITALIC, 14);
        g.setFont(thisFont);
        g.drawString("Serif (italic): " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("Monospaced", Font.PLAIN, 14);
        g.setFont(thisFont);
        g.drawString("Monospaced: " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("Monospaced", Font.BOLD, 14);
        g.setFont(thisFont);
        g.drawString("Monospaced (bold): " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("Monospaced", Font.ITALIC, 14);
        g.setFont(thisFont);
        g.drawString("Monospaced (italic): " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("Symbol", Font.PLAIN, 14);
        g.setFont(thisFont);
        g.drawString("Symbol: " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("Symbol", Font.BOLD, 14);
        g.setFont(thisFont);
        g.drawString("Symbol (bold): " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("Symbol", Font.ITALIC, 14);
        g.setFont(thisFont);
        g.drawString("Symbol (italic): " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("ZapfDingbats", Font.PLAIN, 14);
        g.setFont(thisFont);
        g.drawString("ZapfDingbats: " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("ZapfDingbats", Font.BOLD, 14);
        g.setFont(thisFont);
        g.drawString("ZapfDingbats (bold): " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("ZapfDingbats", Font.ITALIC, 14);
        g.setFont(thisFont);
        g.drawString("ZapfDingbats (italic): " + saying, xp, yp);
        yp += 16;

        thisFont = new Font("Monospaced", Font.PLAIN, 14);
        g.setFont(thisFont);
        g.drawString("Unbalanced (( )) ))) ((( TEST! )T( (T)", xp, yp);
        yp += 16;

    }

    private void drawShapes(VectorGraphics g, int x, int y, int rectWidth,
            int rectHeight, int gridWidth) {
        // draw Line2D.Double
        g.setColor(Color.red);
        g.drawLine(x, y + rectHeight - 1, x + rectWidth, y);
        x += gridWidth;

        // draw Rectangle2D.Double
        g.setColor(Color.green);
        g.drawRect(x, y, rectWidth, rectHeight);
        x += gridWidth;

        // draw RoundRectangle2D.Double
        g.setColor(Color.blue);
        g.drawRoundRect(x, y, rectWidth, rectHeight, 50, 50);
        x += gridWidth;

        // draw Arc2D.Double
        g.setColor(Color.cyan);
        g.drawArc(x, y, rectWidth, rectHeight, 90, 135);
        x += gridWidth;

        // draw Ellipse2D.Double
        g.setColor(Color.magenta);
        g.drawOval(x, y, rectWidth, rectHeight);
        x += gridWidth;

        // draw GeneralPath (polygon)
        int x1Points[] = { x, x + rectWidth, x, x + rectWidth };
        int y1Points[] = { y, y + rectHeight, y + rectHeight, y };

        g.setColor(Color.yellow);
        g.drawPolygon(x1Points, y1Points, 4);

    }

    private void drawFilledShapes(VectorGraphics g, int x, int y,
            int rectWidth, int rectHeight, int gridWidth) {
        // draw GeneralPath (polyline)

        int x2Points[] = { x, x + rectWidth, x, x + rectWidth };
        int y2Points[] = { y, y + rectHeight, y + rectHeight, y };

        g.setColor(Color.red);
        g.drawPolyline(x2Points, y2Points, 4);
        x += gridWidth;

        // fill Rectangle2D.Double (red)
        g.setColor(Color.green);
        g.fillRect(x, y, rectWidth, rectHeight);
        g.setColor(fg);
        g.drawRect(x, y, rectWidth, rectHeight);
        x += gridWidth;

        // fill RoundRectangle2D.Double
        g.setColor(Color.blue);
        g.fillRoundRect(x, y, rectWidth, rectHeight, 50, 50);
        g.setColor(fg);
        x += gridWidth;

        // fill Arc2D
        g.setColor(Color.cyan);
        g.fillArc(x, y, rectWidth, rectHeight, 90, 135);
        g.setColor(fg);
        x += gridWidth;

        // fill Ellipse2D.Double
        g.setColor(Color.magenta);
        g.fillOval(x, y, rectWidth, rectHeight);
        g.setColor(fg);
        x += gridWidth;

        // fill and stroke GeneralPath
        int x3Points[] = { x, x + rectWidth, x, x + rectWidth };
        int y3Points[] = { y, y + rectHeight, y + rectHeight, y };

        g.setColor(Color.yellow);
        g.fillPolygon(x3Points, y3Points, 4);
        g.setColor(fg);
        g.drawPolygon(x3Points, y3Points, 4);
    }

    public static void main(String[] args) throws Exception {
        new TestShapes(args).runTest();
    }
}
