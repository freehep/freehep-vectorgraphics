// Copyright 2003, FreeHEP.
package org.freehep.graphicsio;

import java.util.*;
import javax.imageio.*;

/**
 * This interface is to be implemented by sub classes of ImageWriteParam
 * to make properties available to the ImageWriter as an ImageWriteParam object.
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/ImageParamConverter.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public interface ImageParamConverter {

    /**
     * Returns a subclass of ImageWriteParam with all the
     * instance variable set according to the properties
     */
    public ImageWriteParam getWriteParam(Properties properties);
}

