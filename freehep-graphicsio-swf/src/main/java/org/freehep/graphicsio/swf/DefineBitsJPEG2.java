// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.freehep.graphicsio.ImageGraphics2D;
import org.freehep.util.images.ImageUtilities;

/**
 * DefineBitsJPEG2 TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/DefineBitsJPEG2.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class DefineBitsJPEG2 extends DefinitionTag {

    protected int character;

    protected RenderedImage image;

    protected Properties options;

    public DefineBitsJPEG2(int id, Image image, Properties options, Color bkg,
            ImageObserver observer) {
        this(id, ImageUtilities.createRenderedImage(image, observer, bkg), bkg,
                options);
    }

    public DefineBitsJPEG2(int id, RenderedImage image, Color bkg,
            Properties options) {
        this();
        character = id;
        this.image = image;
        this.options = options;
    }

    public DefineBitsJPEG2() {
        super(21, 2);
    }

    protected DefineBitsJPEG2(int tag, int level) {
        super(tag, level);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        DefineBitsJPEG2 tag = new DefineBitsJPEG2();
        tag.character = swf.readUnsignedShort();
        swf.getDictionary().put(tag.character, tag);

        byte[] data = swf.readByte(len - 2);

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        tag.image = ImageGraphics2D.readImage("jpg", bais);

        if (bais.available() > 0)
            System.err.println("DefineBitsJPEG2: not all bytes read: "
                    + bais.available());

        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {

        swf.writeUnsignedShort(character);
        swf.write(getImageBytes());
    }

    public int getLength() throws IOException {
        return getImageBytes().length + 2;
    }

    private byte[] imageBytes;

    private byte[] getImageBytes() throws IOException {
        if (imageBytes == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageGraphics2D.writeImage(image, "jpg", options, baos);
            baos.close();

            imageBytes = baos.toByteArray();
        }
        return imageBytes;
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(super.toString() + "\n");
        s.append("  character:  " + character + "\n");
        s.append("  image:      " + image + "\n");
        return s.toString();
    }
}
