// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.util.Properties;

import org.freehep.graphicsio.FontConstants;
import org.freehep.swing.layout.TableLayout;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/FontPanel.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class FontPanel extends OptionPanel {
    public FontPanel(Properties options, String rootKey) {
        super("Fonts");

        final OptionCheckBox checkBox = new OptionCheckBox(options, rootKey
                + "." + FontConstants.EMBED_FONTS, "Embed Fonts as");
        add(TableLayout.LEFT, checkBox);
        final OptionComboBox comboBox = new OptionComboBox(options, rootKey
                + "." + FontConstants.EMBED_FONTS_AS, FontConstants
                .getEmbedFontsAsList());
        add(TableLayout.RIGHT, comboBox);
        checkBox.enables(comboBox);
    }
}
