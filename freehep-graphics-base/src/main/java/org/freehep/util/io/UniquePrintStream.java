// Copyright 2001, FreeHEP.
package org.freehep.util.io;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The UniquePrintStream keeps Strings buffered in sorted order, but any
 * duplicates are removed. This stream can be used to print error messages
 * exactly once. When finish is called all messages are printed.
 * 
 * It only acts on the println(String) method, any other method will print
 * directly.
 * 
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/util/io/UniquePrintStream.java 96b41b903496 2005/11/21 19:50:18 duns $
 */
public class UniquePrintStream extends PrintStream implements
        FinishableOutputStream {

    private SortedSet msg = new TreeSet();

    /**
     * Create a Unique Print Stream.
     * 
     * @param out stream to write
     */
    public UniquePrintStream(OutputStream out) {
        super(out);
    }

    public void println(String s) {
        synchronized (this) {
            msg.add(s);
        }
    }

    public void finish() {
        for (Iterator i = msg.iterator(); i.hasNext();) {
            String s = (String) i.next();
            super.println(s);
        }
        msg = new TreeSet();
    }
}
