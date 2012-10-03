// Copyright 2003-2007, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsbase.swing.layout.TableLayout;
import org.freehep.graphicsbase.util.UserProperties;
import org.freehep.graphicsio.ImageConstants;
import org.freehep.graphicsio.exportchooser.AbstractExportFileType;
import org.freehep.graphicsio.exportchooser.BackgroundPanel;
import org.freehep.graphicsio.exportchooser.ImageTypePanel;
import org.freehep.graphicsio.exportchooser.OptionPanel;

/**
 * // FIXME, check all options
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/SWFExportFileType.java 4c4708a97391 2007/06/12 22:32:31 duns $
 */
public class SWFExportFileType extends AbstractExportFileType {

	public String getDescription() {
		return "MacroMedia Flash File Format";
	}

	public String[] getExtensions() {
		return new String[] { "swf" };
	}

	public String[] getMIMETypes() {
		return new String[] { "application/x-shockwave-flash" };
	}

	public boolean hasOptionPanel() {
		return true;
	}

	public JPanel createOptionPanel(Properties user) {
		UserProperties options = new UserProperties(user, SWFGraphics2D
				.getDefaultProperties());

		String rootKey = SWFGraphics2D.class.getName();

		// Make the full panel.
		OptionPanel optionsPanel = new OptionPanel();
		optionsPanel.add("0 0 [5 5 5 5] wt", new BackgroundPanel(options,
				rootKey, true));
		optionsPanel.add("0 1 [5 5 5 5] wt", new ImageTypePanel(options,
				rootKey, new String[] { ImageConstants.SMALLEST,
						ImageConstants.ZLIB, ImageConstants.JPG }));
		optionsPanel.add(TableLayout.COLUMN_FILL, new JLabel());

		return optionsPanel;
	}

	public VectorGraphics getGraphics(OutputStream os, Component target)
			throws IOException {

		return new SWFGraphics2D(os, target);
	}

	public VectorGraphics getGraphics(OutputStream os, Dimension dimension)
			throws IOException {

		return new SWFGraphics2D(os, dimension);
	}

}
