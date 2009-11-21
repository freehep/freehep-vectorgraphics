package org.freehep.graphicsio.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.freehep.graphicsio.exportchooser.ImageExportFileType;
import org.freehep.util.export.ExportFileType;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-tests/src/main/java/org/freehep/graphicsio/test/TestExportFileType.java f493ff6e61b2 2005/12/01 18:46:43 duns $
 */
public class TestExportFileType {

    public static void main(String args[]) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: TestExportFileType className");
            System.exit(1);
        }

        ExportFileType type;
        if (args[0].equals(ImageExportFileType.class.getName())) {
            if (args.length != 2) {
                System.err.println("Usage: " + ImageExportFileType.class
                        + " format");
                System.exit(1);
            }
            type = ImageExportFileType.getInstance(args[1]);
            if (type == null) {
                System.err.println("Format not supported: " + args[1]);
                System.exit(1);
            }
        } else {
            Class<?> clazz = Class.forName(args[0]);
            type = (ExportFileType) clazz.newInstance();
        }

        if (!type.hasOptionPanel()) {
            System.err.println("No options exist for " + args[0]);
            System.exit(1);
        }

        Properties options = new Properties();
        File optionsFile = new File("TestExportFileType.properties");
        try {
            options.load(new FileInputStream(optionsFile));
        } catch (FileNotFoundException e) {
        }

        JPanel panel = type.createOptionPanel(options);
        int rc = JOptionPane.showConfirmDialog(null, panel, "Options for "
                + type.getDescription(), JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (rc == JOptionPane.OK_OPTION) {
            if (type.applyChangedOptions(panel, options)) {
                ;
                System.out.println("New options written");
                options.list(System.out);
                options.store(new FileOutputStream(optionsFile),
                        "TestExportFileType");
            } else {
                System.out.println("No options changed");
            }
        } else {
            System.out.println("Cancelled");
        }

        System.exit(0);
    }
}
