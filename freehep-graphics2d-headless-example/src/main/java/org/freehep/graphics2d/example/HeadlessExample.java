package org.freehep.graphics2d.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.io.File;

import javax.swing.JPanel;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.pdf.PDFGraphics2D;
import org.freehep.graphicsbase.swing.Headless;

/**
 * This example shows how to export what you draw
 * in a panel to any of the supported vector and bitmap formats
 * without drawing it on the screen.
 *
 * Instead of YourPanel you should use your own panel/component.
 *
 * The output formats available depend on the jar files
 * you add to the CLASSPATH.
 *
 * This example (or your application) solely depends
 * on the ExportDialog class and the VectorGraphics
 * class (the latter only if you wish to use special
 * methods from this class in the paint methods).
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/examples/HeadlessExample.java e31519a485cd 2006/12/07 15:42:14 duns $
 */

public class HeadlessExample {

    public static void main(String[] args) throws Exception {
        JPanel yourPanel = new YourPanel();
        
        // run with -Djava.awt.headless=true
        Headless headless = new Headless(yourPanel);
        headless.pack();
        headless.setVisible(true);

        File out = new File("YourPanel.pdf");
        VectorGraphics graphics = new PDFGraphics2D(out, yourPanel);
        graphics.startExport();
        yourPanel.print(graphics);
        graphics.endExport();
    }
    
}

class YourPanel extends JPanel {
    
	public YourPanel() {
        setPreferredSize(new Dimension(600,400));
    }
	
    public void paintComponent(Graphics g) {

        if (g == null) return;

        VectorGraphics vg = VectorGraphics.create(g);

        Dimension dim = getSize();
        Insets insets = getInsets();

        vg.setColor(Color.white);
        vg.fillRect(insets.left, insets.top,
                    dim.width-insets.left-insets.right,
                    dim.height-insets.top-insets.bottom);

        vg.setColor(Color.black);

        vg.setLineWidth(4.0);
        double w=dim.width, h=dim.height;
        vg.translate(w/2,h/2);
        double xhi=w/2-10, yhi=h/2-10;
        vg.drawLine(-xhi,-yhi,xhi, yhi);
        vg.drawLine(-xhi, yhi,xhi,-yhi);
        vg.drawRect(-xhi,-yhi,w-20,h-20);
    }
}
