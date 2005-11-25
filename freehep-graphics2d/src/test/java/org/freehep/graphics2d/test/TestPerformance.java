// University of California, Santa Cruz, USA and
// CERN, Geneva, Switzerland, Copyright (c) 2000
package org.freehep.graphics2d.test;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

import org.freehep.graphics2d.*;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphics2d/src/test/java/org/freehep/graphics2d/test/TestPerformance.java f5b43d67642f 2005/11/25 23:10:27 duns $
 */
public class TestPerformance
    extends BufferedPanel {

    private static final int n = 100;
    private static final int m = 50;

    private int[][] xip = new int[n][m];
    private int[][] yip = new int[n][m];
    private double[][] xdp = new double[n][m];
    private double[][] ydp = new double[n][m];
    private double[] xs = new double[n];
    private double[] ys = new double[n];

    public void paintComponent(VectorGraphics g) {

        Dimension dim = getSize();
        Insets insets = getInsets();

        int width = dim.width;
        int height = dim.height;

        for (int i = 0; i < xip.length; i++) {
            for (int j=0; j < xip[0].length; j++) {
                xip[i][j] = (int)(Math.random() * width);
                yip[i][j] = (int)(Math.random() * height);
            }
        }

        for (int i = 0; i < xdp.length; i++) {
            for (int j=0; j < xdp[0].length; j++) {
                xdp[i][j] = Math.random() * width;
                ydp[i][j] = Math.random() * height;
            }
        }

        for (int i = 0; i < xs.length; i++) {
            xs[i] = Math.random() * width;
            ys[i] = Math.random() * height;
        }

        System.out.print("Waiting 4 seconds... ");
        try { Thread.sleep(4000); } catch (InterruptedException e) {}
        System.out.println("done");

//        g.setLineWidth(1.5);
        g.setColor(Color.black);
        g.fillRect(insets.left, insets.top,
                   dim.width-insets.left-insets.right,
                   dim.height-insets.top-insets.bottom);

        g.setColor(Color.orange);
        drawIntPolylines(g);

        g.setColor(Color.cyan);
        drawDoublePolylines(g);

        g.setColor(Color.red);
//        drawSymbols(g, VectorGraphicsConstants.SYMBOL_STAR, false);
        g.setColor(Color.blue);
//        drawSymbols(g, VectorGraphicsConstants.SYMBOL_CIRCLE, true);
    }

    private void drawIntPolylines(VectorGraphics g) {
        double lineWidth = 2.0;

        long start, end;

        start = System.currentTimeMillis();
        for (int i = 0; i < xip.length; i++) {
            g.drawPolyline(xip[i], yip[i], xip[i].length);
        }
        end = System.currentTimeMillis();
        System.out.println("VG2D:"+xip.length+" IntPolys[" + xip[0].length + "] " + (end-start) + " ms");
    }

    private void drawDoublePolylines(VectorGraphics g) {
        long start, end;

        start = System.currentTimeMillis();
        for (int i = 0; i < xdp.length; i++) {
            g.drawPolyline(xdp[i], ydp[i], xdp[i].length);
        }
        end = System.currentTimeMillis();
        System.out.println("VG2D:"+xdp.length+" DoublePolys[" + xdp[0].length + "] " + (end-start) + " ms");
    }

    private void drawSymbols(VectorGraphics g, int type, boolean fill) {

        long start = System.currentTimeMillis();
        for (int i = 0; i < xs.length; i++) {
            if (fill) {
                g.fillSymbol(xs[i], ys[i], 6, type);
            } else {
                g.drawSymbol(xs[i], ys[i], 6, type);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("VG2D:"+xs.length+" symbols[" + type + "] " + (end-start) + " ms");
    }

    public static void main(String[] args) {

        // Create a new frame to hold everything.
        JFrame frame = new JFrame("Test PixelGraphics2D Performance");

        // Create a new instance of this class and add it to the frame.
        frame.getContentPane().add(new TestPerformance());

        // Give the frame a size and make it visible.
        frame.setSize(new Dimension(1024,768));
        frame.setVisible(true);
    }
}
