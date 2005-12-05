// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

/**
 * Abstract Definition TAG. All definition tags inherit from this class.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefinitionTag.java db861da05344 2005/12/05 00:59:43 duns $
 */
public abstract class DefinitionTag extends SWFTag {

    protected DefinitionTag(int tagID, int version) {
        super(tagID, version);
    }

    public int getTagType() {
        return DEFINITION;
    }

    public String toString() {
        return "Definition: " + getName() + " (" + getTag() + ")";
    }

}
