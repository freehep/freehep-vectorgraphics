// Copyright 2001 freehep
package org.freehep.graphicsio;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.io.IOException;

/**
 * Implements some of the PathConstructor functionality
 *  @author Mark Donszelmann
 *  @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/AbstractPathConstructor.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public abstract class AbstractPathConstructor implements PathConstructor {

    protected double currentX, currentY;

    protected AbstractPathConstructor() {
        currentX = 0;
        currentY = 0;
    }

    public void flush() throws IOException {
        currentX = 0;
        currentY = 0;
    }

    public boolean addPath(Shape s) throws IOException {
        return addPath(s, null);
    }

    public boolean addPath(Shape s, AffineTransform transform) throws IOException {
        return addPath(this, s, transform);
    }

    public static boolean addPath(PathConstructor out, Shape s, AffineTransform transform) throws IOException {
        PathIterator path = s.getPathIterator(transform);
        double[] coords = new double[6];
        double currentX = 0.;
        double currentY = 0.;
        double pathStartX = 0.;
        double pathStartY = 0.;
        while (!path.isDone()) {
            int segType = path.currentSegment(coords);

            switch (segType) {
                case PathIterator.SEG_MOVETO:
                    out.move(coords[0], coords[1]);
                    pathStartX = currentX = coords[0];
                    pathStartY = currentY = coords[1];
                    break;
                case PathIterator.SEG_LINETO:
                    out.line(coords[0], coords[1]);
                    currentX = coords[0];
                    currentY = coords[1];
                    break;
                case PathIterator.SEG_QUADTO:
                    out.quad(coords[0], coords[1],
                             coords[2], coords[3]);
                    currentX = coords[2];
                    currentY = coords[3];
                    break;
                case PathIterator.SEG_CUBICTO:
                    out.cubic(coords[0], coords[1],
                              coords[2], coords[3],
                              coords[4], coords[5]);
                    currentX = coords[4];
                    currentY = coords[5];
                    break;
                case PathIterator.SEG_CLOSE:
                    out.closePath(pathStartX, pathStartY);
                    currentX = pathStartX;
                    currentY = pathStartY;
                    break;
            }
            // Move to the next segment.
            path.next();
        }
        out.flush();
        return (path.getWindingRule()==PathIterator.WIND_EVEN_ODD);
    }

    public static boolean isEvenOdd(Shape s) {
        return s.getPathIterator(null).getWindingRule()==PathIterator.WIND_EVEN_ODD;
    }
}
