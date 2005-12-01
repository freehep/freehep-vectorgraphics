package org.freehep.graphicsio.pdf;

/**
 * Specifies a PDFName object.
 * <p>
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/main/java/org/freehep/graphicsio/pdf/PDFName.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class PDFName implements PDFConstants {

    private String name;

    PDFName(String name) {
        this.name = name;
    }

    public String toString() {
        return "/" + name;
    }
}