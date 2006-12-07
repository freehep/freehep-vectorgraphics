// Copyright FreeHEP, 2003-2006.

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.freehep.util.export.ExportDialog;
import org.freehep.graphics2d.VectorGraphics;

/**
 * This example shows how to add a standard ExportDialog
 * to your application and allow it to export what you draw
 * in panel to any of the supported vector and bitmap formats.
 *
 * The paintComponent() method and the ExportDialogExample
 * panel should be replaced by your own method and component.
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
 * @version $Id: src/main/examples/ExportDialogExample.java accbeaafed87 2006/12/07 01:17:03 duns $
 */
public class ExportDialogExample extends JPanel {


    public ExportDialogExample() {
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

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("ExportDialogExample");
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        final ExportDialogExample panel = new ExportDialogExample();
        frame.getContentPane().add(panel);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        menuBar.add(file);

        JMenuItem exportItem = new JMenuItem("Export...");
        exportItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ExportDialog export = new ExportDialog();
                export.showExportDialog(panel, "Export view as ...", panel, "export");
            }
        });
        file.add(exportItem);

        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        file.add(quitItem);

        frame.pack();
        frame.setVisible(true);
    }
}
