// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * FontList TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/FontList.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class FontList extends CGMTag {

    private String[] names;

    public FontList() {
        super(1, 13, 1);
    }

    public FontList(String[] names) {
        this();
        this.names = names;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {

        for (int i = 0; i < names.length; i++) {
            cgm.writeString(names[i]);
        }
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.println("FONTLIST");
        cgm.indent();
        for (int i = 0; i < names.length; i++) {
            cgm.writeString(names[i]);
            cgm.println();
        }
        cgm.outdent();
    }

}
