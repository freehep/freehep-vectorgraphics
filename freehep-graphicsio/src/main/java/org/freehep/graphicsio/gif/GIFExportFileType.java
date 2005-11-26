// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphicsio.gif;

import java.awt.Component;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.exportchooser.ImageExportFileType;
import org.freehep.graphicsio.exportchooser.OptionCheckBox;
import org.freehep.graphicsio.exportchooser.OptionComboBox;
import org.freehep.swing.layout.TableLayout;
import org.freehep.util.UserProperties;

/**
 * 
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/gif/GIFExportFileType.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class GIFExportFileType extends ImageExportFileType {

    static {
        try {
            Class clazz = Class
                    .forName("org.freehep.graphicsio.gif.GIFImageWriterSpi");
            IIORegistry.getDefaultInstance().registerServiceProvider(
                    clazz.newInstance(), ImageWriterSpi.class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static final String[] quantizeModes = new String[] { "WebColor" };

    public GIFExportFileType() {
        super("gif");
    }

    public boolean hasOptionPanel() {
        return true;
    }

    public JPanel createOptionPanel(Properties user) {
        UserProperties options = new UserProperties(user, GIFGraphics2D
                .getDefaultProperties());
        JPanel panel = super.createOptionPanel(options);

        OptionCheckBox quantize = new OptionCheckBox(options,
                GIFGraphics2D.QUANTIZE_COLORS, "Quantize Colors");
        panel.add(TableLayout.FULL, quantize);

        JLabel quantizeModeLabel = new JLabel("Quantize using ");
        panel.add(TableLayout.LEFT, quantizeModeLabel);
        quantize.enables(quantizeModeLabel);

        OptionComboBox quantizeMode = new OptionComboBox(options,
                GIFGraphics2D.QUANTIZE_MODE, quantizeModes);
        panel.add(TableLayout.RIGHT, quantizeMode);
        quantize.enables(quantizeMode);

        // disable for now
        quantize.setEnabled(false);
        quantizeModeLabel.setEnabled(false);
        quantizeMode.setEnabled(false);

        return panel;
    }

    public VectorGraphics getGraphics(OutputStream os, Component target)
            throws IOException {

        return new GIFGraphics2D(os, target.getSize());
    }

}
