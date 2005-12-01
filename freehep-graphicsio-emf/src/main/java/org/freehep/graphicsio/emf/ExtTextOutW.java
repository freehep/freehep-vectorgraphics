// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Rectangle;
import java.io.IOException;

import org.freehep.util.io.Tag;

/**
 * ExtTextOutW TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/ExtTextOutW.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class ExtTextOutW extends EMFTag implements EMFConstants {

    private Rectangle bounds;
    private int mode;
    private float xScale, yScale;
    private TextW text;

    ExtTextOutW() {
        super(84, 1);
    }

    public ExtTextOutW(Rectangle bounds, int mode, float xScale, float yScale, TextW text) {
        this();
        this.bounds = bounds;
        this.mode = mode;
        this.xScale = xScale;
        this.yScale = yScale;
        this.text = text;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) 
        throws IOException {
    
        ExtTextOutW tag = new ExtTextOutW(emf.readRECTL(), emf.readDWORD(),
                                          emf.readFLOAT(), emf.readFLOAT(),
                                          new TextW(emf));
        return tag;
    }
    
    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
        emf.writeDWORD(mode);
        emf.writeFLOAT(xScale);
        emf.writeFLOAT(yScale);
        text.write(emf);
    }
    
    public String toString() {
        return super.toString()+"\n"+
            "  bounds: "+bounds+"\n"+
            "  mode: "+mode+"\n"+
            "  xScale: "+xScale+"\n"+
            "  yScale: "+yScale+"\n"+
            text.toString();
    }   
}
