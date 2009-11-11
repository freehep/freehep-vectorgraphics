// Copyright 2005, FreeHEP.
package org.freehep.util.io.test;

import java.io.File;

import junit.framework.TestCase;

/**
 * Abstract Test for ASCII85 Output Stream
 * 
 * @author Mark Donszelmann
 * @version $Id: src/test/java/org/freehep/util/io/test/AbstractStreamTest.java c5cb38309f84 2005/12/02 20:35:09 duns $
 */
public abstract class AbstractStreamTest extends TestCase {
    
    protected File refDir;
    protected File testDir;
    protected File outDir;
    
    protected void setUp() throws Exception {
        String baseDir = System.getProperty("basedir");
        if (baseDir == null) baseDir = "";
        refDir = new File(baseDir, "src/test/resources/ref");
        testDir = refDir;
        outDir = new File(baseDir, "target/test-output/ref");
        if (!outDir.exists()) outDir.mkdirs();        
    }
    
}
