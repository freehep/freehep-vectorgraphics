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
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/OptionButton.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class OptionButton extends JButton implements Options {

    protected String key;

    public OptionButton(Properties options, String key, String text, final JDialog dialog) {
        super(text);
        this.key = key;
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dialog.setVisible(true);
                dialog.dispose();
            }
        });
    }

    public boolean applyChangedOptions(Properties options) {
        return false;
    }


}
