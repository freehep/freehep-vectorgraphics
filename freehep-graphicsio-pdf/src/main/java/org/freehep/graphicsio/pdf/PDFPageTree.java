package org.freehep.graphicsio.pdf;

import java.io.IOException;
import java.util.Vector;

/**
 * Implements the Page Tree Node (see Table 3.16).
 * <p>
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/main/java/org/freehep/graphicsio/pdf/PDFPageTree.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class PDFPageTree extends PDFPageBase {

    Vector<PDFRef> pages = new Vector<PDFRef>();

    PDFPageTree(PDF pdf, PDFByteWriter writer, PDFObject object, PDFRef parent)
            throws IOException {
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
