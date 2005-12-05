// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

/**
 * Abstract Control TAG. All control tags derive from this tag.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/ControlTag.java db861da05344 2005/12/05 00:59:43 duns $
 */
public abstract class ControlTag extends SWFTag {

    protected ControlTag(int tagID, int version) {
        super(tagID, version);
    }

    public int getTagType() {
        return CONTROL;
    }

    public String toString() {
        return "Control: " + getName() + " (" + getTag() + ")";
    }

}
