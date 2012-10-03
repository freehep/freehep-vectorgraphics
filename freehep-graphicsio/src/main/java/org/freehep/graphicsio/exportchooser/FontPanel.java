// Copyright 2003-2007, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.awt.Component;
import java.util.Properties;

import org.freehep.graphicsbase.swing.layout.TableLayout;
import org.freehep.graphicsio.FontConstants;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/FontPanel.java 59372df5e0d9 2007/02/06 21:11:19 duns $
 */
public class FontPanel extends OptionPanel {

    /**
     * defines the optionpanel for font embedding and text as shapes.
     *
     * @param options
     * @param embeddingRootKey rootkey for {@link String abstractRootKey = AbstractVectorGraphicsIO.class.getName();#EMBED_FONTS} and {@link FontConstants#EMBED_FONTS_AS}
     * @param shapeRootKey rootkey for {@link FontConstants#TEXT_AS_SHAPES}
     */
    public FontPanel(Properties options, String embeddingRootKey, String shapeRootKey) {
        super("Fonts");

        // to disable / enable for TEXT_AS_SHAPES
        Component enable = null;

        // font embedding
        if (embeddingRootKey != null) {
            final OptionCheckBox checkBox = new OptionCheckBox(
                options,
                embeddingRootKey + "." + FontConstants.EMBED_FONTS,
                "Embed Fonts as");
            add(TableLayout.LEFT, checkBox);

            final OptionComboBox comboBox = new OptionComboBox(
                options,
                embeddingRootKey + "." + FontConstants.EMBED_FONTS_AS,
                FontConstants .getEmbedFontsAsList());
            add(TableLayout.RIGHT, comboBox);
            checkBox.enables(comboBox);

            enable = checkBox;
        }

        // text es shape
        if (shapeRootKey != null) {
            final OptionCheckBox shapeCheckBox = new OptionCheckBox(
                options,
                shapeRootKey + "." + FontConstants.TEXT_AS_SHAPES,
                "Draw text as shapes");
            add(TableLayout.FULL, shapeCheckBox);
            
            if (enable != null) {
                shapeCheckBox.disables(enable);
            }
        }
    }
}
