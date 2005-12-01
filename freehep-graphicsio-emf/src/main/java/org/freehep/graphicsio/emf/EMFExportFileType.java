// Copyright 2000-2003 FreeHEP
package org.freehep.graphicsio.emf;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.exportchooser.AbstractExportFileType;
import org.freehep.graphicsio.exportchooser.BackgroundPanel;
import org.freehep.graphicsio.exportchooser.OptionPanel;
import org.freehep.graphicsio.exportchooser.OptionCheckBox;
import org.freehep.swing.layout.TableLayout;
import org.freehep.util.UserProperties;

/**
 * // FIXME, check all options
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFExportFileType.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class EMFExportFileType extends AbstractExportFileType {

    private OptionCheckBox compress;

    public String getDescription() {
        return "Windows Enhanced Metafile";
    }

    public String[] getExtensions() {
        return new String[] { "emz", "EMZ", "emf", "EMF" };
    }

    public String[] getMIMETypes() {
        return new String[] { "image/x-emf" };
    }

    public boolean hasOptionPanel() {
        return true;
    }

    public JPanel createOptionPanel(Properties user) {
        UserProperties options = new UserProperties(user, EMFGraphics2D.getDefaultProperties());

        String rootKey = EMFGraphics2D.class.getName();

        // Make the full panel.
        OptionPanel optionsPanel = new OptionPanel();
        optionsPanel.add("0 0 [5 5 5 5] wt", new BackgroundPanel(options, rootKey, true));
        optionsPanel.add(TableLayout.COLUMN_FILL, new JLabel());

        OptionPanel format = new OptionPanel("Format");
        compress = new OptionCheckBox(options,
                                      EMFGraphics2D.COMPRESS,
                                      "Compress");
        format.add(TableLayout.FULL,  compress);
        optionsPanel.add(TableLayout.COLUMN_FILL, format);

        return optionsPanel;
    }

    public VectorGraphics getGraphics(OutputStream os, Component target)
            throws IOException {

        return new EMFGraphics2D(os, target);
    }

    public File adjustFilename(File file, Properties user) {
        UserProperties options = new UserProperties(user, EMFGraphics2D.getDefaultProperties());
    	if (options.isProperty(EMFGraphics2D.COMPRESS)) {
    	    return adjustExtension(file, "emz", null);
    	} else {
    	    return adjustExtension(file, "emf", null);
    	}
    }
}
