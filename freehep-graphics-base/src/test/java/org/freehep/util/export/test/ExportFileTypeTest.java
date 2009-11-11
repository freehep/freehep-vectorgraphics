package org.freehep.util.export.test;

import java.util.Iterator;
import org.freehep.util.export.ExportFileTypeRegistry;

public class ExportFileTypeTest {

    public static void main(String[] args) {
        ExportFileTypeRegistry r = ExportFileTypeRegistry.getDefaultInstance(null);
        System.out.println("All ExportFileTypes");
        Iterator providers = r.get().iterator();
        while (providers.hasNext()) {
            System.out.println("   "+providers.next());
        }
    }

}
