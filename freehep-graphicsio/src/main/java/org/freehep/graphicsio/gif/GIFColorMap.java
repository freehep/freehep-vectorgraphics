// Copyright 2006, FreeHEP.
package org.freehep.graphicsio.gif;

/**
 * Creates colormap from set of pixels, making pixels index into the colormap.
 * 
 * @author duns
 * @versionb $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/gif/GIFColorMap.java ebb3d89d5caf 2006/11/23 01:05:05 duns $
 */
public interface GIFColorMap {
    
    public int[] create(int[][] pixels, int maxColors);
}
