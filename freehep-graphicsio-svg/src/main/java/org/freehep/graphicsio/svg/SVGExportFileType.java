// Copyright 2002-2003, FreeHEP.
package org.freehep.graphicsio.svg;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.ImageConstants;
import org.freehep.graphicsio.InfoConstants;
import org.freehep.graphicsio.exportchooser.AbstractExportFileType;
import org.freehep.graphicsio.exportchooser.BackgroundPanel;
import org.freehep.graphicsio.exportchooser.ImageSizePanel;
import org.freehep.graphicsio.exportchooser.ImageTypePanel;
import org.freehep.graphicsio.exportchooser.InfoPanel;
import org.freehep.graphicsio.exportchooser.OptionCheckBox;
import org.freehep.graphicsio.exportchooser.OptionComboBox;
import org.freehep.graphicsio.exportchooser.OptionPanel;
import org.freehep.graphicsio.exportchooser.OptionTextField;
import org.freehep.swing.layout.TableLayout;
import org.freehep.util.UserProperties;

/**
 * Identical to SVGExportFileType except for the creation of a SVGGraphics2D
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-svg/src/main/java/org/freehep/graphicsio/svg/SVGExportFileType.java 1d3bd2a557b2 2005/12/03 07:37:43 duns $
 */
public class SVGExportFileType extends AbstractExportFileType {

    // Constants for the SVG options.
    final private static String versionList[] = {
    // SVGGraphics2D.VERSION_1_0,
    SVGGraphics2D.VERSION_1_1,
    // SVGGraphics2D.VERSION_1_2
    };

    private OptionCheckBox compress;

    public String getDescription() {
        return "Scalable Vector Graphics";
    }

    public String[] getExtensions() {
        return new String[] { "svgz", "SVGZ", "svg", "SVG" };
    }

    public String[] getMIMETypes() {
        return new String[] { "image/svg+xml" };
    }

    public boolean hasOptionPanel() {
        return true;
    }

    public JPanel createOptionPanel(Properties user) {
        UserProperties options = new UserProperties(user, SVGGraphics2D
                .getDefaultProperties());

        String rootKey = SVGGraphics2D.class.getName();
        OptionPanel imageSize = new ImageSizePanel(options, rootKey);

        OptionPanel format = new OptionPanel("Format");
        format.add(TableLayout.LEFT, new JLabel("SVG Version"));
        format.add(TableLayout.RIGHT, new OptionComboBox(options,
                SVGGraphics2D.VERSION, versionList));

        compress = new OptionCheckBox(options, SVGGraphics2D.COMPRESS,
                "Compress");
        format.add(TableLayout.FULL, compress);

        format.add(TableLayout.FULL, new OptionCheckBox(options,
                SVGGraphics2D.STYLABLE, "Stylable"));

        OptionPanel imageExport = new OptionPanel("Embed / Export Images");
        OptionCheckBox exportImages = new OptionCheckBox(options,
                SVGGraphics2D.EXPORT_IMAGES, "Export");
        imageExport.add(TableLayout.FULL, exportImages);

        JLabel exportSuffixLabel = new JLabel("Image Suffix");
        imageExport.add(TableLayout.LEFT, exportSuffixLabel);
        exportImages.enables(exportSuffixLabel);

        final OptionTextField exportSuffix = new OptionTextField(options,
                SVGGraphics2D.EXPORT_SUFFIX, 20);
        imageExport.add(TableLayout.RIGHT, exportSuffix);
        exportImages.enables(exportSuffix);

        InfoPanel infoPanel = new InfoPanel(options, rootKey, new String[] {
                InfoConstants.CREATOR, InfoConstants.TITLE, });

        // TableLayout.LEFT Panel
        JPanel leftPanel = new OptionPanel();
        leftPanel.add(TableLayout.COLUMN, imageSize);
        leftPanel.add(TableLayout.COLUMN, format);
        leftPanel.add(TableLayout.COLUMN_FILL, new JLabel());

        // TableLayout.RIGHT Panel
        JPanel rightPanel = new OptionPanel();
        rightPanel.add(TableLayout.COLUMN, new BackgroundPanel(options,
                rootKey, true));
        rightPanel.add(TableLayout.COLUMN, imageExport);
        rightPanel.add(TableLayout.COLUMN, new ImageTypePanel(options, rootKey,
                new String[] { ImageConstants.SMALLEST, ImageConstants.PNG,
                        ImageConstants.JPG }));
        rightPanel.add(TableLayout.COLUMN_FILL, new JLabel());

        // Make the full panel.
        OptionPanel panel = new OptionPanel();
        panel.add("0 0 [5 5 5 5] wt", leftPanel);
        panel.add("1 0 [5 5 5 5] wt", rightPanel);
        panel.add("0 1 2 1 [5 5 5 5] wt", infoPanel);
        panel.add(TableLayout.COLUMN_FILL, new JLabel());

        return panel;
    }

    public VectorGraphics getGraphics(OutputStream os, Component target)
            throws IOException {

        return new SVGGraphics2D(os, target);
    }

    public VectorGraphics getGraphics(File file, Component target)
            throws IOException {

        return new SVGGraphics2D(file, target);
    }

    public File adjustFilename(File file, Properties user) {
        UserProperties options = new UserProperties(user, SVGGraphics2D
                .getDefaultProperties());
        if (options.isProperty(SVGGraphics2D.COMPRESS)) {
            return adjustExtension(file, "svgz", null, "");
        } else {
            return adjustExtension(file, "svg", null, "");
        }
    }

}
