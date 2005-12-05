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
import org.freehep.graphicsio.cgm.EndMetafile;
import org.freehep.graphicsio.cgm.EndPicture;
import org.freehep.graphicsio.cgm.LineColor;
import org.freehep.graphicsio.cgm.LineType;
import org.freehep.graphicsio.cgm.LineWidth;
import org.freehep.graphicsio.cgm.LineWidthSpecificationMode;
import org.freehep.graphicsio.cgm.MetafileDescription;
import org.freehep.graphicsio.cgm.MetafileElementList;
import org.freehep.graphicsio.cgm.MetafileVersion;
import org.freehep.graphicsio.cgm.Polyline;
import org.freehep.graphicsio.cgm.VDCExtent;

public class PolylineTest {

    public static void main(String[] args) throws IOException {

        Point2D p[] = { new Point2D.Double(2000, 2000),
                new Point2D.Double(4000, 6000), new Point2D.Double(3000, 5000),
                new Point2D.Double(0000, 6000) };

        Vector cgm = new Vector();
        cgm.add(new BeginMetafile("POLYLINE"));
        cgm.add(new MetafileVersion(1));
        cgm.add(new MetafileDescription("FreeHEP/VG-CGM-1.0"));
        cgm.add(new MetafileElementList(MetafileElementList.DRAWING_PLUS_SET));
        cgm.add(new BeginPicture("POLYLINE"));
        cgm.add(new ColorSelectionMode(ColorSelectionMode.DIRECT));
        cgm.add(new LineWidthSpecificationMode(
                LineWidthSpecificationMode.ABSOLUTE));
        cgm.add(new VDCExtent(new Point2D.Double(0, 8000), new Point2D.Double(
                8000, 0)));
        cgm.add(new BeginPictureBody());
        cgm.add(new LineColor(Color.black));
        cgm.add(new LineType(LineType.DASH));
        cgm.add(new LineWidth(60));
        cgm.add(new Polyline(p));
        cgm.add(new EndPicture());
        cgm.add(new EndMetafile());

        CGMOutputStream out = new CGMOutputStream(new FileOutputStream(
                "PolylineTest.cgm"));
        for (int i = 0; i < cgm.size(); i++) {
            out.writeTag((CGMTag) cgm.get(i));
        }
        out.close();
    }
}