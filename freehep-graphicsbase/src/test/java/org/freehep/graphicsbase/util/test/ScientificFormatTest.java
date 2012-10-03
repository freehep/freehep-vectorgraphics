package org.freehep.graphicsbase.util.test;

import java.util.Random;
import junit.framework.*;

import org.freehep.graphicsbase.util.DoubleWithError;
import org.freehep.graphicsbase.util.ScientificFormat;

/**
 *
 * @author Mark Donszelmann
 * @version $Id: ScientificFormatTest.java 8584 2006-08-10 23:06:37Z duns $
 */
public class ScientificFormatTest extends TestCase
{
   
   public ScientificFormatTest(java.lang.String testName)
   {
      super(testName);
   }
   
   public static Test suite()
   {
      TestSuite suite = new TestSuite(ScientificFormatTest.class);
      return suite;
   }
   
   /**
    * Test of versionNumberCompare method, of class org.freehep.util.VersionComparator.
    */
   public void testScientificFormatCompare()
   {
      ScientificFormat f = new ScientificFormat();
      assertEquals("1.0000",    f.format(1.0));
      
      assertEquals(".10000",    f.format(1E-1));
      assertEquals(".010000",   f.format(1E-2));
      assertEquals(".0010000",  f.format(1E-3));
      assertEquals("1.0000E-4", f.format(1E-4));
      assertEquals("1.0000E-5", f.format(1E-5));
      assertEquals("1.0000E-6", f.format(1E-6));
      
      assertEquals("10.000",    f.format(1E1));
      assertEquals("100.00",    f.format(1E2));
      assertEquals("1000.0",    f.format(1E3));
      assertEquals("10000",     f.format(1E4));
      assertEquals("100000",    f.format(1E5));
      assertEquals("1000000",   f.format(1E6));
      assertEquals("10000000",  f.format(1E7));
      assertEquals("1.0000E8",  f.format(1E8));
      
      assertEquals("1000000",   f.format(999999));
      assertEquals("99999",     f.format(99999));
      assertEquals("9999.0",    f.format(9999));
      assertEquals("999.00",    f.format(999));
      assertEquals("99.000",    f.format(99));
      assertEquals("9.0000",    f.format(9));
      assertEquals(".90000",    f.format(.9));
      assertEquals(".090000",   f.format(.09));
      assertEquals("1000.0",    f.format(999.999));
      
      assertEquals("100000",    f.format(100000.000001));
      assertEquals("100000",    f.format(100000.0000001));
      assertEquals("100000",    f.format(100000.00000001));
      assertEquals("100000",    f.format(100000.000000001));
      assertEquals("100000",    f.format(100000.0000000001));
      assertEquals("100000",    f.format(100000.00000000001));
 
      assertEquals("-1.0000",    f.format(-1.0));
      
      assertEquals("-.10000",    f.format(-1E-1));
      assertEquals("-.010000",   f.format(-1E-2));
      assertEquals("-.0010000",  f.format(-1E-3));
      assertEquals("-1.0000E-4", f.format(-1E-4));
      assertEquals("-1.0000E-5", f.format(-1E-5));
      assertEquals("-1.0000E-6", f.format(-1E-6));
      
      assertEquals("-10.000",    f.format(-1E1));
      assertEquals("-100.00",    f.format(-1E2));
      assertEquals("-1000.0",    f.format(-1E3));
      assertEquals("-10000",     f.format(-1E4));
      assertEquals("-100000",    f.format(-1E5));
      assertEquals("-1000000",   f.format(-1E6));
      assertEquals("-10000000",  f.format(-1E7));
      assertEquals("-1.0000E8",  f.format(-1E8));
      
      assertEquals("-1000000",   f.format(-999999));
      assertEquals("-99999",     f.format(-99999));
      assertEquals("-9999.0",    f.format(-9999));
      assertEquals("-999.00",    f.format(-999));
      assertEquals("-99.000",    f.format(-99));
      assertEquals("-9.0000",    f.format(-9));
      assertEquals("-.90000",    f.format(-.9));
      assertEquals("-.090000",   f.format(-.09));
      assertEquals("-1000.0",    f.format(-999.999));
      
      assertEquals("-100000",    f.format(-100000.000001));
      assertEquals("-100000",    f.format(-100000.0000001));
      assertEquals("-100000",    f.format(-100000.00000001));
      assertEquals("-100000",    f.format(-100000.000000001));
      assertEquals("-100000",    f.format(-100000.0000000001));
      assertEquals("-100000",    f.format(-100000.00000000001));
      
      assertEquals("0.0000",     f.format(-0));
      
      Random r = new Random();
      for (int i=0; i<10000; i++)
      {
         double d = r.nextDouble();
         String sd = f.format(d);
         double d2 = Double.parseDouble(sd);
         assertEquals(d,d2,d*1e-4);
      }
      for (int i=0; i<10000; i++)
      {
         long l = r.nextLong();
         double d = Double.longBitsToDouble(l);
         String sd = f.format(d);
         double d2 = Double.parseDouble(sd);
         if (Double.isNaN(d)) assertTrue(Double.isNaN(d2));
         else if (Double.isInfinite(d)) assertTrue(Double.isInfinite(d2));
         else assertEquals(d,d2,Math.abs(d)*1e-4);
      }
   }
   public void testDoubleWitheErrorCompare()
   {
      ScientificFormat f = new ScientificFormat();
      char plusorminus = '\u00b1';
      DoubleWithError de = new DoubleWithError(1,.01);
      assertEquals("1.0000"+plusorminus+".0100",f.format(de));
   }
   
   public static void main(java.lang.String[] args)
   {
      junit.textui.TestRunner.run(suite());
   }
}
