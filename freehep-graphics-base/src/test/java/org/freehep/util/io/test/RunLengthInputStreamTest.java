// Copyright 2001-2005, FreeHEP.
package org.freehep.util.io.test;

import java.io.File;
import java.io.FileInputStream;

import org.freehep.util.Assert;
import org.freehep.util.io.RunLengthInputStream;

/**
 * Test for Run Length Input Stream.
 * 
 * @author Mark Donszelmann
 * @version $Id: src/test/java/org/freehep/util/io/test/RunLengthInputStreamTest.java 5c38dc058ace 2005/12/02 23:30:37 duns $
 */
public class RunLengthInputStreamTest extends AbstractStreamTest {

    /**
     * Test method for 'org.freehep.util.io.RunLengthInputStream.read()'
     * @throws Exception if ref file cannot be found
     */
    public void testRead() throws Exception {
        File testFile = new File(testDir, "TestFile.rnl");
        File refFile = new File(refDir, "TestFile.xml");
            
        RunLengthInputStream in = new RunLengthInputStream(new FileInputStream(testFile));
        Assert.assertEquals(new FileInputStream(refFile), in, true, refFile.getPath());
    }
}
