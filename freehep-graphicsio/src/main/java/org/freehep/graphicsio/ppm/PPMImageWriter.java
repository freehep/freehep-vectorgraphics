// Copyright 2003, FreeHEP
package org.freehep.graphicsio.ppm;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

/**
 * 
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/ppm/PPMImageWriter.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public class PPMImageWriter extends ImageWriter {

    public PPMImageWriter(PPMImageWriterSpi originatingProvider) {
        super(originatingProvider);
    }

    public void write(IIOMetadata streamMetadata, IIOImage image,
            ImageWriteParam param) throws IOException {
        if (image == null)
            throw new IllegalArgumentException("image == null");

        if (image.hasRaster())
            throw new UnsupportedOperationException("Cannot write rasters");

        Object output = getOutput();
        if (output == null)
            throw new IllegalStateException("output was not set");

        if (param == null)
            param = getDefaultWriteParam();

        ImageOutputStream ios = (ImageOutputStream) output;
        RenderedImage ri = image.getRenderedImage();

        if (ri instanceof BufferedImage) {
            BufferedImage bi = (BufferedImage) ri;
            PPMEncoder encoder = new PPMEncoder(bi, ios);
            encoder.encode();
        } else {
            throw new IOException("Image not of type BufferedImage");
        }
    }

    public IIOMetadata convertStreamMetadata(IIOMetadata inData,
            ImageWriteParam param) {
        return null;
    }

    public IIOMetadata convertImageMetadata(IIOMetadata inData,
            ImageTypeSpecifier imageType, ImageWriteParam param) {
        return null;
    }

    public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageType,
            ImageWriteParam param) {
        return null;
    }

    public IIOMetadata getDefaultStreamMetadata(ImageWriteParam param) {
        return null;
    }
}
