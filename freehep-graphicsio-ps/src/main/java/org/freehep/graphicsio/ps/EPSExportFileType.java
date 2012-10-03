// Copyright 2000-2007, FreeHEP.
package org.freehep.graphicsio.ps;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JPanel;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsbase.util.UserProperties;

/**
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/EPSExportFileType.java bed5e3a39f35 2007/09/10 18:13:00 duns $
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

		return new EPSGraphics2D(os, target);
	}

	public VectorGraphics getGraphics(OutputStream os, Dimension dimension)
			throws IOException {

		return new EPSGraphics2D(os, dimension);
	}

}
