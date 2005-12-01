// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf;
import java.awt.Rectangle;
import java.io.IOException;
import org.freehep.util.io.Tag;

/**
 * Ellipse TAG.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/Ellipse.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class Ellipse extends EMFTag {
    private Rectangle bounds;

    public Ellipse( Rectangle bounds ) {
        this(  );
        this.bounds = bounds;
    }

    Ellipse(  ) {
        super( 42, 1 );
    }

    public EMFTag read( int tagID, EMFInputStream emf, int len )
        throws IOException {
        Ellipse tag = new Ellipse( emf.readRECTL(  ) );

        return tag;
    }

    public void write( int tagID, EMFOutputStream emf )
        throws IOException {
        emf.writeRECTL( bounds );
    }

    public String toString(  ) {
        return super.toString(  ) + "\n" + "  bounds: " + bounds;
    }

    public Rectangle getBounds(  ) {
        return bounds;
    }
}
