// Copyright 2007, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import org.freehep.graphicsio.emf.EMFConstants;

import java.util.logging.Logger;
import java.awt.BasicStroke;

/**
 * @author Steffen Greiffenberg
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/AbstractPen.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public abstract class AbstractPen implements EMFConstants, GDIObject {

    /**
     * logger for all instances
     */
    private static final Logger logger = Logger.getLogger("org.freehep.graphicsio.emf");

    /**
     * returns a BasicStroke JOIN for an EMF pen style
     * @param penStyle penstyle
     * @return e.g. {@link java.awt.BasicStroke#JOIN_MITER}
     */
    protected int getJoin(int penStyle) {
        switch (penStyle & 0xF000) {
            case EMFConstants.PS_JOIN_ROUND:
                return BasicStroke.JOIN_ROUND;
            case EMFConstants.PS_JOIN_BEVEL:
                return BasicStroke.JOIN_BEVEL;
            case EMFConstants.PS_JOIN_MITER:
                return BasicStroke.JOIN_MITER;
            default:
                logger.warning("got unsupported pen style " + penStyle);
                return BasicStroke.JOIN_ROUND;
        }
    }

    /**
     * returns a BasicStroke JOIN for an EMF pen style
     * @param penStyle Style to convert
     * @return asicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE, BasicStroke.CAP_BUTT
     */
    protected int getCap(int penStyle) {
        switch (penStyle & 0xF00) {
            case EMFConstants.PS_ENDCAP_ROUND:
                return BasicStroke.CAP_ROUND;
            case EMFConstants.PS_ENDCAP_SQUARE:
                return BasicStroke.CAP_SQUARE;
            case EMFConstants.PS_ENDCAP_FLAT:
                return BasicStroke.CAP_BUTT;
            default:
                logger.warning("got unsupported pen style " + penStyle);
                return BasicStroke.CAP_ROUND;
        }
    }

    /**
     * returns a Dash for an EMF pen style
     * @param penStyle Style to convert
     * @param style used if EMFConstants#PS_USERSTYLE is set
     * @return float[] representing a dash
     */
    protected float[] getDash(int penStyle, int[] style) {
        switch (penStyle & 0xFF) {
            case EMFConstants.PS_SOLID:
                // do not use float[] { 1 }
                // it's _slow_
                return null;
            case EMFConstants.PS_DASH:
                return new float[] { 5, 5 };
            case EMFConstants.PS_DOT:
                return new float[] { 1, 2 };
            case EMFConstants.PS_DASHDOT:
                return new float[] { 5, 2, 1, 2 };
            case EMFConstants.PS_DASHDOTDOT:
                return new float[] { 5, 2, 1, 2, 1, 2 };
            case EMFConstants.PS_NULL:
                // do not use float[] { 1 }
                // it's _slow_
                return null;
            case EMFConstants.PS_USERSTYLE:
                if (style != null && style.length > 0) {
                    float[] result = new float[style.length];
                    for (int i = 0; i < style.length; i++) {
                        result[i] = style[i];
                    }
                    return result;
                } else {
                    return null;
                }
            default:
                logger.warning("got unsupported pen style " + penStyle);
                // do not use float[] { 1 }
                // it's _slow_
                return null;
        }
    }
}
