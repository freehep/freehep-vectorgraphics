package org.freehep.graphicsio.gif;

// GifEncoder - write out an image as a GIF
//
// Transparency handling and variable bit size courtesy of Jack Palevich.
//
// Copyright (C)1996,1998 by Jef Poskanzer <jef@acme.com>. All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//
// Visit the ACME Labs Java page for up-to-date versions of this and other
// fine Java utilities: http://www.acme.com/java/

//package Acme.JPM.Encoders;

import java.awt.Image;
import java.awt.image.ImageProducer;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

//import org.freehep.graphicsio.ImageEncoder;

/// Write out an image as a GIF.
// <P>
// <A HREF="/resources/classes/Acme/JPM/Encoders/GifEncoder.java">Fetch the software.</A><BR>
// <A HREF="/resources/classes/Acme.tar.gz">Fetch the entire Acme package.</A>
// <P>
// @see ToGif

public class GIFEncoder extends ImageEncoder {

    private boolean interlace = false;

    private String comment = null;

    // / Constructor from Image.
    // @param img The image to encode.
    // @param out The stream to write the GIF to.
    public GIFEncoder(Image img, OutputStream out) throws IOException {
        this(img, out, false);
    }

    public GIFEncoder(Image img, DataOutput dos) throws IOException {
        this(img, dos, false);
    }

    // / Constructor from Image with interlace setting.
    // @param img The image to encode.
    // @param out The stream to write the GIF to.
    // @param interlace Whether to interlace.
    public GIFEncoder(Image img, OutputStream out, boolean interlace)
            throws IOException {
        this(img, (DataOutput) new DataOutputStream(out), interlace);
    }

    public GIFEncoder(Image img, DataOutput dos, boolean interlace)
            throws IOException {
        super(img, dos);
        this.interlace = interlace;
    }

    // / Constructor from ImageProducer.
    // @param prod The ImageProducer to encode.
    // @param out The stream to write the GIF to.
    public GIFEncoder(ImageProducer prod, OutputStream out) throws IOException {
        this(prod, out, false);
    }

    public GIFEncoder(ImageProducer prod, DataOutput dos) throws IOException {
        this(prod, dos, false);
    }

    // / Constructor from ImageProducer with interlace setting.
    // @param prod The ImageProducer to encode.
    // @param out The stream to write the GIF to.
    public GIFEncoder(ImageProducer prod, OutputStream out, boolean interlace)
            throws IOException {
        this(prod, (DataOutput) new DataOutputStream(out), interlace);
    }

