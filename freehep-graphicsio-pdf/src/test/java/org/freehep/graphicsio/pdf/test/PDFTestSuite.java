// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.pdf.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/test/java/org/freehep/graphicsio/pdf/test/PDFTestSuite.java e3449d5a3c6c 2005/12/07 22:14:47 duns $
 */
public class PDFTestSuite extends TestSuite {

    protected void addTests(String fmt, String pkg, String dir, String ext, boolean compare) {
        super.addTests(fmt, pkg, dir, ext, compare);
        addTest(new TestCase(
                "org.freehep.graphicsio.pdf.test.PDFTestPreviewThumbnail", fmt, 
                pkg, dir, ext, compare, null));
        addTest(new TestCase(
                "org.freehep.graphicsio.pdf.test.PDFTestFontType1", fmt,
                pkg, dir, ext, compare, null));
        addTest(new TestCase(
                "org.freehep.graphicsio.pdf.test.PDFTestFontType3", fmt,
                pkg, dir, ext, compare, null));
    }

    public static TestSuite suite() {
        PDFTestSuite suite = new PDFTestSuite();
        suite.addTests("PDF");
        return suite;
    }

}
