// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
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
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/EPSExportFileType.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class EPSExportFileType extends AbstractPSExportFileType {

    public String getDescription() {
        return "Encapsulated PostScript";
    }

    public String[] getExtensions() {
        return new String[] { "eps", "EPS", "epi", "EPI", "epsi", "EPSI",
                "epsf", "EPSF" };
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
