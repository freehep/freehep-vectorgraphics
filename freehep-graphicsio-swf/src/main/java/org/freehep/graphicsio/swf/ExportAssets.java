// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.swf;

import java.io.IOException;

/**
 * ExportAssets TAG.
 * 
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: freehep-graphicsio-swf/src/main/java/org/freehep/graphicsio/swf/ExportAssets.java db861da05344 2005/12/05 00:59:43 duns $
 */
public class ExportAssets extends ControlTag {

    private int[] tags;

    private String[] assets;

    public ExportAssets(int[] tags, String[] assets) {
        this();
        this.tags = tags;
        this.assets = assets;
    }

    public ExportAssets() {
        super(56, 5);
    }

    public SWFTag read(int tagID, SWFInputStream swf, int len)
            throws IOException {

        ExportAssets tag = new ExportAssets();
        int n = swf.readUnsignedShort();
        tag.tags = new int[n];
        tag.assets = new String[n];
        for (int i = 0; i < n; i++) {
            tag.tags[i] = swf.readUnsignedShort();
            tag.assets[i] = swf.readString();
        }
        return tag;
    }

    public void write(int tagID, SWFOutputStream swf) throws IOException {
        swf.writeUnsignedShort(tags.length);
        for (int i = 0; i < tags.length; i++) {
            swf.writeUnsignedShort(tags[i]);
            swf.writeString(assets[i]);
        }
    }
}
