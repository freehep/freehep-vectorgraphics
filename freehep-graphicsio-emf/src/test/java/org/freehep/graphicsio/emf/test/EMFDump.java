// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf.test;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFHeader;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.util.io.Tag;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/test/java/org/freehep/graphicsio/emf/test/EMFDump.java eabe3cff0ec9 2005/12/01 22:52:56 duns $
 */
public class EMFDump {

    public static void main(String[] args) {

        try {
            FileInputStream fis = new FileInputStream(args[0]);
            EMFInputStream emf = new EMFInputStream(fis);

            long start = System.currentTimeMillis();
            EMFHeader header = emf.readHeader();
            System.out.println(header);
            
            Tag tag = emf.readTag();
            while (tag != null) {
                System.out.println(tag);
                tag = emf.readTag();
            }
            System.out.println("Parsed file in: "+(System.currentTimeMillis()-start)+" ms.");
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}
