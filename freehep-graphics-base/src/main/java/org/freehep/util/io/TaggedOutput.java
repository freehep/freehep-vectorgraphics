// Copyright 2001, FreeHEP.
package org.freehep.util.io;

import java.io.IOException;

/**
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/util/io/TaggedOutput.java 96b41b903496 2005/11/21 19:50:18 duns $
 */
public interface TaggedOutput {

    /**
     * Write a tag.
     * 
     * @param tag tag to write
     * @throws IOException if write fails
     */
    public void writeTag(Tag tag) throws IOException;

    /**
     * Close the stream
     * 
     * @throws IOException if close fails
     */
    public void close() throws IOException;
}
