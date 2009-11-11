// Copyright 2001-2005 freehep
package org.freehep.util.io.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import org.freehep.util.Assert;
import org.freehep.util.io.EEXECConstants;
import org.freehep.util.io.EEXECDecryption;
import org.freehep.util.io.EEXECEncryption;

/**
 * Test for EEXECEncryption.
 * 
 * @author duns
 * @version $Id: src/test/java/org/freehep/util/io/test/EncryptionTest.java 5c38dc058ace 2005/12/02 23:30:37 duns $
 */
public class EncryptionTest extends AbstractStreamTest {

    /**
     * Tests EExecEncryption and EExecDecryption.
     * 
     * @throws Exception when ref file cannot be found.
     */
    public void testEncryption() throws Exception {
        File refFile = new File(refDir, "Encryption.out");
        File outFile = new File(outDir, "Encryption.out");

        PrintStream out = new PrintStream(new FileOutputStream(outFile));
        
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        int[] ba = new int[] { 0x0010, 0x00BF, 0x0031, 0x0070, 0x004F, 0x00AB,
                0x005B, 0x001F };
        for (int i = 0; i < ba.length; i++)
            bo.write(ba[i]);

        EEXECDecryption in = new EEXECDecryption(new ByteArrayInputStream(bo
                .toByteArray()), EEXECConstants.CHARSTRING_R, 4);
        int r = 0;
        while ((r = in.read()) != -1) {
            out.print(Integer.toHexString(r) + " ");
        }

        out.println();

        int result[] = EEXECEncryption.encryptString(new int[] { 0x00BD,
                0x00F9, 0x00B4, 0x000D }, EEXECEncryption.CHARSTRING_R,
                EEXECEncryption.N);
        for (int i = 0; i < result.length; i++)
            out.print(Integer.toHexString(result[i]) + " ");
        out.println("\n");

        PipedOutputStream pipeOut = new PipedOutputStream();
        PipedInputStream pipeIn = new PipedInputStream();
        pipeOut.connect(pipeIn);

        EEXECEncryption outEncrypted = new EEXECEncryption(pipeOut,
                EEXECConstants.EEXEC_R, EEXECEncryption.N);
        EEXECDecryption inEncrypted = new EEXECDecryption(pipeIn,
                EEXECConstants.EEXEC_R, EEXECEncryption.N);

        byte[] bytes = "Hello World! - advanced version (by Adobe)".getBytes();
        for (int i = 0; i < bytes.length; i++)
            outEncrypted.write(bytes[i]);
        outEncrypted.close();
        int b = -1;

        while ((b = inEncrypted.read()) != -1)
            out.print((char) b + " ");
        inEncrypted.close();

        out.close();
        
        Assert.assertEquals(refFile, outFile, false);
    }
}
