// Copyright 2001-2005, freehep
package org.freehep.graphicsio.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphics2d.font.CharTable;
import org.freehep.graphics2d.font.Lookup;

/**
 * @author Simon Fischer
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestFonts.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestFonts extends TestingPanel {

    public TestFonts(String[] args) throws Exception {
        super(args);
        setName("Fonts");
    }

    public void paintComponent(Graphics g) {

        if (g == null)
            return;

        VectorGraphics vg = VectorGraphics.create(g);

        Dimension dim = getSize();
        Insets insets = getInsets();

        vg.setColor(Color.white);
        vg.fillRect(insets.left, insets.top, dim.width - insets.left
                - insets.right, dim.height - insets.top - insets.bottom);

//        int dw = dim.width / 5;
        int dh = dim.height / 8;

        vg.setColor(Color.black);
        Font serif = new Font("Serif", Font.PLAIN, 12);
        Font sansserif = new Font("SansSerif", Font.PLAIN, 12);
        Font dialog = new Font("Dialog", Font.PLAIN, 12);
        Font monospaced = new Font("Monospaced", Font.PLAIN, 12);

        // Font standard = new Font("TimesRoman", Font.PLAIN, 12);
        // Font standard = new Font("Impact", Font.PLAIN, 12);
        Font standard = serif;

        // Font standard = new Font("Lucida Times Unicode", Font.PLAIN, 12);
        // Font embedded = new Font("ZapfChancery", Font.PLAIN, 12);
        // Font embedded = new Font("Impact", Font.PLAIN, 12);
        // Font embedded = new Font("Arial", Font.PLAIN, 12);
        // Font standard = new Font("Lucida Sans", Font.PLAIN, 12);
        Font embedded = new Font("Monotype Corsiva", Font.PLAIN, 12);
        // Font embedded = new Font("Lucida Sans", Font.PLAIN, 12);
        // Font embedded = new Font("ZapfDingbats", Font.PLAIN, 12);

        vg.setFont(standard);
        vg.drawString("[" + standard + "]", 10, 20);
        vg.drawLine(10, 40, 200, 40);
        vg.drawString("This font is a standard font: " + standard.getName()
                + ".", 10, 40);

        vg.setFont(embedded);
        vg.drawString("[" + embedded + "]", 10, dh);
        vg.drawLine(10, dh + 20, 200, dh + 20);
        vg.drawString("This font is a special font: " + embedded.getName()
                + ".", 10, dh + 20);

        CharTable table = Lookup.getInstance().getTable("PDFLatin");

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 32; j++) {
                int c = 32 * i + j;
                String name = table.toName(c);
                char ch = '?';
                if (name != null) {
                    ch = table.toUnicode(name);
                }

                vg.setFont(standard);
                vg.drawString(" " + ch, 10 + j * 15, i * 35 + 2 * dh);
                vg.setFont(embedded);
                vg.drawString(" " + ch, 10 + j * 15, i * 35 + 15 + 2 * dh);
            }
        }

        vg.setFont(standard);
        vg.drawString("Symbol Font:", 20, 7 * dh - 40);
        vg.setFont(new Font("Symbol", Font.PLAIN, 12));
        vg.drawString("ABC abc 123 .,!", 200, 7 * dh - 40);

        vg.setFont(standard);
        vg.drawString("Dingbats:", 20, 7 * dh - 20);
        vg.setFont(new Font("ZapfDingbats", Font.PLAIN, 12));
        vg.drawString("ABC abc 123 .,!", 200, 7 * dh - 20);

        String ucs = "Unicode chars (greek, dingbats): \u03b1 \u03b2 \u03b3 \u263a \u2665 \u2729 \u270c";
        vg.setFont(serif);
        vg.drawString(ucs + " in " + serif.getName(), 20, 7 * dh);
        vg.setFont(sansserif);
        vg.drawString(ucs + " in " + sansserif.getName(), 20, 7 * dh + 20);
        vg.setFont(dialog);
        vg.drawString(ucs + " in " + dialog.getName(), 20, 7 * dh + 40);
        vg.setFont(monospaced);
        vg.drawString(ucs + " in " + monospaced.getName(), 20, 7 * dh + 60);
    }

    public static void main(String[] args) throws Exception {
        new TestFonts(args).runTest();
    }
}
