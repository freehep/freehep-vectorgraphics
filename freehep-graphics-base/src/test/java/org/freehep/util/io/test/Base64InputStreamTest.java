// Copyright 2003-2005, FreeHEP.
package org.freehep.util.io.test;

import java.io.File;
import java.io.FileInputStream;

import org.freehep.util.Assert;
import org.freehep.util.io.Base64InputStream;

/**
 * Test for Base64 Input Stream.
 * 
 * @author Mark Donszelmann
 * @version $Id: src/test/java/org/freehep/util/io/test/Base64InputStreamTest.java c5cb38309f84 2005/12/02 20:35:09 duns $
 */
public class Base64InputStreamTest extends AbstractStreamTest {

    /**
     * Test method for 'org.freehep.util.io.Base64InputStream.read()'
     * @throws Exception if ref file cannot be found
     */
    public void testRead() throws Exception {
        File testFile = new File(testDir, "TestFile.b64");
        File refFile = new File(refDir, "TestFile.xml");
            
        Base64InputStream in = new Base64InputStream(new FileInputStream(testFile));
        Assert.assertEquals(new FileInputStream(refFile), in, false, refFile.getPath());
    }
}
