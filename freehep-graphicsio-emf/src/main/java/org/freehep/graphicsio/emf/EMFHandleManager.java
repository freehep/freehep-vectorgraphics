package org.freehep.graphicsio.emf;

import java.util.BitSet;

/**
 * Allocates and frees handles for EMF files
 * 
 * @author Tony Johnson
 * @version $Id: freehep-graphicsio-emf/src/main/java/org/freehep/graphicsio/emf/EMFHandleManager.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class EMFHandleManager {
    private BitSet handles = new BitSet();

    private int maxHandle;

    public int getHandle() {
        int handle = nextClearBit();
        handles.set(handle);
        if (handle > maxHandle)
            maxHandle = handle;
        return handle;
    }

    public int freeHandle(int handle) {
        handles.clear(handle);
        return handle;
    }

    private int nextClearBit() {
        // return handles.nextClearBit(1); // JDK 1.4
        for (int i = 1;; i++)
            if (!handles.get(i))
                return i;
    }

    public int highestHandleInUse() {
        return handles.length() - 1;
    }

    public int maxHandlesUsed() {
        return maxHandle + 1;
    }
}
