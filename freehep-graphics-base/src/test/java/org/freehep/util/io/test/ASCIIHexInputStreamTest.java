// Copyright 2001-2005, FreeHEP.
package org.freehep.util.io.test;

import java.io.File;
import java.io.FileInputStream;

import org.freehep.util.Assert;
import org.freehep.util.io.ASCIIHexInputStream;

/**
 * Test for ASCII Hex Input Stream
 * 
 * @author Mark Donszelmann
 * @version $Id: src/test/java/org/freehep/util/io/test/ASCIIHexInputStreamTest.java c5cb38309f84 2005/12/02 20:35:09 duns $
 */
public class ASCIIHexInputStreamTest extends AbstractStreamTest {

    /**
     * Test method for 'org.freehep.util.io.ASCIIHexInputStream.read()'
     * @throws Exception if ref file cannot be found
     */
    public void testRead() throws Exception {
        File testFile = new File(testDir, "TestFile.hex");
        File refFile = new File(refDir, "TestFile.xml");
            
        ASCIIHexInputStream in = new ASCIIHexInputStream(new FileInputStream(testFile));
        Assert.assertEquals(new FileInputStream(refFile), in, false, refFile.getPath());
    }
}
