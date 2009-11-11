// Copyright 2002-2005, FreeHEP.
package org.freehep.util.io.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.freehep.util.Assert;
import org.freehep.util.io.RouteListener;
import org.freehep.util.io.RoutedInputStream;

/**
 * Test for Routed Input Stream.
 * 
 * @author Mark Donszelmann
 * @version $Id: src/test/java/org/freehep/util/io/test/RoutedInputStreamTest.java 5c38dc058ace 2005/12/02 23:30:37 duns $
 */
public class RoutedInputStreamTest extends AbstractStreamTest {

    /**
     * Tests RoutiedInputStream.read()
     * 
     * @throws Exception when reference file cannot be found
     */
    public void testRead() throws Exception {

        File testFile = new File(testDir, "RoutedInputStream.txt");
        File refFile = new File(refDir, "RoutedInputStream.out");
        File outFile = new File(outDir, "RoutedInputStream.out");
        
        RoutedInputStream in = new RoutedInputStream(new BufferedInputStream(
                new FileInputStream(testFile)));
        final PrintStream out = new PrintStream(new FileOutputStream(outFile));
        
        RouteListener listener = new RouteListener() {
            public void routeFound(RoutedInputStream.Route route)
                    throws IOException {
                out.write('[');
                out.write(route.getStart());
                out.write(':');
                int b = route.read();
                while (b != -1) {
                    out.write(b);
                    b = route.read();
                }
                route.close();
                out.write(']');
                out.flush();
            }
        };
        
        in.addRoute("StartA", "EndA", listener);
        in.addRoute("StartB", "EndB", listener);
        in.addRoute("StartC", "EndC", listener);
        
        in.addRoute("SClosed", "EClosed", new RouteListener() {

            public void routeFound(RoutedInputStream.Route route)
                    throws IOException {
                out.print("[EarlyClosed:");
                for (int i = 0; i < 6; i++) {
                    out.write(route.read());
                }
                route.close();
                out.write(']');
                out.flush();
            }
        });

        int b = in.read();
        while (b != -1) {
            out.write(b);
            b = in.read();
        }
        in.close();
        out.close();
        
        Assert.assertEquals(refFile, outFile, false);
    }
}
