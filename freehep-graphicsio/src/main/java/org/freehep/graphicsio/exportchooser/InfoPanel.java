// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.util.Properties;

import javax.swing.JLabel;

import org.freehep.swing.layout.TableLayout;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/InfoPanel.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class InfoPanel extends OptionPanel {
    public InfoPanel(Properties options, String rootKey, String[] keys) {
        super("Info");

        for (int i = 0; i < keys.length; i++) {
            add(TableLayout.LEFT, new JLabel(keys[i]));
            add(TableLayout.RIGHT, new OptionTextField(options, rootKey + "."
                    + keys[i], 40));
        }
    }
}
