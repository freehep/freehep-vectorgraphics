// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.pdf.test;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/test/java/org/freehep/graphicsio/pdf/test/PDFTestSuite.java 4c4708a97391 2007/06/12 22:32:31 duns $
 */
public class PDFTestSuite extends TestSuite {

    /*
    protected void addTests(String category, String fmt, String dir, String ext, boolean compare, Properties properties) {
        super.addTests(category, fmt, dir, ext, compare, properties);

        addTest(new TestCase(
                "org.freehep.graphicsio.pdf.test.PDFTestPreviewThumbnail", category, fmt, 
                dir, ext, compare, null));
        addTest(new TestCase(
                "org.freehep.graphicsio.pdf.test.PDFTestFontType1", category, fmt,
                dir, ext, compare, null));
        addTest(new TestCase(
                "org.freehep.graphicsio.pdf.test.PDFTestFontType3", category, fmt,
                dir, ext, compare, null));
    }
 */

    public static TestSuite suite() {
        PDFTestSuite suite = new PDFTestSuite();
        suite.addTests("PDF");
        return suite;
    }

}
