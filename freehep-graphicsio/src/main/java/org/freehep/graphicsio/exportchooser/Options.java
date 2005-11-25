// Copyright 2003, FreeHEP
package org.freehep.graphicsio.exportchooser;

import java.util.Properties;

/**
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio/src/main/java/org/freehep/graphicsio/exportchooser/Options.java 399e20fc1ed9 2005/11/25 23:40:46 duns $
 */
public interface Options {

    /**
     * Sets all the changed options in the properties object.
     *
     * @return true if any options were set
     */
    public boolean applyChangedOptions(Properties options);
}

