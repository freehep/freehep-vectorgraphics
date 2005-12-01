// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.ps;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.freehep.swing.ExtensionFileFilter;
import org.freehep.util.UserProperties;

/**
 *
 * @author Charles Loomis, Simon Fischer
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/PSExportFileType.java 2689041eec29 2005/12/01 22:37:27 duns $
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
