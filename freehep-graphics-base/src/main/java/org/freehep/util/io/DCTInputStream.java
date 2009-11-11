// Copyright 2003, FreeHEP.
package org.freehep.util.io;

import java.awt.Image;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * Reads images from a JPEG Stream, but only images.
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/util/io/DCTInputStream.java 96b41b903496 2005/11/21 19:50:18 duns $
 */
/**
 * @author duns
 * 
 */
public class DCTInputStream extends FilterInputStream {

    /**
     * Creates a DCT input stream from the given input stream
     * 
     * @param input stream to read from
     */
    public DCTInputStream(InputStream input) {
        super(input);
    }

    /**
     * Read is not supported, only readImage.
     * 
     * @see java.io.FilterInputStream#read()
     */
    public int read() throws IOException {
        throw new IOException(getClass()
                + ": read() not implemented, use readImage().");
    }

    /**
     * @return image read
     * @throws IOException if read fails
     */
    public Image readImage() throws IOException {
        return ImageIO.read(new NoCloseInputStream(this));
    }
}
