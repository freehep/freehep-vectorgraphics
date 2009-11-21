// Copyright 2001-2007, FreeHEP.
package org.freehep.graphicsio.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import junit.framework.AssertionFailedError;

import org.freehep.graphicsio.ImageGraphics2D;
import org.freehep.graphicsio.ImageConstants;
import org.freehep.util.export.ExportFileType;
import org.freehep.util.io.UniquePrintStream;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestSuite.java 3589e18d30b9 2007/06/13 17:26:00 duns $
 */
public class TestSuite extends junit.framework.TestSuite {

    class Format {
        private String name;
        private String lowerCaseName;
        private String upperCaseName;
        private String extension;
        private String moduleName;
        private boolean enabled;
        private boolean bitmap;
        private int jiraId;
        private String testDir;

        public Format(String name, String extension, String moduleName,
                boolean enabled, boolean bitmap, int jiraId, String testDir) {
            this.name = name;
            this.lowerCaseName = name.toLowerCase();
            this.upperCaseName = name.toUpperCase();
            this.extension = extension;
            this.moduleName = moduleName;
            this.enabled = enabled;
            this.bitmap = bitmap;
            this.jiraId = jiraId;
            this.testDir = testDir;
        }

        public String getName() {
            return name;
        }

        public String getLowerCaseName() {
            return lowerCaseName;
        }

        public String getUpperCaseName() {
            return upperCaseName;
        }

        public String getExtension() {
            return extension != null ? extension : lowerCaseName;
        }

        public String getModuleName() {
            return moduleName != null ? moduleName : "freehep-graphicsio-"
                    + lowerCaseName;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public boolean isBitmap() {
            return bitmap;
        }

        public int getJiraId() {
            return jiraId;
        }

        public String getTestDir() {
            return testDir != null ? testDir : lowerCaseName;
        }
    }

    private SortedMap<String, Format> bitmapFormats, vectorFormats;
    private static final String jiraURL = "http://bugs.freehep.org/secure/IssueNavigator.jspa?reset=true&mode=hide&sorter/order=DESC&sorter/field=priority&resolutionIds=-1";
    private static final int jiraProductId = 10170;

    class Test {
        private String name;
        private boolean enabled;

        public Test(String name, boolean enabled) {
            this.name = name;
            this.enabled = enabled;
        }

        public String getName() {
            return name;
        }

        public boolean isEnabled() {
            return enabled;
        }
    }

    private List<Test> tests;

    private static final String gioPackage = "org.freehep.graphicsio.";
    private static final String testPackage = gioPackage + "test.";
    private static final String testDir = "target/site/test-output/";
    private String testOutDir;
    private String os;
    private String jdk;
    
    private boolean local = true;

    public static class TestCase extends junit.framework.TestCase {

        private String name, fullName, pkg, testOutDir;

        private Format fmt;

        private Properties properties;

        public TestCase(String name, Format fmt, String testOutDir,
                Properties properties) {
            super("GraphicsIO Test for " + testPackage + name + " in "
                    + fmt.getName());
            this.fullName = testPackage + name;
            int dot = fullName.lastIndexOf(".");
            this.name = dot < 0 ? fullName : fullName.substring(dot + 1);
            this.fmt = fmt;
            this.pkg = "org.freehep.graphicsio." + fmt.getLowerCaseName();
            this.testOutDir = testOutDir;
            this.properties = properties;
        }

