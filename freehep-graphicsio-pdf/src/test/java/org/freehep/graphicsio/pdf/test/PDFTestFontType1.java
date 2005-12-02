// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.pdf.test;

import org.freehep.graphicsio.FontConstants;
import org.freehep.graphicsio.pdf.PDFGraphics2D;
import org.freehep.graphicsio.test.TestFontType1;
import org.freehep.util.UserProperties;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/test/java/org/freehep/graphicsio/pdf/test/PDFTestFontType1.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class PDFTestFontType1 extends TestFontType1 {

    public PDFTestFontType1(String[] args) throws Exception {
        super(args);
    }

    public static void main(String[] args) throws Exception {
        UserProperties p = new UserProperties();

        p.setProperty(PDFGraphics2D.EMBED_FONTS, true);
        p.setProperty(PDFGraphics2D.EMBED_FONTS_AS,
                FontConstants.EMBED_FONTS_TYPE1);

        new PDFTestFontType1(args).runTest(p);
    }
}
