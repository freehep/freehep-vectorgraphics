// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.font.truetype;

import java.io.IOException;

/**
 * OS/2 Table.
 *
 *  @author Simon Fischer
 *  @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/font/truetype/TTFOS_2Table.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class TTFOS_2Table extends TTFVersionTable {

    private int version;
    private short xAvgCharWidth;
    private int usWeightClass, usWidthClass;
    private short fsType;
    private short ySubscriptXSize, ySubscriptYSize, ySubscriptXOffset, ySubscriptYOffset;
    private short ySuperscriptXSize, ySuperscriptYSize, ySuperscriptXOffset, ySuperscriptYOffset; 
    private short yStrikeoutSize, yStrikeoutPosition;
    private short sFamilyClass;
    private byte[] panose = new byte[10];
    private long[] ulUnicode = new long[4];
    private byte[] achVendID = new byte[4];
    private int fsSelection;
    private int usFirstCharIndex, usLastCharIndes;
    private int sTypoAscender, sTzpoDescender, sTypoLineGap;
    private int usWinAscent, usWinDescent;
    private long[] ulCodePageRange = new long[2];

    public String getTag() { return "OS/2"; }
    
    public void readTable() throws IOException {

	version = ttf.readUShort();
	xAvgCharWidth = ttf.readShort();
	usWeightClass = ttf.readUShort();
	usWidthClass = ttf.readUShort();
	fsType = ttf.readShort();
	
	ySubscriptXSize = ttf.readShort();
	ySubscriptYSize = ttf.readShort();
	ySubscriptXOffset = ttf.readShort();
	ySubscriptYOffset = ttf.readShort();
	ySuperscriptXSize = ttf.readShort();
	ySuperscriptYSize = ttf.readShort();
	ySuperscriptXOffset = ttf.readShort();
	ySuperscriptYOffset = ttf.readShort();
	yStrikeoutSize = ttf.readShort();
	yStrikeoutPosition = ttf.readShort();
	
	sFamilyClass = ttf.readShort();

	ttf.readFully(panose);
	
	for (int i = 0; i < ulUnicode.length; i++)
	    ulUnicode[i] = ttf.readULong();
	ttf.readFully(achVendID);
	fsSelection = ttf.readUShort();
	
	usFirstCharIndex = ttf.readUShort();
	usLastCharIndes = ttf.readUShort();
	
	sTypoAscender = ttf.readUShort();
	sTzpoDescender = ttf.readUShort();
	sTypoLineGap = ttf.readUShort();

	usWinAscent = ttf.readUShort();
	usWinDescent = ttf.readUShort();
	
	ulCodePageRange[0] = ttf.readULong();
	ulCodePageRange[1] = ttf.readULong();
	
    }

    public String getAchVendID() {
	return new String(achVendID);
    }

    public String toString() {
	return super.toString() + 
	    "\n  version: " + version +
	    "\n  vendor: " + getAchVendID();
    }
}
