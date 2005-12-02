// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphicsio.ps;

import java.awt.Image;
import java.awt.image.ImageProducer;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.freehep.graphicsio.ImageEncoder;

/**
 * 
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/EPSIEncoder.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class EPSIEncoder extends ImageEncoder {

    // Number of bits per byte.
    final static int maxBitsPerByte = 8;

    // Number of bytes per scan line (maximum here is 254).
    final static int maxBytesPerScan = 128;

    // The number of bits to use to represent the grayscale.
    private int grayscaleBits;

    // Boolean which gives the orientation of the image.
    private boolean portrait;

    // The width and height of the image.
    int width, height;

    // The array to hold the pixels.
    byte[][] grayPixels;

    // An array which hold enough bytes for one scan line.
    Scanline scanline;

    // Private conversion of bytes to hex digits.
    private static char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    // Masks to strip off last n bits.
    final private static byte[] lowBitMask = { (byte) 0, (byte) 1, (byte) 3,
            (byte) 7, (byte) 15, (byte) 31, (byte) 63, (byte) 127, (byte) 255 };

    /**
     * Constructor from Image with number of grayscale bits to use.
     * 
     * @param img The image to encode.
     * @param out The stream to write the GIF to.
     * @param grayscaleBits Number of grayscale bits to use.
     * @param portrait Flag indicating a portrait orientation.
     */
    public EPSIEncoder(Image img, OutputStream out, int grayscaleBits,
            boolean portrait) throws IOException {

        super(img, new DataOutputStream(out));
        this.grayscaleBits = grayscaleBits;
        this.portrait = portrait;
    }

    /**
     * Constructor from ImageProducer with number of grayscale bits to use.
     * 
     * @param prod The ImageProducer to encode.
     * @param out The stream to write the GIF to.
     * @param grayscaleBits Number of grayscale bits to use.
     * @param portrait Flag indicating a portrait orientation.
     */
    public EPSIEncoder(ImageProducer prod, OutputStream out, int grayscaleBits,
            boolean portrait) throws IOException {

        super(prod, new DataOutputStream(out));
        this.grayscaleBits = grayscaleBits;
        this.portrait = portrait;
    }

    protected void encodeStart(int width, int height) throws IOException {

        this.width = width;
        this.height = height;
        grayPixels = new byte[height][width];

        // Use the appropriate width and height depending on the orientation.
        int w, h;
        if (portrait) {
            w = width;
            h = height;
        } else {
            w = height;
            h = width;
        }

        // Calculate the number of lines in the image.
        int bitsPerScan = w * grayscaleBits;
        int bytesPerScan = (bitsPerScan / maxBitsPerByte)
                + ((bitsPerScan % maxBitsPerByte == 0) ? 0 : 1);
        int linesPerScan = (bytesPerScan / maxBytesPerScan)
                + ((bytesPerScan % maxBytesPerScan == 0) ? 0 : 1);
        int lines = linesPerScan * h;

        // Make a byte array which holds the information for one scan line.
        scanline = new Scanline(bytesPerScan, grayscaleBits);

        // Write out the header.
        putString("%%BeginPreview " + width + " " + height + " "
                + grayscaleBits + " " + lines + "\n");
    }

    protected void encodePixels(int x, int y, int w, int h, int[] rgbPixels,
            int off, int scansize) throws IOException {

        // Save the pixels as a grayscale value.
        for (int row = 0; row < h; ++row) {
            for (int column = 0; column < w; column++) {
                grayPixels[y + row][column] = toGrayscale(rgbPixels[row
                        * scansize + off + column]);
            }
        }
    }

    // Convert a value given as AARRGGBB to a single grayscale value.
    private byte toGrayscale(int argb) {
        int mask = 0xFF;
        double blue = ((double) ((argb >> 0) & mask)) / 255.;
        double green = ((double) ((argb >> 8) & mask)) / 255.;
        double red = ((double) ((argb >> 16) & mask)) / 255.;

        return (byte) (255. * Math.max(0.,
                (1. - (0.3 * red + 0.59 * green + 0.11 * blue))));
    }

    protected void encodeDone() throws IOException {

        if (portrait) {
            for (int row = height - 1; row >= 0; row--) {
                scanline.reset();
                for (int col = 0; col < width; col++) {
                    byte gray = grayPixels[row][col];
                    scanline.add(gray);
                }
                scanline.put();
            }
        } else {
            for (int col = width - 1; col >= 0; col--) {
                scanline.reset();
                for (int row = height - 1; row >= 0; row--) {
                    byte gray = grayPixels[row][col];
                    scanline.add(gray);
                }
                scanline.put();
            }
        }

        // Write out the trailer.
        putString("%%EndPreview\n");
    }

    // Write a string to the file.
    void putString(String s) throws IOException {

        out.write(s.getBytes());
    }

    // Write out a character to the file.
    void putChar(char c) throws IOException {

        out.write(c);
    }

    // Write out a byte to the GIF file
    void putByte(byte b) throws IOException {

        int highNibble = (b >> 4) & 0xF;
        int lowNibble = b & 0xF;

        out.write(hexDigit[highNibble]);
        out.write(hexDigit[lowNibble]);
    }

    // This class handles packing the scan data into a hexadecimal byte array.
    // This only works if the number of bits is 1, 2, 4, or 8.
    private class Scanline {

        private byte[] line;

        private int nbits;

        private int currentByte;

        private int currentOffset;

        public Scanline(int size, int nbits) {
            line = new byte[size];
            this.nbits = nbits;
            reset();
        }

        // Reset the scan line. Always zero the last byte in case the
        // scan line isn't fully used by the image.
        public void reset() {
            currentByte = 0;
            currentOffset = 0;
            for (int i = 0; i < line.length; i++) {
                line[i] = 0;
            }
        }

        public void add(byte b) {

            // Put the most significant bits in the lowest bits.
            b >>= (maxBitsPerByte - nbits);
            b &= lowBitMask[nbits];

            // OR in the information.
            line[currentByte] |= b;

            // Update the offsets.
            if (maxBitsPerByte - nbits - currentOffset == 0) {

                // We're reached the end of a byte, just reset the counters.
                currentOffset = 0;
                currentByte++;
            } else {

                // Increment the offset and shift the word for the next
                // bits of information.
                currentOffset += nbits;
                line[currentByte] <<= nbits;
            }
        }

        public void put() throws IOException {

            // Check the last byte. If currentOffset isn't zero then
            // the last byte hasn't been fully shifted into place.
            // Do it now!
            while (maxBitsPerByte - nbits - currentOffset > 0) {
                line[line.length - 1] <<= nbits;
                currentOffset += nbits;
            }

            for (int i = 0; i < line.length; i++) {
                if (i % maxBytesPerScan == 0) {
                    if (i != 0)
                        putChar('\n');
                    putChar('%');
                }
                putByte(line[i]);
            }
            putChar('\n');
        }
    }

}
