// Copyright 2003, FreeHEP
package org.freehep.graphicsio.raw;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.spi.*;


/**
 *
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/raw/RawImageWriterSpi.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class RawImageWriterSpi extends ImageWriterSpi {

    public RawImageWriterSpi() {
        super(
            "FreeHEP Java Libraries, http://java.freehep.org/",
            "1.0",
            new String[] {"raw"},
            new String[] {"raw"},
            new String[] {"image/x-raw"},
            "org.freehep.graphicsio.raw.RawImageWriter",
            STANDARD_OUTPUT_TYPE,
            null,
            false, null, null, null, null,
            false, null, null, null, null
        );
    }

    public String getDescription(Locale locale) {
        return "RAW image";
    }

    public ImageWriter createWriterInstance(Object extension)
                                          throws IOException {
        return new RawImageWriter(this);
    }

    public boolean canEncodeImage(ImageTypeSpecifier type) {
        // FIXME
        return true;
    }
}