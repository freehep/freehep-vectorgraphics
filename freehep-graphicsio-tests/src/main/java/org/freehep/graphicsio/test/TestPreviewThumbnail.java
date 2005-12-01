// Copyright 2000-2005, FreeHEP.
package org.freehep.graphicsio.test;


/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestPreviewThumbnail.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestPreviewThumbnail extends TestAll {

    public TestPreviewThumbnail(String[] args) throws Exception {
        super(args);
        setName("Test Preview and/or Thumbnail");
    }

    public static void main(String[] args) throws Exception {
        new TestPreviewThumbnail(args).runTest();
    }
}
