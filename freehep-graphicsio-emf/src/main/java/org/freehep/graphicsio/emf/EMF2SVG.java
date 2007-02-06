// Copyright FreeHEP 2006-2007.
package org.freehep.graphicsio.emf;

import org.freehep.graphicsio.ImageConstants;

/**
 * simple class that uses EMFConverter to convert emf to svg.
 * Uses {@link org.freehep.graphicsio.emf.EMFConverter#export(String, String, String)}
 * with type = SVG.
 *
 * @author Steffen Greiffenberg
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMF2SVG.java 10ec7516e3ce 2007/02/06 18:42:34 duns $
 */
public class EMF2SVG extends EMFConverter {

    /**
     * starts the export
     *
     * @param args args[0] source file name, args[1] target file (can be empty)
     */
    public static void main(String[] args) {
        if (args == null || args.length == 0 || args[0] == null || args[0].length() == 0) {
            System.out.println("usage: EMF2SVG imput.emf [output.svg]");
            return;
        }

        export(ImageConstants.SVG, args[0], args.length > 1 ? args[1] : null);
    }
}
