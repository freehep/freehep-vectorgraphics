// Copyright 2000-2007 FreeHEP
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
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFDisplay.java 63c8d910ece7 2007/01/20 15:30:50 duns $
 */

import org.freehep.swing.ExtensionFileFilter;
import org.freehep.swing.AllSupportedFileFilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

/**
 * A simple interpreter displaying an EMF file read in by the EMFInputStream in
 * a JPanel
 */
public class EMFDisplay {

    public static void main(String[] args) {
        try {
            EMFViewer emfViewer = new EMFViewer();
            if (args[0] != null) {
                emfViewer.show(new File(args[0]));
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}

// EMFDisplay
