// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.raw;

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
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/raw/RawExportFileType.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class RawExportFileType extends ImageExportFileType {

    static {
        try {
            Class clazz = Class.forName("org.freehep.graphicsio.raw.RawImageWriterSpi");
            IIORegistry.getDefaultInstance().registerServiceProvider(clazz.newInstance(), ImageWriterSpi.class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public RawExportFileType() {
        super("raw");
    }
}
