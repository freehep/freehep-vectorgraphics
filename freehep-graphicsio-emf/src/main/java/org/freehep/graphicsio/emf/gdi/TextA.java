package org.freehep.graphicsio.emf.gdi;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;

public class TextA extends Text {

    public TextA(Point pos, String string, int options, Rectangle bounds, int[] widths) {
        super(pos, string, options, bounds, widths);
    }

    public static TextA read(EMFInputStream emf) throws IOException {
        Point pos = emf.readPOINTL();
        int sLen = emf.readDWORD();
        /* int sOffset = */ emf.readDWORD();
        int options = emf.readDWORD();
        Rectangle bounds = emf.readRECTL();
        /* int cOffset = */ emf.readDWORD();
        // FIXME: nothing done with offsets
        String string = new String(emf.readBYTE(sLen));
        if ((sLen) % 4 != 0)
            for (int i = 0; i < 4 - (sLen) % 4; i++)
                emf.readBYTE();
        int[] widths = new int[sLen];
        for (int i = 0; i < sLen; i++)
            widths[i] = emf.readDWORD();
        return new TextA(pos, string, options, bounds, widths);
    }

    public void write(EMFOutputStream emf) throws IOException {
        emf.writePOINTL(pos);
        emf.writeDWORD(string.length());
        emf.writeDWORD(8 + 28 + 40); // TagHeader + ExtTextOutA + Text
        emf.writeDWORD(options);
        emf.writeRECTL(bounds);
        int pad = (string.length()) % 4;
        if (pad > 0)
            pad = 4 - pad;
        emf.writeDWORD(8 + 28 + 40 + string.length() + pad); // offset to
                                                                // character
                                                                // spacing array
        emf.writeBYTE(string.getBytes());
        for (int i = 0; i < pad; i++)
            emf.writeBYTE(0);
        for (int i = 0; i < string.length(); i++)
            emf.writeDWORD(widths[i]);
    }

    public String toString() {
        StringBuffer widthsS = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            widthsS.append(",");
            widthsS.append(widths[i]);
        }
        widthsS.append(']');
        widthsS.setCharAt(0, '[');
        return "  TextA\n" + "    pos: " + pos +
            "\n    options: " + options +
            "\n    bounds: " + bounds +
            "\n    string: " + string +
            "\n    widths: " + widthsS;
    }
}
