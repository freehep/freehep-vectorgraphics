// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.ps.test;

import org.freehep.graphicsio.ps.PSGraphics2D;
import org.freehep.util.UserProperties;

import org.freehep.graphicsio.test.TestPreviewThumbnail;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-ps/src/test/java/org/freehep/graphicsio/ps/test/PSTestPreviewThumbnail.java 2689041eec29 2005/12/01 22:37:27 duns $
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
