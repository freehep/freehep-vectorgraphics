// Copyright 2003 FreeHEP
package org.freehep.graphicsio.exportchooser;

import java.util.*;
import javax.imageio.*;
import javax.imageio.spi.*;

import org.freehep.graphicsio.ImageGraphics2D;
import org.freehep.util.export.ExportFileType;

/**
 * This class does not work, since the ExportFileTypeRegistry stores Objects
 * by class. If we automatically generate ImageFileTypes by ImageIO they end up
 * being all different objects from the same class. The Registry currently
 * then overwrites the first one with the second and so on. Sun Bug #Submitted.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/ImageIOExportFileType.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public class ImageIOExportFileType implements RegisterableService {

    /**
     * This constructor will construct register all image formats available
     * in ImageIO into ExportFileTypeRegistry. The ImageExportFileTypeRegistration
     * will deregister itself immediately.
     */
    public ImageIOExportFileType() {
        // empty, registry is not valid yet
    }

    public void onRegistration(ServiceRegistry registry, Class category) {
        // run over all ImageWriterSpis and store their formats Alphabetically
        IIORegistry imageRegistry = IIORegistry.getDefaultInstance();
        Iterator providers = imageRegistry.getServiceProviders(ImageWriterSpi.class, false);
        SortedSet formatSet = new TreeSet();
        while (providers.hasNext()) {
            ImageWriterSpi writerSpi = (ImageWriterSpi)providers.next();
            String[] formats = writerSpi.getFileSuffixes();
            if ((formats != null) && (formats[0] != null)) {
                formatSet.add(formats[0]);
            } else {
                System.err.println(getClass()+": Cannot register "+writerSpi+" because it has no filesuffixes.");
            }
        }

        // Look for the last ExportFileType so that these ImageExportFileTypes
        // are registered neatly behind that one.
        ExportFileType previous = null;
        Iterator exportTypes = registry.getServiceProviders(ExportFileType.class, true);
        while (exportTypes.hasNext()) {
            previous = (ExportFileType)exportTypes.next();
        }

        // run over all formats and book them as ExportFileTypes
        Iterator formats = formatSet.iterator();
        while (formats.hasNext()) {
            String format = (String)formats.next();
            ExportFileType export = ImageExportFileType.getInstance(format);
            if (export != null) {
                registry.registerServiceProvider(export, ExportFileType.class);
                if (previous != null) {
                    registry.unsetOrdering(ExportFileType.class, previous, export);
                    boolean result = registry.setOrdering(ExportFileType.class, previous, export);
//                    System.out.println("Ordering set : "+result);
                }
                previous = export;
            } else {
                System.err.println(getClass()+": Invalid format: "+format+".");
            }
        }

        registry.deregisterServiceProvider(this, category);
    }

    public void onDeregistration(ServiceRegistry registry, Class category) {
    }

    public static void main(String[] args) throws Exception {

        System.out.println("WRITERS");
        IIORegistry imageRegistry = IIORegistry.getDefaultInstance();
        Iterator providers = imageRegistry.getServiceProviders(ImageWriterSpi.class, false);
        while (providers.hasNext()) {
            ImageWriterSpi writerSpi = (ImageWriterSpi)providers.next();
            System.out.println("   "+writerSpi);
            System.out.println("      "+writerSpi.getDescription(Locale.US));
            System.out.print("      ");
            String[] formats = writerSpi.getFileSuffixes();
            for (int i=0; i<formats.length; i++) {
                System.out.print(formats[i]+", ");
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("MIMETYPES");
        String[] formats = ImageIO.getWriterMIMETypes();
        for (int i=0; i<formats.length; i++) {
            System.out.println("   "+formats[i]);
            ImageWriter writer=ImageGraphics2D.getPreferredImageWriterForMIMEType(formats[i]);
            String[] suffixes = writer.getOriginatingProvider().getFileSuffixes();
            System.out.print("      ");
            for (int j=0; j<suffixes.length; j++) {
                System.out.print(suffixes[j]+" ");
            }
            System.out.println();
            System.out.println("      "+writer);
        }

        System.out.println();

        System.out.println("READERS");
        providers = imageRegistry.getServiceProviders(ImageReaderSpi.class, false);
        while (providers.hasNext()) {
            System.out.println("   "+providers.next());
        }
        System.out.println();

        System.out.println("All ExportFileTypes");
        List exportFileTypes = ExportFileType.getExportFileTypes();
        Iterator iterator = exportFileTypes.iterator();
        while (iterator.hasNext()) {
            System.out.println("   "+iterator.next());
        }
    }
}
