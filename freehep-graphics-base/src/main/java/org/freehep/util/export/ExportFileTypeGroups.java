// Copyright 2006, FreeHEP.
package org.freehep.util.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author duns
 * @version $Id: src/main/java/org/freehep/util/export/ExportFileTypeGroups.java ea16f7591925 2006/12/04 07:44:04 duns $
 */
public class ExportFileTypeGroups {
	
	public static final String VECTOR = "vector";
	public static final String BITMAP = "bitmap";
	public static final String OTHER = "other";
	public static final String ALTERNATIVE = "alternative";
	
	private List/*<String>*/ groupNames;
	private Map/*<String, Map<String, ExportFileType>>*/ groups;
	private List/*<ExportFileType>*/ alternatives;
	private Map/*<String, String>*/ labels;
	
	public ExportFileTypeGroups(List/*<ExportFileType>*/ exportFileTypes) {
		groupNames = new ArrayList();
		groupNames.add(VECTOR);
		groupNames.add(BITMAP);
		groupNames.add(OTHER);
		groupNames.add(ALTERNATIVE);
		
		labels = new HashMap();
		labels.put(VECTOR, "Vector Formats");
		labels.put(BITMAP, "Bitmap Formats");
		labels.put(OTHER, "Other");
		labels.put(ALTERNATIVE, "Alternative Formats");
		
		groups = new HashMap();
		alternatives = new ArrayList();
		for (Iterator i = exportFileTypes.iterator(); i.hasNext(); ) {
            ExportFileType exportFileType = (ExportFileType)i.next();
            
            String mimeType = exportFileType.getMIMETypes()[0]; 
            String key;
            if (MimeTypes.isBitmap(mimeType)) {
            	key = BITMAP;
            } else if (MimeTypes.isVector(mimeType)) {
            	key = VECTOR;
            } else {
            	key = OTHER;
            }
            
            Map fileTypes = (Map)groups.get(key);
            if (fileTypes == null) {
            	fileTypes = new HashMap();
            	groups.put(key, fileTypes);
            }
            
            String extension = exportFileType.getExtensions()[0];
            if (fileTypes.get(extension) == null) {
            	fileTypes.put(extension, exportFileType);
            } else {
            	alternatives.add(exportFileType);
            }
        }		
	}
	
    /**
     * Return all registered ExportFileTypes for a certain group.
     */
    public List getExportFileTypes(String group) {
    	List result = new ArrayList();
    	
    	if (group.equals(ALTERNATIVE)) {
    		result.addAll(alternatives);
    	} else {
	    	Map fileTypes = (Map)groups.get(group);
	    	if (fileTypes != null) result.addAll(fileTypes.values());
    	}
    	return result;
    }
    
    public List getGroupNames() {
    	return groupNames;
    }
    
    public String getLabel(String group) {
    	return (String)labels.get(group);
    }
}
