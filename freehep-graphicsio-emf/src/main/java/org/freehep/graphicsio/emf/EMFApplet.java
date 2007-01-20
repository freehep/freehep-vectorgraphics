// Copyright 2007 FreeHEP
package org.freehep.graphicsio.emf;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Applet to render EMF files on any platform in a browser.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFApplet.java 63c8d910ece7 2007/01/20 15:30:50 duns $
 */
public class EMFApplet extends Applet {

//    private EMFRenderer renderer;    
    
    public void init() {
        super.init();
        System.err.println("init");
        try {
            URL url = new URL("file:/Users/duns/svn/freehep/vectorgraphics/freehep-graphicsio-emf/TestOffset.emf");
            EMFInputStream is = new EMFInputStream(url.openStream());
            EMFRenderer renderer = new EMFRenderer(is);
            EMFPanel panel = new EMFPanel();
            panel.setRenderer(renderer);
            add(panel);
        } catch (MalformedURLException mfue) {
            System.err.println("URL Malformed "+mfue);
        } catch (IOException ioe) {
            System.err.println("IO Exception "+ioe);
        }
    }

    public void start() {
        super.start();
        System.err.println("start");
//        repaint();
    }

    public void stop() {
        super.stop();
        System.err.println("stop");
    }

    public void destroy() {
        super.destroy();
        System.err.println("destroy");
    }

    /*
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        System.err.println("paint");
        renderer.paint((Graphics2D)g);
    }
    */
}
