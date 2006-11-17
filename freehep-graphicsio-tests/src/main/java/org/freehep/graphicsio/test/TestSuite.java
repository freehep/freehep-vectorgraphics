// Copyright 2001-2006, FreeHEP.
package org.freehep.graphicsio.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.freehep.graphicsio.ImageGraphics2D;
import org.freehep.util.export.ExportFileType;
import org.freehep.util.io.UniquePrintStream;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestSuite.java e0965045a928 2006/11/17 00:05:34 duns $
 */
public class TestSuite extends junit.framework.TestSuite {

    class Format {
        private String name;
        private boolean enabled;
        private int jiraId;
        private String category;

        public Format(String name, String category, boolean enabled, int jiraId) {
            this.name = name;
            this.category = category;
            this.enabled = enabled;
            this.jiraId = jiraId;
        }

        public String getName() {
            return name;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public int getJiraId() {
            return jiraId;
        }

        public String getCategory() {
            return category != null ? category : getName().toLowerCase();
        }
    }

    private SortedMap formats;
    private static final String jiraURL = "http://bugs.freehep.org/secure/IssueNavigator.jspa?reset=true&mode=hide&sorter/order=DESC&sorter/field=priority&resolutionIds=-1";
    private static final int jiraProductId = 10170;

    class Test {
        private String name;
        private boolean enabled;

        public Test(String name, boolean enabled) {
            this.name =name;
            this.enabled = enabled;
        }
        
        public String getName() {
            return name;
        }
        
        public boolean isEnabled() {
            return enabled;
        }
    }
    
    private List tests;
    
    private static final String gioPackage = "org.freehep.graphicsio.";
    private static final String testPackage = gioPackage + "test.";
    private static final String testDir = "target/site/test-output/";
    private String testOutDir;
    private String os;
    private String jdk;

    public static class TestCase extends junit.framework.TestCase {

        private String name, fullName, category, fmt, pkg, dir, ext,
                testOutDir;

        private boolean compare;

        private Properties properties;

        public TestCase(String name, String category, String fmt, String dir,
                String ext, String testOutDir, boolean compare,
                Properties properties) {
            super("GraphicsIO Test for " + testPackage + name + " in " + fmt);
            this.fullName = testPackage + name;
            int dot = fullName.lastIndexOf(".");
            this.name = dot < 0 ? fullName : fullName.substring(dot + 1);
            this.category = category;
            this.fmt = fmt;
            this.pkg = "org.freehep.graphicsio." + fmt.toLowerCase();
            this.dir = dir;
            this.ext = ext;
            this.testOutDir = testOutDir;
            this.compare = compare;
            this.properties = properties;
        }

        protected void runTest() throws Throwable {
            String base = "src/test/resources/";

            String baseDir = System.getProperty("basedir");
            if (baseDir != null)
                base = baseDir + "/" + base;

            String out = testOutDir + dir + "/";
            if (baseDir != null)
                out = baseDir + "/" + out;
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

                Array.set(args, 0, pkg + "." + fmt + "Graphics2D");
                Array.set(args, 1, targetName);
            }

            // Create Test Object
            Constructor constructor = cls.getConstructor(new Class[] { args
                    .getClass() });
            Object test = constructor.newInstance(new Object[] { args });

            // Call Test.runTest(properties);
            Method runTest = test.getClass().getMethod("runTest",
                    new Class[] { Properties.class });
            runTest.invoke(test, new Object[] { properties });

            if (!compare) {
                return;
            }
            // FVG-242, test comparison is disabled
            return;

            // File refFile = new File(refGZIPName);
            // if (!refFile.exists()) {
            // refFile = new File(refName);
            // }
            // if (!refFile.exists()) {
            // throw new FileNotFoundException("Cannot find reference file
            // '"+refName+"' or '"+refGZIPName+"'.");
            // }

            // boolean isBinary = !fmt.equals("PS") && !ext.equals("svg");

            // FVG-242, test comparison is disabled
            // Assert.assertEquals(refFile, new File(targetName), isBinary);
        }

    }

