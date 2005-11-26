// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.text.Format;
import java.util.Properties;

import javax.swing.JFormattedTextField;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/OptionFormattedTextField.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class OptionFormattedTextField extends JFormattedTextField implements
        Options {
    protected String initialText;

    protected String key;

    public OptionFormattedTextField(Properties options, String key,
            String text, int columns, Format format) {
        super(format);
        setText(options.getProperty(key, text));
        setColumns(columns);
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
