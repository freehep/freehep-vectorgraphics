// Copyright 2005, FreeHEP.
package org.freehep.graphicsio.ps.test;

import java.awt.Rectangle;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.font.FontRenderContext;
import java.io.*;
import java.util.Calendar;

import org.freehep.graphics2d.font.CharTable;
import org.freehep.graphics2d.font.Lookup;
import org.freehep.graphicsio.font.FontEmbedder;
import org.freehep.graphicsio.font.FontEmbedderType1;


public class TestPSFont {

    public static void main(String[] argv) {

	try {

	    PrintStream out = new PrintStream(new FileOutputStream("fonttest.ps"));

	    Font font = new Font("Monotype Corsiva", Font.PLAIN, 1000);
	    FontRenderContext context = new FontRenderContext(new AffineTransform(1,0,0,-1,0,0),
							      true, true);
	    FontEmbedder fe = new FontEmbedderType1(context, out, false);
	    fe.includeFont(font, Lookup.getInstance().getTable("STDLatin"), "ExampleFont");
    	    out.println();
    	    for (int i = 0; i < 16; i++)
    		out.println("00000000000000000000000000000000");
    	    out.println("cleartomark");
	    out.println("/ExampleFont findfont 26 scalefont setfont");
	    out.println("66 72 moveto");
	    out.println("(dede) show");
	    out.println("/ExampleFont findfont 77 scalefont setfont");
	    out.println("56 126 moveto");
	    out.println("([afbycpd]) show");

	    out.close();

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
