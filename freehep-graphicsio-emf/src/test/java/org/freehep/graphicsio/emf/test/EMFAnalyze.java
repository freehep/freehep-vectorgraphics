// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.emf.test;

import hep.aida.IAnalysisFactory;
import hep.aida.ITree;
import hep.aida.ITuple;
import hep.aida.ITupleFactory;

import java.io.FileInputStream;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFHeader;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.util.io.Tag;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/test/java/org/freehep/graphicsio/emf/test/EMFAnalyze.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class EMFAnalyze {

    public static void main(String[] args) {

        try {
            IAnalysisFactory af = IAnalysisFactory.create();
            ITree tree = af.createTreeFactory().create("EMFAnalyze.aida",
                    "xml", false, true);
            ITupleFactory tf = af.createTupleFactory(tree);
            ITuple tuple = tf.create("EMF", "TagType", new String[] { "Tag",
                    "TagSize" }, new Class[] { String.class, int.class });

            FileInputStream fis = new FileInputStream(args[0]);
            EMFInputStream emf = new EMFInputStream(fis);

            long start = System.currentTimeMillis();
            EMFHeader header = emf.readHeader();
            System.out.println(header);

            Tag tag = emf.readTag();
            while (tag != null) {
                // System.out.println(tag);
                tuple.fill(0, tag.getName());
                tuple.addRow();
                tag = emf.readTag();
                // FIXME add tagSize
            }
            tree.commit();
            System.out.println("Analyzed file in: "
                    + (System.currentTimeMillis() - start) + " ms.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
