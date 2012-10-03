// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.freehep.graphics2d.PrintColor;
import org.freehep.graphicsbase.swing.layout.TableLayout;
import org.freehep.graphicsbase.util.UserProperties;
import org.freehep.graphicsio.PageConstants;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/BackgroundPanel.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class BackgroundPanel extends OptionPanel {

    private Color initialBackground;

    private Color background;

    private JColorChooser colorChooser;

    private OptionButton colorButton;

    private String key;

    public BackgroundPanel(Properties options, String rootKey,
            boolean hasTransparency) {
        this(options, rootKey, hasTransparency, "Background");
    }

    public BackgroundPanel(Properties options, String rootKey,
            boolean hasTransparency, String title) {
        super(title);

        key = rootKey + "." + PageConstants.BACKGROUND_COLOR;

        UserProperties user = new UserProperties(options);
        initialBackground = user.getPropertyColor(key);
        if (initialBackground == null)
            initialBackground = Color.WHITE;
        background = initialBackground;

        OptionCheckBox backgroundCheck = new OptionCheckBox(options, rootKey
                + "." + PageConstants.BACKGROUND, "Background");

        colorChooser = new JColorChooser(initialBackground);
        JDialog dialog = JColorChooser.createDialog(this,
                "Choose Background Color", true, colorChooser,
                new ChangeColorListener(), null);

        colorButton = new OptionButton(options, rootKey, "Select Color", dialog);

        backgroundCheck.enables(colorButton);

        String left = title == null ? TableLayout.VERY_LEFT : TableLayout.LEFT;
        String right = title == null ? TableLayout.VERY_RIGHT
                : TableLayout.RIGHT;
        if (hasTransparency) {
            OptionCheckBox transparentCheck = new OptionCheckBox(options,
                    rootKey + "." + PageConstants.TRANSPARENT, "Transparent");
            add(left, transparentCheck);
            add(right, new JLabel());
            transparentCheck.disables(backgroundCheck);
        }

        add(left, backgroundCheck);
        add(right, colorButton);

        // trigger initial setting
        new ChangeColorListener().actionPerformed(null);
    }

    public boolean applyChangedOptions(Properties options) {
        boolean changed = super.applyChangedOptions(options);

        if (!background.equals(initialBackground)) {
            UserProperties.setProperty(options, key, background);
            changed = true;
        }
        return changed;
    }

    class ChangeColorListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            background = colorChooser.getColor();
            colorButton.setBackground(background);
            colorButton.setForeground(PrintColor.invert(background));
        }
    }
}
