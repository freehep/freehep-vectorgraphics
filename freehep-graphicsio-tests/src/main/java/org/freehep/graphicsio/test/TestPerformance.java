// University of California, Santa Cruz, USA and
// CERN, Geneva, Switzerland, Copyright (c) 2000
package org.freehep.graphicsio.test;


/**
 * @author Charles Loomis
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestPerformance.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestPerformance extends TestSymbols2D {

    public TestPerformance(String[] args) throws Exception {
        super(args);
        setName("Performance");
    }

    public static void main(String[] args) throws Exception {
        long t0 = System.currentTimeMillis();
        new TestSymbols2D(args).runTest();
        if (args.length > 0) {
            System.out.println(args[0] + " took "
                    + (System.currentTimeMillis() - t0) + " ms.");
        }
    }
}
