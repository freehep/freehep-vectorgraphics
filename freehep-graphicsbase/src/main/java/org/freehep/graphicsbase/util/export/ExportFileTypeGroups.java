// Copyright 2006-2009, FreeHEP.
package org.freehep.graphicsbase.util.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Mark Donszelmann
 */
public class ExportFileTypeGroups
{

    public static final String VECTOR = "vector";

    public static final String BITMAP = "bitmap";

    public static final String OTHER = "other";

    public static final String ALTERNATIVE = "alternative";

    private List<String> groupNames;

    private Map<String, Map<String, ExportFileType>> groups;

    private List<ExportFileType> alternatives;

    private Map<String, String> labels;

    public ExportFileTypeGroups( List<ExportFileType> exportFileTypes )
    {
        groupNames = new ArrayList<String>();
        groupNames.add( VECTOR );
        groupNames.add( BITMAP );
        groupNames.add( OTHER );
        groupNames.add( ALTERNATIVE );

        labels = new HashMap<String, String>();
        labels.put( VECTOR, "Vector Formats" );
        labels.put( BITMAP, "Bitmap Formats" );
        labels.put( OTHER, "Other" );
        labels.put( ALTERNATIVE, "Alternative Formats" );

        groups = new HashMap<String, Map<String, ExportFileType>>();
        alternatives = new ArrayList<ExportFileType>();
        for ( Iterator<ExportFileType> i = exportFileTypes.iterator(); i.hasNext(); )
        {
            ExportFileType exportFileType = i.next();

            String mimeType = exportFileType.getMIMETypes()[0];
            String key;
            if ( MimeTypes.isBitmap( mimeType ) )
            {
                key = BITMAP;
            }
            else if ( MimeTypes.isVector( mimeType ) )
            {
                key = VECTOR;
            }
            else
            {
                key = OTHER;
            }

            Map<String, ExportFileType> fileTypes = groups.get( key );
            if ( fileTypes == null )
            {
                fileTypes = new HashMap<String, ExportFileType>();
                groups.put( key, fileTypes );
            }

            String extension = exportFileType.getExtensions()[0];
            if ( fileTypes.get( extension ) == null )
            {
                fileTypes.put( extension, exportFileType );
            }
            else
            {
                alternatives.add( exportFileType );
            }
        }
    }

    /**
     * Return all registered ExportFileTypes for a certain group.
     */
    public List<ExportFileType> getExportFileTypes( String group )
    {
        List<ExportFileType> result = new ArrayList<ExportFileType>();

        if ( group.equals( ALTERNATIVE ) )
        {
            result.addAll( alternatives );
        }
        else
        {
            Map<String, ExportFileType> fileTypes = groups.get( group );
            if ( fileTypes != null )
                result.addAll( fileTypes.values() );
        }
        return result;
    }

    public List<String> getGroupNames()
    {
        return groupNames;
    }

    public String getLabel( String group )
    {
        return (String) labels.get( group );
    }
}
