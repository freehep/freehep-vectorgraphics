package org.freehep.graphicsbase.util.export.test;

import java.util.Iterator;

import org.freehep.graphicsbase.util.export.ExportFileType;
import org.freehep.graphicsbase.util.export.ExportFileTypeRegistry;

public class ExportFileTypeTest {

    public static void main(String[] args) {
        ExportFileTypeRegistry r = ExportFileTypeRegistry.getDefaultInstance(null);
        System.out.println("All ExportFileTypes");
        Iterator<ExportFileType> providers = r.get().iterator();
        while (providers.hasNext()) {
            System.out.println("   "+providers.next());
        }
    }

}
