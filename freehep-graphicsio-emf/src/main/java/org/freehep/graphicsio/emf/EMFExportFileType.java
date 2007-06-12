// Copyright 2000-2007, FreeHEP
package org.freehep.graphicsio.emf;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.AbstractVectorGraphicsIO;
import org.freehep.graphicsio.exportchooser.AbstractExportFileType;
import org.freehep.graphicsio.exportchooser.BackgroundPanel;
import org.freehep.graphicsio.exportchooser.FontPanel;
import org.freehep.graphicsio.exportchooser.OptionPanel;
import org.freehep.swing.layout.TableLayout;
import org.freehep.util.UserProperties;

/**
 * // FIXME, check all options
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFExportFileType.java 4c4708a97391 2007/06/12 22:32:31 duns $
 */
public class EMFExportFileType extends AbstractExportFileType {

	// private OptionCheckBox compress;

	public String getDescription() {
		return "Windows Enhanced Metafile";
	}

	public String[] getExtensions() {
		return new String[] { "emf" };
	}

	public String[] getMIMETypes() {
		return new String[] { "image/x-emf" };
	}

	public boolean hasOptionPanel() {
		return true;
	}

	public JPanel createOptionPanel(Properties user) {
		UserProperties options = new UserProperties(user, EMFGraphics2D
				.getDefaultProperties());

		String rootKey = EMFGraphics2D.class.getName();
		String abstractRootKey = AbstractVectorGraphicsIO.class.getName();

		// Make the full panel.
		OptionPanel optionsPanel = new OptionPanel();
		optionsPanel.add("0 0 [5 5 5 5] wt", new BackgroundPanel(options,
				rootKey, true));

		optionsPanel.add("0 1 [5 5 5 5] wt", new FontPanel(options, null,
				abstractRootKey));

		optionsPanel.add(TableLayout.COLUMN_FILL, new JLabel());

		return optionsPanel;
	}

	public VectorGraphics getGraphics(OutputStream os, Component target)
			throws IOException {

		return new EMFGraphics2D(os, target);
	}

	public VectorGraphics getGraphics(OutputStream os, Dimension dimension)
			throws IOException {

		return new EMFGraphics2D(os, dimension);
	}
}
