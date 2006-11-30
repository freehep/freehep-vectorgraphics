// Copyright 2000-2006, FreeHEP.
package org.freehep.graphicsio.ps;

import java.awt.Component;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JPanel;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.util.UserProperties;

/**
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/EPSExportFileType.java 6fc90d16bd14 2006/11/30 18:48:36 duns $
 */
public class EPSExportFileType extends AbstractPSExportFileType {

    public String getDescription() {
        return "Encapsulated PostScript";
    }

    public String[] getExtensions() {
        return new String[] { "eps", "epi", "epsi", "epsf" };
    }

    public JPanel createOptionPanel(Properties user) {
        UserProperties options = new UserProperties(user, PSGraphics2D
                .getDefaultProperties());
        JPanel panel = super.createOptionPanel(options);
        preview.setVisible(true);
        return panel;
    }

    public VectorGraphics getGraphics(OutputStream os, Component target)
            throws IOException {

        return new PSGraphics2D(os, target);
    }
}
