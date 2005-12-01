// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphicsio.ps.test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.border.*;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.freehep.util.export.*;
import org.freehep.graphicsio.ps.*;

/**
 *
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-ps/src/test/java/org/freehep/graphicsio/ps/test/TestColorMap.java af7f4a47f25d 2005/12/01 22:33:12 duns $
 */
public class TestColorMap
    extends JFrame
    implements ActionListener {

    // Create the color map to use.
    ColorMap colorMap = new ColorMap();

    // Export Dialog.
    ExportDialog dialog;

    // The main panel.
    JPanel panel;

    // Fill this panel with nine panels of different colors.
    public TestColorMap() {

	// Title this frame.
	super("Color Map Test");

	// Make this exit when the close button is clicked.
	setDefaultCloseOperation(WindowConstants.
				 DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {quit();}
	});

	// Create the Export dialog.
	dialog = new ExportDialog();

	// Make a menu bar and menu.
	JMenuBar menuBar = new JMenuBar();
	JMenu file = new JMenu("File");

	// Add a menu item which will bring up this dialog.
	JMenuItem export = new JMenuItem("Export...");
	export.addActionListener(this);
	file.add(export);

	// Quit menu item.
	JMenuItem quit = new JMenuItem("Quit");
	quit.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
	    quit();
	}});
	file.add(quit);

	// Add this to the frame.
	menuBar.add(file);
	setJMenuBar(menuBar);

	// Get the content pane.
	Container content = this.getContentPane();

	// Set the layout of this panel.
	content.setLayout(new BorderLayout());

	// Create a combo box with the four color maps in it.
	String[] chooserTest = {"Display","Print","Grayscale","Black&White"};
	JComboBox chooser = new JComboBox(chooserTest);
	chooser.addActionListener(this);

	// Add this to the top of this container.
	content.add(chooser, BorderLayout.NORTH);

	// Create a border of white surrounded by black.
	Border border = BorderFactory.createCompoundBorder(
	  BorderFactory.createMatteBorder(1,1,1,1,Color.white),
	  BorderFactory.createMatteBorder(2,2,2,2,Color.black));

	// Create a subpanel with grid layout to hold the colored tiles.
	panel = new JPanel();
	panel.setLayout(new GridLayout(13,3));

	// Add tiles of colors to this panel.
	for (int i=0; i<13; i++) {
	    TestPanel test;

	    test = new TestPanel(colorMap, i);
	    test.setBorder(border);
	    panel.add(test);

	    test = new TestPanel(colorMap, i+13);
	    test.setBorder(border);
	    panel.add(test);

	    test = new TestPanel(colorMap, i+26);
	    test.setBorder(border);
	    panel.add(test);

	}

	// Add this panel to this container.
	content.add(panel, BorderLayout.CENTER);

    }

    /**
     * This method brings up a dialog box to ask if the user really
     * wants to quit.  If the answer is yes, the application is
     * stopped.  */
    public void quit() {

	// Create a dialog box to ask if the user really wants to quit.
	int n = JOptionPane.showConfirmDialog
	    (this, "Do you really want to quit?","Confirm Quit",
	     JOptionPane.YES_NO_OPTION);

	if (n==JOptionPane.YES_OPTION) {
	    System.exit(0);
	}
    }

    // Action performed method used to change color map.
    public void actionPerformed(ActionEvent e) {
	Object source = e.getSource();
	if (source instanceof JComboBox) {
	    JComboBox cb = (JComboBox) e.getSource();
	    String tag = (String) cb.getSelectedItem();

	    if (tag=="Display") {
		colorMap.useDisplayColorMap();
	    } else if (tag=="Print") {
		colorMap.usePrintColorMap();
	    } else if (tag=="Grayscale") {
		colorMap.useGrayscaleColorMap();
	    } else if (tag=="Black&White") {
		colorMap.useBlackAndWhiteColorMap();
	    }
	    this.repaint();
	} else if (source instanceof JMenuItem) {
	    dialog.showExportDialog(this, "Export...", panel, "untitled");
	}
    }

    // Class panel which just paints a given background color.
    class TestPanel extends JPanel {
	private ColorMap colorMap;
	private int bkgColorIndex;

	public TestPanel(ColorMap colorMap, int bkgColorIndex) {
	    this.colorMap = colorMap;
	    this.bkgColorIndex = bkgColorIndex;
	}

	public void paintComponent(Graphics g) {
	    if (g!=null) {
		Dimension dim = getSize();
		Insets insets = getInsets();

		Color bkgColor = colorMap.getColor(bkgColorIndex);

		g.setColor(bkgColor);
		g.fillRect(insets.left, insets.top,
			   dim.width-insets.left-insets.right,
			   dim.height-insets.top-insets.bottom);
	    }
	}
    }

    public static void main(String[] args) {

	// Create a new instance of this class and add it to the frame.
        TestColorMap test = new TestColorMap();

	// Give the frame a size and make it visible.
        test.pack();
        test.setSize(new Dimension(350,700));
        test.setVisible(true);

    }
}
