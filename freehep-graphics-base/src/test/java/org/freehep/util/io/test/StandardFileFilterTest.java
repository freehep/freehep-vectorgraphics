// Copyright 2003, SLAC, Stanford, U.S.A
package org.freehep.util.io.test;

import java.io.File;
import java.io.FileFilter;

import org.freehep.util.Assert;
import org.freehep.util.io.StandardFileFilter;

/**
 * Test for the Standard File Filter.
 * 
 * @author duns
 * @version $Id: src/test/java/org/freehep/util/io/test/StandardFileFilterTest.java 7e5c8b8fe11e 2005/12/02 23:55:27 duns $
 */
public class StandardFileFilterTest extends AbstractStreamTest {

    /**
     * Counts *.txt files in the ref directory
     */
    public void testFileFilterTxt() {
        FileFilter filter = new StandardFileFilter("*.txt");
        File[] files = refDir.listFiles(filter);
        Assert.assertEquals(4, files.length);
    }

    /**
     * Counts *.ref* files in the ref directory
     */
    public void testFileFilterRef() {
        FileFilter filter = new StandardFileFilter("*.ref*");
        File[] files = refDir.listFiles(filter);
        Assert.assertEquals(3, files.length);
    }
}
