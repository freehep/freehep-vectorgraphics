// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Properties;

import javax.swing.JCheckBox;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/OptionCheckBox.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class OptionCheckBox extends JCheckBox implements Options {
    protected boolean initialState;

    protected String key;

    public OptionCheckBox(Properties options, String key, String text) {
        super(text, new Boolean(options.getProperty(key, "false"))
                .booleanValue());
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
     * Enables (otherwise disables) the supplied component if this checkbox is
     * checked. Can be called for multiple components.
     */
    public void enables(final Component c) {
        if (c.isEnabled()) {
            c.setEnabled(isSelected());

            // selecting
            addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    c.setEnabled(isSelected());
                }
            });

            // disabling
            addPropertyChangeListener("enabled", new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e) {
                    if (e.getNewValue().equals(Boolean.TRUE)) {
                        c.setEnabled(isSelected());
                    } else {
                        c.setEnabled(false);
                    }
                }
            });
        }
    }

    /**
     * Disabled (otherwise enables) the supplied component if this checkbox is
     * checked. Can be called for multiple components.
     */
    public void disables(final Component c) {
        if (c.isEnabled()) {
            c.setEnabled(!isSelected());

            // selecting
            addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    c.setEnabled(!isSelected());
                }
            });

            // disabling
            addPropertyChangeListener("enabled", new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e) {
                    if (e.getNewValue().equals(Boolean.TRUE)) {
                        c.setEnabled(!isSelected());
                    } else {
                        c.setEnabled(false);
                    }
                }
            });
        }
    }

    /**
     * Shows (otherwise hides) the supplied component if this checkbox is
     * checked. Can be called for multiple components.
     */
    public void shows(final Component c) {
        c.setVisible(isSelected());

        addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                c.setVisible(isSelected());
            }
        });
    }

    /**
     * Hides (otherwise shows) the supplied component if this checkbox is
     * checked. Can be called for multiple components.
     */
    public void hides(final Component c) {
        c.setVisible(!isSelected());

        addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                c.setVisible(!isSelected());
            }
        });
    }
}
