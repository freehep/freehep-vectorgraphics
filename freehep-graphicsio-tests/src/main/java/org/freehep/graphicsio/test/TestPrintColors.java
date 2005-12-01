// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphicsio.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.freehep.graphics2d.PrintColor;
import org.freehep.graphics2d.VectorGraphics;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestPrintColors.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestPrintColors extends TestingPanel {

    // Fill this panel with nine panels of different colors.
    public TestPrintColors(String[] args) throws Exception {

        super(args);

        // Set the layout of this panel.
        setLayout(new BorderLayout());

        // Create a border of white surrounded by black.
        Border border = BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(1, 1, 1, 1, Color.white), BorderFactory
                .createMatteBorder(2, 2, 2, 2, Color.black));

        // Create a subpanel with grid layout to hold the colored tiles.
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(14, 3));

        JLabel label1 = new JLabel("Color");
        // label1.setFont(new Font("Lucida Sans", Font.BOLD, 12));
        panel.add(label1);

        JLabel label2 = new JLabel("GrayScale");
        // label2.setFont(new Font("Lucida Sans", Font.BOLD, 12));
        panel.add(label2);

        JLabel label3 = new JLabel("Black and White");
        // label3.setFont(new Font("Lucida Sans", Font.BOLD, 12));
        panel.add(label3);

        // Add tiles of colors to this panel.
        for (int i = 0; i < 13; i++) {
            TestColor test;

            test = new TestColor(PrintColor.COLOR, i);
            test.setBorder(border);
            panel.add(test);

            test = new TestColor(PrintColor.GRAYSCALE, i);
            test.setBorder(border);
            panel.add(test);

            test = new TestColor(PrintColor.BLACK_AND_WHITE, i);
            test.setBorder(border);
            panel.add(test);

        }

        // Add this panel to this container.
        add(panel, BorderLayout.CENTER);
    }

    // Class panel which just paints a given background color.
    class TestColor extends JPanel {
        private int mode;

        private int bkgColorIndex;

        public TestColor(int mode, int bkgColorIndex) {
            this.mode = mode;
            this.bkgColorIndex = bkgColorIndex;
        }

        public void paintComponent(Graphics g) {
            Dimension dim = getSize();
            Insets insets = getInsets();

            Color bkgColor = PrintColor.getDefaultColor(bkgColorIndex);

            VectorGraphics vg = VectorGraphics.create(g);
            vg.setColorMode(mode);
            vg.setColor(bkgColor);
            vg.fillRect(insets.left, insets.top, dim.width - insets.left
                    - insets.right, dim.height - insets.top - insets.bottom);
        }
    }

    public static void main(String[] args) throws Exception {
        new TestPrintColors(args).runTest();
    }
}
