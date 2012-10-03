// Copyright 2003-2009, FreeHEP.
package org.freehep.graphicsbase.util.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.imageio.spi.RegisterableService;
import javax.imageio.spi.ServiceRegistry;

import org.freehep.graphicsbase.util.Service;

/**
 *
 * @author Mark Donszelmann
 */
public class ExportFileTypeRegistry {

    private static ExportFileTypeRegistry registry;
    private static ClassLoader loader;

    private ServiceRegistry service;
    private List<ExportFileType> extraTypes;
    
    private static final Collection<Class<?>> categories = new ArrayList<Class<?>>(2);
    static {
        categories.add(ExportFileType.class);
        categories.add(RegisterableService.class);
    }

    private ExportFileTypeRegistry() {
        service = new ServiceRegistry(categories.iterator());
        extraTypes = new ArrayList<ExportFileType>();
    }

    public static synchronized ExportFileTypeRegistry getDefaultInstance(ClassLoader loader) {
        if ((loader != null) && (loader != ExportFileTypeRegistry.loader)) {
            if (ExportFileTypeRegistry.loader != null) throw new RuntimeException(ExportFileTypeRegistry.class.getName() + ": Different classloader was already used in getDefaultInstance");
            ExportFileTypeRegistry.loader = loader;
        }

        if (registry == null) {
            registry = new ExportFileTypeRegistry();
            // NOTE: not in constructor to avoid recursive calls. If such call comes in
            // as a result of something on the classpath needing the registry inside
            // the onRegistration call. Such call would just return the already created
            // registry.
            addApplicationClasspathExportFileTypes(registry);
        }
        return registry;
    }

    /**
     * Returns a list of all registered ExportFileTypes in the order in which
     * they are found in jar files on the classpath.
     */
    public List<ExportFileType> get() {
        return get(null);
    }

    /**
     * Returns a list of all registered ExportFileTypes in the order in which
     * they are found in jar files on the classpath, followed by all the extra
     * registered ExportFileTypes as long as they do not duplicate any
     * ExportFileType in the list. The list is for a particular
     * format, or all if format is null.
     */
    public List/*<ExportFileType>*/<ExportFileType> get(String format) {
        List<ExportFileType> export = new ArrayList<ExportFileType>();
        
        // add all ExportFileTypes found in service
        addExportFileTypeToList(export, format, service.getServiceProviders(ExportFileType.class, true));
        
        // add all ExportFileTypes on extraTypes
        addExportFileTypeToList(export, format, extraTypes.iterator());
        
        return export;
    }
    
    /**
     * Adds ExportFile.
     * 
     * @param exportFileType
     */
    public void add(ExportFileType exportFileType) {
    	extraTypes.add(exportFileType);
    }

    private void addExportFileTypeToList(List<ExportFileType> list, String format, Iterator<?> iterator) {
        while (iterator.hasNext()) {
            ExportFileType type = (ExportFileType)iterator.next();
            if (format == null) {
            	if (!list.contains(type)) {
            		list.add(type);
            	}
            } else {
                String[] ext = type.getExtensions();
                for (int i=0; i<ext.length; i++) {
                    if (ext[i].equalsIgnoreCase(format)) {
                    	if (!list.contains(type)) {
                    		list.add(type);
                    	}
                        break;
                    }
                }
            }
        }    	
    }
    
    @SuppressWarnings( "unchecked" )
    private static void addApplicationClasspathExportFileTypes(ExportFileTypeRegistry registry) {
	    ClassLoader classLoader = (loader != null) ? loader : Thread.currentThread().getContextClassLoader();

        Iterator<Class<?>> iterator = categories.iterator();
        while (iterator.hasNext()) {
            Class<?> category = iterator.next();
            Iterator<Object> providers = Service.providers(category, classLoader).iterator();
            Object previous = null;
            while (providers.hasNext()) {
                Object current = providers.next();
                registry.service.registerServiceProvider(current);
                if (previous != null) {
                    registry.service.setOrdering((Class<Object>)category, previous, current);
                }
                previous = current;
            }
        }
    }
}
