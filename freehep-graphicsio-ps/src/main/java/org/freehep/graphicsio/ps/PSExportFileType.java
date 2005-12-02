// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.ps;


/**
 * 
 * @author Charles Loomis, Simon Fischer
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/PSExportFileType.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class PSExportFileType extends AbstractPSExportFileType {

    public String getDescription() {
        return "PostScript";
    }

    public String[] getExtensions() {
        return new String[] { "ps", "PS" };
    }

    public boolean isMultipageCapable() {
        return true;
    }

}
