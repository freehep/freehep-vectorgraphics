package org.freehep.util.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Equivalent to writing to /dev/nul
 * 
 * @author tonyj
 * @version $Id: src/main/java/org/freehep/util/io/DummyOutputStream.java 96b41b903496 2005/11/21 19:50:18 duns $
 */
public class DummyOutputStream extends OutputStream {
    /**
     * Creates a Dummy output steram.
     */
    public DummyOutputStream() {
    }

    public void write(int b) throws IOException {
    }

    public void write(byte[] b) throws IOException {
    }

    public void write(byte[] b, int off, int len) throws IOException {
    }
}
