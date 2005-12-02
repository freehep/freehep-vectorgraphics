package org.freehep.graphicsio.emf.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JPanel;

import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFHandleManager;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EOF;
import org.freehep.graphicsio.emf.GDIComment;
import org.freehep.graphicsio.emf.SetBkMode;
import org.freehep.graphicsio.emf.SetMapMode;
import org.freehep.graphicsio.emf.SetStretchBltMode;
import org.freehep.graphicsio.emf.SetViewportExtEx;
import org.freehep.graphicsio.emf.SetViewportOrgEx;
import org.freehep.graphicsio.emf.SetWindowExtEx;
import org.freehep.graphicsio.emf.SetWindowOrgEx;
import org.freehep.graphicsio.emf.StretchDIBits;
import org.freehep.util.images.ImageHandler;
import org.freehep.util.images.ImageUtilities;

public class EMFTest extends JPanel {

    public EMFTest() {
    }

    public void run() throws IOException {
        Rectangle bounds = new Rectangle(0, 0, 800, 600);

/*        Point p1[][] = { { new Point(40, 50), new Point(160, 170),
                new Point(80, 30) } };
        Point p2[] = { new Point(20, 20), new Point(40, 60), new Point(30, 50),
                new Point(00, 60) };
*/
        Vector emf = new Vector();

        emf.add(new GDIComment("Settings"));
        emf.add(new SetMapMode(SetMapMode.MM_ANISOTROPIC));
        emf.add(new SetBkMode(SetBkMode.BKG_TRANSPARENT));
        emf.add(new SetWindowOrgEx(new Point(0, 0)));
        emf.add(new SetViewportOrgEx(new Point(0, 0)));
        emf.add(new SetWindowExtEx(new Dimension(bounds.width, bounds.height)));
        emf
                .add(new SetViewportExtEx(new Dimension(bounds.width,
                        bounds.height)));

        /*
         * emf.add(new SaveDC());
         * 
         * emf.add(new GDIComment("Polygon")); LogBrush32 brush = new
         * LogBrush32(LogBrush32.BS_SOLID, Color.red, 0); emf.add(new
         * CreateBrushIndirect(1, brush)); emf.add(new SelectObject(1));
         * ExtLogPen pen1 = new ExtLogPen(ExtLogPen.PS_GEOMETRIC |
         * ExtLogPen.PS_SOLID | ExtLogPen.PS_ENDCAP_ROUND |
         * ExtLogPen.PS_JOIN_ROUND, 5, ExtLogPen.BS_SOLID, Color.green, 0, new
         * int[0]); emf.add(new ExtCreatePen(2, pen1)); emf.add(new
         * SelectObject(2)); emf.add(new SetMiterLimit(10)); emf.add(new
         * PolyPolygon(bounds, p1)); emf.add(new DeleteObject(1)); emf.add(new
         * DeleteObject(2));
         * 
         * emf.add(new GDIComment("Polyline")); emf.add(new
         * ModifyWorldTransform(new AffineTransform(1, 0, 0, 1, 200, 0),
         * ModifyWorldTransform.MWT_LEFTMULTIPLY)); ExtLogPen pen2 = new
         * ExtLogPen(ExtLogPen.PS_GEOMETRIC | ExtLogPen.PS_SOLID |
         * ExtLogPen.PS_ENDCAP_ROUND | ExtLogPen.PS_JOIN_ROUND, 5,
         * ExtLogPen.BS_SOLID, Color.blue, 0, new int[0]); emf.add(new
         * ExtCreatePen(1, pen2)); emf.add(new SelectObject(1)); emf.add(new
         * SetMiterLimit(10)); emf.add(new Polyline(bounds, p2)); emf.add(new
         * DeleteObject(1));
         * 
         * emf.add(new GDIComment("Ellipse")); emf.add(new
         * ModifyWorldTransform(new AffineTransform(1, 0, 0, 1, 200, 0),
         * ModifyWorldTransform.MWT_LEFTMULTIPLY)); ExtLogPen pen3 = new
         * ExtLogPen(ExtLogPen.PS_GEOMETRIC | ExtLogPen.PS_SOLID |
         * ExtLogPen.PS_ENDCAP_ROUND | ExtLogPen.PS_JOIN_ROUND, 5,
         * ExtLogPen.BS_SOLID, Color.orange, 0, new int[0]); emf.add(new
         * ExtCreatePen(1, pen3)); emf.add(new SelectObject(1)); emf.add(new
         * SetMiterLimit(10)); emf.add(new Ellipse(new Rectangle(20,20,50,30)));
         * emf.add(new DeleteObject(1));
         * 
         * emf.add(new RestoreDC());
         * 
         * emf.add(new GDIComment("Arc")); emf.add(new ModifyWorldTransform(new
         * AffineTransform(1, 0, 0, 1, 0, 200),
         * ModifyWorldTransform.MWT_LEFTMULTIPLY)); ExtLogPen pen4 = new
         * ExtLogPen(ExtLogPen.PS_GEOMETRIC | ExtLogPen.PS_SOLID |
         * ExtLogPen.PS_ENDCAP_ROUND | ExtLogPen.PS_JOIN_ROUND, 5,
         * ExtLogPen.BS_SOLID, Color.cyan, 0, new int[0]); emf.add(new
         * ExtCreatePen(1, pen4)); emf.add(new SelectObject(1)); emf.add(new
         * SetMiterLimit(10)); emf.add(new AngleArc(new Point(100,100), 50, 20,
         * 280)); emf.add(new DeleteObject(1));
         * 
         * emf.add(new GDIComment("Image")); emf.add(new
         * ModifyWorldTransform(new AffineTransform(1, 0, 0, 1, 0, 200),
         * ModifyWorldTransform.MWT_LEFTMULTIPLY));
         */
        MediaTracker t = new MediaTracker(this);
        Image image = ImageHandler.getImage("BrokenCursor.gif", getClass());
        t.addImage(image, 0);
        try {
            t.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        emf.add(new SetStretchBltMode(EMFConstants.COLORONCOLOR));
        emf.add(new StretchDIBits(bounds, 0, 0, image.getWidth(this), image
                .getHeight(this), ImageUtilities.createRenderedImage(image,
                this, Color.BLACK), Color.BLACK));
        /*
         * emf.add(new GDIComment("Text")); emf.add(new ModifyWorldTransform(new
         * AffineTransform(1, 0, 0, 1, 0, 200),
         * ModifyWorldTransform.MWT_LEFTMULTIPLY));
         * 
         * LogFontW font = new LogFontW(-82, 0, 0, 0, 265, false, false, false,
         * 0xCC, 7, 2, 1, 0x22, "Arial TUR"); Panose panose = new Panose();
         * ExtLogFontW extFont = new ExtLogFontW(font, "", "", 0, 0, 0, new
         * byte[] {0, 0, 0, 0}, 0, panose); emf.add(new
         * ExtCreateFontIndirectW(1, extFont));
         * 
         * emf.add(new SelectObject(1)); Text text = new Text(new Point (0, 0),
         * "Test", Text.ETO_OPAQUE, bounds); emf.add(new ExtTextOutA(new
         * Rectangle(0,0,100,100), ExtTextOutA.GM_COMPATIBLE, 100, 100, text));
         * emf.add(new DeleteObject(1));
         * 
         */
        emf.add(new GDIComment("End"));
        emf.add(new EOF());

        EMFOutputStream out = new EMFOutputStream(new FileOutputStream(
                "EMFTest.emf"), bounds, new EMFHandleManager(), "EMFTest",
                "TestFile", new Dimension(1024, 768));
        for (int i = 0; i < emf.size(); i++) {
            out.writeTag((EMFTag) emf.get(i));
        }
        out.close();
    }

    public static void main(String[] args) throws IOException {
        EMFTest test = new EMFTest();
        test.run();
        System.exit(0);
    }
}
