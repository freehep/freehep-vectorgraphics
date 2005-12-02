// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.ps.test;

import org.freehep.graphicsio.ps.PSGraphics2D;
import org.freehep.graphicsio.test.TestPreviewThumbnail;
import org.freehep.util.UserProperties;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-ps/src/test/java/org/freehep/graphicsio/ps/test/PSTestPreviewThumbnail.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class PSTestPreviewThumbnail extends TestPreviewThumbnail {

    public PSTestPreviewThumbnail(String[] args) throws Exception {
        super(args);
    }

    public static void main(String[] args) throws Exception {
        UserProperties p = new UserProperties();
        p.setProperty(PSGraphics2D.PREVIEW, true);

        new PSTestPreviewThumbnail(args).runTest(p);
    }
}
