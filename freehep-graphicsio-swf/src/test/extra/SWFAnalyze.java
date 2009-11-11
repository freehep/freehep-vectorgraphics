// Copyright 2003, FreeHEP.
package org.freehep.graphicsio.swf.test;

import hep.aida.IAnalysisFactory;
import hep.aida.ITree;
import hep.aida.ITuple;
import hep.aida.ITupleFactory;

import java.io.FileInputStream;
import java.io.IOException;

import org.freehep.graphicsio.swf.SWFHeader;
import org.freehep.graphicsio.swf.SWFInputStream;
import org.freehep.util.io.Tag;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-swf/src/test/java/org/freehep/graphicsio/swf/test/SWFAnalyze.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class SWFAnalyze {

    public static void main(String[] args) {

        try {
            IAnalysisFactory af = IAnalysisFactory.create();
            ITree tree = af.createTreeFactory().create("SWFAnalyze.aida",
                    "xml", false, true);
            ITupleFactory tf = af.createTupleFactory(tree);
            ITuple tuple = tf.create("SWF", "TagType", new String[] { "Tag",
                    "TagSize" }, new Class[] { String.class, int.class });

            FileInputStream fis = new FileInputStream(args[0]);
            SWFInputStream swf = new SWFInputStream(fis);

            long start = System.currentTimeMillis();
            SWFHeader header = swf.readHeader();
            System.out.println(header);

            Tag tag = swf.readTag();
            while (tag != null) {
                // System.out.println(tag);
                tuple.fill(0, tag.getName());
                System.out.print(" " + tag.getName());
                tuple.addRow();
                tag = swf.readTag();
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
