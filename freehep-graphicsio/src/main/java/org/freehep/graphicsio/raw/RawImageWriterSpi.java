// Copyright 2003, FreeHEP
package org.freehep.graphicsio.raw;

import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;

/**
 * 
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/raw/RawImageWriterSpi.java 19ee023ce098 2006/12/04 07:44:04 duns $
 */
public class RawImageWriterSpi extends ImageWriterSpi {

    public RawImageWriterSpi() {
        super("FreeHEP Java Libraries, http://java.freehep.org/", "1.0",
                new String[] { "raw" }, new String[] { "raw" },
                new String[] { "image/x-raw" },
                "org.freehep.graphicsio.raw.RawImageWriter",
                STANDARD_OUTPUT_TYPE, null, false, null, null, null, null,
                false, null, null, null, null);
    }

    public String getDescription(Locale locale) {
        return "FreeHEP RAW Image Format";
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