// Copyright 2001-2005, FreeHEP.
package org.freehep.graphicsio.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
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
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestSuite.java aec2c73c5283 2005/12/03 07:40:25 duns $
 */
public class TestSuite extends junit.framework.TestSuite {

    private static final String pkg = "org.freehep.graphicsio.test.";

    public static class TestCase extends junit.framework.TestCase {

        private String name, fullName, fmt, dir, ext;

        private boolean compare;

        private Properties properties;

        public TestCase(String fullName, String fmt, String dir, String ext,
                boolean compare, Properties properties) {
            super("GraphicsIO Test for " + fullName + " in " + fmt);
            this.fullName = fullName;
            int dot = fullName.lastIndexOf(".");
            name = dot < 0 ? fullName : fullName.substring(dot + 1);
            this.fmt = fmt;
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

                Array.set(args, 0, "org.freehep.graphicsio." + dir + "." + fmt
                        + "Graphics2D");
                Array.set(args, 1, targetName);
            }

            // set general properties
            if (properties != null) {
                try {
                    Method setProperties = cls.getMethod("setProperties",
                            new Class[] { Properties.class });
                    setProperties.invoke(null, new Object[] { properties });
                } catch (NoSuchMethodException nsme) {
                    // ignored
                }
            }

            Method main = cls
                    .getMethod("main", new Class[] { args.getClass() });
            main.invoke(null, new Object[] { args });

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
        addTests(fmt, fmt.toLowerCase(), fmt.toLowerCase(), compare);
    }

    protected void addTests(String fmt, String dir, String ext, boolean compare) {
        addTests(fmt, dir, ext, compare, null);
    }

    protected void addTests(String fmt, String dir, String ext,
            boolean compare, Properties properties) {
        // Alphabetically
        addTest(new TestCase(pkg + "TestAll", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestClip", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestColors", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestFonts", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestFontDerivation", fmt, dir, ext,
                compare, properties));
        addTest(new TestCase(pkg + "TestGraphicsContexts", fmt, dir, ext,
                compare, properties));
        addTest(new TestCase(pkg + "TestHTML", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestImages", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestImage2D", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestLabels", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestLineStyles", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestOffset", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestPaint", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestPrintColors", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestResolution", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestShapes", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestSymbols2D", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestTaggedString", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestText2D", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestTransforms", fmt, dir, ext, compare,
                properties));
        addTest(new TestCase(pkg + "TestTransparency", fmt, dir, ext, compare,
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
                addTests(args[i].toUpperCase(), args[i].toLowerCase(), ext,
                        compare);
            }
        } else {
            addTests("CGM", compare);
            addTests("EMF", compare);
            addTests("GIF", compare);
            addTests("JPG", compare);
            addTests("LATEX", "latex", "tex", compare);
            addTests("PDF", compare);
            addTests("PNG", compare);
            addTests("PS", compare);
            addTests("SVG", "svg", "svgz", compare);
            addTests("SWF", compare);
            addTests("JAVA", compare);
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
