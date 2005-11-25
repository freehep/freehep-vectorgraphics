// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

import org.freehep.graphicsio.ImageConstants;
import org.freehep.swing.layout.TableLayout;

/**
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/ImagePanel.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class ImagePanel extends OptionPanel {
    public ImagePanel(Properties options, String rootKey, String[] formats) {
        super("Images");

        add(TableLayout.FULL,  new JLabel("Write Images as"));
        ButtonGroup group = new ButtonGroup();
        OptionRadioButton imageType[] = new OptionRadioButton[formats.length];
        for (int i=0; i<formats.length; i++) {
            imageType[i] = new OptionRadioButton(options,
                                                rootKey+"."+ImageConstants.WRITE_IMAGES_AS,
                                                formats[i]);
            add(TableLayout.FULL,  imageType[i]);
            group.add(imageType[i]);
//            add(TableLayout.RIGHT, new OptionTextField(options,
//                                            rootKey+"."+keys[i], 40));
        }
    }
}
