// Copyright 2001-2003, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.IOException;

import org.freehep.util.images.ImageUtilities;

/**
 * DefineBitsLossless2 TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineBitsLossless2.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineBitsLossless2 extends DefineBitsLossless {

    public DefineBitsLossless2(int id, Image image, Color bkg,
            ImageObserver observer) {
        this(id, ImageUtilities.createRenderedImage(image, observer, bkg), bkg);
    }

    public DefineBitsLossless2(int id, RenderedImage image, Color bkg) {
        this();
        character = id;
        this.image = image;
        this.bkg = bkg;
    }

    public DefineBitsLossless2() {
        super(36, 3);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {
        DefineBitsLossless2 tag = new DefineBitsLossless2();
        tag.read(tagID, swf, len, true);
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {

        write(tagID, swf, true);
    }
}
