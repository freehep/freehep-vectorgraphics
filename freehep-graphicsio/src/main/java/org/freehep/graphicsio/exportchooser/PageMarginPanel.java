// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.PageConstants;
import org.freehep.graphicsio.ImageConstants;
import org.freehep.swing.layout.TableLayout;
import org.freehep.util.UserProperties;

/**
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/PageMarginPanel.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class PageMarginPanel extends OptionPanel {

    final private static String pageMarginList[] = {
        "Custom",
        PageConstants.SMALL,
        PageConstants.MEDIUM,
        PageConstants.LARGE
    };

    private String key;
    private Insets initialMargins;
    private JComboBox pageMarginCombo;
    private JFormattedTextField top, left, bottom, right;

    public PageMarginPanel(Properties user, String rootKey) {
        super("Page Margins");
        key = rootKey+"."+PageConstants.PAGE_MARGINS;

        UserProperties options = new UserProperties(user);
        initialMargins = options.getPropertyInsets(key);

        pageMarginCombo = new JComboBox(pageMarginList);
        add(TableLayout.LEFT,  new JLabel("Preset Margins"));
        add(TableLayout.RIGHT, pageMarginCombo);

        add(TableLayout.LEFT,  new JLabel("Top"));
        top = new JFormattedTextField(new TextFieldFormatter());
        top.setColumns(10);
        add(TableLayout.RIGHT, top);

        add(TableLayout.LEFT,  new JLabel("Bottom"));
        bottom = new JFormattedTextField(new TextFieldFormatter());
        bottom.setColumns(10);
        add(TableLayout.RIGHT, bottom);

        add(TableLayout.LEFT,  new JLabel("Left"));
        left = new JFormattedTextField(new TextFieldFormatter());
        left.setColumns(10);
        add(TableLayout.RIGHT, left);

        add(TableLayout.LEFT,  new JLabel("Right"));
        right = new JFormattedTextField(new TextFieldFormatter());
        right.setColumns(10);
        add(TableLayout.RIGHT, right);

        pageMarginCombo.addItemListener(new ComboListener());
        top.addActionListener(new TextFieldListener());
        bottom.addActionListener(new TextFieldListener());
        left.addActionListener(new TextFieldListener());
        right.addActionListener(new TextFieldListener());

        // now set the initial values
        top.setValue(new Integer(initialMargins.top));
        bottom.setValue(new Integer(initialMargins.bottom));
        left.setValue(new Integer(initialMargins.left));
        right.setValue(new Integer(initialMargins.right));

        // trigger the changes
        new TextFieldListener().actionPerformed(null);
        new ComboListener().itemStateChanged(null);
    }

    public boolean applyChangedOptions(Properties options) {
        boolean changed = false;

        Insets margins = new Insets(((Number)top.getValue()).intValue(),
                                    ((Number)left.getValue()).intValue(),
                                    ((Number)bottom.getValue()).intValue(),
                                    ((Number)right.getValue()).intValue());

        if (!margins.equals(initialMargins)) {
            UserProperties.setProperty(options, key, margins);
            changed = true;
        }

        return changed;
    }

    private class ComboListener implements ItemListener {
    	public void itemStateChanged(ItemEvent e) {
    	    int index = pageMarginCombo.getSelectedIndex();
            if (index != 0) {
                Insets insets = PageConstants.getMargins(pageMarginList[index]);
                top.setValue(new Integer(insets.top));
                bottom.setValue(new Integer(insets.bottom));
                left.setValue(new Integer(insets.left));
                right.setValue(new Integer(insets.right));
            }
        }
    }

    private class TextFieldListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            Insets margins = new Insets(
                ((Number)top.getValue()).intValue(),
                ((Number)left.getValue()).intValue(),
                ((Number)bottom.getValue()).intValue(),
                ((Number)right.getValue()).intValue());

            for (int i=1; i<pageMarginList.length; i++) {
                Insets insets = PageConstants.getMargins(pageMarginList[i]);
                if (margins.equals(insets)) {
                    pageMarginCombo.setSelectedIndex(i);
                    return;
                }
            }

            pageMarginCombo.setSelectedIndex(0);  // Custom
        }
    }

    private class TextFieldFormatter extends JFormattedTextField.AbstractFormatter {
        JFormattedTextField field;

        public void install(JFormattedTextField field) {
            super.install(field);
            this.field = field;
        }

        public void uninstall() {
            field = null;
        }

        // only positive (and zero) integer values
        public Object stringToValue(String text) throws ParseException {
            try {
                Integer value = new Integer(text);
                return value;
            } catch (NumberFormatException nfe) {
                Object value = field.getValue();
                field.setValue(value);
                return value;
            }
        }

        public String valueToString(Object value) throws ParseException {
            if (value == null) return "0";

            return value.toString();
        }
    }
}