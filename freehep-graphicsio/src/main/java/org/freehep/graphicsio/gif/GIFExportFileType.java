// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphicsio.gif;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.spi.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.exportchooser.ImageExportFileType;
import org.freehep.graphicsio.exportchooser.OptionPanel;
import org.freehep.graphicsio.exportchooser.OptionCheckBox;
import org.freehep.graphicsio.exportchooser.OptionComboBox;
import org.freehep.swing.layout.TableLayout;
import org.freehep.util.UserProperties;

/**
 *
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/gif/GIFExportFileType.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class GIFExportFileType extends ImageExportFileType {

    static {
        try {
            Class clazz = Class.forName("org.freehep.graphicsio.gif.GIFImageWriterSpi");
            IIORegistry.getDefaultInstance().registerServiceProvider(clazz.newInstance(), ImageWriterSpi.class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static final String[] quantizeModes = new String[] {"WebColor"};

    public GIFExportFileType() {
        super("gif");
    }

    public boolean hasOptionPanel() {
        return true;
    }

    public JPanel createOptionPanel(Properties user) {
        UserProperties options = new UserProperties(user, GIFGraphics2D.getDefaultProperties());
        JPanel panel = super.createOptionPanel(options);

        OptionCheckBox quantize = new OptionCheckBox(options,
                                                GIFGraphics2D.QUANTIZE_COLORS,
                                                "Quantize Colors");
        panel.add(TableLayout.FULL, quantize);

        JLabel quantizeModeLabel = new JLabel("Quantize using ");
        panel.add(TableLayout.LEFT, quantizeModeLabel);
        quantize.enables(quantizeModeLabel);

        OptionComboBox quantizeMode = new OptionComboBox(options,
                                                GIFGraphics2D.QUANTIZE_MODE,
                                                quantizeModes);
        panel.add(TableLayout.RIGHT, quantizeMode);
        quantize.enables(quantizeMode);

        // disable for now
        quantize.setEnabled(false);
        quantizeModeLabel.setEnabled(false);
        quantizeMode.setEnabled(false);

    	return panel;
    }

    public VectorGraphics getGraphics(OutputStream os, Component target) throws IOException {

        return new GIFGraphics2D(os, target.getSize());
    }
 
}
