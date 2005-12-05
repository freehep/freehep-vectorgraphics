// Copyright 2004, FreeHEP.
package org.freehep.graphicsio.latex;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.exportchooser.AbstractExportFileType;
import org.freehep.swing.ExtensionFileFilter;
import org.freehep.util.UserProperties;

/**
 *
 * @author Andre Bach
 * @version $Id: freehep-graphicsio-latex/src/main/java/org/freehep/graphicsio/latex/LatexExportFileType.java 937ca67e5f7c 2005/12/05 04:38:24 duns $
 */
public class LatexExportFileType extends AbstractExportFileType {

    public String getDescription() {
        return "LaTeX";
    }

    public String[] getExtensions() {
        return new String[] { "tex" };
    }
    
    public String[] getMIMETypes() {
        return new String[] { "image/tex" };
    }
    
    public VectorGraphics getGraphics(OutputStream os, Component target)
            throws IOException {
        // Not sure what the value of the boolean should be. Or add another constructor?
        return new LatexGraphics2D(os, target, false);
    }
}
