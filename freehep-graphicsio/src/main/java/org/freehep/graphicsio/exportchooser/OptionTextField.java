// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.util.Properties;

import javax.swing.JTextField;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/OptionTextField.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class OptionTextField extends JTextField implements Options {
    protected String initialText;

    protected String key;

    public OptionTextField(Properties options, String key, int columns) {
        super(options.getProperty(key, ""), columns);
        this.key = key;
        initialText = getText();
    }

    public boolean applyChangedOptions(Properties options) {
        if (!getText().equals(initialText)) {
            options.setProperty(key, getText());
            return true;
        }
        return false;
    }
}
