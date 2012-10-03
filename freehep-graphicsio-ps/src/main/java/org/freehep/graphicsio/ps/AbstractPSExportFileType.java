// Copyright 2003-2009, FreeHEP.
package org.freehep.graphicsio.ps;

import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.freehep.graphicsbase.swing.layout.TableLayout;
import org.freehep.graphicsbase.util.UserProperties;
import org.freehep.graphicsio.AbstractVectorGraphicsIO;
import org.freehep.graphicsio.ImageConstants;
import org.freehep.graphicsio.InfoConstants;
import org.freehep.graphicsio.exportchooser.AbstractExportFileType;
import org.freehep.graphicsio.exportchooser.BackgroundPanel;
import org.freehep.graphicsio.exportchooser.FontPanel;
import org.freehep.graphicsio.exportchooser.ImageTypePanel;
import org.freehep.graphicsio.exportchooser.InfoPanel;
import org.freehep.graphicsio.exportchooser.OptionCheckBox;
import org.freehep.graphicsio.exportchooser.OptionComboBox;
import org.freehep.graphicsio.exportchooser.OptionPanel;
import org.freehep.graphicsio.exportchooser.PageLayoutPanel;
import org.freehep.graphicsio.exportchooser.PageMarginPanel;

/**
 * 
 * @author Charles Loomis, Simon Fischer
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/AbstractPSExportFileType.java bed5e3a39f35 2007/09/10 18:13:00 duns $
 */
public abstract class AbstractPSExportFileType extends AbstractExportFileType {

	protected static final String bitsList[] = { "1", "2", "4", "8" };

	protected OptionPanel preview;

	protected OptionCheckBox previewCheckBox;

	public boolean hasOptionPanel() {
		return true;
	}

	public String[] getMIMETypes() {
		return new String[] { "application/postscript" };
	}

	public JPanel createOptionPanel(Properties user) {
		UserProperties options = new UserProperties(user, PSGraphics2D
				.getDefaultProperties());
		preview = new OptionPanel("Preview Image");
		previewCheckBox = new OptionCheckBox(options, PSGraphics2D.PREVIEW,
				"Include preview");

		preview.add(TableLayout.FULL, previewCheckBox);

		final JLabel previewLabel = new JLabel("Bits per sample");
		preview.add(TableLayout.LEFT, previewLabel);
		previewCheckBox.enables(previewLabel);

		final OptionComboBox previewComboBox = new OptionComboBox(options,
				PSGraphics2D.PREVIEW_BITS, bitsList);
		preview.add(TableLayout.RIGHT, previewComboBox);
		previewCheckBox.enables(previewComboBox);
		preview.setVisible(false);

		// rootKeys for FontProperties
		String rootKey = PSGraphics2D.class.getName();
		String abstractRootKey = AbstractVectorGraphicsIO.class.getName();

		JPanel infoPanel = new InfoPanel(options, rootKey, new String[] {
				InfoConstants.FOR, InfoConstants.TITLE });

		// TableLayout.LEFT Panel
		JPanel leftPanel = new OptionPanel();
		leftPanel
				.add(TableLayout.COLUMN, new PageLayoutPanel(options, rootKey));
		leftPanel
				.add(TableLayout.COLUMN, new PageMarginPanel(options, rootKey));
		leftPanel.add(TableLayout.COLUMN_FILL, new JLabel());

		// TableLayout.RIGHT Panel
		JPanel rightPanel = new OptionPanel();
		rightPanel.add(TableLayout.COLUMN, new BackgroundPanel(options,
				rootKey, false));
		rightPanel.add(TableLayout.COLUMN, preview);
		rightPanel.add(TableLayout.COLUMN, new ImageTypePanel(options, rootKey,
				new String[] { ImageConstants.SMALLEST, ImageConstants.ZLIB,
						ImageConstants.JPG }));
		rightPanel.add(TableLayout.COLUMN, new FontPanel(options, rootKey,
				abstractRootKey));
		rightPanel.add(TableLayout.COLUMN_FILL, new JLabel());

		// Make the full panel.
		OptionPanel optionsPanel = new OptionPanel();
		optionsPanel.add("0 0 [5 5 5 5] wt", leftPanel);
		optionsPanel.add("1 0 [5 5 5 5] wt", rightPanel);
		optionsPanel.add("0 1 2 1 [5 5 5 5] wt", infoPanel);
		optionsPanel.add(TableLayout.COLUMN_FILL, new JLabel());

		return optionsPanel;
	}
}
