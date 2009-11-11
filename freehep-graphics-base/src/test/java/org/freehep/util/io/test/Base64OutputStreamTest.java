// Copyright 2001-2005, FreeHEP.
package org.freehep.util.io.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.freehep.util.Assert;
import org.freehep.util.io.Base64OutputStream;

/**
 * Test for Base64 Output Stream.
 * 
 * @author Mark Donszelmann
 * @version $Id: src/test/java/org/freehep/util/io/test/Base64OutputStreamTest.java 6e9dcf5329c1 2005/12/10 00:33:07 duns $
 */
public class Base64OutputStreamTest extends AbstractStreamTest {

    /**
     * Test method for 'org.freehep.util.io.Base64OutputStream.write()'
     * @throws Exception if ref file cannot be found
     */
    public void testWrite() throws Exception {
        // this XML file needs to be fixed: eol-style=CRLF
        File testFile = new File(testDir, "TestFile.xml");
        File outFile = new File(outDir, "TestFile.b64");
        File refFile = new File(refDir, "TestFile.b64");
        
        Base64OutputStream out = new Base64OutputStream(new FileOutputStream(outFile));
        // NOTE: read byte by byte, so the test will work on all platforms
        InputStream in = new FileInputStream(testFile);
        int b;
        while ((b = in.read()) >= 0) {
            out.write(b);
        }
        in.close();
        out.close();
        
        Assert.assertEquals(refFile, outFile, false);
    }
}
