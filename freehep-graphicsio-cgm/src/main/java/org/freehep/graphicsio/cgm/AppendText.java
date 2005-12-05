// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * AppendText TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/AppendText.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class AppendText extends CGMTag {

    protected static final int NOT_FINAL = 0;

    protected static final int FINAL = 1;

    protected String text;

    public AppendText() {
        super(4, 6, 1);
    }

    public AppendText(String text) {
        this();
        this.text = text;
    }

    protected AppendText(int elementClass, int elementID, int version) {
        super(elementClass, elementID, version);
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeEnumerate(FINAL);
        cgm.writeString(text);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("APNDTEXT ");
        writeTextPiece(cgm);
    }

    protected void writeTextPiece(CGMWriter cgm) throws IOException {
        cgm.print("FINAL ");
        cgm.writeString(text);
    }
}
