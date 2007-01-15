package org.freehep.graphicsio.emf;

/**
 * EMFDisplay.java
 *
 * Created: Mon May 26 09:43:10 2003
 *
 * Copyright:    Copyright (c) 2000, 2001<p>
 * Company:      ATLANTEC Enterprise Solutions GmbH<p>
 *
 * @author Carsten Zerbst carsten.zerbst@atlantec-es.com
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFDisplay.java 11783e27e55b 2007/01/15 16:30:03 duns $
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A simple interpreter displaying an EMF file read in by the EMFInputStream in
 * a JPanel
 */
public class EMFDisplay extends JPanel {
    private EMFRenderer renderer;

    public EMFDisplay(EMFInputStream is) throws IOException {
        renderer = new EMFRenderer(is);

        // Set the size and background color.
        this.setSize(renderer.getSize());
        this.setBackground(Color.white);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        renderer.paint((Graphics2D) g);
    }

    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream(args[0]);
            EMFInputStream emf = new EMFInputStream(fis);

            JFrame frame = new JFrame("EMF " + args[0]);
            JScrollPane sp = new JScrollPane(new EMFDisplay(emf));
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(sp, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setSize(550, 400);
            frame.setVisible(true);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}

// EMFDisplay
