// Copyright 2000-2007 FreeHEP
package org.freehep.graphicsio.ps;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.freehep.graphicsio.MultiPageDocument;

/**
 * @author Charles Loomis
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-ps/src/main/java/org/freehep/graphicsio/ps/PSGraphics2D.java bed5e3a39f35 2007/09/10 18:13:00 duns $
 */
public class PSGraphics2D extends AbstractPSGraphics2D implements
        MultiPageDocument {

    private boolean multiPage;

    private int currentPage;

    public PSGraphics2D(File file, Dimension size) throws FileNotFoundException {
        this(new FileOutputStream(file), size);
    }

    public PSGraphics2D(File file, Component component)
            throws FileNotFoundException {
        this(new FileOutputStream(file), component);
    }

    public PSGraphics2D(OutputStream os, Dimension size) {
        super(size, false);
        init(os);
    }

    public PSGraphics2D(OutputStream os, Component component) {
        super(component, false);
        init(os);
    }

    protected PSGraphics2D(PSGraphics2D graphics, boolean doRestoreOnDispose) {
    	super(graphics, doRestoreOnDispose);
        multiPage = graphics.multiPage;
        currentPage = graphics.currentPage;
    }
    
    protected void init(OutputStream os) {
    	super.init(os);
        this.multiPage = false;
        this.currentPage = 0;

    }

    public void setMultiPage(boolean multiPage) {
        this.multiPage = multiPage;
    }

    public boolean isMultiPage() {
        return multiPage;
    }
    
    public void openPage(Component component) throws IOException {
        openPage(component.getSize(), component.getName(), component);
    }

    public void openPage(Dimension size, String title) throws IOException {
        openPage(size, title, null);
    }

    protected void openPage(Dimension size, String title, Component component) {
        currentPage++;
        title = (title == null) ? "" + currentPage : "(" + title + ")";
        if (isMultiPage())
            os.println("%%Page: " + title + " " + currentPage);

        os.println(isMultiPage() ? "%%BeginPageSetup" : "%%BeginSetup");

        super.openPage(size, title, component);
        
        os.println(isMultiPage() ? "%%EndPageSetup" : "%%EndSetup");
        os.println();

        try {
            writeGraphicsState();
            writeBackground();
        } catch (Exception e) {
            handleException(e);
        }
    }
    
    public void closePage() {
        if (isMultiPage())
            os.println("%%PageTrailer");
    }
    
    public void writeHeader() throws IOException {
        if (!isMultiPage()) {
            Dimension size = getSize();
            // moved to openPage for multiPage
            resetClip(new Rectangle(0, 0, size.width, size.height));
        }

        os = new PrintStream(ros, true);
        os.println("%!PS-Adobe-3.0");

        super.writeHeader();

        if (!isMultiPage())
            openPage(getSize(), null, getComponent());
    }
    
    public void writeTrailer() throws IOException {
        if (!isMultiPage())
            closePage();
        super.writeTrailer();
        if (isMultiPage())
            os.println("%%Pages: " + currentPage);
        os.println("%%EOF");
    }
    
    public Graphics create() {
        try {
            writeGraphicsSave();
        } catch (IOException e) {
            handleException(e);
        }
        return new PSGraphics2D(this, true);
    }

    public Graphics create(double x, double y, double width, double height) {
        try {
            writeGraphicsSave();
        } catch (IOException e) {
            handleException(e);
        }
        PSGraphics2D graphics = new PSGraphics2D(this, true);
        graphics.translate(x, y);
        graphics.clipRect(0, 0, width, height);
        return graphics;
    }    
}
