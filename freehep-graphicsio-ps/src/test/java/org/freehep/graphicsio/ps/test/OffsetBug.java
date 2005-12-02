package org.freehep.graphicsio.ps.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.freehep.graphicsio.ps.EPSExportFileType;
import org.freehep.util.export.ExportFileType;

public class OffsetBug {

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        JPanel panel = new JPanel(new GridLayout(2, 2));
        frame.getContentPane().add(panel);

        JPanel first = new SubPanel("First", Color.red);
        panel.add(first);
        JPanel second = new SubPanel("Second", Color.green);
        panel.add(second);
        JPanel third = new SubPanel("Third", Color.yellow);
        panel.add(third);
        JPanel fourth = new SubPanel("Fourth", Color.cyan);
        panel.add(fourth);

        frame.pack();
        frame.setVisible(true);

        ExportFileType epsOut = new EPSExportFileType();
        try {

            File file;
            FileOutputStream fos;

            System.out.println("\nPrinting first...\n");
            file = new File("first.eps");
            fos = new FileOutputStream(file);
            epsOut.exportToFile(fos, first, frame, new Properties(), null);
            fos.close();

            System.out.println("\nPrinting second...\n");
            file = new File("second.eps");
            fos = new FileOutputStream(file);
            epsOut.exportToFile(fos, second, frame, new Properties(), null);
            fos.close();

            System.out.println();
            System.out.println("Two EPS files should have been generated:");
            System.out.println("first.eps and second.eps.  These should ");
            System.out.println("contain images of the first and second");
            System.out.println("buttons properly scaled and clipped.");
            System.out.println("");
            System.out.println("If second.eps is not correct, then check ");
            System.out.println("the clipping bounds which are initially ");
            System.out.println("given to the printing component.");
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static private class SubPanel extends JPanel {

        String label;

        public SubPanel(String label, Color color) {
            super(new BorderLayout());
            this.label = label;
            setOpaque(true);
            setBackground(color);

            setBorder(BorderFactory.createLineBorder(Color.black, 1));
            JButton button = new TalkativeButton(label);
            add(button, BorderLayout.CENTER);
        }

        public void paintComponent(Graphics g) {
            System.out.println("paintComponent called: " + label);
            super.paintComponent(g);
            System.out.println("graphics class: " + g.getClass());
            System.out.println("Container clip: " + g.getClipBounds());
            System.out.println("Container bounds: " + getBounds());
        }

        public void paintChildren(Graphics g) {
            System.out.println("clip (child.) " + g.getClipBounds());
            super.paintChildren(g);
        }
    }

    static private class TalkativeButton extends JButton {

        public TalkativeButton(String label) {
            super(label);
        }

        public void paintComponent(Graphics g) {
            System.out.println("button: " + getText());
            super.paintComponent(g);
        }
    }

}
