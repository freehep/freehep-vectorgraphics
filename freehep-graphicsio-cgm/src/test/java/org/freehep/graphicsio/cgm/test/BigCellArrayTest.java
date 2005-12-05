// Copyright 2003 FreeHEP
package org.freehep.graphicsio.cgm.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.freehep.graphicsio.cgm.CGMGraphics2D;
import org.freehep.util.UserProperties;

/**
 * BigCellArrayTest illustrates drawing a larger image, to demonstrate that the
 * CGM support for partitioning large commands works properly. Commands for
 * which the parameter data is larger than 32767 octets(bytes), must be
 * partitioned into separate pieces. The original CellArray support exposed this
 * limitation, so a buffering option has been added to properly partion large
 * data into 16K chunks.
 * 
 * The image displayed should show a rectangle with green increasing from left
 * to right mixed with red increasing from top to bottom. The image should be
 * surrounded by a one-pixel wide extent of background grey, and have a large
 * corner to corner black X drawn overtop to prove that subsequent commands are
 * not lost.
 * 
 * @author Ian Graham
 */
public class BigCellArrayTest {

    public static void main(String[] args) throws IOException {

        BufferedImage image = new BufferedImage(160, 120,
                BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int red = x * 0xFF / (image.getWidth() - 1);
                int green = y * 0xFF / (image.getHeight() - 1);
                int blue = 0;
                int alpha = 0xFF;
                int rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, rgb);
            }
        }

        UserProperties graphicsProperties = (UserProperties) CGMGraphics2D
                .getDefaultProperties();
        graphicsProperties.setProperty(CGMGraphics2D.BINARY, true);
        graphicsProperties.setProperty(CGMGraphics2D.BACKGROUND_COLOR,
                Color.LIGHT_GRAY);
        graphicsProperties.setProperty(CGMGraphics2D.BACKGROUND, true);

        OutputStream out = new FileOutputStream("BigCellArrayTest.cgm");
        CGMGraphics2D graphics = new CGMGraphics2D(out, new Dimension(image
                .getWidth() + 2, image.getHeight() + 2));
        graphics.setCreator(System.getProperty("user.name"));
        graphics.startExport();
        graphics.setTransform(AffineTransform.getTranslateInstance(1, 1));
        graphics.drawImage(image, 0, 0, null);
        graphics.drawLine(1, 1, image.getWidth(), image.getHeight());
        graphics.drawLine(1.0, image.getHeight(), image.getWidth(), 1);
        graphics.endExport();
    }

}
