// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.pdf.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/test/java/org/freehep/graphicsio/pdf/test/PDFTestSuite.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class PDFTestSuite extends TestSuite {

    protected void addTests(String fmt, String dir, String ext, boolean compare) {
        super.addTests(fmt, dir, ext, compare);
        addTest(new TestCase(
                "org.freehep.graphicsio.pdf.test.PDFTestPreviewThumbnail", fmt, dir,
                ext, compare, null));
        addTest(new TestCase("org.freehep.graphicsio.pdf.test.PDFTestFontType1",
                fmt, dir, ext, compare, null));
        addTest(new TestCase("org.freehep.graphicsio.pdf.test.PDFTestFontType3",
                fmt, dir, ext, compare, null));
    }

    public static TestSuite suite() {
        PDFTestSuite suite = new PDFTestSuite();
        suite.addTests("PDF");
        return suite;
    }

}
