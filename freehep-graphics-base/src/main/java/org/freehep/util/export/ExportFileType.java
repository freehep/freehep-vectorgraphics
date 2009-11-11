// Copyright 2003-2006, FreeHEP.
package org.freehep.util.export;

import java.awt.Component;
import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.freehep.swing.ExtensionFileFilter;

/**
 * Objects which extend this class provide enough functionality
 * to provide an output file type for the ExportDialog.
 *
 * @author Charles Loomis
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/util/export/ExportFileType.java e26a31b64eb2 2007/06/12 22:18:45 duns $
 */
public abstract class ExportFileType implements Comparable {

    private static ClassLoader loader;

    /**
     * Returns a short description of the format
     */
    public abstract String getDescription();

    /**
     * Returns an array of possible extensions for the format, the first of
     * which is the preferred extension
     */
    public abstract String[] getExtensions();

    /**
     * Return the MIME-type(s) for this format, the first of which is the
     * preferred MIME type.
     */
    public abstract String[] getMIMETypes();

    /**
     * Writes the given component out in the format of this file
     * type.
     */
    public void exportToFile(OutputStream os, Component target,
                             Component parent, Properties properties, String creator)
	    throws java.io.IOException {
	    exportToFile(os, new Component[] {target}, parent, properties, creator);
	}

    /**
     * Writes the given component out in the format of this file
     * type.
     */
    public void exportToFile(File file, Component target,
                             Component parent, Properties properties, String creator)
	    throws java.io.IOException {
	    exportToFile(file, new Component[] {target}, parent, properties, creator);
	}

    /**
     * Writes the all given components out in the format of this file
     * type.
     */
    public abstract void exportToFile(OutputStream os, Component[] target,
                             Component parent, Properties properties, String creator)
	    throws java.io.IOException;

    /**
     * Writes the all given components out in the format of this file
     * type.
     */
    public abstract void exportToFile(File file, Component[] target,
                             Component parent, Properties properties, String creator)
	    throws java.io.IOException;


    /**
     * Compares to other exportfiletype in alphabetical order on the description string.
     */
    public int compareTo(Object o) {
        ExportFileType type = (ExportFileType)o;
        return getDescription().compareTo(type.getDescription());
    }

    /**
     * Returns true if this format has extra options.
     */
    public boolean hasOptionPanel() {
        return false;
    }

    /**
     * Returns a panel which allows to user to set options associated
     * with this particular output format.
     */
    public JPanel createOptionPanel(Properties options) {
        return null;
    }

    /**
     * Sets any changed options from the optionPanel to the properties object.
     *
     * @return true if any options were set.
     */
    public boolean applyChangedOptions(JPanel optionPanel, Properties options) {
        return false;
    }

    /**
     * Returns a file filter which selects only those files which
     * correspond to a particular file type.
     */
    public FileFilter getFileFilter() {
        return new ExtensionFileFilter(getExtensions(), getDescription());
    }

    /**
     * Gives the accessory the chance to change the output filename.
     * In particular, to change the extension to match the output file
     * format.
     */
    public File adjustFilename(File file, Properties properties) {
        return adjustFilename(file, "", properties);
    }

    /**
     * Gives the accessory the chance to change the output filename.
     * In particular, to change the extension to match the output file
     * format.
     */
    public File adjustFilename(File file, String extension, Properties properties) {
        return adjustExtension(file, getExtensions()[0], getExtensions(), extension);
    }

    /**
     * Returns the longest valid extension on this filename, or "".
     */
    public String getFileExtension(File file) {
        return getExtension(file, getExtensions());
    }

    /**
     * This method returns true if the given file has an extension
     * which can be handled by this file type.
     */
    public boolean fileHasValidExtension(File file) {
        return checkExtension(file, getExtensions());
    }

    /**
     * Returns true if this ExportFileType can handle multipage output.
     * The default implementation return false;
     */
    public boolean isMultipageCapable() {
        return false;
    }

    /**
     * Sets the classloader to be used for loading ExportFileTypes
     */
    public static void setClassLoader(ClassLoader loader) {
        ExportFileType.loader = loader;
    }

    /**
     * Return all registered ExportFileTypes
     */
    public static List getExportFileTypes() {
        return getExportFileTypes(null);
    }

    /**
     * Return all registered ExportFileTypes for a certain format. Format may be null,
     * in which case all ExportFileTypes are returned.
     */
    public static List getExportFileTypes(String format) {
        return ExportFileTypeRegistry.getDefaultInstance(loader).get(format);
    }
    
    /**
     * Returns the file's (longest) valid extension (ps, eps, heprep.zip), or "".
     */
    public static String getExtension(File file, String[] acceptableExtensions) {
        String extension = "";
        if (file!=null && acceptableExtensions!=null) {

            String name = file.getName();
            name = name.toLowerCase();
            
            for (int i=0; i<acceptableExtensions.length; i++) {
                String acceptableExtension = acceptableExtensions[i].toLowerCase();
                if (name.endsWith("."+acceptableExtension) && (acceptableExtension.length() > extension.length())) {
                    extension = acceptableExtensions[i];
                }
            }
        }
        return extension;
    }

    /**
     * A utility function that checks a file's extension.
     */
    public static boolean checkExtension(File file,
                     String[] acceptableExtensions) {

        return getExtension(file, acceptableExtensions).length() > 0;
    }

    /**
     * Change the extension of a file if it is not of the appropriate
     * type.
     *
     * @deprecated use adjustExtension(File, String, String[], String)
     */
    public static File adjustExtension(File file,
                       String preferredExtension,
                       String[] acceptableExtensions) {
        return adjustExtension(file, preferredExtension, acceptableExtensions, "");
    }

    /**
     * Change the extension of a file if it is not of the appropriate
     * type.
     * @param extension ext to be removed.
     */
    public static File adjustExtension(File file,
                       String preferredExtension,
                       String[] acceptableExtensions,
                       String extension) {

        File returnValue = file;

        if (file!=null) {

            // Get first the filename without the extension and the
            // extension itself.
            String originalParent = file.getParent();
            String originalName = file.getName();
            String mainName = "";

            if (originalParent!=null) {
                mainName = originalParent+File.separator;
            }

            if ((extension.length() > 0) && originalName.endsWith(extension)) {               
                mainName += originalName.substring(0, originalName.length()-extension.length()-1);
            } else {
                // calculate extension as last part of name
                int dotIndex = originalName.lastIndexOf('.');
                if(dotIndex>0 && dotIndex<(originalName.length()-1)) {
                    mainName += originalName.substring(0,dotIndex);
                    extension = originalName.substring(dotIndex+1);
                } else {
                    mainName += originalName;
                    extension = "";
                }
            }
            
            // Check to see if the extension is one of the acceptable
            // extensions.
            returnValue = new File(mainName+"."+extension);

            // If the extension isn't an acceptable one, then change
            // the extension to the preferred one.
            if (!checkExtension(returnValue, acceptableExtensions)) returnValue = new File(mainName+"."+preferredExtension);
        }
        return returnValue;
    }
}
