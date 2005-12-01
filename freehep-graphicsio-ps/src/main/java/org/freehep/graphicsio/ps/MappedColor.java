// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphicsio.ps;

import java.awt.Color;

/**
 *
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/MappedColor.java 2689041eec29 2005/12/01 22:37:27 duns $
 */
public class MappedColor
    extends java.awt.Color {

    /**
     * Index of the color in the color map. */
    protected int colorIndex;

    /**
     * Value of the brightness/darkness of this color. Larger values
     * indicate darker colors and vice versa. */
    protected int brightness;

    /**
     * Constructor takes the RGB color components and the color index. */
    public MappedColor(int r, int g, int b,
		       int colorIndex) {

	// Call the Color constructor.
	super(r,g,b);

	// Set the color index and brightness.
	this.colorIndex = colorIndex;
	brightness = 0;
    }

    /**
     * Constructor takes the RGB color components, the color index,
     * and the brightness. */
    public MappedColor(int r, int g, int b,
		       int colorIndex,
		       int brightness) {

	// Call the Color constructor.
	super(r,g,b);

	// Set the color index and brightness.
	this.colorIndex = colorIndex;
	this.brightness = brightness;
    }

    /**
     * Make a brightened color based on this color. */
    public Color brighter() {

	int r = getRed()*10/7;
	int g = getGreen()*10/7;
	int b = getBlue()*10/7;

	r = (int) Math.max(0,Math.min(255,r));
	g = (int) Math.max(0,Math.min(255,g));
	b = (int) Math.max(0,Math.min(255,b));

	return (Color) new MappedColor(r,g,b,colorIndex,brightness-1);
    }

    /**
     * Make a darkened color based on this color. */
    public Color darker() {

	int r = getRed()*7/10;
	int g = getGreen()*7/10;
	int b = getBlue()*7/10;

	r = (int) Math.max(0,Math.min(255,r));
	g = (int) Math.max(0,Math.min(255,g));
	b = (int) Math.max(0,Math.min(255,b));

	return (Color) new MappedColor(r,g,b,colorIndex,brightness-1);
    }

    /**
     * Get the brightness of this color. */
    public int getBrightness() {
	return brightness;
    }

    /**
     * Get the tag associated with this color. */
    public String getColorTag() {
	return ColorMap.getTag(colorIndex);
    }

}
