// Copyright 2000-2007, FreeHEP.
package org.freehep.graphicsio.ps;

import java.util.Properties;

import javax.swing.JPanel;

import org.freehep.util.UserProperties;

/**
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/EPSExportFileType.java 4c4708a97391 2007/06/12 22:32:31 duns $
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
}
