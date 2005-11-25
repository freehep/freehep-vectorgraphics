//Copyright 2001-2005 FreeHep
package org.freehep.graphics2d.font;

/**
 * Abstract Character Table, inherited by all the Generated Encoding Tables
 *
 * @author Simon Fischer
 * @version $Id: freehep-graphics2d/src/main/java/org/freehep/graphics2d/font/AbstractCharTable.java f5b43d67642f 2005/11/25 23:10:27 duns $
 */
public abstract class AbstractCharTable implements CharTable {

    public int toEncoding(char unicode) {
    	try {
    	    String name = toName(unicode);
    	    if (name == null) return 0;
    	    int enc = toEncoding(name); 
    	    if (enc > 255) {
    		System.out.println("toEncoding() returned illegal value for '"+name+"': "+enc);
    		return 0;
    	    }
    	    return enc;
    	} catch (Exception e) {
    	    return 0;
    	}
    }

    public String toName(char c) {
	    return toName(new Character(c));
    }

    public String toName(Integer enc) {
	    return toName(enc.intValue());
    }
}
