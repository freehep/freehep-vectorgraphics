// Copyright 2000-2003 FreeHEP
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
 * @version $Id: freehep-graphicsio-java/src/main/java/org/freehep/graphicsio/java/JAVAExportFileType.java 01c7a0af1b3a 2005/12/05 06:02:47 duns $
 */
public class JAVAExportFileType extends AbstractExportFileType {

    public String getDescription() {
        return "Java Source File (for Testing)";
    }

    public String[] getExtensions() {
        return new String[] { "java", "JAVA" };
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
