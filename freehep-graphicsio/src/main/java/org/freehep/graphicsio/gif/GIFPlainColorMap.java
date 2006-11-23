// Copyright 2006, FreeHEP.
package org.freehep.graphicsio.gif;

/**
 * Just maps the colors straight into a map.
 * 
 * Slow implementation...
 * 
 * @author duns
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/gif/GIFPlainColorMap.java ebb3d89d5caf 2006/11/23 01:05:05 duns $
 */
public class GIFPlainColorMap implements GIFColorMap {
    
    public int[] create(int[][] pixels, int maxColors) {
        int[] colors = new int[maxColors];
        int n = 0;
        
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[x].length; y++) {
                int i = 0;
                while (i<n) {
                    if (colors[i] == pixels[x][y]) break;
                    i++;
                }
                if (i==n) {
                    if (i >= maxColors) throw new IllegalArgumentException("GIF: Too many colors > "+maxColors);
                    colors[i] = pixels[x][y];
                    n++;
                }
                pixels[x][y] = i;
            }
        }
        
        return colors;
    }

}
