// Copyright 2000-2003 FreeHEP
package org.freehep.graphicsio.java;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-java/src/main/java/org/freehep/graphicsio/java/JAVAGeneralPath.java 4d2239eafa4d 2007/06/07 18:07:57 duns $
 */
public class JAVAGeneralPath implements Shape {
    private int rule;

    private PathElement[] path;

    private float minX, minY = Float.MAX_VALUE;

    private float maxX, maxY = Float.MIN_VALUE;

    public JAVAGeneralPath(int rule, PathElement[] path) {
        this.rule = rule;
        this.path = path;
        for (int i = 0; i < path.length; i++) {
            minX = Math.min(minX, path[i].getMinX());
            maxX = Math.max(maxX, path[i].getMaxX());
            minY = Math.min(minY, path[i].getMinY());
            maxY = Math.max(maxY, path[i].getMaxY());
        }
    }

    public static abstract class PathElement {
        public abstract float getMinX();

        public abstract float getMaxX();

        public abstract float getMinY();

        public abstract float getMaxY();

        public abstract int currentSegment(float[] coords);

        public abstract int currentSegment(double[] coords);
    }

    public static abstract class Point extends PathElement {
        private float x, y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getMinX() {
            return x;
        }

        public float getMaxX() {
            return x;
        }

        public float getMinY() {
            return y;
        }

        public float getMaxY() {
            return y;
        }

        protected void fill(float[] coords) {
            coords[0] = x;
            coords[1] = y;
        }

        protected void fill(double[] coords) {
            coords[0] = x;
            coords[1] = y;
        }
    }

    public static class MoveTo extends Point {
        public MoveTo(float x, float y) {
            super(x, y);
        }

        public int currentSegment(float[] coords) {
            fill(coords);
            return PathIterator.SEG_MOVETO;
        }

        public int currentSegment(double[] coords) {
            fill(coords);
            return PathIterator.SEG_MOVETO;
        }
    }

    public static class LineTo extends Point {
        public LineTo(float x, float y) {
            super(x, y);
        }

        public int currentSegment(float[] coords) {
            fill(coords);
            return PathIterator.SEG_LINETO;
        }

        public int currentSegment(double[] coords) {
            fill(coords);
            return PathIterator.SEG_LINETO;
        }
    }

    public static class QuadTo extends PathElement {
        private float x1, y1, x2, y2;

        public QuadTo(float x1, float y1, float x2, float y2) {
            super();
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public float getMinX() {
            return Math.min(x1, x2);
        }

        public float getMaxX() {
            return Math.max(x1, x2);
        }

        public float getMinY() {
            return Math.min(y1, y2);
        }

        public float getMaxY() {
            return Math.max(y1, y2);
        }

        public int currentSegment(float[] coords) {
            coords[0] = x1;
            coords[1] = y1;
            coords[2] = x2;
            coords[3] = y2;
            return PathIterator.SEG_QUADTO;
        }

        public int currentSegment(double[] coords) {
            coords[0] = x1;
            coords[1] = y1;
            coords[2] = x2;
            coords[3] = y2;
            return PathIterator.SEG_QUADTO;
        }
    }

    public static class CurveTo extends PathElement {
        private float x1, y1, x2, y2, x3, y3;

        public CurveTo(float x1, float y1, float x2, float y2, float x3,
                float y3) {
            super();
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.x3 = x3;
            this.y3 = y3;
        }

        public float getMinX() {
            return Math.min(x1, Math.min(x2, x3));
        }

        public float getMaxX() {
            return Math.max(x1, Math.max(x2, x3));
        }

        public float getMinY() {
            return Math.min(y1, Math.min(y2, y3));
        }

        public float getMaxY() {
            return Math.max(y1, Math.max(y2, y3));
        }

        public int currentSegment(float[] coords) {
            coords[0] = x1;
            coords[1] = y1;
            coords[2] = x2;
            coords[3] = y2;
            coords[4] = x3;
            coords[5] = y3;
            return PathIterator.SEG_CUBICTO;
        }

        public int currentSegment(double[] coords) {
            coords[0] = x1;
            coords[1] = y1;
            coords[2] = x2;
            coords[3] = y2;
            coords[4] = x3;
            coords[5] = y3;
            return PathIterator.SEG_CUBICTO;
        }
    }

    public static class ClosePath extends PathElement {
        public ClosePath() {
            super();
        }

        public float getMinX() {
            return Float.MAX_VALUE;
        }

        public float getMaxX() {
            return Float.MIN_VALUE;
        }

        public float getMinY() {
            return Float.MAX_VALUE;
        }

        public float getMaxY() {
            return Float.MIN_VALUE;
        }

        public int currentSegment(float[] coords) {
            return PathIterator.SEG_CLOSE;
        }

        public int currentSegment(double[] coords) {
            return PathIterator.SEG_CLOSE;
        }
    }

    public Rectangle getBounds() {
        return getBounds2D().getBounds();
    }

    public Rectangle2D getBounds2D() {
        return new Rectangle2D.Float(minX, minY, maxX - minX, maxY - minY);
    }

    // approximation
    public boolean contains(double x, double y) {
        return getBounds2D().contains(x, y);
    }

    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    // approximation
    public boolean intersects(double x, double y, double w, double h) {
        return getBounds2D().intersects(x, y, w, h);
    }

    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    // approximation
    public boolean contains(double x, double y, double w, double h) {
        return getBounds2D().contains(x, y, w, h);
    }

    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public PathIterator getPathIterator(final AffineTransform at) {

        return new PathIterator() {
            private int index = 0;

            private AffineTransform transform = at;

            public int getWindingRule() {
                return rule;
            }

            public boolean isDone() {
                return index >= path.length;
            }

            public void next() {
                if (!isDone())
                    index++;
            }

            public int currentSegment(float[] coords) {
                int type = path[index].currentSegment(coords);
                if (transform != null) transform.transform(coords, 0, coords, 0, coords.length / 2);
                return type;
            }

            public int currentSegment(double[] coords) {
                int type = path[index].currentSegment(coords);
                if (transform != null) transform.transform(coords, 0, coords, 0, coords.length / 2);
                return type;
            }
        };
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new FlatteningPathIterator(getPathIterator(at), flatness);
    }
}
