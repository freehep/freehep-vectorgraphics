// Copyright 2000-2007, FreeHEP
package org.freehep.graphicsio.exportchooser;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsbase.swing.layout.TableLayout;
import org.freehep.graphicsbase.util.UserProperties;
import org.freehep.graphicsio.ImageConstants;
import org.freehep.graphicsio.ImageGraphics2D;

/**
 * // FIXME, check all options
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/ImageExportFileType.java 4c4708a97391 2007/06/12 22:32:31 duns $
 */
public class ImageExportFileType extends AbstractExportFileType {

	protected String format;

	protected ImageWriterSpi spi;

	protected ImageWriteParam param;

	protected OptionCheckBox antialias;

	protected OptionCheckBox antialiasText;

	protected OptionCheckBox progressive;

	protected OptionCheckBox compress;

	protected OptionComboBox compressMode;

	protected OptionComboBox compressDescription;

	protected OptionTextField compressQuality;

	protected ImageExportFileType(String format) {
		Iterator<?> iterator = ImageIO.getImageWritersByFormatName(format);
		if (iterator.hasNext()) {
			ImageWriter writer = (ImageWriter) iterator.next();
			this.format = format;
			this.spi = writer.getOriginatingProvider();
			this.param = writer.getDefaultWriteParam();
			return;
		}
		throw new IllegalArgumentException(getClass() + ": Format not valid: "
				+ format);

	}

	public ImageExportFileType(ImageWriterSpi spi) {
		this.format = spi.getFormatNames()[0];
		this.spi = spi;
		try {
			this.param = spi.createWriterInstance().getDefaultWriteParam();
		} catch (IOException e) {
			throw new RuntimeException("Failed to create Writer instance", e);
		}
	}

	// FIXME only based on spi class name.
	public boolean equals(Object obj) {
		if (obj instanceof ImageExportFileType) {
			ImageExportFileType type = (ImageExportFileType) obj;
			return spi.getClass().equals(type.spi.getClass());
		}
		return super.equals(obj);
	}

	// FIXME only based on spi class name.
	public int hashCode() {
		return spi.getClass().hashCode();
	}

	public static ImageExportFileType getInstance(String format) {
		format = format.toLowerCase();
		if (format.equals(ImageConstants.GIF.toLowerCase()))
			return exportFileType("org.freehep.graphicsio.gif.GIFExportFileType");
		if (format.equals(ImageConstants.PNG.toLowerCase()))
			return exportFileType("org.freehep.graphicsio.png.PNGExportFileType");
		if (format.equals(ImageConstants.JPG.toLowerCase()))
			return exportFileType("org.freehep.graphicsio.jpg.JPGExportFileType");
		if (format.equals(ImageConstants.RAW.toLowerCase()))
			return exportFileType("org.freehep.graphicsio.raw.RawExportFileType");
		if (format.equals(ImageConstants.BMP.toLowerCase()))
			return exportFileType("org.freehep.graphicsio.bmp.BMPExportFileType");
		if (format.equals(ImageConstants.WBMP.toLowerCase()))
			return exportFileType("org.freehep.graphicsio.wbmp.WBMPExportFileType");
		return null;
	}

	private static ImageExportFileType exportFileType(String className) {
		try {
			Class<?> clazz = Class.forName(className);
			return (ImageExportFileType) clazz.newInstance();
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public String getDescription() {
		return spi.getDescription(Locale.getDefault());
	}

	public String[] getExtensions() {
		return spi.getFileSuffixes();
	}

	public String[] getMIMETypes() {
		return spi.getMIMETypes();
	}

	public boolean hasOptionPanel() {
		return true;
	}

	public JPanel createOptionPanel(Properties user) {
		UserProperties options = new UserProperties(user, ImageGraphics2D
				.getDefaultProperties(format));
		OptionPanel panel = new OptionPanel(format.toUpperCase() + " Format");

		String formatKey = ImageGraphics2D.rootKey + "." + format;

		panel.add(TableLayout.FULL, new BackgroundPanel(options, formatKey,
				ImageGraphics2D.canWriteTransparent(format), null));

		antialias = new OptionCheckBox(options, formatKey
				+ ImageGraphics2D.ANTIALIAS, "Antialias");
		panel.add(TableLayout.FULL, antialias);

		antialiasText = new OptionCheckBox(options, formatKey
				+ ImageGraphics2D.ANTIALIAS_TEXT, "Antialias Text");
		panel.add(TableLayout.FULL, antialiasText);

		progressive = new OptionCheckBox(options, formatKey
				+ ImageGraphics2D.PROGRESSIVE, "Progressive");
		if (param.canWriteProgressive()) {
			panel.add(TableLayout.FULL, progressive);
		}

		compress = new OptionCheckBox(options, formatKey
				+ ImageGraphics2D.COMPRESS, "Compress");
		if (param.canWriteCompressed()) {
			if (ImageGraphics2D.canWriteUncompressed(format)) {
				panel.add(TableLayout.FULL, compress);

				// NOTE: force compression
				param
						.setCompressionMode(options.isProperty(formatKey
								+ ImageGraphics2D.COMPRESS) ? ImageWriteParam.MODE_EXPLICIT
								: ImageWriteParam.MODE_DISABLED);
			}
		}

		if (param.canWriteCompressed()
				&& (param.getCompressionMode() == ImageWriteParam.MODE_EXPLICIT)) {

			String[] compressionTypes = param.getCompressionTypes();
			JLabel compressModeLabel = new JLabel("Compression Mode");
			compressMode = new OptionComboBox(options, formatKey
					+ ImageGraphics2D.COMPRESS_MODE, compressionTypes);

			if (compressionTypes.length > 1) {
				panel.add(TableLayout.LEFT, compressModeLabel);
				panel.add(TableLayout.RIGHT, compressMode);
				compress.enables(compressModeLabel);
				compress.enables(compressMode);
			}
			/*
			 * FIXME to be connected to the rest String[]
			 * compressionDescriptions =
			 * param.getCompressionQualityDescriptions(); JLabel
			 * compressDescriptionLabel = new JLabel("Quality Mode");
			 * compressDescription = new OptionComboBox(options,
			 * formatKey+ImageGraphics2D.COMPRESS_DESCRIPTION,
			 * compressionDescriptions);
			 * 
			 * if (compressionDescriptions.length > 1) {
			 * panel.add(TableLayout.LEFT, compressDescriptionLabel);
			 * panel.add(TableLayout.RIGHT, compressDescription);
			 * compress.enables(compressDescriptionLabel);
			 * compress.enables(compressDescription); }
			 */

			// FIXME check value
			JLabel compressQualityLabel = new JLabel("Quality Value");
			panel.add(TableLayout.LEFT, compressQualityLabel);

			compressQuality = new OptionTextField(options, formatKey
					+ ImageGraphics2D.COMPRESS_QUALITY, 5);
			panel.add(TableLayout.RIGHT, compressQuality);
			compress.enables(compressQualityLabel);
			compress.enables(compressQuality);

			// FIXME add slider
		}

		return panel;
	}

	public VectorGraphics getGraphics(OutputStream os, Component target)
			throws IOException {

		return new ImageGraphics2D(os, target, format);
	}

	public VectorGraphics getGraphics(OutputStream os, Dimension dimension)
			throws IOException {

		return new ImageGraphics2D(os, dimension, format);
	}

	public String toString() {
		return super.toString() + " for " + format + " using " + spi;
	}
}
