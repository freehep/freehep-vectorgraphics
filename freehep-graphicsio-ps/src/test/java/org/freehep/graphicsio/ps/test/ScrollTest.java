package org.freehep.graphicsio.ps.test;

import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

import org.freehep.graphicsio.*;
import org.freehep.graphicsio.exportchooser.*;
import org.freehep.graphicsio.ps.*;
import org.freehep.util.export.*;

public class ScrollTest {

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        JPanel jcomponent = new TestPanel();
        jcomponent.setLayout(new BoxLayout(jcomponent, BoxLayout.X_AXIS));

        JScrollPane scroll = new JScrollPane(jcomponent);
        frame.setContentPane(scroll);
        scroll.setPreferredSize(new Dimension(300,50));

        // Make a long row of buttons.
        int i = 0;
        int xmax = 32;
        for (int x = 0; x < xmax; x++) {
            JPanel button = new TestSubPanel("Test:" + x);
            jcomponent.add(button);
        }

        // Make the frame visible.
        frame.pack();
        frame.setVisible(true);

        // Reset the scroll bar to the middle of the row.
        JScrollBar bar = scroll.getHorizontalScrollBar();
        int max = bar.getMaximum();
        int min = bar.getMinimum();
        bar.setValue((max+min)/2);

        ExportFileType epsOut = new EPSExportFileType();
        try {

            File file;
            FileOutputStream fos;

            System.out.println("\nPrinting image...\n");
            file = new File("ScrollTest.eps");
            fos = new FileOutputStream(file);
            epsOut.exportToFile(fos,scroll,frame,new Properties(),null);
            fos.close();

            System.out.println();
            System.out.println("An image of the frame should have been ");
            System.out.println("produced (ScrollTest.eps).  If this image ");
            System.out.println("is not correct, then check the clipping");
            System.out.println("rectangles in the graphics code.");
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class TestSubPanel extends JPanel {

        private String label = null;

        public TestSubPanel(String label) {
            super(new BorderLayout(), false);
            this.label = label;
            setBorder(BorderFactory.createLineBorder(Color.black, 1));
            JButton button = new JButton(label);
            add(button, "North");
        }

        public String getLabel() {
            return label;
        }

        public void paintComponent(Graphics g) {
            System.out.println("Draw subpanel: "+label+" "+g.getClip());
            super.paintComponent(g);
        }
    }

    static class TestPanel extends JPanel {
        public TestPanel() {
        }
        public void paintComponent(Graphics g) {
            System.out.println("Draw panel: "+g.getClip());
            super.paintComponent(g);
        }
    }

}
