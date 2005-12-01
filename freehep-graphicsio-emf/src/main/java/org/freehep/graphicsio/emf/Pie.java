// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import org.freehep.util.io.Tag;

/**
 * Pie TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/Pie.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class Pie
    extends EMFTag {

    private Rectangle bounds;
    private Point start, end;

    Pie() {
        super(47, 1);
    }

    public Pie(Rectangle bounds, Point start, Point end) {
        this();
        this.bounds = bounds;
        this.start = start;
        this.end = end;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len) 
        throws IOException {
    
        Pie tag = new Pie(emf.readRECTL(), emf.readPOINTL(), emf.readPOINTL());
        return tag;
    }
    
    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeRECTL(bounds);
        emf.writePOINTL(start);
        emf.writePOINTL(end);
    }

    public String toString() {
        return super.toString()+"\n"+
            "  bounds: "+bounds+"\n"+
            "  start: "+start+"\n"+
            "  end: "+end;
    }   
       public Rectangle getBounds() { 
        return bounds;
    }
    public Point getStart() {
            return start;
    }
    public Point getEnd() {
        return end;
    }
}
