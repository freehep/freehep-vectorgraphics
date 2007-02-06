/**
 * $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/CreateDIBPatternBrushPt.java 10ec7516e3ce 2007/02/06 18:42:34 duns $
 *
 * Copyright (c) 1998-2006
 * semture GmbH
 *
 * Alle Rechte vorbehalten
 */
package org.freehep.graphicsio.emf.gdi;

import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFImageLoader;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFRenderer;

import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.TexturePaint;
import java.awt.Rectangle;

/**
 * @author Steffen Greiffenberg
 * @version $Revision$
 */
public class CreateDIBPatternBrushPt extends EMFTag {

    private int usage;

    private BitmapInfo bmi;

    private BufferedImage image;

    private int index;

    public CreateDIBPatternBrushPt() {
        super(94, 1);
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        CreateDIBPatternBrushPt tag = new CreateDIBPatternBrushPt();

        // read the index for storing the GDIObject
        tag.index = emf.readDWORD();

        // read whatever
        /*byte[] bytes =*/ emf.readByte(24);

        tag.bmi = new BitmapInfo(emf);

        // not used but read:
        // DIB_PAL_COLORS 	A color table is provided and
        // consists of an array of 16-bit indexes into the
        // logical palette of the device context into which
        // the brush is to be selected.
        // DIB_RGB_COLORS 	A color table is provided and
        // contains literal RGB values.
        tag.usage = emf.readDWORD();

        tag.image = EMFImageLoader.readImage(
            tag.bmi.getHeader(),
            tag.bmi.getHeader().getWidth(),
            tag.bmi.getHeader().getHeight(),
            emf,
            len - 4 - 24 - BitmapInfoHeader.size - 4, null);

        return tag;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        logger.warning("not implemented");
    }

    public String toString() {
        return super.toString() +
            "\n  usage: " + usage +
            "\n" + bmi.toString();
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // The StretchDIBits function copies the color data for a rectangle of pixels in a
        // DIB to the specified destination rectangle. If the destination rectangle is larger
        // than the source rectangle, this function stretches the rows and columns of color
        // data to fit the destination rectangle. If the destination rectangle is smaller
        // than the source rectangle, this function compresses the rows and columns by using
        // the specified raster operation.
        renderer.storeGDIObject(index, new GDIObject() {
            public void render(EMFRenderer renderer) {
                if (image != null) {
                    renderer.setBrushPaint(new TexturePaint(image, new Rectangle(0, 0, 16, 16)));
                }
            }
        });
    }
}
