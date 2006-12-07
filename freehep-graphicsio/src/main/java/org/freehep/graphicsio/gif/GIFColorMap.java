// Copyright 2006, FreeHEP.
package org.freehep.graphicsio.gif;

/**
 * Creates colormap from set of pixels, making pixels index into the colormap.
 * 
 * @author duns
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/gif/GIFColorMap.java 850fba8b8008 2006/12/07 00:29:46 duns $
 */
public interface GIFColorMap {
    
    public int[] create(int[][] pixels, int maxColors);
}
