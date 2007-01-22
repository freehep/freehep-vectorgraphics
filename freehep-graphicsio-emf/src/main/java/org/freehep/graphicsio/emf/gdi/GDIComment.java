// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.awt.Image;
import java.awt.Rectangle;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFRenderer;

import javax.imageio.ImageIO;

/**
 * GDIComment TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/GDIComment.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class GDIComment extends EMFTag {

    private int type;

    private String comment = "";

    private Image image;

    public GDIComment() {
        super(70, 1);
    }

    public GDIComment(String comment) {
        this();
        this.comment = comment;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        GDIComment result = new GDIComment();

        int l = emf.readDWORD();

        result.type = emf.readDWORD();

        // not documented, but embedded GIF / PNG images
        // start with that tag
        if (result.type == 726027589) {
            /*byte[] bytes = */ emf.readByte(l - 4);
            if (l % 4 != 0) {
                emf.readBYTE(4 - l % 4);
            }
        } else if (result.type == EMFConstants.GDICOMMENT_BEGINGROUP) {
            // This is the bounding rectangle for the
            // object in logical coordinates.
            /* Rectangle rclOutput = */ emf.readRECTL();

            // This is the number of characters in the
            // optional Unicode description string that
            // follows. This is zero if there is no
            // description string.
            int nDescription = emf.readDWORD();

            // read the description
            if (nDescription > 0) {
                result.comment = new String(emf.readByte(nDescription));
            }
        } else if (result.type == EMFConstants.GDICOMMENT_ENDGROUP) {
            // nothing to to
        } else if (result.type == EMFConstants.GDICOMMENT_MULTIFORMATS) {
            // This is the bounding rectangle for the
            // picture in logical coordinates.
            /* Rectangle rclOutput = */ emf.readRECTL();

            // This contains the number of formats in
            // the comment.
            /* nFormats = */ emf.readDWORD();
            
            // This is an array of EMRFORMAT structures
            // in the order of preference.  The data
            // for each format follows the last
            // EMRFORMAT structure.

            // TODO read tagEMRFORMAT

            /*
            typedef struct tagEMRFORMAT {
                  DWORD   dSignature;
                  DWORD   nVersion;
                  DWORD   cbData;
                  DWORD   offData;
                } EMRFORMAT;
            */

            l = l - 4 - 8;

            result.comment = new String(emf.readBYTE(l));
            // Align to 4-byte boundary
            if (l % 4 != 0)
                emf.readBYTE(4 - l % 4);

        } else if (result.type == EMFConstants.GDICOMMENT_WINDOWS_METAFILE) {
            // This contains the version number of the
            // Windows-format metafile.
            /*int version =*/ emf.readDWORD();

            // This is the additive DWORD checksum for
            // the enhanced metafile.  The checksum
            // for the enhanced metafile data including
            // this comment record must be zero.
            // Otherwise, the enhanced metafile has been
            //  modified and the Windows-format
            // metafile is no longer valid.
            /*int checksum =*/ emf.readDWORD();

            // This must be zero.
            /* int fFlags = */ emf.readDWORD();

            // This is the size, in bytes. of the
            // Windows-format metafile data that follows.
            int size = emf.readDWORD();

            // read the image data
            byte[] bytes = emf.readByte(size);
            result.image = ImageIO.read(new ByteArrayInputStream(bytes));
            return this;

        } else {
            l = l - 4;

            result.comment = new String(emf.readBYTE(l));
            // Align to 4-byte boundary
            if (l % 4 != 0)
                emf.readBYTE(4 - l % 4);
        }
        return result;
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        byte[] b = comment.getBytes();
        emf.writeDWORD(b.length);
        emf.writeBYTE(b);
        if (b.length % 4 != 0)
            for (int i = 0; i < 4 - b.length % 4; i++)
                emf.writeBYTE(0);
    }

    public String toString() {
        return super.toString() + "\n  length: " + comment.length();
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        // do nothing
    }
}