    protected TestSuite() {
        super("GraphicsIO Test Suite");

        formats = new TreeMap();
        // formats.put("cgm", new Format("CGM", null, false, 10230));
        formats.put("emf", new Format("EMF", null, false, 10231));
        formats.put("gif", new Format("GIF", "tests", true, 10241));
        // formats.put("java", new Format("JAVA", null, false, 10238));
        formats.put("jpg", new Format("JPG", "tests", true, 10241));
        // formats.put("latex", new Format("LATEX", null, false, 10240));
        formats.put("pdf", new Format("PDF", null, true, 10235));
        formats.put("png", new Format("PNG", "tests", true, 10241));
        formats.put("ps", new Format("PS", null, true, 10232));
        formats.put("svg", new Format("SVG", null, true, 10236));
        formats.put("swf", new Format("SWF", null, true, 10237));

        // FVG-241, TestCustomStrokes [3] disabled for MacOS X
        boolean onMacOSXandJDK15 = System.getProperty("os.name").equals("Mac OS X")
                && System.getProperty("java.version").startsWith("1.5");

        tests = new ArrayList();
        tests.add(new Test("TestAll", true));
        tests.add(new Test("TestClip", true));
        tests.add(new Test("TestColors", true));
        tests.add(new Test("TestCustomStrokes", !onMacOSXandJDK15));
        tests.add(new Test("TestFonts", true));
        tests.add(new Test("TestFontDerivation", true));
        tests.add(new Test("TestGraphicsContexts", true));
        tests.add(new Test("TestHistogram", true));
        tests.add(new Test("TestHTML", true));
        tests.add(new Test("TestImages", true));
        tests.add(new Test("TestImage2D", true));
        tests.add(new Test("TestLabels", true));
        tests.add(new Test("TestLineStyles", true));
        tests.add(new Test("TestOffset", true));
        tests.add(new Test("TestPaint", true));
        tests.add(new Test("TestPrintColors", true));
        // FVG-197, TestResolution not very useful yet.
        tests.add(new Test("TestResolution", false));
        tests.add(new Test("TestScatterPlot", true));
        tests.add(new Test("TestShapes", true));
        tests.add(new Test("TestSymbols2D", true));
        tests.add(new Test("TestTaggedString", true));
        tests.add(new Test("TestText2D", true));
        tests.add(new Test("TestTransforms", true));
        tests.add(new Test("TestTransparency", true));
                
        os = System.getProperty("os.name", "OS");
        if (os.equals("Mac OS X")) {
            os = "MacOSX";
        } else if (os.startsWith("Windows")) {
            os = "Windows";
        }
        jdk = System.getProperty("java.version", "0.0");
        int dot;
        if ((dot = jdk.indexOf('.')) > 0) {
            if ((dot = jdk.indexOf('.', dot + 1)) > 0) {
                jdk = jdk.substring(0, dot);
            }
        }
        jdk = "JDK-" + jdk;
        testOutDir = testDir + os + "/" + jdk + "/";
    }

    protected void addTests(String fmt) {
        addTests(fmt, true);
    }

    protected void addTests(String fmt, boolean compare) {
        if (!((Format) formats.get(fmt.toLowerCase())).isEnabled())
            return;
        String category = fmt.toLowerCase();
        if (fmt.equals("GIF") || fmt.equals("PNG") || fmt.equals("PPM")
                || fmt.equals("JPG"))
            category = "image";
        String dir = fmt.toLowerCase();
        if (fmt.equals("JAVA"))
            dir = "org/freehep/graphicsio/java/test";
        String ext = fmt.toLowerCase();
        if (fmt.equals("LATEX")) {
            fmt = "Latex";
            ext = "tex";
        }
        addTests(category, fmt, dir, ext, true, null);
    }

