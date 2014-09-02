// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;

/**
 * EMF File Header.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFHeader.java 39340faa2114 2007/02/12 08:14:31 duns $
 */
public class EMFHeader implements EMFConstants {
    public static final Dimension screenMM = new Dimension(320, 240);

    private Rectangle bounds;

    private Rectangle frame;

    private String signature;

    private int versionMajor;

    private int versionMinor;

    private int bytes;

    private int records;

    private int handles;

    private String description;

    private int palEntries;

    private Dimension device;

    private Dimension millimeters;

    private Dimension micrometers;

    private boolean openGL;

    public EMFHeader(Rectangle bounds, int versionMajor, int versionMinor,
            int bytes, int records, int handles, String application,
            String name, Dimension device) {
        this.bounds = bounds;

        // this assumes you use MM_ANISOTROPIC or MM_ISOTROPIC as MapMode
        double pixelWidth = (double) screenMM.width / device.width;
        double pixelHeight = (double) screenMM.height / device.height;
        this.frame = new Rectangle((int) (bounds.x * 100 * pixelWidth),
                (int) (bounds.y * 100 * pixelHeight),
                (int) (bounds.width * 100 * pixelWidth),
                (int) (bounds.height * 100 * pixelHeight));

        this.signature = " EMF";
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
        this.bytes = bytes;
        this.records = records;
        this.handles = handles;
        this.description = application.trim() + "\0" + name.trim() + "\0\0";

        this.palEntries = 0;
        this.device = device;
        this.millimeters = screenMM;

        this.openGL = false;
        this.micrometers = new Dimension(screenMM.width * 1000,
                screenMM.height * 1000);
    }

    EMFHeader(EMFInputStream emf) throws IOException {
        // FIXME: incomplete
        emf.readUnsignedInt(); // 4

        int length = emf.readDWORD(); // 8

        bounds = emf.readRECTL(); // 24
        frame = emf.readRECTL(); // 40
        signature = new String(emf.readBYTE(4)); // 44

        int version = emf.readDWORD(); // 48
        versionMajor = version >> 16;
        versionMinor = version & 0xFFFF;
        bytes = emf.readDWORD(); // 52
        records = emf.readDWORD(); // 56
        handles = emf.readWORD(); // 58
        emf.readWORD(); // 60

        int dLen = emf.readDWORD(); // 64
        int dOffset = emf.readDWORD(); // 68
        palEntries = emf.readDWORD(); // 72
        device = emf.readSIZEL(); // 80
        millimeters = emf.readSIZEL(); // 88

        int bytesRead = 88;
        if (dOffset > 88) {
            emf.readDWORD(); // 92
            emf.readDWORD(); // 96
            openGL = (emf.readDWORD() != 0) ? true : false; // 100
            bytesRead += 12;
            if (dOffset > 100) {
                micrometers = emf.readSIZEL(); // 108
                bytesRead += 8;
            }
        }

        // Discard any bytes leading up to the description (usually zero, but safer not to assume.)
        if (bytesRead < dOffset) {
            emf.skipBytes(dOffset - bytesRead);
            bytesRead = dOffset;
        }

        description = emf.readWCHAR(dLen);
        bytesRead += dLen * 2;

        // Discard bytes after the description up to the end of the header.
        if (bytesRead < length) {
            emf.skipBytes(length - bytesRead);
        }
    }

    public void write(EMFOutputStream emf) throws IOException {
        int align = emf.getTagAlignment();
        int padding = (align - (size() % align)) % align;
        int alignedSize = size() + padding;

        emf.writeDWORD(0x00000001); // Header Type
        emf.writeDWORD(alignedSize); // length of header
        emf.writeRECTL(bounds); // inclusive bounds
        emf.writeRECTL(frame); // inclusive picture

        emf.writeBYTE(signature.getBytes()); // signature ID EMF
        emf.writeDWORD((versionMajor << 16) | versionMinor); // version
        emf.writeDWORD(alignedSize + bytes); // file size
        emf.writeDWORD(records); // # of records
        emf.writeWORD(handles); // # of handles, 1 minimum
        emf.writeWORD(0); // reserved
        emf.writeDWORD(description.length()); // size of descriptor in WORDS
        emf.writeDWORD(0x6C); // offset to descriptor
        emf.writeDWORD(palEntries); // # of palette entries
        emf.writeSIZEL(device); // size of ref device
        emf.writeSIZEL(millimeters); // size of ref device in MM
        emf.writeDWORD(0); // cbPixelFormat
        emf.writeDWORD(0); // offPixelFormat
        emf.writeDWORD(openGL); // bOpenGL
        emf.writeSIZEL(micrometers); // size of ref device in microns

        // optional description
        emf.writeWCHAR(description);

        // padding
        for (int i = 0; i < padding; i++) {
            emf.write(0);
        }
    }

    /**
     * @return size of emf file in bytes ?
     */
    public int size() {
        return 108 + (2 * description.length());
    }

    public String toString() {
        StringBuffer s = new StringBuffer("EMF Header\n");
        s.append("  bounds: ").append(bounds).append("\n");
        s.append("  frame: ").append(frame).append("\n");
        s.append("  signature: ").append(signature).append("\n");
        s.append("  versionMajor: ").append(versionMajor).append("\n");
        s.append("  versionMinor: ").append(versionMinor).append("\n");
        s.append("  #bytes: ").append(bytes).append("\n");
        s.append("  #records: ").append(records).append("\n");
        s.append("  #handles: ").append(handles).append("\n");
        s.append("  description: ").append(description).append("\n");
        s.append("  #palEntries: ").append(palEntries).append("\n");
        s.append("  device: ").append(device).append("\n");
        s.append("  millimeters: ").append(millimeters).append("\n");

        s.append("  openGL: ").append(openGL).append("\n");
        s.append("  micrometers: ").append(micrometers);

        return s.toString();
    }

    /**
     * Specifies the dimensions, in device units, of the smallest rectangle that
     * can be drawn around the picture stored in the metafile. This rectangle is
     * supplied by graphics device interface (GDI). Its dimensions include the
     * right and bottom edges.
     * @return bounds of device
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Specifies the dimensions, in .01 millimeter units, of a rectangle that
     * surrounds the picture stored in the metafile. This rectangle must be
     * supplied by the application that creates the metafile. Its dimensions
     * include the right and bottom edges.
     * @return bounds of frame
     */
    public Rectangle getFrame() {
        return frame;
    }

    /**
     * Specifies a double word signature. This member must specify the value
     * assigned to the ENHMETA_SIGNATURE constant.
     * @return signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @return the description of the enhanced metafile's contents
     */
    public String getDescription() {
        return description;
    }

    /**
     * Specifies the resolution of the reference device, in pixels.
     * @return resolution of the reference device, in pixels
     */
    public Dimension getDevice() {
        return device;
    }

    /**
     * Specifies the resolution of the reference device, in millimeters.
     * @return size in millimeters
     */
    public Dimension getMillimeters() {
        return millimeters;
    }

    /**
     * Windows 98/Me, Windows 2000/XP: Size of the reference device in
     * micrometers.
     * @return size in micrometers
     */
    public Dimension getMicrometers() {
        return micrometers;
    }

    /**
     * Windows 95/98/Me, Windows NT 4.0 and later: Specifies whether any OpenGL
     * records are present in a metafile. bOpenGL is a simple Boolean flag that
     * you can use to determine whether an enhanced metafile requires OpenGL
     * handling. When a metafile contains OpenGL records, bOpenGL is TRUE;
     * otherwise it is FALSE.
     *
     * @return false is default
     */
    public boolean isOpenGL() {
        return openGL;
    }
}
