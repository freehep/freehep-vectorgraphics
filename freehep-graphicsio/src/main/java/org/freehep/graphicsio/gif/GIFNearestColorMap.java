// Copyright 2006, FreeHEP.
package org.freehep.graphicsio.gif;

/**
 * Reduces the number of colors by looking for the nearest colors.
 * 
 * @author duns
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/gif/GIFNearestColorMap.java ebb3d89d5caf 2006/11/23 01:05:05 duns $
 */
public class GIFNearestColorMap implements GIFColorMap {

    public int[] create(int[][] pixels, int maxColors) {
        return Quantize.quantizeImage(pixels, maxColors);
    }
}
