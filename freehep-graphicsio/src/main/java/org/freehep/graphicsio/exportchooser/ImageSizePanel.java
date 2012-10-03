// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import org.freehep.graphics2d.ScreenConstants;
import org.freehep.graphicsbase.swing.layout.TableLayout;
import org.freehep.graphicsbase.util.UserProperties;
import org.freehep.graphicsio.ImageConstants;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/ImageSizePanel.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class ImageSizePanel extends OptionPanel {

    final private static String imageSizeList[] = { "ImageSize", "Custom",
            ScreenConstants.VGA, ScreenConstants.SVGA, ScreenConstants.XGA,
            ScreenConstants.SXGA, ScreenConstants.SXGA_PLUS,
            ScreenConstants.UXGA };

    private String key;

    private Dimension initialDimension;

    private JComboBox imageSizeCombo;

    private JFormattedTextField imageWidth, imageHeight;

    public ImageSizePanel(Properties user, String rootKey) {
        super("Image Size");
        key = rootKey + "." + ImageConstants.IMAGE_SIZE;

        UserProperties options = new UserProperties(user);
        initialDimension = options.getPropertyDimension(key);

        imageSizeCombo = new JComboBox(imageSizeList);
        add(TableLayout.LEFT, new JLabel("Preset Sizes"));
        add(TableLayout.RIGHT, imageSizeCombo);

        add(TableLayout.LEFT, new JLabel("Width"));
        imageWidth = new JFormattedTextField(new TextFieldFormatter());
        imageWidth.setColumns(10);
        add(TableLayout.RIGHT, imageWidth);

        add(TableLayout.LEFT, new JLabel("Height"));
        imageHeight = new JFormattedTextField(new TextFieldFormatter());
        imageHeight.setColumns(10);
        add(TableLayout.RIGHT, imageHeight);

        imageSizeCombo.addItemListener(new ComboListener());
        imageWidth.addActionListener(new TextFieldListener());
        imageHeight.addActionListener(new TextFieldListener());

        // now set the initial values
        imageWidth.setValue(new Integer(initialDimension.width));
        imageHeight.setValue(new Integer(initialDimension.height));

        // trigger the changes
        new TextFieldListener().actionPerformed(null);
        new ComboListener().itemStateChanged(null);
    }

    public boolean applyChangedOptions(Properties options) {
        boolean changed = false;

        Dimension size = new Dimension(((Number) imageWidth.getValue())
                .intValue(), ((Number) imageHeight.getValue()).intValue());

        if (!size.equals(initialDimension)) {
            options.setProperty(key, size.width + ", " + size.height);
            changed = true;
        }

        return changed;
    }

    private class ComboListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            int index = imageSizeCombo.getSelectedIndex();
            switch (index) {
            case 0: // ImageSize
                imageWidth.setEnabled(false);
                imageHeight.setEnabled(false);
                imageWidth.setValue(new Integer(0));
                imageHeight.setValue(new Integer(0));
                break;
            case 1: // Custom
                imageWidth.setEnabled(true);
                imageHeight.setEnabled(true);
                break;
            default: // any other preset value
                imageWidth.setEnabled(true);
                imageHeight.setEnabled(true);
                Dimension d = ScreenConstants.getSize(imageSizeList[index]);
                imageWidth.setValue(new Integer(d.width));
                imageHeight.setValue(new Integer(d.height));
                break;
            }
        }
    }

    private class TextFieldListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            int width = ((Number) imageWidth.getValue()).intValue();
            int height = ((Number) imageHeight.getValue()).intValue();

            if ((width == 0) && (height == 0)) {
                imageSizeCombo.setSelectedIndex(0); // ImageSize
                return;
            }

            for (int i = 2; i < imageSizeList.length; i++) {
                Dimension d = ScreenConstants.getSize(imageSizeList[i]);
                if ((width == d.width) && (height == d.height)) {
                    imageSizeCombo.setSelectedIndex(i);
                    return;
                }
            }

            imageSizeCombo.setSelectedIndex(1); // Custom
        }
    }

    private class TextFieldFormatter extends
            JFormattedTextField.AbstractFormatter {
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
                if (value.intValue() < 0)
                    throw new NumberFormatException();
                return value;
            } catch (NumberFormatException nfe) {
                Object value = field.getValue();
                field.setValue(value);
                return value;
            }
        }

        public String valueToString(Object value) throws ParseException {
            if (value == null)
                return "0";

            return value.toString();
        }
    }
}