    protected void addTests(String category, String fmt, String dir,
            String ext, boolean compare, Properties properties) {
        for (Iterator i = tests.iterator(); i.hasNext(); ) {
            Test test = (Test)i.next();
            if (test.isEnabled()) {
                addTest(new TestCase(test.getName(), category, fmt, dir, ext,
                        testOutDir, compare, properties));
                writeHTML(test, fmt, dir, os, jdk, ext);
            } else {
                System.err.println("NOTE: " + test.getName() + " disabled.");
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
            for (Iterator i = formats.keySet().iterator(); i.hasNext();) {
                String key = (String) i.next();
                Format value = (Format) formats.get(key);
                if (value.getName().equals("JAVA"))
                    addTests(value.getName(), compare);
            }
        }
    }

    private void writeHTML(Test test, String fmt, String dir, String os,
            String jdk, String ext) {
        String css = "../../../../css";
        String refFormat = "png";
        String top = "../../../../../../../";
        String ref = top + "freehep-graphicsio-tests/target/site/ref-output/"
                + refFormat + "/";
        String cloud = top + "freehep-graphicsio-tests/target/site/images/"
                + "cloudy.jpg";
        String title = "VectorGraphics " + fmt + " " + test.getName();
        String freehep = "http://java.freehep.org/";
        String freehepImage = freehep + "images/sm-freehep.gif";
        String url = freehep + "mvn/freehep-graphicsio-" + fmt.toLowerCase();

        String out = testOutDir + dir + "/";
        String baseDir = System.getProperty("basedir");
        if (baseDir != null)
            out = baseDir + "/" + out;
        try {
            // Create Export filetype to get mime type
            Class cls = Class.forName(gioPackage + fmt.toLowerCase() + "."
                    + fmt + "ExportFileType");
            ExportFileType fileType = (ExportFileType) cls.newInstance();
            String mimeType = fileType.getMIMETypes()[0];

            (new File(out)).mkdirs();
            PrintWriter w = new PrintWriter(new FileWriter(out
                    + test.getName() + ".html"));

            w
                    .println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
            w.println("<html>");
            w.println("    <head>");
            w.println("        <title>" + title + "</title>");
            w.println("        <style type=\"text/css\" media=\"all\">");
            w.println("          @import url(\"" + css + "/maven-base.css\");");
            w
                    .println("          @import url(\"" + css
                            + "/maven-theme.css\");");
            w.println("          @import url(\"" + css + "/site.css\");");
            w.println("        </style>");
            w.println("        <link rel=\"stylesheet\" href=\"" + css
                    + "/print.css\" type=\"text/css\" media=\"print\" />");
            w
                    .println("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
            w.println("      </head>");
            w.println("      <body class=\"composite\">");
            w.println("        <div id=\"banner\">");
            w.println("          <a href=\"" + url + "\" id=\"bannerLeft\">");
            w.println("            FreeHEP VectorGraphics Test " + fmt);
            w.println("          </a>");
            w.println("          <a href=\"" + freehep
                    + "\" id=\"bannerRight\">");
            w.println("            <img src=\"" + freehepImage
                    + "\" alt=\"\" />");
            w.println("          </a>");
            w.println("          <div class=\"clear\">");
            w.println("            <hr/>");
            w.println("          </div>");
            w.println("        </div>");
            w.println("        <div id=\"breadcrumbs\">");
            w.println("          <div class=\"xleft\">Last Run: "
                    + (new Date()) + "</div>");
            w.println("          <div class=\"xright\"><a href=\"" + freehep
                    + "\">FreeHEP</a>");
            w.println("            |");
            w
                    .println("            <a href=\"http://jas.freehep.org/\">JAS</a>");
            w.println("            |");
            w
                    .println("            <a href=\"http://wired.freehep.org/\">WIRED</a>");
            w.println("          </div>");
            w.println("          <div class=\"clear\">");
            w.println("            <hr/>");
            w.println("          </div>");
            w.println("        </div>");
            w.println("        <div id=\"leftColumn\">");
            w.println("          <div id=\"navcolumn\">");

            w.println("            <h5><a href=\"" + top
                    + "vectorgraphics/index.html" + "\">Back</a></h5>");

            w.println("            <h5>Operating System</h5>");
            w.println("            <ul>");

            String category = ((Format) formats.get(fmt.toLowerCase()))
                    .getCategory();

            String[] oss = { "Windows", "Linux", "MacOSX" };
            for (int i = 0; i < oss.length; i++) {
                w.println("              <li class=\"none\">");
                if (os.equals(oss[i]))
                    w.println("                <strong>");
                w.println("                  <a href=\"" + top
                        + "freehep-graphicsio-" + fmt.toLowerCase()
                        + "/target/site/test-output/" + oss[i] + "/" + jdk
                        + "/" + fmt.toLowerCase() + "/" + test.getName()
                        + ".html\">" + oss[i] + "</a>");
                if (os.equals(oss[i]))
                    w.println("                </strong>");
                w.println("              </li>");
            }
            w.println("            </ul>");

            w.println("            <h5>Java</h5>");
            w.println("            <ul>");
            w.println("              <li class=\"none\">");
            if (jdk.equals("JDK-1.5"))
                w.println("                <strong>");
            w.println("                  <a href=\"" + top
                    + "freehep-graphicsio-" + fmt.toLowerCase()
                    + "/target/site/test-output/" + os + "/" + jdk + "/"
                    + fmt.toLowerCase() + "/" + test.getName()
                    + ".html\">" + jdk + "</a>");
            if (jdk.equals("JDK-1.5"))
                w.println("                </strong>");
            w.println("              </li>");
            w.println("            </ul>");

            w.println("            <h5>Formats</h5>");
            w.println("            <ul>");
            for (Iterator i = formats.keySet().iterator(); i.hasNext();) {
                String key = (String) i.next();
                w.println("              <li class=\"none\">");
                if (key.equalsIgnoreCase(fmt)) {
                    w.println("                <strong>");
                }
                Format value = (Format) formats.get(key);
                w.print("                  ");
                if (value.isEnabled()) {
                    String cat = value.getCategory();
                    w.print("<a href=\"" + top + "freehep-graphicsio-" + cat
                            + "/target/site/test-output/" + os + "/" + jdk
                            + "/" + key + "/" + test.getName()
                            + ".html\">");
                }
                w.print(value.getName());
                if (value.isEnabled()) {
                    w.print("</a>");
                }
                w.println();
                if (key.equalsIgnoreCase(fmt))
                    w.println("                </strong>");
                w.println("              </li>");
            }
            w.println("            </ul>");
            w.println("            <h5>" + fmt + " Tests</h5>");
            w.println("            <ul>");
            for (Iterator i = tests.iterator(); i.hasNext(); ) {
                Test t = (Test)i.next();
                w.println("              <li class=\"none\">");
                if (t.equals(test))
                    w.println("                <strong>");
                w.print("                ");
                if (t.isEnabled()) {
                    w.print("<a href=\"" + t.getName() + ".html\">");
                }
                w.print(t.getName());
                if (t.isEnabled()) {
                    w.print("</a>");
                }
                w.println();
                if (t.equals(test))
                    w.println("                </strong>");
                w.println("              </li>");
            }
            w.println("            </ul>");

            w.println("            <h5>" + fmt + " Links</h5>");
            w.println("            <ul>");
            w.println("              <li><a href=\"" + jiraURL + "&pid="
                    + jiraProductId + "&component="
                    + ((Format) formats.get(fmt.toLowerCase())).getJiraId()
                    + "\">Issues</a></li>");
            w.println("            </ul>");

            w.println("            <a href=\"" + freehep
                    + "\" title=\"Built by FreeHEP\" id=\"poweredBy\">");
            w.println("              <img alt=\"Built by FreeHEP\" src=\""
                    + freehepImage + "\"></img>");
            w.println("            </a>");
            w.println("          </div>");
            w.println("        </div>");
            w.println("        <div id=\"bodyColumn\">");
            w.println("          <div id=\"contentBox\">");
            w.println("            <div class=\"section\">");
            w.println("              <h2>" + test.getName() + " " + fmt
                    + "</h2>");
            w.println("              <table class=\"bodyTable\">");
            // w.println(" <caption></caption>");
            w.println("                <tr class=\"a\">");
            w.println("                  <th>" + fmt + "</th>");
            w.println("                  <th>Reference ("
                    + refFormat.toUpperCase() + ")</th>");
            w.println("                </tr>");
            w.println("                <tr class=\"a\">");
            w.println("                  <td><a href=\"" + test.getName()
                    + "." + ext + "\">" + test.getName() + "." + ext
                    + "</a></td>");
            w.println("                  <td><a href=\"" + ref
                    + test.getName() + "." + refFormat + "\">"
                    + test.getName() + "." + refFormat + "</a></td>");
            w.println("                </tr>");
            w.println("                <tr class=\"a\">");
            Test[] testArray = (Test[])tests.toArray(new Test[0]);
            int testIndex = 0;
            while (testIndex < testArray.length) {
                if (testArray[testIndex].equals(test)) break; 
                testIndex++;
            }
            int previousIndex = testIndex - 1;
            while ((previousIndex >= 0) && !testArray[previousIndex].isEnabled())
                previousIndex--;
            if (previousIndex >= 0) {
                w.println("                  <td><a href=\""
                        + testArray[previousIndex].getName()
                        + ".html\">previous</a></td>");
            } else {
                w.println("                  <td/>");
            }
            int nextIndex = testIndex + 1;
            while ((nextIndex < testArray.length) && !testArray[nextIndex].isEnabled())
                nextIndex++;
            if (nextIndex < testArray.length) {
                w.println("                  <td><a href=\""
                        + testArray[nextIndex].getName() + ".html\">next</a></td>");
            } else {
                w.println("                  <td/>");
            }
            w.println("                </tr>");
            w.println("                <tr class=\"b\">");
            // w.println(" <td><a
            // href=\""+name+"."+ext+"\">"+name+"."+ext+"</a></td>");
            w
                    .println("                  <td background=\"" + cloud
                            + "\"><object type=\"" + mimeType + "\" name=\""
                            + test.getName() + "\" data=\""
                            + test.getName() + "." + ext + "\" width=\""
                            + TestingPanel.width + "\" height=\""
                            + TestingPanel.height + "\">Image not embeddable: "
                            + mimeType + "</object></td>");
            w.println("                  <td background=\"" + cloud
                    + "\"><img src=\"" + ref + test.getName()+ "."
                    + refFormat + "\"/></td>");
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
            System.err.println("Could not write " + out);
        } catch (ClassNotFoundException e) {
            System.err.println("writeHTML " + e);
        } catch (IllegalAccessException e) {
            System.err.println("writeHTML " + e);
        } catch (InstantiationException e) {
            System.err.println("writeHTML " + e);
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
