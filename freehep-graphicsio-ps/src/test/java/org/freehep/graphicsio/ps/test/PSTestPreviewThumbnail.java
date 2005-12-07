// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.ps.test;

import java.util.Properties;

import org.freehep.graphicsio.ps.PSGraphics2D;
import org.freehep.graphicsio.test.TestPreviewThumbnail;
import org.freehep.util.UserProperties;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-ps/src/test/java/org/freehep/graphicsio/ps/test/PSTestPreviewThumbnail.java 11a78ba01bc8 2005/12/07 23:01:33 duns $
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
