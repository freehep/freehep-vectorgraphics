// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.Shape;

import org.freehep.graphicsio.emf.EMFConstants;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFOutputStream;
import org.freehep.graphicsio.emf.EMFTag;
import org.freehep.graphicsio.emf.EMFRenderer;

/**
 * SelectClipPath TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/gdi/SelectClipPath.java c0f15e7696d3 2007/01/22 19:26:48 duns $
 */
public class SelectClipPath extends EMFTag implements EMFConstants {

    private int mode;

    public SelectClipPath() {
        super(67, 1);
    }

    public SelectClipPath(int mode) {
        this();
        this.mode = mode;
    }

    public EMFTag read(int tagID, EMFInputStream emf, int len)
            throws IOException {

        return new SelectClipPath(emf.readDWORD());
    }

    public void write(int tagID, EMFOutputStream emf) throws IOException {
        emf.writeDWORD(mode);
    }

    public String toString() {
        return super.toString() + "\n  mode: " + mode;
    }

    /**
     * displays the tag using the renderer
     *
     * @param renderer EMFRenderer storing the drawing session data
     */
    public void render(EMFRenderer renderer) {
        GeneralPath currentPath = renderer.getPath();

        if (currentPath != null) {
            // The new clipping region includes the intersection
            // (overlapping areas) of the current clipping region and the current path.
            if (mode == EMFConstants.RGN_AND) {
                renderer.clip(currentPath);
            }
            // The new clipping region is the current path
            else if (mode == EMFConstants.RGN_COPY) {
                // rest the clip ...
                AffineTransform at = renderer.getTransform();
                // temporarly switch to the base transformation to
                // aplly the base clipping area
                renderer.resetTransformation();
                // set the clip
                renderer.setClip(renderer.getInitialClip());
                renderer.setTransform(at);
                renderer.clip(currentPath);
            }
            // The new clipping region includes the areas of the
            // current clipping region with those of the current path excluded.
            else if (mode == EMFConstants.RGN_DIFF) {
                Shape clip = renderer.getClip();
                if (clip != null) {
                    Area a = new Area(currentPath);
                    a.subtract(new Area(clip));
                    renderer.setClip(a);
                } else {
                    renderer.setClip(currentPath);
                }
            }
            // The new clipping region includes the union (combined areas)
            // of the current clipping region and the current path.
            else if(mode == EMFConstants.RGN_OR) {
                Shape clip = renderer.getClip();
                if (clip != null) {
                    currentPath.append(clip, false);
                }
                renderer.setClip(currentPath);
            }
            // The new clipping region includes the union of the current
            // clipping region and the current path but without the overlapping areas.
            else if(mode == EMFConstants.RGN_XOR) {
                Shape clip = renderer.getClip();
                if (clip != null) {
                    Area a = new Area(currentPath);
                    a.exclusiveOr(new Area(clip));
                    renderer.setClip(a);
                } else {
                    renderer.setClip(currentPath);
                }
            }
        }

        // delete the current path
        renderer.setPath(null);
    }
}
