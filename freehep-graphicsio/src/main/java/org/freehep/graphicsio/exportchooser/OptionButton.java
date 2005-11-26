// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/OptionButton.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class OptionButton extends JButton implements Options {

    protected String key;

    public OptionButton(Properties options, String key, String text,
            final JDialog dialog) {
        super(text);
        this.key = key;
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dialog.setVisible(true);
                dialog.dispose();
            }
        });
    }

    public boolean applyChangedOptions(Properties options) {
        return false;
    }

}
