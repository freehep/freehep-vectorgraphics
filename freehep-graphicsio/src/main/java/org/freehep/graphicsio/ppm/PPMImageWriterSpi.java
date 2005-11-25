// Copyright 2003, FreeHEP
package org.freehep.graphicsio.ppm;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.spi.*;


/**
 *
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/ppm/PPMImageWriterSpi.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class PPMImageWriterSpi extends ImageWriterSpi {

    public PPMImageWriterSpi() {
        super(
            "FreeHEP Java Libraries, http://java.freehep.org/",
            "1.0",
            new String[] {"ppm", "PPM"},
            new String[] {"ppm", "PPM"},
            new String[] {"image/x-portable-pixmap", "image/x-portable-pixmap"},
            "org.freehep.graphicsio.ppm.PPMImageWriter",
            STANDARD_OUTPUT_TYPE,
            null,
            false, null, null, null, null,
            false, null, null, null, null
        );
    }

    public String getDescription(Locale locale) {
        return "UNIX Portable PixMap";
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