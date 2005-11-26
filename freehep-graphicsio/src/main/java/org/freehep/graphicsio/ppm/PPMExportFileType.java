// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphicsio.ppm;

import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;

import org.freehep.graphicsio.exportchooser.ImageExportFileType;

/**
 * 
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/ppm/PPMExportFileType.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class PPMExportFileType extends ImageExportFileType {

    static {
        try {
            Class clazz = Class
                    .forName("org.freehep.graphicsio.ppm.PPMImageWriterSpi");
            IIORegistry.getDefaultInstance().registerServiceProvider(
                    clazz.newInstance(), ImageWriterSpi.class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public PPMExportFileType() {
        super("ppm");
    }

}
