// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.exportchooser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

import org.freehep.graphicsio.PageConstants;
import org.freehep.swing.layout.TableLayout;

/**
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/InfoPanel.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class InfoPanel extends OptionPanel {
    public InfoPanel(Properties options, String rootKey, String[] keys) {
        super("Info");

        for (int i=0; i<keys.length; i++) {
            add(TableLayout.LEFT,  new JLabel(keys[i]));
            add(TableLayout.RIGHT, new OptionTextField(options,
                                            rootKey+"."+keys[i], 40));
        }
    }
}
