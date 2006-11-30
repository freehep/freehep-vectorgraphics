// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.png;

import org.freehep.graphicsio.exportchooser.ImageExportFileType;

/**
 * 
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/png/PNGExportFileType.java 6fc90d16bd14 2006/11/30 18:48:36 duns $
 */
public class PNGExportFileType extends ImageExportFileType {

    public PNGExportFileType() {
        super("png");
    }

    public String[] getExtensions() {
        return new String[] { "png" };
    }
}
