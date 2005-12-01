package org.freehep.graphicsio.pdf;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * Implements the Outline Dictionary (see Table 7.3).
 * <p>
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/main/java/org/freehep/graphicsio/pdf/PDFOutlineList.java 967bf3619090 2005/12/01 05:41:40 duns $
 */
public class PDFOutlineList extends PDFDictionary {
    
    PDFOutlineList(PDF pdf, PDFByteWriter writer, PDFObject object, PDFRef first, PDFRef last) throws IOException {
        super(pdf, writer, object);
        entry("Type", pdf.name("Outlines"));
        entry("First", first);
        entry("Last", last);
    }
    
    public void setCount(int count) throws IOException {
        entry("Count", count);
    }    
}
