// Copyright 2003-2005, FreeHEP
package org.freehep.graphicsio.exportchooser;

import java.awt.Component;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JPanel;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.MultiPageDocument;
import org.freehep.util.export.ExportFileType;

/**
 * 
 * @author Charles Loomis
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/AbstractExportFileType.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public abstract class AbstractExportFileType extends ExportFileType {

    public abstract class CancelThread extends Thread {

        private boolean stop;

        private File currentFile;

        private OutputStream currentOut;

        ProgressDialog progress;

        private CancelThread(ProgressDialog progress) {
            super();
            this.progress = progress;
            this.stop = false;
            progress.interruptOnCancel(this);
        }

        abstract void export() throws IOException;

        public void cancel() {
            this.stop = true;
        }

        public boolean isStopped() {
            return stop;
        }

        /**
         * Exports the graphics and afterwards cleans up, i.e. deletes the
         * current file. Hence, current file must be set to null by the
         * subclasses export() method when export is finished. Exceptions will
         * be reported to the ProgressDialog.
         */
        public void run() {
            try {
                export();
            } catch (IOException e) {
                if (!stop) {
                    progress.exceptionOccured(e);
                }
            }
            try {
                cleanUp();
            } catch (IOException e) {
                System.err.println("While cleaning up:");
                e.printStackTrace();
            }
        }

        void setCurrentFile(File file) {
            this.currentFile = file;
        }

        void setCurrentOutputStream(OutputStream out) {
            this.currentOut = out;
        }

        void cleanUp() throws IOException {
            if (currentOut != null)
                currentOut.close();
            if (currentFile != null)
                currentFile.delete();
        }
    }

    private class MultiPageSingleFileExportThread extends CancelThread {

        private MultiPageDocument mdoc;

        private VectorGraphics graphics;

        private Component[] saveTargets;

        private MultiPageSingleFileExportThread(MultiPageDocument mdoc,
                VectorGraphics g, Component[] saveTargets,
                ProgressDialog progress) {

            super(progress);
            this.mdoc = mdoc;
            this.graphics = g;
            this.saveTargets = saveTargets;
            this.progress = progress;
        }

        private MultiPageSingleFileExportThread(MultiPageDocument mdoc,
                VectorGraphics g, Component[] saveTargets,
                ProgressDialog progress, OutputStream out) {
            this(mdoc, g, saveTargets, progress);
            setCurrentOutputStream(out);
        }

        private MultiPageSingleFileExportThread(MultiPageDocument mdoc,
                VectorGraphics g, Component[] saveTargets,
                ProgressDialog progress, File file) {
            this(mdoc, g, saveTargets, progress);
            setCurrentFile(file);
        }

        void export() throws IOException {

            graphics.startExport();

            for (int i = 0; i < saveTargets.length; i++) {
                if (isStopped())
                    return;

                // Now actually print the components.
                if ((graphics != null) && (mdoc != null)) {
                    progress.step("Writing page " + (i + 1) + "/"
                            + saveTargets.length + ".");
                    mdoc.openPage(saveTargets[i]);
                    if (isStopped())
                        return;
                    progress.step("Writing page " + (i + 1) + "/"
                            + saveTargets.length + "..");
                    saveTargets[i].print(graphics);
                    if (isStopped())
                        return;
                    progress.step("Writing page " + (i + 1) + "/"
                            + saveTargets.length + "...");
                    mdoc.closePage();
                    if (isStopped())
                        return;
                }
            }
            if (isStopped())
                return;

            progress.step("Writing trailer...");
            graphics.endExport();

            setCurrentOutputStream(null);
            setCurrentFile(null);
            progress.dispose();
        }
    }

    private class MultipageMultipleFilesExportThread extends CancelThread {

        private File file;

        private Component[] saveTargets;

        private Component parent;

        private Properties properties;

        private String creator;

        private MultipageMultipleFilesExportThread(Component[] saveTargets,
                File file, ProgressDialog progress, Component parent,
                Properties properties, String creator) {
            super(progress);
            this.file = file;
            this.saveTargets = saveTargets;
            this.parent = parent;
            this.properties = properties;
            this.creator = creator;
        }

        void export() throws IOException {
            for (int i = 0; i < saveTargets.length; i++) {
                if (isStopped()) {
                    cleanUp();
                    return;
                }
                progress.step("Page " + (i + 1) + "/" + saveTargets.length
                        + "...");
                String filename = file.getAbsolutePath();
                int dotIndex = filename.lastIndexOf(".");
                if (dotIndex == -1)
                    dotIndex = filename.length() - 1;
                filename = filename.substring(0, dotIndex) + "-" + (i + 1)
                        + filename.substring(dotIndex);
                File singleFile = new File(filename);
                setCurrentFile(singleFile);
                exportToFile(singleFile, saveTargets[i], parent, properties,
                        creator);
                setCurrentFile(null);
            }
            progress.dispose();
        }
    }

    /**
     * Delegates to getGraphics(OutputStream, Component);
     */
    public VectorGraphics getGraphics(File file, Component target)
            throws java.io.IOException {
        return getGraphics(
                new BufferedOutputStream(new FileOutputStream(file)), target);
    }

    /**
     * The method returns a graphics context specific for this ExportFileType.
     */
    public abstract VectorGraphics getGraphics(OutputStream os,
            Component printTarget) throws java.io.IOException;

    /**
     * Show this dialog and save component as the given file type.
     */
    public void exportToFile(OutputStream os, Component target,
            Component parent, Properties properties, String creator)
            throws java.io.IOException {

        VectorGraphics g = getGraphics(os, target);

        if (g != null) {
            g.setCreator(creator);
            g.setProperties(properties);
            g.startExport();
            target.print(g);
            g.endExport();
        }
    }

    /**
     * Show this dialog and save component as the given file type.
     */
    public void exportToFile(File file, Component target, Component parent,
            Properties properties, String creator) throws java.io.IOException {

        VectorGraphics g = getGraphics(file, target);

        if (g != null) {
            g.setCreator(creator);
            g.setProperties(properties);
            g.startExport();
            target.print(g);
            g.endExport();
        }
    }

    /**
     * Save all components as the given file type. If
     * <tt>getConfiguredGraphics(OutputStream, Component[])</tt> does not
     * return a <tt>MultiPageDocument</tt> (or null)
     * <tt>saveTargets.length</tt> numbered documents are created each of
     * which contains a single page.
     */
    public void exportToFile(OutputStream os, Component[] targets,
            Component parent, Properties properties, String creator)
            throws java.io.IOException {

        ProgressDialog progress = null;
        Thread exportThread = null;
        VectorGraphics g = getGraphics(os, targets[0]);
        if ((g != null) && (g instanceof MultiPageDocument)) {
            g.setCreator(creator);
            g.setProperties(properties);
            progress = new ProgressDialog(parent, targets.length * 3 + 2,
                    "Writing header...");
            MultiPageDocument mdoc = (MultiPageDocument) g;
            mdoc.setMultiPage(true);
            exportThread = new MultiPageSingleFileExportThread(mdoc, g,
                    targets, progress, os);
            exportThread.start();
            progress.setVisible(true);
            IOException exc = progress.getException();
            if (exc != null)
                throw exc;
        } else {
            System.out
                    .println("Cannot write multi files to one output stream.");
        }
    }

    /**
     * Save all components as the given file type. If
     * <tt>getConfiguredGraphics(File, Component[])</tt> does not return a
     * <tt>MultiPageDocument</tt> (or null) <tt>saveTargets.length</tt>
     * numbered documents are created each of which contains a single page.
     */
    public void exportToFile(File file, Component[] targets, Component parent,
            Properties properties, String creator) throws java.io.IOException {

        ProgressDialog progress = null;
        Thread exportThread = null;
        VectorGraphics g = getGraphics(file, targets[0]);
        if ((g != null) && (g instanceof MultiPageDocument)) {
            g.setCreator(creator);
            g.setProperties(properties);
            progress = new ProgressDialog(parent, targets.length * 3 + 2,
                    "Writing header...");
            MultiPageDocument mdoc = (MultiPageDocument) g;
            mdoc.setMultiPage(true);
            exportThread = new MultiPageSingleFileExportThread(mdoc, g,
                    targets, progress, file);
        } else {
            progress = new ProgressDialog(parent, targets.length + 2,
                    "Exporting files...");
            exportThread = new MultipageMultipleFilesExportThread(targets,
                    file, progress, parent, properties, creator);
        }
        exportThread.start();
        progress.setVisible(true);
        IOException e = progress.getException();
        if (e != null)
            throw e;
    }

    public boolean applyChangedOptions(JPanel panel, Properties options) {
        if (panel instanceof Options) {
            return ((Options) panel).applyChangedOptions(options);
        }
        System.err
                .println(getClass()
                        + ": applyChangedOptions(...), panel does not implement Options interface.");
        return false;
    }
}
