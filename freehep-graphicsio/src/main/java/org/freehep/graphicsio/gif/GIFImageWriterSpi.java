// Copyright 2003, FreeHEP
package org.freehep.graphicsio.gif;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.spi.*;


/**
 *
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/gif/GIFImageWriterSpi.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class GIFImageWriterSpi extends ImageWriterSpi {

    public GIFImageWriterSpi() {
        super(
            "FreeHEP Java Libraries, http://java.freehep.org/",
            "1.0",
            new String[] {"gif", "GIF"},
            new String[] {"gif", "GIF"},
            new String[] {"image/gif", "image/x-gif"},
            "org.freehep.graphicsio.gif.GIFImageWriter",
            STANDARD_OUTPUT_TYPE,
            null,
            false, null, null, null, null,
            false, null, null, null, null
        );
    }

    public String getDescription(Locale locale) {
        return "Graphics Interchange Format";
    }

    public ImageWriter createWriterInstance(Object extension)
                                          throws IOException {
        return new GIFImageWriter(this);
    }

    public boolean canEncodeImage(ImageTypeSpecifier type) {
        // FIXME handle # colors
        return true;
    }
}