package org.freehep.graphicsio.cgm.test;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.freehep.graphicsio.cgm.BeginMetafile;
import org.freehep.graphicsio.cgm.BeginPicture;
import org.freehep.graphicsio.cgm.BeginPictureBody;
import org.freehep.graphicsio.cgm.CGMOutputStream;
import org.freehep.graphicsio.cgm.CGMTag;
import org.freehep.graphicsio.cgm.CharacterHeight;
import org.freehep.graphicsio.cgm.CharacterOrientation;
import org.freehep.graphicsio.cgm.ColorSelectionMode;
import org.freehep.graphicsio.cgm.EndMetafile;
import org.freehep.graphicsio.cgm.EndPicture;
import org.freehep.graphicsio.cgm.FontList;
import org.freehep.graphicsio.cgm.MetafileDescription;
import org.freehep.graphicsio.cgm.MetafileElementList;
import org.freehep.graphicsio.cgm.MetafileVersion;
import org.freehep.graphicsio.cgm.Text;
import org.freehep.graphicsio.cgm.TextColor;
import org.freehep.graphicsio.cgm.TextFontIndex;
import org.freehep.graphicsio.cgm.VDCExtent;

public class TextTest {

    public static void main(String[] args) throws IOException {

        Vector cgm = new Vector();
        cgm.add(new BeginMetafile("TEXT"));
        cgm.add(new MetafileVersion(1));
        cgm.add(new MetafileDescription("FreeHEP/VG-CGM-1.0"));
        cgm.add(new MetafileElementList(MetafileElementList.DRAWING_PLUS_SET));
        cgm.add(new FontList(new String[] { "Courier", "Courier-Bold",
                "Courier-Italic", "Courier-BoldItalic", "Helvetica",
                "Helvetica-Bold", "Helvetica-Italic", "Helvetica-BoldItalic",
                "TimesRoman", "TimesRoman-Bold", "TimesRoman-Italic",
                "TimesRoman-BoldItalic", "Symbol", "ZapfDingbats" }));

        cgm.add(new BeginPicture("TEXT"));
        cgm.add(new ColorSelectionMode(ColorSelectionMode.DIRECT));
        cgm.add(new VDCExtent(new Point2D.Double(0, 6000), new Point2D.Double(
                6000, 0)));
        cgm.add(new BeginPictureBody());
        cgm.add(new TextColor(Color.red));
        cgm.add(new CharacterHeight(250));
        cgm.add(new CharacterOrientation());
        cgm.add(new TextFontIndex(1));
        cgm.add(new Text(new Point2D.Double(100, 300), "Courier"));
        cgm.add(new TextFontIndex(2));
        cgm.add(new Text(new Point2D.Double(100, 600), "Courier-Bold"));
        cgm.add(new TextFontIndex(3));
        cgm.add(new Text(new Point2D.Double(100, 900), "Courier-Italic"));
        cgm.add(new TextFontIndex(4));
        cgm.add(new Text(new Point2D.Double(100, 1200), "Courier-BoldItalic"));
        cgm.add(new TextFontIndex(5));
        cgm.add(new Text(new Point2D.Double(100, 1500), "Helvetica"));
        cgm.add(new TextFontIndex(6));
        cgm.add(new Text(new Point2D.Double(100, 1800), "Helvetica-Bold"));
        cgm.add(new TextFontIndex(7));
        cgm.add(new Text(new Point2D.Double(100, 2100), "Helvetica-Italic"));
        cgm.add(new TextFontIndex(8));
        cgm
                .add(new Text(new Point2D.Double(100, 2400),
                        "Helvetica-BoldItalic"));
        cgm.add(new TextFontIndex(9));
        cgm.add(new Text(new Point2D.Double(100, 2700), "TimesRoman"));
        cgm.add(new TextFontIndex(10));
        cgm.add(new Text(new Point2D.Double(100, 3000), "TimesRoman-Bold"));
        cgm.add(new TextFontIndex(11));
        cgm.add(new Text(new Point2D.Double(100, 3300), "TimesRoman-Italic"));
        cgm.add(new TextFontIndex(12));
        cgm
                .add(new Text(new Point2D.Double(100, 3600),
                        "TimesRoman-BoldItalic"));
        cgm.add(new TextFontIndex(13));
        cgm.add(new Text(new Point2D.Double(100, 3900), "Symbol"));
        cgm.add(new TextFontIndex(14));
        cgm.add(new Text(new Point2D.Double(100, 4200), "ZapfDingbats"));
        cgm.add(new EndPicture());
        cgm.add(new EndMetafile());

        CGMOutputStream out = new CGMOutputStream(new FileOutputStream(
                "TextTest.cgm"));
        for (int i = 0; i < cgm.size(); i++) {
            out.writeTag((CGMTag) cgm.get(i));
        }
        out.close();
    }
}