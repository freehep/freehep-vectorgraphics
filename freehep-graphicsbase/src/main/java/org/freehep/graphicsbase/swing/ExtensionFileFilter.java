package org.freehep.graphicsbase.swing;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.filechooser.FileFilter;

/**
 * A FileFilter which filters out all files except those which end with the
 * given tag.  This filter is case sensitive and the string is compared
 * literally.  
 *
 * You may add extensions with or without the leading dot. The comparison
 * always uses the leading dot.
 *
 * Long extensions such as ".heprep.zip" are also allowed.
 *
 * Example - create a new filter that filerts out all files
 * but gif and jpg image files:
 *
 * <pre>
 *     JFileChooser chooser = new JFileChooser();
 *     ExtensionFileFilter filter = new ExtensionFileFilter(
 *                   new String{".gif", ".jpg"}, "JPEG & GIF Images")
 *     chooser.addChoosableFileFilter(filter);
 *     chooser.showOpenDialog(this);
 * </pre>
 *
 * Based on the ExtensionFileFilter written by Jeff Dinkins and provided by
 * Sun.
 *
 * @author Charles A. Loomis, Jr.
 * @author Mark Donszelmann
 * @version $Id: ExtensionFileFilter.java 8584 2006-08-10 23:06:37Z duns $
 */
public class ExtensionFileFilter extends FileFilter {

//    private static String TYPE_UNKNOWN = "Type Unknown";
//    private static String HIDDEN_FILE = "Hidden File";

    private String description = null;
    private String fullDescription = null;
    private boolean useExtensionsInDescription = true;

    private LinkedList<String> endings = null;

    /**
     * Creates a file filter. If no filters are added, then all
     * files are rejected.
     *
     * @see #addExtension
     */
    public ExtensionFileFilter() {
        this.endings = new LinkedList<String>();
    }

    /**
     * Creates a file filter that accepts files with the given ending.
     *
     * @see #addExtension
     */
    public ExtensionFileFilter(String extension) {
	    this(extension, null);
    }

    /**
     * Creates a file filter that accepts the files with the given endings.
     *
     * @see #addExtension
     */
    public ExtensionFileFilter(String extension, String description) {
	    this();
	    if (extension!=null) addExtension(extension);
 	    if (description!=null) setDescription(description);
    }

    /**
     * Creates a file filter from the array of given endings.
     *
     * @see #addExtension
     */
    public ExtensionFileFilter(String[] filters) {
	    this(filters, null);
    }

    /**
     * Creates a file filter from the given string array and description.
     *
     * @see #addExtension
     */
    public ExtensionFileFilter(String[] filters, String description) {
	    this();
    	for (int i = 0; i < filters.length; i++) {
    	    // add filters one by one
    	    addExtension(filters[i]);
    	}
     	if  (description!=null) setDescription(description);
    }

    /**
     * Return true if this file should be shown in the directory pane,
     * false if it shouldn't.
     *
     * @see FileFilter#accept
     */
    public boolean accept(File f) {
    	if (f!=null) {
    	    if (f.isDirectory()) return true;
    	    if (match(f)) return true;
    	}
    	return false;
    }

    /**
     * This determines if the file matches any of the extensions. 
     */
    protected boolean match(File f) {
        return (getExtension(f) != null);
    }

    /**
     * Returns the extension portion of the files name (if part of this file filter).
     * 
     * @return extension including leading ".", or null if not found in filter.
     */
    public String getExtension(File f) {

    	if (f!=null) {
    	    String filename = f.getName();
            Iterator<String> i = endings.iterator();
            while (i.hasNext()) {
                String end = i.next();
                if (filename.endsWith(end)) return end;
    	    }
    	}
        return null;
    }

    /**
     * Adds an extension to filter against.
     * Leading "." is not mandatory.
     */
    public void addExtension(String extension) {
        endings.add(extension.startsWith(".") ? extension : "."+extension);
    	fullDescription = null;
    }


    /**
     * Returns the human readable description of this filter. For
     * example: "JPEG and GIF Image Files (*.jpg, *.gif)"
     *
     * @see #setDescription
     * @see #setExtensionListInDescription
     * @see #isExtensionListInDescription
     * @see FileFilter#getDescription
     */
    public String getDescription() {
    	if (fullDescription == null) {
    	    if (description == null || isExtensionListInDescription()) {
     		    fullDescription = description==null ? "(" : description + " (";
    		    // build the description from the extension list
    		    Iterator<String> i = endings.iterator();
    		    while (i.hasNext()) {
    		        fullDescription += i.next();
                    if (i.hasNext()) fullDescription += ", ";
    		    }
    		    fullDescription += ")";
    	    } else {
    		    fullDescription = description;
    	    }
    	}
    	return fullDescription;
    }

    /**
     * Sets the human readable description of this filter. For
     * example: filter.setDescription("Gif and JPG Images");
     *
     * @see #setDescription
     * @see #setExtensionListInDescription
     * @see #isExtensionListInDescription
     */
    public void setDescription(String description) {
    	this.description = description;
    	fullDescription = null;
    }

    /**
     * Determines whether the extension list (.jpg, .gif, etc) should
     * show up in the human readable description.
     *
     * Only relevent if a description was provided in the constructor
     * or using setDescription();
     *
     * @see #getDescription
     * @see #setDescription
     * @see #isExtensionListInDescription
     */
    public void setExtensionListInDescription(boolean b) {
    	useExtensionsInDescription = b;
    	fullDescription = null;
    }

    /**
     * Returns whether the extension list (.jpg, .gif, etc) should
     * show up in the human readable description.
     *
     * Only relevent if a description was provided in the constructor
     * or using setDescription();
     *
     * @see #getDescription
     * @see #setDescription
     * @see #setExtensionListInDescription
     */
    public boolean isExtensionListInDescription() {
    	return useExtensionsInDescription;
    }
}
