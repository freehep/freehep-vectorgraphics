// Copyright 2000-2007 FreeHEP
package org.freehep.graphicsio.emf;

/**
 * EMFDisplay.java
 *
 * Created: Mon May 26 09:43:10 2003
 *
 * Copyright:    Copyright (c) 2000, 2001<p>
 * Company:      ATLANTEC Enterprise Solutions GmbH<p>
 *
 * @author Carsten Zerbst carsten.zerbst@atlantec-es.com
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFDisplay.java 4c4708a97391 2007/06/12 22:32:31 duns $
 */

import java.io.File;

/**
 * A simple interpreter displaying an EMF file read in by the EMFInputStream in
 * a JPanel
 */
public class EMFDisplay {

    public static void main(String[] args) {
        try {
            EMFViewer emfViewer = new EMFViewer();
            if (args[0] != null) {
                emfViewer.show(new File(args[0]));
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}

// EMFDisplay
