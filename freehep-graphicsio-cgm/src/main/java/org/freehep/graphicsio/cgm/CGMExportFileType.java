// Copyright 2000-2006 FreeHEP
package org.freehep.graphicsio.cgm;

import java.awt.Component;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.InfoConstants;
import org.freehep.graphicsio.exportchooser.AbstractExportFileType;
import org.freehep.graphicsio.exportchooser.BackgroundPanel;
import org.freehep.graphicsio.exportchooser.InfoPanel;
import org.freehep.graphicsio.exportchooser.OptionCheckBox;
import org.freehep.graphicsio.exportchooser.OptionPanel;
import org.freehep.graphicsbase.swing.layout.TableLayout;
import org.freehep.graphicsbase.util.UserProperties;

/**
 * // FIXME, check all options
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CGMExportFileType.java 6fc90d16bd14 2006/11/30 18:48:36 duns $
 */
public class CGMExportFileType extends AbstractExportFileType {

    public String getDescription() {
        return "Computer Graphics Metafile";
    }

    public String[] getExtensions() {
        return new String[] { "cgm" };
    }

    public String[] getMIMETypes() {
        return new String[] { "image/cgm" };
    }

    public boolean hasOptionPanel() {
        return true;
    }

    public JPanel createOptionPanel(Properties user) {
        UserProperties options = new UserProperties(user, CGMGraphics2D
                .getDefaultProperties());

        OptionPanel formatPanel = new OptionPanel("Format");
        formatPanel.add(TableLayout.FULL, new OptionCheckBox(options,
                CGMGraphics2D.BINARY, "Binary"));

        String rootKey = CGMGraphics2D.class.getName();

        JPanel infoPanel = new InfoPanel(options, rootKey, new String[] {
                InfoConstants.AUTHOR, InfoConstants.TITLE,
                InfoConstants.SUBJECT, InfoConstants.KEYWORDS, });

        // TableLayout.LEFT Panel
        JPanel leftPanel = new OptionPanel();
        leftPanel.add(TableLayout.COLUMN, formatPanel);
        leftPanel.add(TableLayout.COLUMN_FILL, new JLabel());

        // TableLayout.RIGHT Panel
        JPanel rightPanel = new OptionPanel();
        rightPanel.add(TableLayout.COLUMN, new BackgroundPanel(options,
                rootKey, false));
        rightPanel.add(TableLayout.COLUMN_FILL, new JLabel());

        // Make the full panel.
        OptionPanel optionsPanel = new OptionPanel();
        optionsPanel.add("0 0 [5 5 5 5] wt", leftPanel);
        optionsPanel.add("1 0 [5 5 5 5] wt", rightPanel);
        optionsPanel.add("0 1 2 1 [5 5 5 5] wt", infoPanel);
        optionsPanel.add(TableLayout.COLUMN_FILL, new JLabel());

        return optionsPanel;
    }

    public VectorGraphics getGraphics(OutputStream os, Component target)
            throws IOException {

        return new CGMGraphics2D(os, target);
    }
}
