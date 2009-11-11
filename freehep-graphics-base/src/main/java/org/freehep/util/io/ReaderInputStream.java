// Copyright 2003, FreeHEP.
package org.freehep.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * The ReaderInputStream makes a Reader look like an InputStream so one can use
 * any of the filters. This is not without danger of loosing some data.
 * 
 * IMPORTANT: inherits from InputStream rather than FilterInputStream so that
 * the correct read(byte[], int, int) method is used.
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/util/io/ReaderInputStream.java 96b41b903496 2005/11/21 19:50:18 duns $
 */
public class ReaderInputStream extends InputStream {

    private Reader reader;

    /**
     * Creates an InputStream from given Reader.
     * 
     * @param reader reader to read from.
     */
    public ReaderInputStream(Reader reader) {
        this.reader = reader;
    }

    // Note: here we may loose Unicode data.
    public int read() throws IOException {
        return reader.read() & 0xFF;
    }

    // Note: here we may loose Unicode data.
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    // Note: here we may loose Unicode data.
    public int read(byte[] b, int off, int len) throws IOException {
        char c[] = new char[len];
        int result = reader.read(c, 0, len);
        for (int i = 0; i < len; i++) {
            b[off + i] = (byte) (c[i] & 0xFF);
        }
        return result;
    }

    public int available() throws IOException {
        return 0;
    }

    public void close() throws IOException {
        reader.close();
    }

    public void mark(int readAheadLimit) {
        // Note: IOException ignored
        try {
            reader.mark(readAheadLimit);
        } catch (IOException ioe) {
            // ignored
        }
    }

    public boolean markSupported() {
        return reader.markSupported();
    }

    public void reset() throws IOException {
        reader.reset();
    }

    public long skip(long n) throws IOException {
        return reader.skip(n);
    }
}
