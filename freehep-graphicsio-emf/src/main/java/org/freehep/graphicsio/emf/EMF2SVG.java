// Copyright FreeHEP 2006-2007.
package org.freehep.graphicsio.emf;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.freehep.graphicsio.svg.SVGGraphics2D;
import org.freehep.graphicsio.ImageConstants;

/**
 * @author Steffen Greiffenberg
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMF2SVG.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class EMF2SVG {

    public static void main(String[] args) {
        if (args == null || args.length == 0 || args[0] == null || args[0].length() == 0) {
            System.out.println("usage: EMF2SVG imput.emf [output.svg]");
            return;
        }

        try {
            // read the EMF file
            String emfFileName = args[0];
            FileInputStream fis = new FileInputStream(args[0]);
            EMFInputStream emf = new EMFInputStream(fis);
            EMFRenderer emfRenderer = new EMFRenderer(emf);

            // create the SVG output file
            String svgFileName;
            if (args.length < 2 || args[1].length() == 0) {
                svgFileName =
                    emfFileName.substring(0, emfFileName.lastIndexOf(".") - 1) +
                    ImageConstants.SVG.toLowerCase();
            } else {
                svgFileName = args[1];
            }

            // create SVG properties
            Properties p = new Properties();
            p.put(SVGGraphics2D.EMBED_FONTS, Boolean.toString(false));
            p.put(SVGGraphics2D.CLIP, Boolean.toString(true));
            p.put(SVGGraphics2D.COMPRESS, Boolean.toString(false));
            p.put(SVGGraphics2D.TEXT_AS_SHAPES, Boolean.toString(false));
            p.put(SVGGraphics2D.FOR, "Freehep EMF2SVG");
            p.put(SVGGraphics2D.TITLE, emfFileName);

            // create the SVG painting context
            SVGGraphics2D svgGraphics2D = new SVGGraphics2D(
                new File(svgFileName),
                emfRenderer.getSize());
            svgGraphics2D.setProperties(p);
            svgGraphics2D.setCreator("Freehep EMF2SVG");

            // export the emf renderer output
            svgGraphics2D.startExport();
            emfRenderer.paint(svgGraphics2D);
            svgGraphics2D.endExport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
