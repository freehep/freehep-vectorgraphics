// Copyright 2000-2006 FreeHEP
package org.freehep.graphicsio.java;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.exportchooser.AbstractExportFileType;

/**
 * // FIXME, check all options
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-java/src/main/java/org/freehep/graphicsio/java/JAVAExportFileType.java 6fc90d16bd14 2006/11/30 18:48:36 duns $
 */
public class JAVAExportFileType extends AbstractExportFileType {

    public String getDescription() {
        return "Java Source File (for Testing)";
    }

    public String[] getExtensions() {
        return new String[] { "java" };
    }

    public String[] getMIMETypes() {
        return new String[] { "application/java" };
    }

    public boolean hasOptionPanel() {
        return false;
    }

    public VectorGraphics getGraphics(File file, Component target)
            throws IOException {

        return new JAVAGraphics2D(file, target);
    }

    public VectorGraphics getGraphics(OutputStream os, Component target)
            throws IOException {

        return new JAVAGraphics2D(os, target);
    }

}
