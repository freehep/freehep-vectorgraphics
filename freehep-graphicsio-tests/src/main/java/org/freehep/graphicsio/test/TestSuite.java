// Copyright 2001-2006, FreeHEP.
package org.freehep.graphicsio.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.freehep.graphicsio.ImageGraphics2D;
import org.freehep.util.Assert;
import org.freehep.util.io.UniquePrintStream;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestSuite.java f2ace2456064 2006/04/05 00:26:49 duns $
 */
public class TestSuite extends junit.framework.TestSuite {

    private static final String testPackage = "org.freehep.graphicsio.test.";
    private static final String testDir = "target/site/test-output/";
    
    public static class TestCase extends junit.framework.TestCase {

        private String name, fullName, fmt, pkg, dir, ext;

        private boolean compare;

        private Properties properties;

        public TestCase(String fullName, String fmt, String pkg, String dir, String ext,
                boolean compare, Properties properties) {
            super("GraphicsIO Test for " + fullName + " in " + fmt);
            this.fullName = fullName;
            int dot = fullName.lastIndexOf(".");
            name = dot < 0 ? fullName : fullName.substring(dot + 1);
            this.fmt = fmt;
            this.pkg = pkg;
            this.dir = dir;
            this.ext = ext;
            this.compare = compare;
            this.properties = properties;
        }

        protected void runTest() throws Throwable {
            String base = "src/test/resources/";
            
            String baseDir = System.getProperty("basedir");
            if (baseDir != null) base = baseDir + "/" + base;
            
            String out = testDir + dir + "/";
            (new File(out)).mkdirs();

            Class cls = Class.forName(fullName);
            String targetName = out + name + "." + ext;
            String refName = base + dir + "/" + name + "." + ext;
            String refGZIPName = base + dir + "/" + name + "." + ext + ".gz";
            
            Object args;
            if (fmt.equals("GIF") || fmt.equals("PNG") || fmt.equals("PPM")
                    || fmt.equals("JPG")) {
                args = Array.newInstance(String.class, 3);
                Array.set(args, 0, ImageGraphics2D.class.getName());
                Array.set(args, 1, fmt.toLowerCase());
                Array.set(args, 2, targetName);
            } else {
                args = Array.newInstance(String.class, 2);
                if (fmt.equals("LATEX"))
                    fmt = "Latex";

                Array.set(args, 0, pkg + "." + fmt
                        + "Graphics2D");
                Array.set(args, 1, targetName);
            }

            // Create Test Object
            Constructor constructor = cls.getConstructor(new Class[] { args.getClass() });
            Object test = constructor.newInstance(new Object[] { args });

            // Call Test.runTest(properties);
            Method runTest = test.getClass().getMethod("runTest", new Class[] { Properties.class });
            runTest.invoke(test, new Object[] { properties });

            if (!compare) {
                return;
            }
            File refFile = new File(refGZIPName);
            if (!refFile.exists()) {
                refFile = new File(refName);
            }
            if (!refFile.exists()) {
                throw new FileNotFoundException("Cannot find reference file '"+refName+"' or '"+refGZIPName+"'.");
            }
            
            boolean isBinary = !fmt.equals("PS") && !ext.equals("svg");
            Assert.assertEquals(refFile, new File(targetName), isBinary);            
        }

    }
    
    protected TestSuite() {
        super("GraphicsIO Test Suite");
    }

    protected void addTests(String fmt) {
        addTests(fmt, true);
    }

    protected void addTests(String fmt, boolean compare) {
        addTests(fmt, "org.freehep.graphicsio."+fmt.toLowerCase(), fmt.toLowerCase(), fmt.toLowerCase(), compare);
    }

    protected void addTests(String fmt, String pkg, String dir, String ext, boolean compare) {
        addTests(fmt, pkg, dir, ext, compare, null);
    }

