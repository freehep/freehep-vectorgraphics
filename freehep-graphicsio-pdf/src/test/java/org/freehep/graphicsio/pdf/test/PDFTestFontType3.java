// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.pdf.test;

import org.freehep.graphicsio.FontConstants;
import org.freehep.graphicsio.pdf.PDFGraphics2D;
import org.freehep.graphicsio.test.TestFontType3;
import org.freehep.util.UserProperties;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/test/java/org/freehep/graphicsio/pdf/test/PDFTestFontType3.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class PDFTestFontType3 extends TestFontType3 {

    public PDFTestFontType3(String[] args) throws Exception {
        super(args);
    }

    public static void main(String[] args) throws Exception {
        UserProperties p = new UserProperties();

        p.setProperty(PDFGraphics2D.EMBED_FONTS, true);
        p.setProperty(PDFGraphics2D.EMBED_FONTS_AS,
                FontConstants.EMBED_FONTS_TYPE3);

        new PDFTestFontType3(args).runTest(p);
    }
}
