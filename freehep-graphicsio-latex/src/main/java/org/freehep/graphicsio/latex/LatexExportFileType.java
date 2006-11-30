// Copyright 2004-2006, FreeHEP.
package org.freehep.graphicsio.latex;

import java.awt.Component;
import java.io.IOException;
import java.io.OutputStream;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.exportchooser.AbstractExportFileType;

/**
 * 
 * @author Andre Bach
 * @version $Id: freehep-graphicsio-latex/src/main/java/org/freehep/graphicsio/latex/LatexExportFileType.java 6fc90d16bd14 2006/11/30 18:48:36 duns $
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
        // Not sure what the value of the boolean should be. Or add another
        // constructor?
        return new LatexGraphics2D(os, target, false);
    }
}