    protected void addTests(String fmt, String pkg, String dir, String ext,
            boolean compare, Properties properties) {
        // Alphabetically
        String[] testNames = { 
            "TestAll", 
            "TestClip", 
            "TestColors", 
            "TestCustomStrokes",
            "TestFonts",
            "TestFontDerivation",
            "TestGraphicsContexts",
            "TestHTML",
            "TestImages",
            "TestImages2D",
            "TestLabels",
            "TestLineStyles",
            "TestOffset",
            "TestPaint",
            "TestPrintColors",
            "TestResolution",
            "TestShapes",
            "TestSymbols2D",
            "TestTaggedString",
            "TestText2D",
            "TestTransforms",
            "TestTransparency",
        };
        
        for (int i=0; i<testNames.length; i++) {
            addTest(new TestCase(testPackage + testNames[i], fmt, pkg, dir, ext, compare, properties));            
            writeHTML(testNames[i], fmt, dir, ext, testNames);
        }
    }

    protected void addTests(String[] args) {
        int first = 0;
        boolean compare = true;
        if ((args.length > 0) && args[0].equals("-nc")) {
            compare = false;
            first = 1;
        }

        if (args.length - first > 0) {
            for (int i = first; i < args.length; i++) {
                String ext = args[i].toLowerCase();
                if (ext.equals("latex"))
                    ext = "tex";
                if (ext.equals("svg"))
                    ext = "svgz";
                addTests(args[i].toUpperCase(), "org.freehep.graphicsio."+args[i].toLowerCase(), args[i].toLowerCase(), ext,
                        compare);
            }
        } else {
            addTests("CGM", compare);
            addTests("EMF", compare);
            addTests("GIF", compare);
            addTests("JPG", compare);
            addTests("LATEX", "org.freehep.graphicsio.latex", "latex", "tex", compare);
            addTests("PDF", compare);
            addTests("PNG", compare);
            addTests("PS", compare);
            addTests("SVG", "org.freehep.graphicsio.svg", "svg", "svgz", compare);
            addTests("SWF", compare);
            addTests("JAVA", "org.freehep.graphicsio.java", "org/freehep/graphicsio/java/test", "java", compare);
        }
    }

