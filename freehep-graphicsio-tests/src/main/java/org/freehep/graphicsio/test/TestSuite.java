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
import org.freehep.util.export.ExportFileType;
import org.freehep.util.io.UniquePrintStream;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestSuite.java 05b2c7229aec 2006/11/13 20:41:54 duns $
 */
public class TestSuite extends junit.framework.TestSuite {
    // Alphabetically
    private static final String[] formatNames = {
        "CGM",
        "EMF",
        "GIF",
        "JAVA",
        "JPG",
        "LATEX",
        "PDF",
        "PNG",
        "PS",
        "SVG",
        "SWF",
    };
    private static final String jiraURL = "http://bugs.freehep.org/secure/IssueNavigator.jspa?reset=true&mode=hide&sorter/order=DESC&sorter/field=priority&resolutionIds=-1";
    private static final int jiraProductId = 10170;
    private static final int[] jiraComponentId = {
        10230, // "CGM"
        10231, // "EMF"
        10241, // "GIF"
        10238, // "JAVA"
        10241, // "JPG"
        10240, // "LATEX"
        10235, // "PDF"
        10241, // "PNG"
        10232, // "PS"
        10236, // "SVG"
        10237, // "SWF"
    };
    private static String[] testNames = { 
        "TestAll", 
        "TestClip", 
        "TestColors", 
        "TestCustomStrokes",
        "TestFonts",
        "TestFontDerivation",
        "TestGraphicsContexts",
        "TestHTML",
        "TestImages",
        "TestImage2D",
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
    private boolean[] testDisabled = new boolean[testNames.length];

    private static final String gioPackage = "org.freehep.graphicsio.";
    private static final String testPackage = gioPackage+"test.";
    private static final String testDir = "target/site/test-output/";
    private String testOutDir;
    private String os;
    private String jdk;
    
    public static class TestCase extends junit.framework.TestCase {

        private String name, fullName, category, fmt, pkg, dir, ext, testOutDir;

        private boolean compare;

        private Properties properties;

        public TestCase(String name, String category, String fmt, String dir, String ext, String testOutDir,
                boolean compare, Properties properties) {
            super("GraphicsIO Test for " + testPackage + name + " in " + fmt);
            this.fullName = testPackage + name;
            int dot = fullName.lastIndexOf(".");
            this.name = dot < 0 ? fullName : fullName.substring(dot + 1);
            this.category = category;
            this.fmt = fmt;
            this.pkg = "org.freehep.graphicsio."+fmt.toLowerCase();
            this.dir = dir;
            this.ext = ext;
            this.testOutDir = testOutDir;
            this.compare = compare;
            this.properties = properties;
        }

        protected void runTest() throws Throwable {            
            String base = "src/test/resources/";
            
            String baseDir = System.getProperty("basedir");
            if (baseDir != null) base = baseDir + "/" + base;
            
            String out = testOutDir + dir + "/";
            if (baseDir != null) out = baseDir + "/" +out;
            (new File(out)).mkdirs();

            Class cls = Class.forName(fullName);
            String targetName = out + name + "." + ext;
            String refName = base + dir + "/" + name + "." + ext;
            String refGZIPName = base + dir + "/" + name + "." + ext + ".gz";
            
            Object args;
            if (category.equals("image")) {
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
            
            // FVG-242, test comparison is disabled
//            Assert.assertEquals(refFile, new File(targetName), isBinary);            
        }

    }
    
    protected TestSuite() {
        super("GraphicsIO Test Suite");
        
        // FVG-241, TestCustomStrokes [3] disabled for MacOS X
        if (System.getProperty("os.name").equals("Mac OS X") && 
            System.getProperty("java.version").startsWith("1.5")) {
        	testDisabled[3 /*TestCustomStrokes*/] = true;
        }
        
        os = System.getProperty("os.name","OS");
        if (os.equals("Mac OS X")) {
        	os = "MacOSX";
        } else if (os.startsWith("Windows")) {
        	os = "Windows";
        }
        jdk = System.getProperty("java.version","0.0");
        int dot;
        if ((dot = jdk.indexOf('.')) > 0) {
        	if ((dot = jdk.indexOf('.',dot+1)) > 0) {
        		jdk = jdk.substring(0, dot);
        	}
        }
        jdk = "JDK-"+jdk;
        testOutDir = testDir+os+"/"+jdk+"/";
    }

    protected void addTests(String fmt) {
        addTests(fmt, true);
    }
    
    protected void addTests(String fmt, boolean compare) {
        String category = fmt.toLowerCase(); 
        if (fmt.equals("GIF") || fmt.equals("PNG") || fmt.equals("PPM") || fmt.equals("JPG")) category = "image";
        String dir = fmt.toLowerCase();
        if (fmt.equals("JAVA")) dir = "org/freehep/graphicsio/java/test"; 
        String ext = fmt.toLowerCase();
        if (fmt.equals("LATEX")) {
            fmt = "Latex";
            ext = "tex";
        }
        addTests(category, fmt, dir, os, jdk, ext, true, null);
    }

    protected void addTests(String category, String fmt, String dir, String os, String jdk, String ext,
            boolean compare, Properties properties) {     
        for (int i=0; i<testNames.length; i++) {
            if (testDisabled[i]) { 
                System.err.println("NOTE: "+testNames[i]+" disabled.");
            } else {
                addTest(new TestCase(testNames[i], category, fmt, dir, ext, testOutDir, compare, properties));            
                writeHTML(i, fmt, dir, os, jdk, ext);
            }
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
                addTests(args[i].toUpperCase(), compare);
            }
        } else {
            for (int i = 0; i< formatNames.length; i++) {
                if (formatNames[i].equals("JAVA"))                   
                addTests(formatNames[i], compare);
            }
        }
    }

    private void writeHTML(int testIndex, String fmt, String dir, String os, String jdk, String ext) {
        String css = "../../../../css";
        String refFormat = "png";
        String top = "../../../../../../../";
        String ref = top+"freehep-graphicsio-tests/target/site/ref-output/"+refFormat+"/";
        String title = "VectorGraphics "+fmt+" "+testNames[testIndex];
        String freehep = "http://java.freehep.org/";
        String freehepImage = freehep+"images/sm-freehep.gif";
        String url = freehep+"mvn/freehep-graphicsio-"+fmt.toLowerCase();
        int formatIndex = -1;        
        
        String out = testOutDir + dir + "/";        
        String baseDir = System.getProperty("basedir");
        if (baseDir != null) out = baseDir + "/" +out;
        try {
            // Create Export filetype to get mime type
            Class cls = Class.forName(gioPackage+fmt.toLowerCase()+"."+fmt+"ExportFileType");
            ExportFileType fileType = (ExportFileType)cls.newInstance();
            String mimeType = fileType.getMIMETypes()[0];

            (new File(out)).mkdirs();
            PrintWriter w = new PrintWriter(new FileWriter(out+testNames[testIndex]+".html"));
    
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
            w.println("            <h5>Formats</h5>");
            w.println("            <ul>");
            for (int i=0; i<formatNames.length; i++) {
                w.println("              <li class=\"none\">");
                if (formatNames[i].equals(fmt)) {
                    formatIndex = i;
                    w.println("                <strong>");
                }
                String category = formatNames[i].toLowerCase();
                if (formatNames[i].equals("GIF") || formatNames[i].equals("PNG") || formatNames[i].equals("PPM") || formatNames[i].equals("JPG")) category = "tests";
                w.println("                  <a href=\""+top+"freehep-graphicsio-"+category+"/target/site/test-output/"+os+"/"+jdk+"/"+formatNames[i].toLowerCase()+"/"+testNames[testIndex]+".html\">"+formatNames[i]+"</a>");
                if (formatNames[i].equals(fmt)) w.println("                </strong>");
                w.println("              </li>");
            }
            w.println("            </ul>");
            w.println("            <h5>"+fmt+" Tests</h5>");
            w.println("            <ul>");
            for (int i=0; i<testNames.length; i++) {
                w.println("              <li class=\"none\">");
                if (i == testIndex) w.println("                <strong>");
                w.print("                ");
                if (!testDisabled[i]) {
                   w.print("<a href=\""+testNames[i]+".html\">");
                }
                w.print(testNames[i]);
                if (!testDisabled[i]) {
                   w.print("</a>");
                }
                w.println();
                if (i == testIndex) w.println("                </strong>");
                w.println("              </li>");
            }
            w.println("            </ul>");
            
            w.println("            <h5>"+fmt+" Links</h5>");
            w.println("            <ul>");
            if (formatIndex >= 0) w.println("              <li><a href=\""+jiraURL+"&pid="+jiraProductId+"&component="+jiraComponentId[formatIndex]+"\">Issues</a></li>");
            w.println("            </ul>");

            w.println("            <a href=\""+freehep+"\" title=\"Built by FreeHEP\" id=\"poweredBy\">");
            w.println("              <img alt=\"Built by FreeHEP\" src=\""+freehepImage+"\"></img>");
            w.println("            </a>");
            w.println("          </div>");
            w.println("        </div>");
            w.println("        <div id=\"bodyColumn\">");
            w.println("          <div id=\"contentBox\">");
            w.println("            <div class=\"section\">");
            w.println("              <h2>"+testNames[testIndex]+" "+fmt+"</h2>");
            w.println("              <table class=\"bodyTable\">");
    //        w.println("                <caption></caption>");
            w.println("                <tr class=\"a\">");
            w.println("                  <th>"+fmt+"</th>");
            w.println("                  <th>Reference ("+refFormat.toUpperCase()+")</th>");
            w.println("                </tr>");
            w.println("                <tr class=\"a\">");
            w.println("                  <td><a href=\""+testNames[testIndex]+"."+ext+"\">"+testNames[testIndex]+"."+ext+"</a></td>");
            w.println("                  <td><a href=\""+ref+testNames[testIndex]+"."+refFormat+"\">"+testNames[testIndex]+"."+refFormat+"</a></td>");
            w.println("                </tr>");
            w.println("                <tr class=\"a\">");
            int previousIndex = testIndex - 1;
            while ((previousIndex >= 0) && testDisabled[previousIndex]) previousIndex--;
            if (previousIndex >= 0) {
                w.println("                  <td><a href=\""+testNames[previousIndex]+".html\">previous</a></td>");
            } else {
                w.println("                  <td/>");
            }
            int nextIndex = testIndex + 1;
            while ((nextIndex < testNames.length) && testDisabled[nextIndex]) nextIndex++;
            if (nextIndex < testNames.length) {
                w.println("                  <td><a href=\""+testNames[nextIndex]+".html\">next</a></td>");
            } else {
                w.println("                  <td/>");
            } 
            w.println("                </tr>");
            w.println("                <tr class=\"b\">");
    //        w.println("                  <td><a href=\""+name+"."+ext+"\">"+name+"."+ext+"</a></td>");
            w.println("                  <td><object type=\""+mimeType+"\" name=\""+testNames[testIndex]+"\" data=\""+testNames[testIndex]+"."+ext+"\" width=\""+TestingPanel.width+"\" height=\""+TestingPanel.height+"\">Image not embeddable: "+mimeType+"</object></td>");
            w.println("                  <td><img src=\""+ref+testNames[testIndex]+"."+refFormat+"\"/></td>");
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
        } catch (ClassNotFoundException e) {
            System.err.println("writeHTML "+e);
        } catch (IllegalAccessException e) {
            System.err.println("writeHTML "+e);
        } catch (InstantiationException e) {
            System.err.println("writeHTML "+e);
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
