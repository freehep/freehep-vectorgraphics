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
import org.freehep.graphicsio.cgm.EdgeColor;
import org.freehep.graphicsio.cgm.EdgeType;
import org.freehep.graphicsio.cgm.EdgeVisibility;
import org.freehep.graphicsio.cgm.EdgeWidth;
import org.freehep.graphicsio.cgm.EdgeWidthSpecificationMode;
import org.freehep.graphicsio.cgm.EndMetafile;
import org.freehep.graphicsio.cgm.EndPicture;
import org.freehep.graphicsio.cgm.FillColor;
import org.freehep.graphicsio.cgm.InteriorStyle;
import org.freehep.graphicsio.cgm.MetafileDescription;
import org.freehep.graphicsio.cgm.MetafileElementList;
import org.freehep.graphicsio.cgm.MetafileVersion;
import org.freehep.graphicsio.cgm.Polygon;
import org.freehep.graphicsio.cgm.VDCExtent;

public class PolygonTest {

    public static void main(String[] args) throws IOException {

        Point2D p[] = { new Point2D.Double(4000, 5000),
                new Point2D.Double(16000, 17000),
                new Point2D.Double(8000, 3000) };

        Vector cgm = new Vector();
        cgm.add(new BeginMetafile("POLYGON"));
        cgm.add(new MetafileVersion(1));
        cgm.add(new MetafileDescription("FreeHEP/VG-CGM-1.0"));
        cgm.add(new MetafileElementList(MetafileElementList.DRAWING_PLUS_SET));
        cgm.add(new BeginPicture("POLYGON"));
        cgm.add(new ColorSelectionMode(ColorSelectionMode.DIRECT));
        cgm.add(new EdgeWidthSpecificationMode(
                EdgeWidthSpecificationMode.ABSOLUTE));
        cgm.add(new VDCExtent(new Point2D.Double(0, 20000), new Point2D.Double(
                20000, 0)));
        cgm.add(new BeginPictureBody());
        cgm.add(new EdgeVisibility(true));
        cgm.add(new EdgeColor(Color.red));
        cgm.add(new FillColor(Color.black));
        cgm.add(new EdgeType(EdgeType.SOLID));
        cgm.add(new EdgeWidth(60));
        cgm.add(new InteriorStyle(InteriorStyle.SOLID));
        cgm.add(new Polygon(p));
        cgm.add(new EndPicture());
        cgm.add(new EndMetafile());

        CGMOutputStream out = new CGMOutputStream(new FileOutputStream(
                "PolygonTest.cgm"));
        for (int i = 0; i < cgm.size(); i++) {
            out.writeTag((CGMTag) cgm.get(i));
        }
        out.close();
    }
}