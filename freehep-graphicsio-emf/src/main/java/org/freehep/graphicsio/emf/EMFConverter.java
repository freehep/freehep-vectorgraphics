// Copyright FreeHEP 2007.
package org.freehep.graphicsio.emf;

import org.freehep.util.export.ExportFileType;

import java.util.List;
import java.io.FileInputStream;
import java.io.File;

/**
 * This class converts an EMF image to all available
 * grafik formats, e.g. PDF, or PNG
 *
 * @author Steffen Greiffenberg
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFConverter.java 9c0688d78e6b 2007/01/30 23:58:16 duns $
 */
public class EMFConverter {

    /**
     * Looks for an (FreeHEP-) ExportFileType in class path
     * to create the selected output format for destFileName.
     *
     * @param type extension / file format to write
     * @param srcFileName emf file to read
     * @param destFileName file to create, if null srcFileName
     *        is used with the extension type
     */
    protected static void export(String type, String srcFileName, String destFileName) {
        try {
            List exportFileTypes = ExportFileType.getExportFileTypes(type);
            if (exportFileTypes == null || exportFileTypes.size() == 0) {
                System.out.println(
                    type + " library is not available. check your classpath!");
                return;
            }

            ExportFileType exportFileType = (ExportFileType) exportFileTypes.get(0);

            // read the EMF file
            EMFRenderer emfRenderer = new EMFRenderer(
                new EMFInputStream(
                    new FileInputStream(srcFileName)));

            // create the destFileName,
            // replace or add the extension to the destFileName
            if (destFileName == null || destFileName.length() == 0) {
                // index of the beginning of the extension
                int lastPointIndex = srcFileName.lastIndexOf(".");

                // to be sure that the point separates an extension
                // and is not part of a directory name
                int lastSeparator1Index = srcFileName.lastIndexOf("/");
                int lastSeparator2Index = srcFileName.lastIndexOf("\\");

                if (lastSeparator1Index > lastPointIndex ||
                    lastSeparator2Index > lastPointIndex) {
                    destFileName = srcFileName + ".";
                } else if (lastPointIndex > -1) {
                    destFileName = srcFileName.substring(
                        0, lastPointIndex + 1);
                }

                // add the extension
                destFileName += type.toLowerCase();
            }

            // TODO there is no possibility to use Constants of base class!
            /* create SVG properties
            Properties p = new Properties();
            p.put(SVGGraphics2D.EMBED_FONTS, Boolean.toString(false));
            p.put(SVGGraphics2D.CLIP, Boolean.toString(true));
            p.put(SVGGraphics2D.COMPRESS, Boolean.toString(false));
            p.put(SVGGraphics2D.TEXT_AS_SHAPES, Boolean.toString(false));
            p.put(SVGGraphics2D.FOR, "Freehep EMF2SVG");
            p.put(SVGGraphics2D.TITLE, emfFileName);*/

            EMFPanel emfPanel = new EMFPanel();
            emfPanel.setRenderer(emfRenderer);

            // TODO why uses this classes components?!
            exportFileType.exportToFile(
               new File(destFileName),
               emfPanel,
               emfPanel,
               null,
               "Freehep EMF converter");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * starts the conversion
     *
     * @param args args[0] source file, args[1] destination
     *        file with target format in extension
     */
    public static void main(String[] args) {
        try {
            export(
                args[1].substring(args[1].lastIndexOf(".") + 1),
                args[0],
                args[1]);
        } catch (Exception e) {
            System.out.println("usage: EMFConverter imput.emf output.extension");
        }
    }
}
