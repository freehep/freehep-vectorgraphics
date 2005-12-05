// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.cgm;

import java.io.IOException;

/**
 * CharacterCodingAnnouncer TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CharacterCodingAnnouncer.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CharacterCodingAnnouncer extends CGMTag {

    public final static int BASIC_7 = 0;

    public final static int BASIC_8 = 1;

    public final static int EXTENDED_7 = 2;

    public final static int EXTENDED_8 = 3;

    private int type;

    public CharacterCodingAnnouncer() {
        super(1, 15, 1);
    }

    public CharacterCodingAnnouncer(int type) {
        this();
        this.type = type;
    }

    public void write(int tagID, CGMOutputStream cgm) throws IOException {
        cgm.writeInteger(type);
    }

    public void write(int tagID, CGMWriter cgm) throws IOException {
        cgm.print("CHARCODING ");
        switch (type) {
        default:
        case BASIC_7:
            cgm.print("BASIC7BIT");
            break;
        case BASIC_8:
            cgm.print("BASIC8BIT");
            break;
        case EXTENDED_7:
            cgm.print("EXTD7BIT");
            break;
        case EXTENDED_8:
            cgm.print("EXTD8BIT");
            break;
        }
    }
}
