// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.freehep.graphicsbase.swing.layout.TableLayout;
import org.freehep.graphicsbase.util.UserProperties;
import org.freehep.graphicsio.ImageConstants;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/ImageTypePanel.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class ImageTypePanel extends OptionPanel {

    private String key;

    private String initialType;

    private JComboBox imageTypeCombo;

    public ImageTypePanel(Properties user, String rootKey, String[] types) {
        super("Image Type");
        key = rootKey + "." + ImageConstants.WRITE_IMAGES_AS;

        UserProperties options = new UserProperties(user);
        initialType = options.getProperty(key);

        imageTypeCombo = new OptionComboBox(options, key, types);
        // FREEHEP-575
        imageTypeCombo.setSelectedItem(initialType);
        add(TableLayout.LEFT, new JLabel("Include Images as "));
        add(TableLayout.RIGHT, imageTypeCombo);
    }
}