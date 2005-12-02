// Copyright 2001-2005 FreeHEP

package org.freehep.graphicsio.ps.test;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.freehep.graphics2d.font.CharTable;
import org.freehep.graphics2d.font.Lookup;
import org.freehep.graphicsio.font.FontIncluder;
import org.freehep.graphicsio.ps.PSFontEmbedder;

/**
 * TestClass for PSFontEmbedder.
 * 
 * @author Sami Kama
 * @version $Id: freehep-graphicsio-ps/src/test/java/org/freehep/graphicsio/ps/test/TestPSFontEmbedder.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */

public class TestPSFontEmbedder {
    protected PrintStream os;

    public void openFile() throws IOException {
        this.os = new PrintStream(new FileOutputStream("FontEmbedderPSTest.ps"));
    }

    public void writeFont() throws IOException {

        CharTable table = Lookup.getInstance().getTable("STDLatin");
        // System.out.println(table);
        AffineTransform aff = new AffineTransform(1, 0, 0, -1, 0, 0);

        FontRenderContext context = new FontRenderContext(aff, false, false);
        // System.out.println(context);
        // Font f = new Font("Lucida Sans Unicode", Font.PLAIN, 1000);
        Font f = new Font("Arial", Font.PLAIN, 1000);
        // System.out.println(f);
        FontIncluder fe = new PSFontEmbedder(context, os);

        // System.out.println(fe.getUnicode());
        // System.out.println("start include font");
        // ??????? fe.includeFont(f, table, "STDLatin");
        fe.includeFont(f, table, "ExampleFont");
        // System.out.println(fe.getNODefinedChars());
    }

    public void closeFile() throws IOException {
        // ??????? os.println("\t/ExampleFont exch definefont pop");
        os.println("/ExampleFont findfont 26 scalefont setfont");
        os.println("66 72 moveto");
        os.println("(ABC abc) show");
        os.flush();
        os.close();
    }

    public static void main(String[] args) throws IOException {
        TestPSFontEmbedder test = new TestPSFontEmbedder();
        test.openFile();
        test.writeFont();
        test.closeFile();
    }
}
