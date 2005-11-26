// Copyright 2003, FreeHEP
package org.freehep.graphicsio.exportchooser;

import java.util.Properties;

/**
 * 
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/Options.java 5641ca92a537 2005/11/26 00:15:35 duns $
 */
public interface Options {

    /**
     * Sets all the changed options in the properties object.
     * 
     * @return true if any options were set
     */
    public boolean applyChangedOptions(Properties options);
}
