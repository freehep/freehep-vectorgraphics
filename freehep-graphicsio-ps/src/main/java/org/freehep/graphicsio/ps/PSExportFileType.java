// Copyright 2003-2006, FreeHEP.
package org.freehep.graphicsio.ps;


/**
 * @author Mark Donszelmann
 * @author Charles Loomis, Simon Fischer
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/PSExportFileType.java 6fc90d16bd14 2006/11/30 18:48:36 duns $
 */
public class PSExportFileType extends AbstractPSExportFileType {

    public String getDescription() {
        return "PostScript";
    }

    public String[] getExtensions() {
        return new String[] { "ps" };
    }

    public boolean isMultipageCapable() {
        return true;
    }

}
