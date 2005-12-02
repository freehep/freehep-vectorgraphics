// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.pdf.test;

import org.freehep.graphicsio.pdf.PDFGraphics2D;
import org.freehep.graphicsio.test.TestPreviewThumbnail;
import org.freehep.util.UserProperties;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/test/java/org/freehep/graphicsio/pdf/test/PDFTestPreviewThumbnail.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class PDFTestPreviewThumbnail extends TestPreviewThumbnail {

    public PDFTestPreviewThumbnail(String[] args) throws Exception {
        super(args);
    }

    public static void main(String[] args) throws Exception {
        UserProperties p = new UserProperties();
        p.setProperty(PDFGraphics2D.THUMBNAILS, true);

        new PDFTestPreviewThumbnail(args).runTest(p);
    }
}
