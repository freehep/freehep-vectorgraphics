// Copyright 2000-2006 FreeHEP
package org.freehep.graphicsio.exportchooser;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.ImageGraphics2D;
import org.freehep.swing.layout.TableLayout;
import org.freehep.util.UserProperties;

/**
 * // FIXME, check all options
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/ImageExportFileType.java 5293c168717f 2006/11/26 23:12:57 duns $
 */
public class ImageExportFileType extends AbstractExportFileType {

    protected String format;

    protected ImageWriterSpi spi;

    protected ImageWriteParam param;

    protected OptionCheckBox antialias;

    protected OptionCheckBox antialiasText;

    protected OptionCheckBox progressive;

    protected OptionCheckBox compress;

    protected OptionComboBox compressMode;

    protected OptionComboBox compressDescription;

    protected OptionTextField compressQuality;

    protected ImageExportFileType(String format) {
        Iterator iterator = ImageIO.getImageWritersByFormatName(format);
        if (iterator.hasNext()) {
            ImageWriter writer = (ImageWriter) iterator.next();
            this.format = format;
            this.spi = writer.getOriginatingProvider();
            this.param = writer.getDefaultWriteParam();
            return;
        }
        throw new IllegalArgumentException(getClass() + ": Format not valid: "
                + format);
    }

    public static ImageExportFileType getInstance(String format) {
    	format = format.toLowerCase();
        if (format.equals("gif"))
            return exportFileType("org.freehep.graphicsio.gif.GIFExportFileType");
        if (format.equals("png"))
            return exportFileType("org.freehep.graphicsio.png.PNGExportFileType");
        if (format.equals("jpg"))
            return exportFileType("org.freehep.graphicsio.jpg.JPGExportFileType");
        if (format.equals("raw"))
            return exportFileType("org.freehep.graphicsio.raw.RawExportFileType");
        if (format.equals("bmp"))
            return exportFileType("org.freehep.graphicsio.bmp.BMPExportFileType");
        if (format.equals("wbmp"))
            return exportFileType("org.freehep.graphicsio.wbmp.WBMPExportFileType");
        return null;
    }

    private static ImageExportFileType exportFileType(String className) {
        try {
            Class clazz = Class.forName(className);
            return (ImageExportFileType) clazz.newInstance();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public String getDescription() {
        // Sun's descriptions are no good ("Standard JPEG writer")
        String f = format.toLowerCase();
        if (f.equals("jpg") || f.equals("jpeg")) {
            return "Joint Photographers Expert Group Format";
        } else if (f.equals("png")) {
            return "Portable Network Graphics Format";
        } else if (f.equals("gif")) {
            return "Graphics Interchange Format";
        } else {
            return spi.getDescription(Locale.US);
        }
    }

    public String[] getExtensions() {
        return spi.getFileSuffixes();
    }

    public String[] getMIMETypes() {
        return spi.getMIMETypes();
    }

    public boolean hasOptionPanel() {
        return true;
    }

    public JPanel createOptionPanel(Properties user) {
        UserProperties options = new UserProperties(user, ImageGraphics2D
                .getDefaultProperties(format));
        OptionPanel panel = new OptionPanel(format.toUpperCase() + " Format");

        String formatKey = ImageGraphics2D.rootKey + "." + format;

        panel.add(TableLayout.FULL, new BackgroundPanel(options, formatKey,
                ImageGraphics2D.canWriteTransparent(format), null));

        antialias = new OptionCheckBox(options, formatKey
                + ImageGraphics2D.ANTIALIAS, "Antialias");
        panel.add(TableLayout.FULL, antialias);

        antialiasText = new OptionCheckBox(options, formatKey
                + ImageGraphics2D.ANTIALIAS_TEXT, "Antialias Text");
        panel.add(TableLayout.FULL, antialiasText);

        progressive = new OptionCheckBox(options, formatKey
                + ImageGraphics2D.PROGRESSIVE, "Progressive");
        if (param.canWriteProgressive()) {
            panel.add(TableLayout.FULL, progressive);
        }

        compress = new OptionCheckBox(options, formatKey
                + ImageGraphics2D.COMPRESS, "Compress");
        if (param.canWriteCompressed()) {
            if (ImageGraphics2D.canWriteUncompressed(format)) {
                panel.add(TableLayout.FULL, compress);
            }

            // NOTE: force compression
            param.setCompressionMode(options.isProperty(formatKey
                    + ImageGraphics2D.COMPRESS) ? ImageWriteParam.MODE_EXPLICIT
                    : ImageWriteParam.MODE_DISABLED);
        }

        if (param.canWriteCompressed()
                && (param.getCompressionMode() == ImageWriteParam.MODE_EXPLICIT)) {

            String[] compressionTypes = param.getCompressionTypes();
            JLabel compressModeLabel = new JLabel("Compression Mode");
            compressMode = new OptionComboBox(options, formatKey
                    + ImageGraphics2D.COMPRESS_MODE, compressionTypes);

            if (compressionTypes.length > 1) {
                panel.add(TableLayout.LEFT, compressModeLabel);
                panel.add(TableLayout.RIGHT, compressMode);
                compress.enables(compressModeLabel);
                compress.enables(compressMode);
            }
            /*
             * FIXME to be connected to the rest String[]
             * compressionDescriptions =
             * param.getCompressionQualityDescriptions(); JLabel
             * compressDescriptionLabel = new JLabel("Quality Mode");
             * compressDescription = new OptionComboBox(options,
             * formatKey+ImageGraphics2D.COMPRESS_DESCRIPTION,
             * compressionDescriptions);
             * 
             * if (compressionDescriptions.length > 1) {
             * panel.add(TableLayout.LEFT, compressDescriptionLabel);
             * panel.add(TableLayout.RIGHT, compressDescription);
             * compress.enables(compressDescriptionLabel);
             * compress.enables(compressDescription); }
             */

            // FIXME check value
            JLabel compressQualityLabel = new JLabel("Quality Value");
            panel.add(TableLayout.LEFT, compressQualityLabel);

            compressQuality = new OptionTextField(options, formatKey
                    + ImageGraphics2D.COMPRESS_QUALITY, 5);
            panel.add(TableLayout.RIGHT, compressQuality);
            compress.enables(compressQualityLabel);
            compress.enables(compressQuality);

            // FIXME add slider
        }

        return panel;
    }

    public VectorGraphics getGraphics(OutputStream os, Component target)
            throws IOException {

        return new ImageGraphics2D(os, target, format);
    }

    public String toString() {
        return super.toString() + " for " + format;
    }
}
