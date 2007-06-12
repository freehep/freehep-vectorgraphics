// Copyright 2003-2007, FreeHEP.
package org.freehep.graphicsio.ps;


/**
 * @author Mark Donszelmann
 * @author Charles Loomis, Simon Fischer
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/PSExportFileType.java 4c4708a97391 2007/06/12 22:32:31 duns $
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
