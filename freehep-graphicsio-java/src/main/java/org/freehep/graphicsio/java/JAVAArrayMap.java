// Copyright 2007, FreeHEP
package org.freehep.graphicsio.java;

import java.util.HashMap;
import java.awt.RenderingHints;

/**
 * Hashmap that can be constructed from an Array
 *
 * @author Steffen Greiffenberg
 * @version $Revision$
 */
public class JAVAArrayMap extends HashMap {

    /**
     * Stores the string representation for JDK 1.5 Rendering Hints
     */
    public static HashMap HINTS = new HashMap();
    static {
        HINTS.put(RenderingHints.KEY_ANTIALIASING,"RenderingHints.KEY_ANTIALIASING");
        HINTS.put(RenderingHints.VALUE_ANTIALIAS_ON,"RenderingHints.VALUE_ANTIALIAS_ON");
        HINTS.put(RenderingHints.VALUE_ANTIALIAS_OFF,"RenderingHints.VALUE_ANTIALIAS_OFF");
        HINTS.put(RenderingHints.VALUE_ANTIALIAS_DEFAULT,"RenderingHints.VALUE_ANTIALIAS_DEFAULT");

        HINTS.put(RenderingHints.KEY_RENDERING,"RenderingHints.KEY_RENDERING");
        HINTS.put(RenderingHints.VALUE_RENDER_SPEED,"RenderingHints.VALUE_RENDER_SPEED");
        HINTS.put(RenderingHints.VALUE_RENDER_QUALITY,"RenderingHints.VALUE_RENDER_QUALITY");
        HINTS.put(RenderingHints.VALUE_RENDER_DEFAULT,"RenderingHints.VALUE_RENDER_DEFAULT");

        HINTS.put(RenderingHints.KEY_DITHERING,"RenderingHints.KEY_DITHERING");
        HINTS.put(RenderingHints.VALUE_DITHER_DISABLE,"RenderingHints.VALUE_DITHER_DISABLE");
        HINTS.put(RenderingHints.VALUE_DITHER_ENABLE,"RenderingHints.VALUE_DITHER_ENABLE");
        HINTS.put(RenderingHints.VALUE_DITHER_DEFAULT,"RenderingHints.VALUE_DITHER_DEFAULT");

        HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING,"RenderingHints.KEY_TEXT_ANTIALIASING");
        HINTS.put(RenderingHints.VALUE_TEXT_ANTIALIAS_ON,"RenderingHints.VALUE_TEXT_ANTIALIAS_ON");
        HINTS.put(RenderingHints.VALUE_TEXT_ANTIALIAS_OFF,"RenderingHints.VALUE_TEXT_ANTIALIAS_OFF");
        HINTS.put(RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT,"RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT");

        HINTS.put(RenderingHints.KEY_FRACTIONALMETRICS,"RenderingHints.KEY_FRACTIONALMETRICS");
        HINTS.put(RenderingHints.VALUE_FRACTIONALMETRICS_OFF,"RenderingHints.VALUE_FRACTIONALMETRICS_OFF");
        HINTS.put(RenderingHints.VALUE_FRACTIONALMETRICS_ON,"RenderingHints.VALUE_FRACTIONALMETRICS_ON");
        HINTS.put(RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT,"RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT");

        HINTS.put(RenderingHints.KEY_INTERPOLATION,"RenderingHints.KEY_INTERPOLATION");
        HINTS.put(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR,"RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR");
        HINTS.put(RenderingHints.VALUE_INTERPOLATION_BILINEAR,"RenderingHints.VALUE_INTERPOLATION_BILINEAR");
        HINTS.put(RenderingHints.VALUE_INTERPOLATION_BICUBIC,"RenderingHints.VALUE_INTERPOLATION_BICUBIC");

        HINTS.put(RenderingHints.KEY_ALPHA_INTERPOLATION,"RenderingHints.KEY_ALPHA_INTERPOLATION");
        HINTS.put(RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED,"RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED");
        HINTS.put(RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY,"RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY");
        HINTS.put(RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT,"RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT");

        HINTS.put(RenderingHints.KEY_COLOR_RENDERING,"RenderingHints.KEY_COLOR_RENDERING");
        HINTS.put(RenderingHints.VALUE_COLOR_RENDER_SPEED,"RenderingHints.VALUE_COLOR_RENDER_SPEED");
        HINTS.put(RenderingHints.VALUE_COLOR_RENDER_QUALITY,"RenderingHints.VALUE_COLOR_RENDER_QUALITY");
        HINTS.put(RenderingHints.VALUE_COLOR_RENDER_DEFAULT,"RenderingHints.VALUE_COLOR_RENDER_DEFAULT");

        HINTS.put(RenderingHints.KEY_STROKE_CONTROL,"RenderingHints.KEY_STROKE_CONTROL");
        HINTS.put(RenderingHints.VALUE_STROKE_DEFAULT,"RenderingHints.VALUE_STROKE_DEFAULT");
        HINTS.put(RenderingHints.VALUE_STROKE_NORMALIZE,"RenderingHints.VALUE_STROKE_NORMALIZE");
        HINTS.put(RenderingHints.VALUE_STROKE_PURE,"RenderingHints.VALUE_STROKE_PURE");
    }

    /**
     * writes the array into this
     *
     * @param elements array with [key, value, key, value ...]
     */
    public JAVAArrayMap(Object[] elements) {
        super(elements.length / 2);
        for (int i = 0; i < elements.length; i = i + 2) {
            put(elements[i], elements[i + 1]);
        }
    }
}
