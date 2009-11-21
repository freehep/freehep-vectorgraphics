// Copyright 2003-2006, FreeHEP.
package org.freehep.graphicsio.raw;

import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;

import org.freehep.graphicsio.exportchooser.ImageExportFileType;

/**
 * 
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/raw/RawExportFileType.java 12103cee2b7c 2006/11/12 16:30:03 duns $
 */
public class RawExportFileType extends ImageExportFileType {

    static {
        try {
            Class<?> clazz = Class
                    .forName("org.freehep.graphicsio.raw.RawImageWriterSpi");
            IIORegistry.getDefaultInstance().registerServiceProvider(
                    (ImageWriterSpi)clazz.newInstance(), ImageWriterSpi.class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public RawExportFileType() {
        super("raw");
    }
}
