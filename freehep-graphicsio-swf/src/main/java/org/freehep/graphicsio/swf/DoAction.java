// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;
import java.util.Vector;

import org.freehep.util.io.Action;

/**
 * DoAction TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DoAction.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DoAction extends ControlTag {

    private Vector actions;

    public DoAction(Vector actions) {
        this();
        this.actions = actions;
    }

    public DoAction() {
        super(12, 1);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DoAction tag = new DoAction();
        tag.actions = new Vector();
        Action action = swf.readAction();
        while (action != null) {
            tag.actions.add(action);
            action = swf.readAction();
        }
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        for (int i = 0; i < actions.size(); i++) {
            Action a = (Action) actions.get(i);
            swf.writeAction(a);
        }
        swf.writeAction(null);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        for (int i = 0; i < actions.size(); i++) {
            s.append("  " + actions.get(i) + "\n");
        }
        return s.toString();
    }
}
