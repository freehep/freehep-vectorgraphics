package org.freehep.graphicsio.pdf;

/**
 * Specifies a PDFName object.
 * <p>
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/main/java/org/freehep/graphicsio/pdf/PDFName.java 967bf3619090 2005/12/01 05:41:40 duns $
 */
public class PDFName implements PDFConstants {
    
    private String name;
    
    PDFName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return "/"+name;
    }
}