// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.pdf.test;

import java.util.Properties;

import org.freehep.graphicsio.test.TestSuite;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/test/java/org/freehep/graphicsio/pdf/test/PDFTestSuite.java 4e4ed8246a90 2006/04/05 23:00:50 duns $
 */
public class PDFTestSuite extends TestSuite {

    protected void addTests(String fmt, String dir, String ext, boolean compare, Properties properties) {
        super.addTests(fmt, dir, ext, compare, properties);
        addTest(new TestCase(
                "org.freehep.graphicsio.pdf.test.PDFTestPreviewThumbnail", fmt, 
                dir, ext, compare, null));
        addTest(new TestCase(
                "org.freehep.graphicsio.pdf.test.PDFTestFontType1", fmt,
                dir, ext, compare, null));
        addTest(new TestCase(
                "org.freehep.graphicsio.pdf.test.PDFTestFontType3", fmt,
                dir, ext, compare, null));
    }

    public static TestSuite suite() {
        PDFTestSuite suite = new PDFTestSuite();
        suite.addTests("PDF");
        return suite;
    }

}
