// Copyright 2000-2003 FreeHEP
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
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/ImageExportFileType.java dc1b223a6664 2006/06/01 21:46:02 duns $
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

//    private ImageExportFileType(String format, ImageWriterSpi spi,
//            ImageWriteParam param) {
//        super();
//        this.format = format;
//        this.spi = spi;
//        this.param = param;
//    }

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
        Throwable e = null;
        try {
            // create special derived class from ZZZImageExportFileType
            ClassLoader loader = new SpecialClassLoader(
                    ImageExportFileType.class.getClassLoader(), format);
            String className = "ImageExportFileType";
            return (ImageExportFileType) loader.loadClass(className)
                    .newInstance();
        } catch (IllegalArgumentException iae) {
            e = iae;
        } catch (NoClassDefFoundError ncdfe) {
            e = ncdfe;
        } catch (ClassNotFoundException cnfe) {
            e = cnfe;
        } catch (InstantiationException ie) {
            e = ie;
        } catch (IllegalAccessException iace) {
            e = iace;
        } catch (ClassFormatError cfe) {
            e = cfe;
        } catch (SecurityException se) {
            // We run in a restricted environment (WebStart)
            // so we browse for specific formats
            e = se;
         }
        ImageExportFileType type = getStaticInstance(format);
        if ((e != null) && (type == null)) System.err.println(e);
        return type;
    }
    
    public static ImageExportFileType getStaticInstance(String format) {
        if (format.equalsIgnoreCase("gif"))
            return exportFileType("org.freehep.graphicsio.gif.GIFExportFileType");
        if (format.equalsIgnoreCase("png"))
            return exportFileType("org.freehep.graphicsio.png.PNGExportFileType");
        if (format.equalsIgnoreCase("jpg"))
            return exportFileType("org.freehep.graphicsio.jpg.JPGExportFileType");
        if (format.equalsIgnoreCase("raw"))
            return exportFileType("org.freehep.graphicsio.raw.RawExportFileType");
        if (format.equalsIgnoreCase("bmp"))
            return exportFileType("org.freehep.graphicsio.exportchooser.BMPExportFileType");
        if (format.equalsIgnoreCase("wbmp"))
            return exportFileType("org.freehep.graphicsio.exportchooser.WBMPExportFileType");
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

    private static class SpecialClassLoader extends ClassLoader {
        private String format;

        SpecialClassLoader(ClassLoader parent, String format) {
            super(parent);
            this.format = format;
        }

        public Class findClass(String name) throws ClassNotFoundException {
            try {
                String formatUp = format.toUpperCase();
                String pkg = "org.freehep.graphicsio.exportchooser.";
                String templateName = pkg.replaceAll("\\.", "/")
                        + (format.length() == 4 ? "ZZZZ" : "ZZZ") + name
                        + ".class";
                String className = pkg + formatUp + name;
                InputStream in = getResourceAsStream(templateName);
                if (in == null)
                    throw new IllegalArgumentException(templateName
                            + " class does not exist.");

                int n = 0;
                int offset = 0;
                byte[] bytes = new byte[4096];
                while (n >= 0) {
                    n = in.read(bytes, offset, bytes.length - offset);
                    if (n > 0)
                        offset += n;
                }
                in.close();

                // modify class to have correct name, constructor and formatname
                if (format.length() == 3) {
                    for (int i = 0; i < 3; i++) {
                        bytes[133 + i] = bytes[172 + i] = bytes[250 + i] = (byte) formatUp
                                .charAt(i);
                        bytes[202 + i] = (byte) format.charAt(i);
                    }
                } else {
                    for (int i = 0; i < 4; i++) {
                        bytes[133 + i] = bytes[173 + i] = bytes[253 + i] = (byte) formatUp
                                .charAt(i);
                        bytes[204 + i] = (byte) format.charAt(i);
                    }
                }
                return defineClass(className, bytes, 0, offset);
            } catch (IOException e) {
                System.out.println(e);
                throw new ClassNotFoundException();
            }
        }
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
