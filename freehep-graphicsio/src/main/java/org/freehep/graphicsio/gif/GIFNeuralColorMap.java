// Copyright 2006, FreeHEP.
package org.freehep.graphicsio.gif;

/**
 * 
 * @author duns
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/gif/GIFNeuralColorMap.java 7a2e6daa0f4f 2006/11/26 22:00:48 duns $
 */
public class GIFNeuralColorMap implements GIFColorMap {

	public int[] create(int[][] pixels, int maxColors) {
		NeuQuant quantizer = new NeuQuant(1, pixels);
		quantizer.init();

        // convert to indexed color
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++ ) {
            	if ((pixels[x][y] & 0xFF000000) == 0) {
            		pixels[x][y] = 0;
            	} else {
            	    pixels[x][y] = quantizer.lookup(pixels[x][y]);
            	}
            }
        }
        
		return quantizer.getColorMap();
	}

}
