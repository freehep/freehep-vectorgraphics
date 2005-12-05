// Copyright 2001 FreeHEP.
package org.freehep.graphicsio.cgm;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Vector;

import org.freehep.graphicsio.PolylinePathConstructor;
import org.freehep.util.io.TaggedOutput;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-cgm/src/main/java/org/freehep/graphicsio/cgm/CGMPathConstructor.java 278fac7cefaa 2005/12/05 04:00:43 duns $
 */
public class CGMPathConstructor extends PolylinePathConstructor {
    private TaggedOutput os;

    private AffineTransform matrix;

    public CGMPathConstructor(TaggedOutput os, boolean fill,
            AffineTransform matrix, double resolution) {
        super(fill, resolution);
        this.os = os;
        this.matrix = matrix;
    }

    protected void writePolyline(Vector polyline) throws IOException {
        int n = polyline.size();
        Point2D[] src = new Point2D[n];
        polyline.copyInto(src);
        Point2D[] dst = new Point2D[n];
        matrix.transform(src, 0, dst, 0, n);
        if (fill) {
            os.writeTag(new Polygon(dst));
        } else if (closed) {
            os.writeTag(new EdgeVisibility(true));
            os.writeTag(new InteriorStyle(InteriorStyle.HOLLOW));
            os.writeTag(new Polygon(dst));
            os.writeTag(new InteriorStyle(InteriorStyle.SOLID));
            os.writeTag(new EdgeVisibility(false));
        } else {
            os.writeTag(new Polyline(dst));
        }
    }

}