    public GIFEncoder(ImageProducer prod, DataOutput dos, boolean interlace)
            throws IOException {
        super(prod, dos);
        this.interlace = interlace;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    int width, height;

    int[][] rgbPixels;

    protected void encodeStart(int width, int height) throws IOException {
        this.width = width;
        this.height = height;
        rgbPixels = new int[height][width];
    }

    protected void encodePixels(int x, int y, int w, int h, int[] rgbPixels,
            int off, int scansize) throws IOException {
        // Save the pixels.
        for (int row = 0; row < h; ++row) {
            System.arraycopy(rgbPixels, row * scansize + off, this.rgbPixels[y
                    + row], x, w);
        }
    }

    protected void encodeDone() throws IOException {
        // MD: added ColorMap for max color limitation
        // returning a color palette (including one transparent color)
        // and rgbPixels changes from colors into palette indices.
        GIFColorMap colorMap = new GIFNearestColorMap();
        int[] palette = colorMap.create(rgbPixels, 255);
        
        // MD: look for first fully transparent color
        int transparentIndex = -1;
    	for (int i=0; i<palette.length; i++) { 
            if ((palette[i] & 0xFF000000) == 0) {
            	transparentIndex = i;
            	break;
            }
        }
        
        for (int i=0; i<palette.length; i++) {
            System.err.print(Integer.toHexString(palette[i])+", ");
        }
        System.err.println("\n n = "+palette.length+" "+ (transparentIndex >= 0 ? Integer.toHexString(palette[transparentIndex]) : "Not Transparent"));

        // Figure out how many bits to use.
        int logColors;
        int nColors = palette.length;
        if (nColors <= 2)
            logColors = 1;
        else if (nColors <= 4)
            logColors = 2;
        else if (nColors <= 16)
            logColors = 4;
        else
            logColors = 8;

        GIFEncode(width, height, interlace, (byte) 0, transparentIndex,
                logColors, palette);
    }

    byte GetPixel(int x, int y) throws IOException {
        return (byte)rgbPixels[y][x];
    }

    void writeString(String str) throws IOException {
        byte[] buf = str.getBytes();
        out.write(buf);
    }

    // Adapted from ppmtogif, which is based on GIFENCOD by David
    // Rowley <mgardi@watdscu.waterloo.edu>. Lempel-Zim compression
    // based on "compress".

    int Width, Height;

    boolean Interlace;

    int curx, cury;

    int CountDown;

    int Pass = 0;

    void GIFEncode(int Width, int Height, boolean Interlace, byte Background,
            int Transparent, int BitsPerPixel, int[] palette) throws IOException {

        byte B;
        int LeftOfs, TopOfs;
        int InitCodeSize;
        int i;

        this.Width = Width;
        this.Height = Height;
        this.Interlace = Interlace;
        LeftOfs = TopOfs = 0;

        // Calculate number of bits we are expecting
        CountDown = Width * Height;

        // Indicate which pass we are on (if interlace)
        Pass = 0;

        // The initial code size
        if (BitsPerPixel <= 1)
            InitCodeSize = 2;
        else
            InitCodeSize = BitsPerPixel;

        // Set up the current x and y position
        curx = 0;
        cury = 0;

        // Write the Magic header
        writeString("GIF89a");

        // Write out the screen width and height
        Putword(Width);
        Putword(Height);

        // Indicate that there is a global colour map
        B = (byte) 0x80; // Yes, there is a color map
        // OR in the resolution
        B |= (byte) ((8 - 1) << 4);
        // Not sorted
        // OR in the Bits per Pixel
        B |= (byte) ((BitsPerPixel - 1));

        // Write it out
        Putbyte(B);

        // Write out the Background colour
        Putbyte(Background);

        // Pixel aspect ratio - 1:1.
        // Putbyte( (byte) 49);
        // Java's GIF reader currently has a bug, if the aspect ratio byte is
        // not zero it throws an ImageFormatException. It doesn't know that
        // 49 means a 1:1 aspect ratio. Well, whatever, zero works with all
        // the other decoders I've tried so it probably doesn't hurt.
        Putbyte((byte) 0);

        // Write out the Global Colour Map
        int colorMapSize = 1 << BitsPerPixel;
        for (i = 0; i < colorMapSize; ++i) {
            if (i < palette.length) {
               Putbyte((byte)((palette[i] >> 16) & 0xFF));
               Putbyte((byte)((palette[i] >> 8) & 0xFF));
               Putbyte((byte)((palette[i]) & 0xFF));
            } else {
                Putbyte((byte)0);
                Putbyte((byte)0);
                Putbyte((byte)0);
            }
        }

        // Write out extension for transparent colour index, if necessary.
        if (Transparent != -1) {
            Putbyte((byte) '!');
            Putbyte((byte) 0xf9);
            Putbyte((byte) 4);
            Putbyte((byte) 1);
            Putbyte((byte) 0);
            Putbyte((byte) 0);
            Putbyte((byte) Transparent);
            Putbyte((byte) 0);
        }

        // Write an Image separator
        Putbyte((byte) ',');

        // Write the Image header
        Putword(LeftOfs);
        Putword(TopOfs);
        Putword(Width);
        Putword(Height);

        // Write out whether or not the image is interlaced
        if (Interlace)
            Putbyte((byte) 0x40);
        else
            Putbyte((byte) 0x00);

        // Write out the initial code size
        Putbyte((byte) InitCodeSize);

        // Go and actually compress the data
        compress(InitCodeSize + 1);

        // Write out a Zero-length packet (to end the series)
        Putbyte((byte) 0);

        // Write out the comment
        if ((comment != null) && (comment.length() > 0)) {
            Putbyte((byte) 0x21);
            Putbyte((byte) 0xFE);
            Putbyte((byte) comment.length());
            writeString(comment);
            Putbyte((byte) 0);
        }

        // Write the GIF file terminator
        Putbyte((byte) ';');
    }

    // Bump the 'curx' and 'cury' to point to the next pixel
    void BumpPixel() {
        // Bump the current X position
        ++curx;

        // If we are at the end of a scan line, set curx back to the beginning
        // If we are interlaced, bump the cury to the appropriate spot,
        // otherwise, just increment it.
        if (curx == Width) {
            curx = 0;

            if (!Interlace) {
                ++cury;
            } else {
                switch (Pass) {
                case 0:
                    cury += 8;
                    if (cury >= Height) {
                        ++Pass;
                        cury = 4;
                    }
                    break;

                case 1:
                    cury += 8;
                    if (cury >= Height) {
                        ++Pass;
                        cury = 2;
                    }
                    break;

                case 2:
                    cury += 4;
                    if (cury >= Height) {
                        ++Pass;
                        cury = 1;
                    }
                    break;

                case 3:
                    cury += 2;
                    break;
                }
            }
        }
    }

    static final int EOF = -1;

    // Return the next pixel from the image
    int GIFNextPixel() throws IOException {
        byte r;

        if (CountDown == 0)
            return EOF;

        --CountDown;

        r = GetPixel(curx, cury);

        BumpPixel();

        return r & 0xff;
    }

    // Write out a word to the GIF file
    void Putword(int w) throws IOException {
        Putbyte((byte) (w & 0xff));
        Putbyte((byte) ((w >> 8) & 0xff));
    }

    // Write out a byte to the GIF file
    void Putbyte(byte b) throws IOException {
        out.write(b);
    }

    // GIFCOMPR.C - GIF Image compression routines
    //
    // Lempel-Ziv compression based on 'compress'. GIF modifications by
    // David Rowley (mgardi@watdcsu.waterloo.edu)

    // General DEFINEs

    static final int BITS = 12;

    static final int HSIZE = 5003; // 80% occupancy

    // GIF Image compression - modified 'compress'
    //
    // Based on: compress.c - File compression ala IEEE Computer, June 1984.
    //
    // By Authors: Spencer W. Thomas (decvax!harpo!utah-cs!utah-gr!thomas)
    // Jim McKie (decvax!mcvax!jim)
    // Steve Davies (decvax!vax135!petsd!peora!srd)
    // Ken Turkowski (decvax!decwrl!turtlevax!ken)
    // James A. Woods (decvax!ihnp4!ames!jaw)
    // Joe Orost (decvax!vax135!petsd!joe)

    int n_bits; // number of bits/code

    int maxbits = BITS; // user settable max # bits/code

    int maxcode; // maximum code, given n_bits

    int maxmaxcode = 1 << BITS; // should NEVER generate this code

    final int MAXCODE(int n_bits) {
        return (1 << n_bits) - 1;
    }

    int[] htab = new int[HSIZE];

    int[] codetab = new int[HSIZE];

    int hsize = HSIZE; // for dynamic table sizing

    int free_ent = 0; // first unused entry

    // block compression parameters -- after all codes are used up,
    // and compression rate changes, start over.
    boolean clear_flg = false;

    // Algorithm: use open addressing double hashing (no chaining) on the
    // prefix code / next character combination. We do a variant of Knuth's
    // algorithm D (vol. 3, sec. 6.4) along with G. Knott's relatively-prime
    // secondary probe. Here, the modular division first probe is gives way
    // to a faster exclusive-or manipulation. Also do block compression with
    // an adaptive reset, whereby the code table is cleared when the compression
    // ratio decreases, but after the table fills. The variable-length output
    // codes are re-sized at this point, and a special CLEAR code is generated
    // for the decompressor. Late addition: construct the table according to
    // file size for noticeable speed improvement on small files. Please direct
    // questions about this implementation to ames!jaw.

    int g_init_bits;

    int ClearCode;

    int EOFCode;

    void compress(int init_bits) throws IOException {
        int fcode;
        int i /* = 0 */;
        int c;
        int ent;
        int disp;
        int hsize_reg;
        int hshift;

        // Set up the globals: g_init_bits - initial number of bits
        g_init_bits = init_bits;

        // Set up the necessary values
        clear_flg = false;
        n_bits = g_init_bits;
        maxcode = MAXCODE(n_bits);

        ClearCode = 1 << (init_bits - 1);
        EOFCode = ClearCode + 1;
        free_ent = ClearCode + 2;

        char_init();

        ent = GIFNextPixel();

        hshift = 0;
        for (fcode = hsize; fcode < 65536; fcode *= 2)
            ++hshift;
        hshift = 8 - hshift; // set hash code range bound

        hsize_reg = hsize;
        cl_hash(hsize_reg); // clear hash table

        output(ClearCode);

        outer_loop: while ((c = GIFNextPixel()) != EOF) {
            fcode = (c << maxbits) + ent;
            i = (c << hshift) ^ ent; // xor hashing

            if (htab[i] == fcode) {
                ent = codetab[i];
                continue;
            } else if (htab[i] >= 0) // non-empty slot
            {
                disp = hsize_reg - i; // secondary hash (after G. Knott)
                if (i == 0)
                    disp = 1;
                do {
                    if ((i -= disp) < 0)
                        i += hsize_reg;

                    if (htab[i] == fcode) {
                        ent = codetab[i];
                        continue outer_loop;
                    }
                } while (htab[i] >= 0);
            }
            output(ent);
            ent = c;
            if (free_ent < maxmaxcode) {
                codetab[i] = free_ent++; // code -> hashtable
                htab[i] = fcode;
            } else
                cl_block();
        }
        // Put out the final code.
        output(ent);
        output(EOFCode);
    }

    // output
    //
    // Output the given code.
    // Inputs:
    // code: A n_bits-bit integer. If == -1, then EOF. This assumes
    // that n_bits =< wordsize - 1.
    // Outputs:
    // Outputs code to the file.
    // Assumptions:
    // Chars are 8 bits long.
    // Algorithm:
    // Maintain a BITS character long buffer (so that 8 codes will
    // fit in it exactly). Use the VAX insv instruction to insert each
    // code in turn. When the buffer fills up empty it and start over.

    int cur_accum = 0;

    int cur_bits = 0;

    int masks[] = { 0x0000, 0x0001, 0x0003, 0x0007, 0x000F, 0x001F, 0x003F,
            0x007F, 0x00FF, 0x01FF, 0x03FF, 0x07FF, 0x0FFF, 0x1FFF, 0x3FFF,
            0x7FFF, 0xFFFF };

    void output(int code) throws IOException {
        cur_accum &= masks[cur_bits];

        if (cur_bits > 0)
            cur_accum |= (code << cur_bits);
        else
            cur_accum = code;

        cur_bits += n_bits;

        while (cur_bits >= 8) {
            char_out((byte) (cur_accum & 0xff));
            cur_accum >>= 8;
            cur_bits -= 8;
        }

        // If the next entry is going to be too big for the code size,
        // then increase it, if possible.
        if (free_ent > maxcode || clear_flg) {
            if (clear_flg) {
                maxcode = MAXCODE(n_bits = g_init_bits);
                clear_flg = false;
            } else {
                ++n_bits;
                if (n_bits == maxbits)
                    maxcode = maxmaxcode;
                else
                    maxcode = MAXCODE(n_bits);
            }
        }

        if (code == EOFCode) {
            // At EOF, write the rest of the buffer.
            while (cur_bits > 0) {
                char_out((byte) (cur_accum & 0xff));
                cur_accum >>= 8;
                cur_bits -= 8;
            }

            flush_char();
        }
    }

    // Clear out the hash table

    // table clear for block compress
    void cl_block() throws IOException {
        cl_hash(hsize);
        free_ent = ClearCode + 2;
        clear_flg = true;

        output(ClearCode);
    }

    // reset code table
    void cl_hash(int hsize) {
        for (int i = 0; i < hsize; ++i)
            htab[i] = -1;
    }

    // GIF Specific routines

    // Number of characters so far in this 'packet'
    int a_count;

    // Set up the 'byte output' routine
    void char_init() {
        a_count = 0;
    }

    // Define the storage for the packet accumulator
    byte[] accum = new byte[256];

    // Add a character to the end of the current packet, and if it is 254
    // characters, flush the packet to disk.
    void char_out(byte c) throws IOException {
        accum[a_count++] = c;
        if (a_count >= 254)
            flush_char();
    }

    // Flush the packet to disk, and reset the accumulator
    void flush_char() throws IOException {
        if (a_count > 0) {
            out.write(a_count);
            out.write(accum, 0, a_count);
            a_count = 0;
        }
    }

}
