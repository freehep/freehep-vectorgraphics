// Copyright 2007 FreeHEP
package org.freehep.graphicsio.emf;

import org.freehep.swing.ExtensionFileFilter;

import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

/**
 * Simple frame to display EMF images.
 *
 * @author Steffen Greiffenberg
 * @version $ID$
 */
public class EMFViewer extends JFrame {

    /**
     * Prefix for the TITLE
     */
    private static String TITLE = "Freehep EMF Viewer";

    /**
     * Title for the Button
     */
    private static String LOAD_BUTTON_TITLE = "Open EMF";

    /**
     * Title for the Button
     */
    private static String ZOOMIN__BUTTON_TITLE = "Zoom in";

    /**
     * Title for the Button
     */
    private static String ZOOMOUT_BUTTON_TITLE = "Zoom out";

    /**
     * simple panel for displaying an EMF
     */
    private EMFPanel emfPanel = new EMFPanel();

    /**
     * used to open EMF-Files
     */
    private JFileChooser fileChooser = new JFileChooser();

    public EMFViewer() {
        initGUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
    }

    /**
     * creates the content of tha frame
     */
    private void initGUI() {
        setTitle(TITLE);

        // create a panel for the window content
        getContentPane().setLayout(new BorderLayout(0, 0));
        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel);

        // layout the mainPanel
        mainPanel.setLayout(new BorderLayout(3, 3));
        mainPanel.add(
            BorderLayout.CENTER,
            new JScrollPane(emfPanel));

        // prepare the filechooser
        fileChooser.addChoosableFileFilter(
            new ExtensionFileFilter("emf", "Encapsulated Metafile"));

        // panel for buttons
        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 3, 3));
        mainPanel.add(
            BorderLayout.SOUTH,
            buttonPanel);


        // button to zoom in
        JButton zoomInButton = new JButton(ZOOMIN__BUTTON_TITLE);
        zoomInButton.addActionListener(new ZoomInAction());
        buttonPanel.add(zoomInButton);

        // button to zoom out
        JButton zoomOutButton = new JButton(ZOOMOUT_BUTTON_TITLE);
        zoomOutButton.addActionListener(new ZoomOutAction());
        buttonPanel.add(zoomOutButton);

        // loadButton to open files
        JButton loadButton = new JButton(LOAD_BUTTON_TITLE);
        loadButton.addActionListener(new OpenFileAction());
        buttonPanel.add(loadButton);
    }

    /**
     * displays the file and
     * @param emfFile File to show
     */
    public void show(File emfFile) {
        try {
            FileInputStream fis = new FileInputStream(emfFile);
            EMFInputStream emf = new EMFInputStream(new BufferedInputStream(fis));

            EMFRenderer renderer = new EMFRenderer(emf);
            emfPanel.setRenderer(renderer);

            // set the window title
            setTitle(TITLE + " - " + emfFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // show the frame
        if (!isVisible()) {
            setVisible(true);
        }
    }

    /**
     * Uses the fileChooser to open a file.
     * Uses the emfPanel to display the file.
     */
    private class OpenFileAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int result = fileChooser.showOpenDialog(EMFViewer.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fileChooser.getSelectedFile();
                    FileInputStream fis = new FileInputStream(file);
                    EMFInputStream emf = new EMFInputStream(new BufferedInputStream(fis));

                    EMFRenderer renderer = new EMFRenderer(emf);
                    emfPanel.setRenderer(renderer);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * zoom the panel in
     */
    private class ZoomInAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            emfPanel.zoomIn();
        }
    }

    /**
     * zoom the panel out
     */
    private class ZoomOutAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            emfPanel.zoomOut();
        }
    }
}