    private void writeHTML(String name, String fmt, String dir, String ext, String[] testNames) {
        int width = 500;
        int height = 500;
        String css = "../../css";
        String ref = "../../../../../freehep-graphicsio-tests/target/site/test-output/png/";
        String title = "VectorGraphics "+fmt+" "+name;
        String freehep = "http://java.freehep.org/";
        String freehepImage = freehep+"images/sm-freehep.gif";
        String url = freehep+"mvn/freehep-graphicsio-"+name.toLowerCase();
        
        String out = testDir + dir + "/";
        try {
            PrintWriter w = new PrintWriter(new FileWriter(out+name+".html"));
    
            w.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
            w.println("<html>");
            w.println("    <head>");
            w.println("        <title>"+title+"</title>");
            w.println("        <style type=\"text/css\" media=\"all\">");
            w.println("          @import url(\""+css+"/maven-base.css\");");
            w.println("          @import url(\""+css+"/maven-theme.css\");");
            w.println("          @import url(\""+css+"/site.css\");");
            w.println("        </style>");
            w.println("        <link rel=\"stylesheet\" href=\""+css+"/print.css\" type=\"text/css\" media=\"print\" />");
            w.println("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
            w.println("      </head>");
            w.println("      <body class=\"composite\">");
            w.println("        <div id=\"banner\">");
            w.println("          <a href=\""+url+"\" id=\"bannerLeft\">");
            w.println("            FreeHEP VectorGraphics Test "+fmt);
            w.println("          </a>");
            w.println("          <a href=\""+freehep+"\" id=\"bannerRight\">");
            w.println("            <img src=\""+freehepImage+"\" alt=\"\" />");
            w.println("          </a>");
            w.println("          <div class=\"clear\">");
            w.println("            <hr/>");
            w.println("          </div>");
            w.println("        </div>");
            w.println("        <div id=\"breadcrumbs\">");
            w.println("          <div class=\"xleft\">Last Run: "+(new Date())+"</div>");
            w.println("          <div class=\"xright\"><a href=\""+freehep+"\">FreeHEP</a>");
            w.println("            |");
            w.println("            <a href=\"http://jas.freehep.org/\">JAS</a>");
            w.println("            |");
            w.println("            <a href=\"http://wired.freehep.org/\">WIRED</a>");
            w.println("          </div>");
            w.println("          <div class=\"clear\">");
            w.println("            <hr/>");
            w.println("          </div>");
            w.println("        </div>");
            w.println("        <div id=\"leftColumn\">");
            w.println("          <div id=\"navcolumn\">");
            w.println("            <h5>General</h5>");
            w.println("            <ul>");
            for (int i=0; i<testNames.length; i++) {
                w.println("              <li class=\"none\">");
                w.println("                <a href=\""+testNames[i]+".html\">"+testNames[i]+"</a>");
                w.println("              </li>");
            }
            w.println("            </ul>");
            w.println("            <a href=\""+freehep+"\" title=\"Built by FreeHEP\" id=\"poweredBy\">");
            w.println("              <img alt=\"Built by FreeHEP\" src=\""+freehepImage+"\"></img>");
            w.println("            </a>");
            w.println("          </div>");
            w.println("        </div>");
            w.println("        <div id=\"bodyColumn\">");
            w.println("          <div id=\"contentBox\">");
            w.println("            <div class=\"section\">");
            w.println("              <h2>"+name+" "+fmt+"</h2>");
            w.println("              <table class=\"bodyTable\">");
    //        w.println("                <caption></caption>");
            w.println("                <tr class=\"a\">");
            w.println("                  <th>"+fmt+"</th>");
            w.println("                  <th>Reference (PNG)</th>");
            w.println("                </tr>");
            w.println("                <tr class=\"b\">");
    //        w.println("                  <td><a href=\""+name+"."+ext+"\">"+name+"."+ext+"</a></td>");
            w.println("                  <td><object type=\"image/svg+xml\" name=\""+name+"\" data=\""+name+"."+ext+"\" width=\""+width+"\" height=\""+height+"\"</td>");
            w.println("                  <td><img src=\""+ref+name+".png"+"\"/></td>");
            w.println("                </tr>");
            w.println("                <tr class=\"a\">");
            w.println("                  <td><a href=\""+name+"."+ext+"\">"+name+"."+ext+"</a></td>");
            w.println("                  <td><a href=\""+ref+name+".png"+"\">"+name+".png"+"</a></td>");
            w.println("                </tr>");
            w.println("                <tr class=\"a\">");
            w.println("                  <td><a href=\""+"\">previous</a></td>");
            w.println("                  <td><a href=\""+"\">next</a></td>");
            w.println("                </tr>");
            w.println("             </table>");
            w.println("           </div>");
            w.println("          </div>");
            w.println("        </div>");
            w.println("        <div class=\"clear\">");
            w.println("          <hr/>");
            w.println("        </div>");
            w.println("        <div id=\"footer\">");
            w.println("          <div class=\"xright\">&#169;");  
            w.println("              2000-2006");   
            w.println("              FreeHEP");
            w.println("          </div>");
            w.println("          <div class=\"clear\">");
            w.println("            <hr/>");
            w.println("          </div>");
            w.println("        </div>");
            w.println("      </body>");
            w.println("    </html>");
            w.close();
        } catch (IOException e) {
            System.err.println("Could not write "+out);
        }
    }

    public static TestSuite suite() {
        // get command line arguments from environment var (set by ANT)
        StringTokenizer st = new StringTokenizer(
                System.getProperty("args", ""), " ");
        List argList = new ArrayList();
        while (st.hasMoreTokens()) {
            String arg = st.nextToken();
            System.out.println(arg);
            argList.add(arg);
        }
        String[] args = new String[argList.size()];
        argList.toArray(args);

        TestSuite suite = new TestSuite();
        suite.addTests(args);
        return suite;
    }

    public static void main(String[] args) {
        UniquePrintStream stderr = new UniquePrintStream(System.err);
        System.setErr(stderr);
        TestSuite suite = new TestSuite();
        suite.addTests(args);
        junit.textui.TestRunner.run(suite);
        stderr.finish();
    }
}
