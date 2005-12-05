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
import org.freehep.graphicsio.cgm.Ellipse;
import org.freehep.graphicsio.cgm.EndMetafile;
import org.freehep.graphicsio.cgm.EndPicture;
import org.freehep.graphicsio.cgm.InteriorStyle;
import org.freehep.graphicsio.cgm.MetafileDescription;
import org.freehep.graphicsio.cgm.MetafileElementList;
import org.freehep.graphicsio.cgm.MetafileVersion;
import org.freehep.graphicsio.cgm.VDCExtent;

public class EllipseTest {

    public static void main(String[] args) throws IOException {

        Vector cgm = new Vector();
        cgm.add(new BeginMetafile("ELLIPSE"));
        cgm.add(new MetafileVersion(1));
        cgm.add(new MetafileDescription("FreeHEP/VG-CGM-1.0"));
        cgm.add(new MetafileElementList(MetafileElementList.DRAWING_PLUS_SET));
        cgm.add(new BeginPicture("ELLIPSE"));
        cgm.add(new ColorSelectionMode(ColorSelectionMode.DIRECT));
        cgm.add(new EdgeWidthSpecificationMode(
                EdgeWidthSpecificationMode.ABSOLUTE));
        cgm.add(new VDCExtent(new Point2D.Double(0, 4000), new Point2D.Double(
                4000, 0)));
        cgm.add(new BeginPictureBody());
        cgm.add(new EdgeColor(Color.red));
        cgm.add(new EdgeVisibility(true));
        cgm.add(new EdgeWidth(4));
        cgm.add(new EdgeType(EdgeType.SOLID));
        cgm.add(new InteriorStyle(InteriorStyle.EMPTY));
        cgm.add(new Ellipse(new Point2D.Double(1000, 2000), new Point2D.Double(
                2000, 2000), new Point2D.Double(1000, 3000)));
        cgm.add(new EndPicture());
        cgm.add(new EndMetafile());

        CGMOutputStream out = new CGMOutputStream(new FileOutputStream(
                "EllipseTest.cgm"));
        for (int i = 0; i < cgm.size(); i++) {
            out.writeTag((CGMTag) cgm.get(i));
        }
        out.close();
    }
}
