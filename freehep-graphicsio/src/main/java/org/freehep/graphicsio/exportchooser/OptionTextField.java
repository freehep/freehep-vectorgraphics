// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

import org.freehep.swing.layout.TableLayout;

/**
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/OptionTextField.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
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

