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
import org.freehep.graphicsio.cgm.ColorSelectionMode;
import org.freehep.graphicsio.cgm.EllipticalArc;
import org.freehep.graphicsio.cgm.EndMetafile;
import org.freehep.graphicsio.cgm.EndPicture;
import org.freehep.graphicsio.cgm.LineColor;
import org.freehep.graphicsio.cgm.LineType;
import org.freehep.graphicsio.cgm.LineWidth;
import org.freehep.graphicsio.cgm.MetafileDescription;
import org.freehep.graphicsio.cgm.MetafileElementList;
import org.freehep.graphicsio.cgm.MetafileVersion;
import org.freehep.graphicsio.cgm.VDCExtent;

public class ArcTest {

    public static void main(String[] args) throws IOException {

        Vector cgm = new Vector();
        cgm.add(new BeginMetafile("ARC"));
        cgm.add(new MetafileVersion(1));
        cgm.add(new MetafileDescription("FreeHEP/VG-CGM-1.0"));
        cgm.add(new MetafileElementList(MetafileElementList.DRAWING_PLUS_SET));
        cgm.add(new BeginPicture("ARC"));
        cgm.add(new ColorSelectionMode(ColorSelectionMode.DIRECT));
        cgm.add(new VDCExtent(new Point2D.Double(0, 3000), new Point2D.Double(
                3000, 0)));
        cgm.add(new BeginPictureBody());
        cgm.add(new LineWidth(4));
        cgm.add(new LineColor(Color.blue));
        cgm.add(new LineType(LineType.SOLID));
        cgm.add(new EllipticalArc(new Point2D.Double(1000, 1000),
                new Point2D.Double(1000, 500), new Point2D.Double(2000, 1000),
                new Point2D.Double(1500, 5000), new Point2D.Double(500, 500)));
        cgm.add(new EndPicture());
        cgm.add(new EndMetafile());

        CGMOutputStream out = new CGMOutputStream(new FileOutputStream(
                "ArcTest.cgm"));
        for (int i = 0; i < cgm.size(); i++) {
            out.writeTag((CGMTag) cgm.get(i));
        }
        out.close();
    }
}
