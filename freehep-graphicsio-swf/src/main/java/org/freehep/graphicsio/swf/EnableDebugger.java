// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * EnableDebugger TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/EnableDebugger.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class EnableDebugger extends ControlTag {

    private String password;

    public EnableDebugger(String password) {
        this();

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] b = md5.digest(password.getBytes());
            this.password = new String(b);
        } catch (NoSuchAlgorithmException nsae) {
            this.password = password;
        }
    }

    public EnableDebugger() {
        super(58, 5);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        EnableDebugger tag = new EnableDebugger();
        tag.password = swf.readString();
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeString(password);
    }
}