        protected void runTest() throws Throwable {
            String base = "src/test/resources/";

            String baseDir = System.getProperty("basedir");
            if (baseDir != null)
                base = baseDir + "/" + base;

            String out = testOutDir + fmt.getTestDir() + "/";
            if (baseDir != null)
                out = baseDir + "/" + out;
            (new File(out)).mkdirs();

            Class<?> cls = Class.forName(fullName);
            String targetName = out + name + "." + fmt.getExtension();

            Object args;
            if (fmt.isBitmap()) {
                args = Array.newInstance(String.class, 3);
                Array.set(args, 0, ImageGraphics2D.class.getName());
                Array.set(args, 1, fmt.getLowerCaseName());
                Array.set(args, 2, targetName);
            } else {
                args = Array.newInstance(String.class, 2);
                Array.set(args, 0, pkg + "." + fmt.getName() + "Graphics2D");
                Array.set(args, 1, targetName);
            }

            // Create Test Object
            Constructor<?> constructor = cls.getConstructor(new Class[] { args
                    .getClass() });
            Object test = constructor.newInstance(new Object[] { args });

            // Call Test.runTest(properties);
            Method runTest = test.getClass().getMethod("runTest",
                    new Class[] { Properties.class });
            runTest.invoke(test, new Object[] { properties });

            return;
        }
    }

    protected TestSuite() {
        super("GraphicsIO Test Suite");

        local = !System.getProperty("vg.local", "true").equals("false");

        bitmapFormats = new TreeMap<String, Format>();
        bitmapFormats.put(
            ImageConstants.BMP.toLowerCase(),
            new Format(ImageConstants.BMP, null, "freehep-graphicsio-tests", true, true, 10241, null));
        bitmapFormats.put(
            ImageConstants.GIF.toLowerCase(),
            new Format(ImageConstants.GIF, null, "freehep-graphicsio-tests", true, true, 10241, null));
        bitmapFormats.put(
            ImageConstants.JPG.toLowerCase(),
            new Format(ImageConstants.JPG, null, "freehep-graphicsio-tests", true, true, 10241, null));
        bitmapFormats.put(
            ImageConstants.PNG.toLowerCase(),
            new Format(ImageConstants.PNG, null, "freehep-graphicsio-tests", true, true, 10241, null));
        bitmapFormats.put(
            ImageConstants.WBMP.toLowerCase(),
            new Format(ImageConstants.WBMP, null, "freehep-graphicsio-tests", true, true, 10241, null));

        vectorFormats = new TreeMap<String, Format>();
        // vectorFormats.put("cgm", new Format("CGM", null, false, false, 10230,
        // null));
        vectorFormats.put(
            ImageConstants.EMF.toLowerCase(),
            new Format(ImageConstants.EMF, null, null, true, false, 10231, null));
        vectorFormats.put(
            ImageConstants.JAVA.toLowerCase(),
            new Format(ImageConstants.JAVA, null, null, false, false,
                10238, "org/freehep/graphicsio/java/test"));
        // vectorFormats.put("latex", new Format("Latex", "tex", null, false, false,
        // 10240, null));
        vectorFormats.put(
            ImageConstants.PDF.toLowerCase(),
            new Format("PDF", null, null, true, false, 10235, null));
        vectorFormats.put(
            ImageConstants.PS.toLowerCase(),
            new Format(ImageConstants.PS, null, null, true, false, 10232, null));
        vectorFormats.put(
            ImageConstants.SVG.toLowerCase(),
            new Format(ImageConstants.SVG, null, null, true, false, 10236, null));
        vectorFormats.put(
            ImageConstants.SWF.toLowerCase(),
            new Format(ImageConstants.SWF, null, null, true, false, 10237, null));

        boolean on = true;
        // FVG-241, TestCustomStrokes [3] disabled for MacOS X
        boolean onMacOSXandJDK15 = System.getProperty("os.name").equals(
                "Mac OS X")
                && System.getProperty("java.version").startsWith("1.5");

        
        tests = new ArrayList<Test>();
        tests.add(new Test("TestAll", on));
        tests.add(new Test("TestClip", on));
        tests.add(new Test("TestColors", on));
        tests.add(new Test("TestCustomStrokes", on)); // && !onMacOSXandJDK15));
        tests.add(new Test("TestFonts", on));
        tests.add(new Test("TestFontDerivation", on));
        tests.add(new Test("TestGraphicsContexts", on));
        tests.add(new Test("TestHistogram", on));
        tests.add(new Test("TestHTML", on));
        tests.add(new Test("TestImages", on));
        tests.add(new Test("TestImage2D", on));
        tests.add(new Test("TestLabels", on));
        tests.add(new Test("TestLineStyles", on));
        tests.add(new Test("TestOffset", on));
        tests.add(new Test("TestPaint", on));
        tests.add(new Test("TestPrintColors", on));
        tests.add(new Test("TestRenderingHints", on));
        tests.add(new Test("TestResolution", on));
        tests.add(new Test("TestScatterPlot", on));
        tests.add(new Test("TestShapes", on));
        tests.add(new Test("TestSymbols2D", on));
        tests.add(new Test("TestTaggedString", on));
        tests.add(new Test("TestText2D", on));
        tests.add(new Test("TestTransforms", on));
        tests.add(new Test("TestTransparency", on));

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

    protected void addTests(Format fmt, Properties properties) {
        if ((fmt == null) || !fmt.isEnabled())
            return;

        for (Iterator<Test> i = tests.iterator(); i.hasNext();) {
            Test test = i.next();
            if (test.isEnabled()) {
                addTest(new TestCase(test.getName(), fmt, testOutDir,
                        properties));
                writeHTML(test, fmt, os, jdk);
            } else {
                System.err.println("NOTE: " + test.getName() + " disabled for "+fmt.getName()+".");
            }
        }
    }

    protected void addTests(String formatName, Properties properties) {
    	Format fmt = bitmapFormats.get(formatName.toLowerCase());
    	if (fmt == null) {
    		fmt = vectorFormats.get(formatName.toLowerCase());
    	}
    	addTests(fmt, properties);
    }
    
    protected void addTests(String formatName) {
        addTests(formatName, null);
    }
        
    protected void addTests(String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                addTests(args[i]);
            }
        } else {
            for (Iterator<String> i = bitmapFormats.keySet().iterator(); i.hasNext();) {
            	Format fmt = bitmapFormats.get(i.next());
                addTests(fmt, null);
            }
            for (Iterator<String> i = vectorFormats.keySet().iterator(); i.hasNext();) {
                Format fmt = vectorFormats.get(i.next());
//                if (fmt.getUpperCaseName().equals("JAVA"))
                    addTests(fmt, null);
            }
        }
    }

