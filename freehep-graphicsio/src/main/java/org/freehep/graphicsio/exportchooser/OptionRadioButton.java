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
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/OptionRadioButton.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class OptionRadioButton extends JRadioButton implements Options {
    protected boolean initialState;
    protected String key;

    public OptionRadioButton(Properties options, String key, String text) {
        super(text, new Boolean(options.getProperty(key, "false")).booleanValue());
        this.key = key;
        initialState = isSelected();
    }

    public boolean applyChangedOptions(Properties options) {
        if (isSelected() != initialState) {
            options.setProperty(key, Boolean.toString(isSelected()));
            return true;
        }
        return false;
    }

    /**
     * Enables (otherwise disables) the supplied component if this radiobutton is checked.
     * Can be called for multiple components.
     */
    public void enables(final Component c) {
        if (c.isEnabled()) {
            c.setEnabled(isSelected());

            addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    c.setEnabled(isSelected());
                }
            });
        }
    }

    /**
     * Shows (otherwise hides) the supplied component if this radiobutton is checked.
     * Can be called for multiple components.
     */
    public void shows(final Component c) {
        c.setVisible(isSelected());

        addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                c.setVisible(isSelected());
            }
        });
    }
}
