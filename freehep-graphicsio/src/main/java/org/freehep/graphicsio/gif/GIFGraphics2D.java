// Copyright 2003-2006, FreeHEP.
package org.freehep.graphicsio.gif;

import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;

import org.freehep.graphicsio.ImageGraphics2D;
import org.freehep.util.UserProperties;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/gif/GIFGraphics2D.java 7a2e6daa0f4f 2006/11/26 22:00:48 duns $
 */
public class GIFGraphics2D extends ImageGraphics2D {

    static {
        try {
            Class clazz = Class
                    .forName("org.freehep.graphicsio.gif.GIFImageWriterSpi");
            IIORegistry.getDefaultInstance().registerServiceProvider(
                    (ImageWriterSpi)clazz.newInstance(), ImageWriterSpi.class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static final String rootKey = GIFGraphics2D.class.getName();

    public static final String QUANTIZE_COLORS = rootKey + ".QuantizeColors";

    public static final String QUANTIZE_MODE = rootKey + ".QuantizeMode";

    private static final UserProperties defaultProperties = new UserProperties();
    static {
        defaultProperties.setProperty(QUANTIZE_COLORS, true);
        defaultProperties.setProperty(QUANTIZE_MODE, "NeuralNetworkColor");
    }

    public static Properties getDefaultProperties() {
        return defaultProperties;
    }

    public static void setDefaultProperties(Properties newProperties) {
        defaultProperties.setProperties(newProperties);
    }

    public static String version = "$Revision$";

    public GIFGraphics2D(File file, Dimension size)
            throws FileNotFoundException {
        this(new FileOutputStream(file), size);
    }

    public GIFGraphics2D(File file, Component component)
            throws FileNotFoundException {
        this(new FileOutputStream(file), component);
    }

    public GIFGraphics2D(OutputStream os, Dimension size) {
        super(new BufferedOutputStream(os), size, "gif");
        initProperties(getDefaultProperties());
    }

    public GIFGraphics2D(OutputStream os, Component component) {
        super(new BufferedOutputStream(os), component, "gif");
        initProperties(getDefaultProperties());
    }
}