    private void writeHTML(Test test, Format fmt, String os, String jdk) {

        String site = "../../../../";
        String css = site + "css";

        String top = site + (local ? "../../" : "") + "../";
        String refFormat = ImageConstants.PNG.toLowerCase();

        String testSite = top + "freehep-graphicsio-tests/"
                + (local ? "target/site/" : "");
        String ref = testSite + "ref-output/" + refFormat + "/";
        String cloud = testSite + "images/" + "cloudy.jpg";

        String title = (local ? "[LOCAL] " : "") + "VectorGraphics " + fmt.getName()
                + " " + test.getName();
        String freehep = "http://java.freehep.org/";
        String freehepImage = freehep + "images/sm-freehep.gif";
        String url = freehep + "vectorgraphics/freehep-graphicsio-" + fmt.getLowerCaseName();

        String out = testOutDir + fmt.getTestDir() + "/";
        String baseDir = System.getProperty("basedir");
        if (baseDir != null)
            out = baseDir + "/" + out;
        try {
            // Create Export filetype to get mime type
//            Class cls = Class.forName(gioPackage + fmt.getLowerCaseName() + "."
//                    + fmt.getName() + "ExportFileType");
//            ExportFileType fileType = (ExportFileType) cls.newInstance();
        	ExportFileType.setClassLoader(getClass().getClassLoader());
        	List<?> fileTypes = ExportFileType.getExportFileTypes(fmt.getLowerCaseName());
        	if (fileTypes.size() <= 0) throw new AssertionFailedError("No ExportFileType found for format '"+fmt.getLowerCaseName()+"'");
        	String[] mimeTypes = ((ExportFileType)fileTypes.get(0)).getMIMETypes();
        	if (mimeTypes.length <= 0) throw new AssertionFailedError("No MimeTypes found for ExportFileType '"+fmt.getLowerCaseName()+"'");
        	
            (new File(out)).mkdirs();
            PrintWriter w = new PrintWriter(new FileWriter(out + test.getName()
                    + ".html"));

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
            w.println("            " + (local ? "[LOCAL] " : "")
                    + "FreeHEP VectorGraphics Test " + fmt.getName());
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

            if (!local) {
                w.println("            <h5><a href=\"" + top
                        + "index.html" + "\">Back</a></h5>");
            }

            w.println("            <h5>Operating System</h5>");
            w.println("            <ul>");

            String[] oss = { "Windows", "Linux", "MacOSX" };
            for (int i = 0; i < oss.length; i++) {
                if (!local || os.equals(oss[i])) {
                    w.println("              <li class=\"none\">");
                    if (os.equals(oss[i]))
                        w.println("                <strong>");
                    w.println("                  <a href=\"" + top
                            + fmt.getModuleName() + "/" 
                            + (local ? "target/site/" : "") + "test-output/" + oss[i] + "/" + jdk
                            + "/" + fmt.getLowerCaseName() + "/"
                            + test.getName() + ".html\">" + oss[i] + "</a>");
                    if (os.equals(oss[i]))
                        w.println("                </strong>");
                    w.println("              </li>");
                }
            }
            w.println("            </ul>");

            w.println("            <h5>Java</h5>");
            w.println("            <ul>");
            
            w.println("              <li class=\"none\">");
            String[] jdks = { "JDK-1.5", "JDK-1.6" };
            for (int i=0; i<jdks.length; i++) {
                if (!local || jdk.equals(jdks[i])) {
                    w.println("              <li class=\"none\">");
                    if (jdk.equals(jdks[i]))
                        w.println("                <strong>");
                    w.println("                  <a href=\"" + top
                            + fmt.getModuleName() + "/"
                            + (local ? "target/site/" : "") + "test-output/" + os + "/" + jdks[i] + "/"
                            + fmt.getLowerCaseName() + "/" + test.getName()
                            + ".html\">" + jdks[i] + "</a>");
                    if (jdk.equals(jdks[i]))
                        w.println("                </strong>");
                    w.println("              </li>");
                }
                
            }
            w.println("            </ul>");

            w.println("            <h5>Vector Formats</h5>");
            w.println("            <ul>");
            for (Iterator<String> i = vectorFormats.keySet().iterator(); i.hasNext();) {
                String key = i.next();
                w.println("              <li class=\"none\">");
                if (key.equalsIgnoreCase(fmt.getLowerCaseName())) {
                    w.println("                <strong>");
                }
                Format value = vectorFormats.get(key);
                w.print("                  ");
                if (value.isEnabled()) {
                    w.print("<a href=\"" + top + value.getModuleName() + "/"
                            + (local ? "target/site/" : "") + "test-output/" + os + "/" + jdk
                            + "/" + key + "/" + test.getName() + ".html\">");
                }
                w.print(value.getName());
                if (value.isEnabled()) {
                    w.print("</a>");
                }
                w.println();
                if (key.equalsIgnoreCase(fmt.getLowerCaseName()))
                    w.println("                </strong>");
                w.println("              </li>");
            }
            w.println("            </ul>");
            
            w.println("            <h5>Bitmap Formats</h5>");
            w.println("            <ul>");
            for (Iterator<String> i = bitmapFormats.keySet().iterator(); i.hasNext();) {
                String key = i.next();
                w.println("              <li class=\"none\">");
                if (key.equalsIgnoreCase(fmt.getLowerCaseName())) {
                    w.println("                <strong>");
                }
                Format value = bitmapFormats.get(key);
                w.print("                  ");
                if (value.isEnabled()) {
                    w.print("<a href=\"" + top + value.getModuleName() + "/"
                            + (local ? "target/site/" : "") + "test-output/" + os + "/" + jdk
                            + "/" + key + "/" + test.getName() + ".html\">");
                }
                w.print(value.getName());
                if (value.isEnabled()) {
                    w.print("</a>");
                }
                w.println();
                if (key.equalsIgnoreCase(fmt.getLowerCaseName()))
                    w.println("                </strong>");
                w.println("              </li>");
            }
            w.println("            </ul>");
            
            w.println("            <h5>" + fmt.getName() + " Tests</h5>");
            w.println("            <ul>");
            for (Iterator<Test> i = tests.iterator(); i.hasNext();) {
                Test t = i.next();
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

            w.println("            <h5>" + fmt.getName() + " Links</h5>");
            w.println("            <ul>");
            w.println("              <li><a href=\"" + jiraURL + "&pid="
                    + jiraProductId + "&component=" + fmt.getJiraId()
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
            w.println("              <h2>" + (local ? "[LOCAL] " : "")
                    + test.getName() + " " + fmt.getName() + "</h2>");
            w.println("              <table class=\"bodyTable\">");
            // w.println(" <caption></caption>");
            w.println("                <tr class=\"a\">");
            w.println("                  <th>" + fmt.getName() + "</th>");
            w.println("                  <th>Reference ("
                    + refFormat.toUpperCase() + ")</th>");
            w.println("                </tr>");
            w.println("                <tr class=\"a\">");
            w.println("                  <td><a href=\"" + test.getName() + "."
                    + fmt.getExtension() + "\">" + test.getName() + "." + fmt.getExtension() + "</a></td>");
            w.println("                  <td><a href=\"" + ref + test.getName()
                    + "." + refFormat + "\">" + test.getName() + "."
                    + refFormat + "</a></td>");
            w.println("                </tr>");
            w.println("                <tr class=\"a\">");
            Test[] testArray = tests.toArray(new Test[0]);
            int testIndex = 0;
            while (testIndex < testArray.length) {
                if (testArray[testIndex].equals(test))
                    break;
                testIndex++;
            }
            int previousIndex = testIndex - 1;
            while ((previousIndex >= 0)
                    && !testArray[previousIndex].isEnabled())
                previousIndex--;
            if (previousIndex >= 0) {
                w.println("                  <td><a href=\""
                        + testArray[previousIndex].getName()
                        + ".html\">previous</a></td>");
            } else {
                w.println("                  <td/>");
            }
            int nextIndex = testIndex + 1;
            while ((nextIndex < testArray.length)
                    && !testArray[nextIndex].isEnabled())
                nextIndex++;
            if (nextIndex < testArray.length) {
                w.println("                  <td><a href=\""
                        + testArray[nextIndex].getName()
                        + ".html\">next</a></td>");
            } else {
                w.println("                  <td/>");
            }
            w.println("                </tr>");
            w.println("                <tr class=\"b\">");
            // w.println(" <td><a
            // href=\""+name+"."+ext+"\">"+name+"."+ext+"</a></td>");
            w.print("                  ");
            w.print("<td background=\"" + cloud + "\">");
            w.print("<object type=\"" + mimeTypes[0] + "\" name=\""
                    + test.getName() + "\" data=\"" + test.getName() + "."
                    + fmt.getExtension() + "\" width=\"" + TestingPanel.width + "\" height=\""
                    + TestingPanel.height + "\">");
            w.print("<param name=\"wmode\" value=\"transparent\"/>");
            w.print("Image not embeddable: " + mimeTypes[0]);
            w.print("</object>");
            w.println("</td>");
            w.println("                  <td background=\"" + cloud
                    + "\"><img src=\"" + ref + test.getName() + "." + refFormat
                    + "\"/></td>");
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
            w.println("              2000-2007");
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
        }
    }

    public static TestSuite suite() {
        // get command line arguments from environment var (set by ANT)
        StringTokenizer st = new StringTokenizer(
                System.getProperty("args", ""), " ");
        List<String> argList = new ArrayList<String>();
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
