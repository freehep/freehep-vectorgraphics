// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import org.freehep.util.io.TagSet;

/**
 * Defines the tags in the MiniTag (Sprite) structure
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SWFSpriteTagSet.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class SWFSpriteTagSet extends TagSet {

    private int version;

    public SWFSpriteTagSet(int version) {

        super();
        this.version = version;

        // Initialize tags
        // Version 1
        if (version >= 1) {
            addTag(new End()); // 0
            addTag(new ShowFrame()); // 1
            addTag(new PlaceObject()); // 4
            addTag(new RemoveObject()); // 5
            addTag(new DoAction()); // 12
            addTag(new StartSound()); // 15
            addTag(new SoundStreamHead()); // 18
            addTag(new SoundStreamBlock()); // 19
        }

        // Version 2
        if (version >= 2) {
        }

        // Version 3
        if (version >= 3) {
            addTag(new PlaceObject2()); // 26
            addTag(new RemoveObject2()); // 28
            addTag(new FrameLabel()); // 43
        }

        // version 4
        if (version >= 4) {
        }

        // version 5
        if (version >= 5) {
        }

        // version 6
        if (version >= 6) {
        }
    }

    public int getVersion() {
        return version;
    }
}
