// Copyright 2000, CERN, Geneva, Switzerland and University of Santa Cruz, California, U.S.A.
package org.freehep.graphics2d;


import java.util.*;

/**
 *
 * @author Mark Donszelmann
 * @version $Id: freehep-graphics2d/src/main/java/org/freehep/graphics2d/TagString.java f5b43d67642f 2005/11/25 23:10:27 duns $
 */
public class TagString {

    private String string;

    public TagString(String value) {
        string = value;
    }

    public int hashCode() {
        return string.hashCode();
    }

    public boolean equals(Object obj) {
        return string.equals(obj);
    }

    public String toString() {
        return string;
    }
}
