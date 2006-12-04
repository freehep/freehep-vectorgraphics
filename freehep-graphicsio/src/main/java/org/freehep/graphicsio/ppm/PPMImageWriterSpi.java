// Copyright 2003-2006, FreeHEP
package org.freehep.graphicsio.ppm;

import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/ppm/PPMImageWriterSpi.java 381498a7681c 2006/12/04 15:41:17 duns $
 */
public class PPMImageWriterSpi extends ImageWriterSpi {

    public PPMImageWriterSpi() {
        super("FreeHEP Java Libraries, http://java.freehep.org/", "1.0",
                new String[] { "ppm" }, new String[] { "ppm" },
                new String[] { "image/x-portable-pixmap",
                        "image/x-portable-pixmap" },
                "org.freehep.graphicsio.ppm.PPMImageWriter",
                STANDARD_OUTPUT_TYPE, null, false, null, null, null, null,
                false, null, null, null, null);
    }

    public String getDescription(Locale locale) {
        return "FreeHEP UNIX Portable PixMap Format";
    }

    public ImageWriter createWriterInstance(Object extension)
            throws IOException {
        return new PPMImageWriter(this);
    }

    public boolean canEncodeImage(ImageTypeSpecifier type) {
        // FIXME
        return true;
    }
}
