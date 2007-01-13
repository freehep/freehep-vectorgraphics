// Copyright 2000-2006 FreeHEP
package org.freehep.graphicsio.java;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.exportchooser.AbstractExportFileType;
import org.freehep.graphicsio.exportchooser.OptionPanel;
import org.freehep.graphicsio.exportchooser.OptionCheckBox;
import org.freehep.util.UserProperties;
import org.freehep.swing.layout.TableLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 * // FIXME, check all options
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-java/src/main/java/org/freehep/graphicsio/java/JAVAExportFileType.java e908a30ae307 2007/01/13 00:45:55 duns $
 */
public class JAVAExportFileType extends AbstractExportFileType {

    public String getDescription() {
        return "Java Source File (for Testing)";
    }

    public String[] getExtensions() {
        return new String[] { "java" };
    }

    public String[] getMIMETypes() {
        return new String[] { "application/java" };
    }

    public boolean hasOptionPanel() {
        return true;
    }

    public VectorGraphics getGraphics(File file, Component target)
            throws IOException {

        return new JAVAGraphics2D(file, target);
    }

    public JPanel createOptionPanel(Properties user) {
        UserProperties options = new UserProperties(
            user, JAVAGraphics2D.getDefaultProperties());

        // Make the full panel.
        OptionPanel optionsPanel = new OptionPanel();

        // Image settings
        OptionPanel imagePanel = new OptionPanel("Images");
        optionsPanel.add(
            "0 0 [5 5 5 5] wt",
            imagePanel);

        OptionCheckBox embedOptionCheckBox = new OptionCheckBox(
            options,
            JAVAGraphics2D.EMBED_IMAGES,
            "Embed imagePanel as byte[]");
        imagePanel.add(TableLayout.FULL, embedOptionCheckBox);

        OptionCheckBox pathOptionCheckBox = new OptionCheckBox(
            options,
            JAVAGraphics2D.COMPLETE_IMAGE_PATHS,
            "Write complete path to image");
        imagePanel.add(TableLayout.FULL, pathOptionCheckBox);
        embedOptionCheckBox.disables(pathOptionCheckBox);

        optionsPanel.add(TableLayout.COLUMN_FILL, new JLabel());

        return optionsPanel;
    }

    public VectorGraphics getGraphics(OutputStream os, Component target)
            throws IOException {

        return new JAVAGraphics2D(os, target);
    }

}
