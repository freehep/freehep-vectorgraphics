// Copyright 2003, FreeHEP
package org.freehep.graphicsio.ppm;

import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;

/**
 * 
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/ppm/PPMImageWriterSpi.java 19ee023ce098 2006/12/04 07:44:04 duns $
 */
public class PPMImageWriterSpi extends ImageWriterSpi {

    public PPMImageWriterSpi() {
        super("FreeHEP Java Libraries, http://java.freehep.org/", "1.0",
                new String[] { "ppm", "PPM" }, new String[] { "ppm", "PPM" },
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