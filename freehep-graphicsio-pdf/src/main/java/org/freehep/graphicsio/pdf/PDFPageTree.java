package org.freehep.graphicsio.pdf;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Implements the Page Tree Node (see Table 3.16).
 * <p>
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/main/java/org/freehep/graphicsio/pdf/PDFPageTree.java 967bf3619090 2005/12/01 05:41:40 duns $
 */
public class PDFPageTree extends PDFPageBase {
    
    Vector pages = new Vector();
    
    PDFPageTree(PDF pdf, PDFByteWriter writer, PDFObject object, PDFRef parent) throws IOException {
        super(pdf, writer, object, parent);
        entry("Type", pdf.name("Pages"));
    }
    
    public void addPage(String name) {
        pages.add(pdf.ref(name));
    }    
    
    void close() throws IOException {
        Object[] kids = new Object[pages.size()];
        pages.copyInto(kids);
        entry("Kids", kids);
        entry("Count", kids.length);
        super.close();
    }
}
