// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;
import java.util.Vector;

import org.freehep.util.io.Action;

/**
 * SWF Button Condition Action
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/ButtonCondAction.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class ButtonCondAction {

    private int condition;

    private Vector actions;

    public ButtonCondAction(int condition, Vector actions) {
        this.condition = condition;
        this.actions = actions;
    }

    public ButtonCondAction(SWFInputStream input) throws IOException {

        condition = input.readUnsignedShort();

        actions = new Vector();
        Action action = input.readAction();
        while (action != null) {
            actions.add(action);
            action = input.readAction();
        }
    }

    public void write(SWFOutputStream swf) throws IOException {
        swf.writeUnsignedShort(condition);

        for (int i = 0; i < actions.size(); i++) {
            Action a = (Action) actions.get(i);
            swf.writeAction(a);
        }
        swf.writeAction(null);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("ButtonCondAction " + condition + "\n");
        s.append("    actions: " + actions.size() + "\n");
        for (int i = 0; i < actions.size(); i++) {
            s.append("      " + actions.get(i) + "\n");
        }
        return s.toString();
    }
}
