// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Protect TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/Protect.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class Protect extends ControlTag {

    private String password;

    public Protect(String password) {
        this();

        this.password = password;
        if (password != null) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] b = md5.digest(password.getBytes());
                this.password = new String(b);
            } catch (NoSuchAlgorithmException nsae) {
            }
        }
    }

    public Protect() {
        super(24, 2);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        if (len != 0) {
            Protect tag = new Protect();
            tag.password = new String(swf.readByte(len));
            return tag;
        } else {
            return this;
        }
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        if ((password != null) && (swf.getVersion() >= 5)) {
            swf.writeByte(password.getBytes());
        }
    }
}
