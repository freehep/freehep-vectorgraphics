// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.pdf.test;

import java.util.Properties;

import org.freehep.graphicsio.pdf.PDFGraphics2D;
import org.freehep.graphicsio.test.TestPreviewThumbnail;
import org.freehep.util.UserProperties;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/test/java/org/freehep/graphicsio/pdf/test/PDFTestPreviewThumbnail.java cba39eb5843a 2006/03/20 18:04:28 duns $
 */
public class PDFTestPreviewThumbnail extends TestPreviewThumbnail {

    public PDFTestPreviewThumbnail(String[] args) throws Exception {
        super(args);
    }

    public void runTest(Properties options) throws Exception {
        UserProperties user = (options == null) ? new UserProperties()
                : new UserProperties(options);
        user.setProperty(PDFGraphics2D.THUMBNAILS, true);
        
        super.runTest(user);
    }

    public static void main(String[] args) throws Exception {
        new PDFTestPreviewThumbnail(args).runTest();
    }
}
