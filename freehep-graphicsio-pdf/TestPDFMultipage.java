package org.freehep.graphicsio.pdf.test;

import org.freehep.graphics2d.*;

import java.awt.*;
import java.io.*;

import org.freehep.graphicsio.pdf.PDFGraphics2D;

public class TestPDFMultipage {

    public static void main(String[] argv) throws Exception {
    	PDFGraphics2D graphics = new PDFGraphics2D(new FileOutputStream("multi.pdf"), new Dimension(400, 600));
    	graphics.setMultiPage(true);

    	Font font = new Font("Serif", Font.PLAIN, 12);

    	graphics.startExport();
    	graphics.setHeader(font,
    			   new TagString("TEST DOCUMENT"),
    			   null,
    			   new TagString("<i>Multipage</i>"),
    			   1);
    	graphics.setFooter(font,
    			   null,
    			   new TagString("- %P% -"),
    			   null,
    			   -1);

    	graphics.openPage(new Dimension(200, 400), "Page 1");
    	graphics.setColor(Color.black);
    	graphics.setFont(font);
    	graphics.drawRect(50, 100, 150, 20);
    	graphics.drawString("Page 1", 60., 115);
    	graphics.closePage();

    	graphics.openPage(new Dimension(200, 400), "Page 2");
    	graphics.setColor(Color.black);
    	graphics.setFont(new Font("SansSerif", Font.PLAIN, 12));
    	graphics.drawRect(50, 100, 150, 20);
    	graphics.drawString("Page 2", 60., 115);
    	graphics.drawString(new TagString("This is <tt>Page 2</tt> of 2"), 40.,150);
    	graphics.closePage();

    	graphics.endExport();

    	System.exit(0);
    }
}
