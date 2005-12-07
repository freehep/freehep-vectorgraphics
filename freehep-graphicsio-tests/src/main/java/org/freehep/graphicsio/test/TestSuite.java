// Copyright 2001-2005, FreeHEP.
package org.freehep.graphicsio.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.freehep.graphicsio.ImageGraphics2D;
import org.freehep.util.Assert;
import org.freehep.util.io.UniquePrintStream;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestSuite.java e3449d5a3c6c 2005/12/07 22:14:47 duns $
 */
public class TestSuite extends junit.framework.TestSuite {

    private static final String testPackage = "org.freehep.graphicsio.test.";

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
            
            String out = "target/test-output/" + dir + "/";
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
        addTest(new TestCase(testPackage + "TestAll", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestClip", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestColors", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestFonts", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestFontDerivation", fmt, pkg, dir, ext,
                compare, properties));
        addTest(new TestCase(testPackage + "TestGraphicsContexts", fmt, pkg, dir, ext,
                compare, properties));
        addTest(new TestCase(testPackage + "TestHTML", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestImages", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestImage2D", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestLabels", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestLineStyles", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestOffset", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestPaint", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestPrintColors", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestResolution", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestShapes", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestSymbols2D", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestTaggedString", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestText2D", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestTransforms", fmt, pkg, dir, ext, compare,
                properties));
        addTest(new TestCase(testPackage + "TestTransparency", fmt, pkg, dir, ext, compare,
                properties));
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
