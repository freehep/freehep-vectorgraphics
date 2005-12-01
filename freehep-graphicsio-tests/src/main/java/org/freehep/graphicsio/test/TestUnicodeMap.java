// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphicsio.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.util.export.ExportDialog;

/**
 * 
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestUnicodeMap.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestUnicodeMap extends JFrame implements ActionListener {

    // Export Dialog.
    ExportDialog dialog;

    // The main panel.
    JPanel panel;

    // The symbols panel.
    TestPanel symbols;

    // Fill this panel with nine panels of different colors.
    public TestUnicodeMap() {

        // Title this frame.
        super("Text Test");

        // Make this exit when the close button is clicked.
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });

        // Create the Export dialog.
        dialog = new ExportDialog();

        // Make a menu bar and menu.
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu page = new JMenu("Page");
        JMenu font = new JMenu("Font");

        // Add a menu item which will bring up this dialog.
        JMenuItem export = new JMenuItem("Export...");
        export.addActionListener(this);
        file.add(export);
        file.addSeparator();

        // Quit menu item.
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
        file.add(quit);

        // Latin menu item.
        JMenuItem item = new JMenuItem("Latin");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                symbols.setStartString("\u0000");
                symbols.setEndString("\u00ff");
                symbols.repaint();
            }
        });
        page.add(item);

        item = new JMenuItem("Greek");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                symbols.setStartString("\u0300");
                symbols.setEndString("\u03ff");
                symbols.repaint();
            }
        });
        page.add(item);

        item = new JMenuItem("Punctuation");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                symbols.setStartString("\u2000");
                symbols.setEndString("\u20ff");
                symbols.repaint();
            }
        });
        page.add(item);

        item = new JMenuItem("Arrows");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                symbols.setStartString("\u2100");
                symbols.setEndString("\u21ff");
                symbols.repaint();
            }
        });
        page.add(item);

        item = new JMenuItem("MathOps");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                symbols.setStartString("\u2200");
                symbols.setEndString("\u22ff");
                symbols.repaint();
            }
        });
        page.add(item);

        item = new JMenuItem("Dingbats");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                symbols.setStartString("\u2700");
                symbols.setEndString("\u27ff");
                symbols.repaint();
            }
        });
        page.add(item);

        item = new JMenuItem("Courier");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                symbols.setFont("courier");
                repaint();
            }
        });
        font.add(item);

        item = new JMenuItem("Times");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                symbols.setFont("serif");
                repaint();
            }
        });
        font.add(item);

        item = new JMenuItem("Helvetica");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                symbols.setFont("sansserif");
                repaint();
            }
        });
        font.add(item);

        // Add this to the frame.
        menuBar.add(file);
        menuBar.add(page);
        menuBar.add(font);
        setJMenuBar(menuBar);

        // Get the content pane.
        Container content = this.getContentPane();

        // Set the layout of this panel.
        content.setLayout(new BorderLayout());

        // Create a border of white surrounded by black.
        Border border = BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(1, 1, 1, 1, Color.white), BorderFactory
                .createMatteBorder(2, 2, 2, 2, Color.black));

        // Create a subpanel to hold the symbol panel.
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        symbols = new TestPanel();
        symbols.setBorder(border);
        panel.add(symbols);

        // Add this panel to this container.
        content.add(panel, BorderLayout.CENTER);

    }

    // Action performed method used to change color map.
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof JMenuItem) {
            dialog.showExportDialog(this, "Export...", panel, "untitled");
        }
    }

    /**
     * This method brings up a dialog box to ask if the user really wants to
     * quit. If the answer is yes, the application is stopped.
     */
    public void quit() {

        // Create a dialog box to ask if the user really wants to quit.
        int n = JOptionPane.showConfirmDialog(this,
                "Do you really want to quit?", "Confirm Quit",
                JOptionPane.YES_NO_OPTION);

        if (n == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // Class panel which just paints a given background color.
    class TestPanel extends JPanel {

        String[] label = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "A", "B", "C", "D", "E", "F" };

        String startString = "\u0000";

        String endString = "\u00ff";

        String fontname = "serif";

        int fontsize = 0;

        public void setStartString(String startString) {
            this.startString = startString;
        }

        public void setEndString(String endString) {
            this.endString = endString;
        }

        public void setFont(String fontname) {
            this.fontname = fontname;
        }

        public void paintComponent(Graphics g) {
            if (g != null) {

                VectorGraphics vg = VectorGraphics.create(g);

                Dimension dim = getSize();
                Insets insets = getInsets();

                vg.setColor(Color.white);
                vg
                        .fillRect(insets.left, insets.top, dim.width
                                - insets.left - insets.right, dim.height
                                - insets.top - insets.bottom);

                int dw = dim.width / 17;
                int dh = dim.height / 17;

                vg.setColor(Color.black);
                fontsize = dh * 2 / 3;
                Font font = new Font(fontname, Font.ITALIC, fontsize);
                vg.setFont(font);

                StringBuffer character = new StringBuffer(" ");

                int iy = dh / 2 + dh;
                int ix = dw / 2 + dw;
                for (int i = 0; i < 16; i++) {
                    vg.drawString(label[i], dw / 2, iy,
                            VectorGraphics.TEXT_CENTER,
                            VectorGraphics.TEXT_CENTER, false, Color.cyan, 2,
                            false, Color.yellow);
                    vg.drawString(label[i], ix, dh / 2,
                            VectorGraphics.TEXT_CENTER,
                            VectorGraphics.TEXT_CENTER, false, Color.cyan, 2,
                            false, Color.yellow);
                    iy += dh;
                    ix += dw;
                }

                iy = dh / 2 + dh;
                ix = dw / 2;
                for (int i = (int) startString.charAt(0); i <= (int) endString
                        .charAt(0); i++) {

                    if (i % 16 == 0) {
                        iy = dh / 2 + dh;
                        ix += dw;
                    }

                    character.setCharAt(0, (char) i);
                    vg.drawString(character.toString(), ix, iy,
                            VectorGraphics.TEXT_CENTER,
                            VectorGraphics.TEXT_CENTER, false, Color.cyan, 2,
                            true, Color.yellow);
                    iy += dh;
                }

            }
        }
    }

    public static void main(String[] args) {

        // Create a new instance of this class and add it to the frame.
        TestUnicodeMap test = new TestUnicodeMap();

        // Give the frame a size and make it visible.
        test.pack();
        test.setSize(new Dimension(400, 600));
        test.setVisible(true);

    }
}
