// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;
import java.io.InputStream;

import org.freehep.util.io.ActionHeader;
import org.freehep.util.io.TagHeader;
import org.freehep.util.io.TaggedInputStream;

/**
 * This class extends the TaggedInputStream with several methods to read
 * TagHeaders. It is a dummy implementation at this time, and only used for
 * tagID analysis.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CGMInputStream.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CGMInputStream extends TaggedInputStream {

    public static int DEFAULT_VERSION = 3;

    public CGMInputStream(InputStream is) throws IOException {

        this(is, DEFAULT_VERSION);
    }

    public CGMInputStream(InputStream is, int version) throws IOException {

        this(is, new CGMTagSet(version));
    }

    public CGMInputStream(InputStream is, CGMTagSet tagSet) throws IOException {

        // CGM is big-endian
        super(is, tagSet, null, false);
    }

    protected TagHeader readTagHeader() throws IOException {
        int dummy = (getLength() % 2 != 0) ? read() : 0;
        if (dummy == -1)
            return null;
        // Read the tag.
        int tagID = read();
        // End of stream
        if (tagID == -1)
            return null;

        tagID = (tagID << 8) | readUnsignedByte();

        long length = tagID & 0x1F;
        if (length >= 0x1f) {
            length = readUnsignedShort();
        }
        tagID = tagID >> 5;
        return new TagHeader(tagID, length);
    }

    protected ActionHeader readActionHeader() throws IOException {
        return null;
    }

    public int getVersion() {
        return DEFAULT_VERSION;
    }
}
