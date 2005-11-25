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
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/OptionPanel.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class OptionPanel extends JPanel implements Options {
    public OptionPanel() {
        this(null);
    }

    public OptionPanel(String title) {
        super(new TableLayout());
        if (title != null) setBorder(BorderFactory.createTitledBorder(
                                     BorderFactory.createEtchedBorder(),
                                     title));
    }

    public void setEnabled(boolean enable) {
        for (int i=0; i<getComponentCount(); i++) {
            Component c = getComponent(i);
            c.setEnabled(enable);
        }
    }

    public boolean applyChangedOptions(Properties options) {
        boolean changed = false;
        for (int i=0; i<getComponentCount(); i++) {
            Component c = getComponent(i);
            if (c instanceof Options) {
                boolean changedThis = ((Options)c).applyChangedOptions(options);
                changed = changed || changedThis;
            }
        }
        return changed;
    }
}
