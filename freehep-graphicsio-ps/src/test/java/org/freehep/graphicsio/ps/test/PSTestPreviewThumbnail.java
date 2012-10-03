// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.ps.test;

import java.util.Properties;

import org.freehep.graphicsbase.util.UserProperties;
import org.freehep.graphicsio.ps.PSGraphics2D;
import org.freehep.graphicsio.test.TestPreviewThumbnail;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-ps/src/test/java/org/freehep/graphicsio/ps/test/PSTestPreviewThumbnail.java cba39eb5843a 2006/03/20 18:04:28 duns $
 */
public class PSTestPreviewThumbnail extends TestPreviewThumbnail {

    public PSTestPreviewThumbnail(String[] args) throws Exception {
        super(args);
    }

    public void runTest(Properties options) throws Exception {
        UserProperties user = (options == null) ? new UserProperties()
                : new UserProperties(options);
        user.setProperty(PSGraphics2D.PREVIEW, true);
        
        super.runTest(user);
    }

    public static void main(String[] args) throws Exception {
        new PSTestPreviewThumbnail(args).runTest();
    }
}
