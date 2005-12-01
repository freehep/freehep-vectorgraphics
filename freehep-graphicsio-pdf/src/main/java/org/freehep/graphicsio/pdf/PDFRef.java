package org.freehep.graphicsio.pdf;

/**
 * This class implements a numbered reference to a PDFObject. Internally the
 * class keeps track of the numbers. The user only sees its logical name. Only
 * generation 0 is used in this PDFWriter, since we do not allow for updates of
 * the PDF file.
 * <p>
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-pdf/src/main/java/org/freehep/graphicsio/pdf/PDFRef.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class PDFRef implements PDFConstants {

    private String name;

    private int objectNumber;

    private int generationNumber;

    PDFRef(String name, int objectNumber, int generationNumber) {
        this.name = name;
        this.objectNumber = objectNumber;
        this.generationNumber = generationNumber;
    }

    public String getName() {
        return name;
    }

    public int getObjectNumber() {
        return objectNumber;
    }

    public int getGenerationNumber() {
        return generationNumber;
    }

    public String toString() {
        return objectNumber + " " + generationNumber + " R";
    }
}