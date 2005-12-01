// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

/**
 * EMF Text
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/Text.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class Text implements EMFConstants {

    private Point pos;
    private String string;
    private int options;
    private int[] widths;
    private Rectangle bounds;
    
    public Text(Point pos, String string, int options, Rectangle bounds, int[] widths) {
        this.pos = pos;
        this.string = string;
        this.options = options;
        this.bounds = bounds;
        this.widths = widths;
    }

    Text(EMFInputStream emf) throws IOException {
        pos = emf.readPOINTL();
        int sLen = emf.readDWORD();
        int sOffset = emf.readDWORD();
        options = emf.readDWORD();
        bounds = emf.readRECTL();
        int cOffset = emf.readDWORD();
        // FIXME: nothing done with offsets
        string = new String(emf.readBYTE(sLen));
        if ((sLen)%4 != 0) for (int i=0; i<4-(sLen)%4; i++) emf.readBYTE();
        widths = new int[sLen];
        for (int i=0; i<sLen; i++) widths[i] = emf.readDWORD();
    }

    public void write(EMFOutputStream emf) throws IOException {
        emf.writePOINTL(pos);
        emf.writeDWORD(string.length());
        emf.writeDWORD(8 + 28 + 40); // TagHeader + ExtTextOutA + Text
        emf.writeDWORD(options);
        emf.writeRECTL(bounds);
        int pad = (string.length())%4;
        if (pad > 0) pad = 4-pad;
        emf.writeDWORD(8 + 28 + 40 + string.length() + pad); // offset to character spacing array
        emf.writeBYTE(string.getBytes());
        for (int i=0; i<pad; i++) emf.writeBYTE(0);
        for (int i=0;i<string.length(); i++) emf.writeDWORD(widths[i]);
    }
    
    public String toString() {
       StringBuffer widthsS = new StringBuffer();
       for (int i=0; i<string.length(); i++) widthsS.append(","+widths[i]);
       widthsS.append(']');
       widthsS.setCharAt(0,'[');
        return "  Text\n"+
               "    pos: "+pos+"\n"+
               "    options: "+options+"\n"+
               "    bounds: "+bounds+"\n"+
               "    string: "+string+"\n"+
               "    widths: "+widthsS;
    }
}
