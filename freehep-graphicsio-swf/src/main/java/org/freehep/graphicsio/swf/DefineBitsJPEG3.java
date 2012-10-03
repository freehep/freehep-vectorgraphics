// Copyright 2001-2007, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.InflaterInputStream;

import org.freehep.graphicsbase.util.images.ImageUtilities;
import org.freehep.graphicsio.ImageGraphics2D;
import org.freehep.graphicsio.ImageConstants;

/**
 * DefineBitsJPEG3 TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineBitsJPEG3.java d7c75c135a1d 2007/01/09 00:32:55 duns $
 */
public class DefineBitsJPEG3 extends DefineBitsJPEG2 {

    public DefineBitsJPEG3(int id, Image image, Properties options, Color bkg,
            ImageObserver observer) {
        this(id, ImageUtilities.createRenderedImage(image, observer, bkg), bkg,
                options);
    }

    public DefineBitsJPEG3(int id, RenderedImage image, Color bkg,
            Properties options) {
        this();
        character = id;
        this.image = image;
        this.options = options;
    }

    public DefineBitsJPEG3() {
        super(35, 3);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DefineBitsJPEG3 tag = new DefineBitsJPEG3();
        tag.character = swf.readUnsignedShort();
        swf.getDictionary().put(tag.character, tag);
        int jpegLen = (int) swf.readUnsignedInt();
        byte[] data = swf.readByte(jpegLen);

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BufferedImage bi = ImageGraphics2D.readImage(ImageConstants.JPG.toLowerCase(), bais);
        if (bais.available() > 0)
            System.err.println("DefineBitsJPEG3: not all bytes read: "
                    + bais.available());

        int width = bi.getWidth();
        int height = bi.getHeight();
        InflaterInputStream zip = new InflaterInputStream(swf);
        byte[] alpha = new byte[width * height];
        zip.read(alpha);

        WritableRaster raster = bi.getAlphaRaster();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                raster.setPixel(x, y, new int[] { alpha[y * width + x] });
            }
        }

        tag.image = bi;
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {

        swf.writeUnsignedShort(character);
        swf.writeUnsignedInt(imageLength);
        swf.write(getImageBytes());
    }

    public int getLength() throws IOException {
        return getImageBytes().length + 2;
    }

    private byte[] imageBytes;

    private int imageLength;

    private byte[] getImageBytes() throws IOException {
        if (imageBytes == null) {

            // calculate jpg bytes
            byte[] jpgBytes = ImageGraphics2D.toByteArray(
                image, ImageConstants.JPG, null, null);
            imageLength = jpgBytes.length;

            // calculate raw bytes
            byte[] rawBytes = ImageGraphics2D.toByteArray(
                image,
                ImageConstants.RAW,
                ImageConstants.ENCODING_FLATE,
                ImageGraphics2D.getRAWProperties(Color.black, ImageConstants.COLOR_MODEL_A));

            // write jpgBytes and rawBytes into the imageBytes
            imageBytes = new byte[jpgBytes.length + rawBytes.length];
            System.arraycopy(jpgBytes, 0, imageBytes, 0, jpgBytes.length);
            System.arraycopy(rawBytes, 0, imageBytes, jpgBytes.length, rawBytes.length);
        }
        return imageBytes;
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  character:   " + character + "\n");
        s.append("  image:      " + image + "\n");
        return s.toString();
    }
}
