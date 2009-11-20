// Copyright 2005-2009, FreeHEP.
package org.freehep.graphics2d.font.test;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.freehep.graphics2d.font.CharTable;
import org.freehep.graphics2d.font.Lookup;

/**
 * Test class to test the generated Encoding Tables.
 * 
 * @author Sami Kama
 * @version $Id:
 *          freehep-graphics2d/src/test/java/org/freehep/graphics2d/font/test
 *          /TestLookup.java 7aee336a8992 2005/11/25 23:19:05 duns $
 */
public class TestLookup extends TestCase {

	public void testPDFLatin() {
		Lookup lookup = Lookup.getInstance();
		CharTable table = lookup.getTable("PDFLatin");
		Assert.assertNotNull(table);
	}

	public void testSTDLatin() {
		Lookup lookup = Lookup.getInstance();
		CharTable table = lookup.getTable("STDLatin");
		Assert.assertNotNull(table);
	}

	public void testMACLatin() {
		Lookup lookup = Lookup.getInstance();
		CharTable table = lookup.getTable("MACLatin");
		Assert.assertNotNull(table);
	}

	public void testWINLatin() {
		Lookup lookup = Lookup.getInstance();
		CharTable table = lookup.getTable("WINLatin");
		Assert.assertNotNull(table);
	}

	public void testISOLatin() {
		Lookup lookup = Lookup.getInstance();
		CharTable table = lookup.getTable("ISOLatin");
		Assert.assertNotNull(table);
	}

	public void testSymbol() {
		Lookup lookup = Lookup.getInstance();
		CharTable table = lookup.getTable("Symbol");
		Assert.assertNotNull(table);
	}

	public void testZapfDingbats() {
		Lookup lookup = Lookup.getInstance();
		CharTable table = lookup.getTable("ZapfDingbats");
		Assert.assertNotNull(table);
	}

	public void testExpert() {
		Lookup lookup = Lookup.getInstance();
		CharTable table = lookup.getTable("Expert");
		Assert.assertNotNull(table);
		Assert.assertEquals(135, table.toEncoding("Aacutesmall"));
	}
	
	public void testLookup() {
		Lookup lookup = Lookup.getInstance();
		Assert.assertEquals("registersans", lookup.toName('\uF8E8'));
	}
}