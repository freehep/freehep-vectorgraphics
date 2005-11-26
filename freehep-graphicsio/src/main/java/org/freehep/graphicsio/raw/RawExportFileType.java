// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.raw;

import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;

import org.freehep.graphicsio.exportchooser.ImageExportFileType;

/**
 * 
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/raw/RawExportFileType.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class RawExportFileType extends ImageExportFileType {

    static {
        try {
            Class clazz = Class
                    .forName("org.freehep.graphicsio.raw.RawImageWriterSpi");
            IIORegistry.getDefaultInstance().registerServiceProvider(
                    clazz.newInstance(), ImageWriterSpi.class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public RawExportFileType() {
        super("raw");
    }
}
