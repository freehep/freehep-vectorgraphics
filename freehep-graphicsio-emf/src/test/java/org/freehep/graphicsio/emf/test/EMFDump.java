// Copyright 2001, FreeHEP.
package org.freehep.graphicsio.emf.test;

import java.io.FileInputStream;
import java.io.IOException;

import org.freehep.graphicsio.emf.EMFHeader;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.util.io.Tag;

/**
 * @author Mark Donszelmann
 * @version $Id: freehep-graphicsio-emf/src/test/java/org/freehep/graphicsio/emf/test/EMFDump.java f24bd43ca24b 2005/12/02 00:39:35 duns $
 */
public class EMFDump {

    public static void main(String[] args) {
		FileInputStream fis = null;
		EMFInputStream emf = null;

		try {
			fis = new FileInputStream(args[0]);
			emf = new EMFInputStream(fis);

			long start = System.currentTimeMillis();
			EMFHeader header = emf.readHeader();
			System.out.println(header);

			Tag tag = null;
			do {
				tag = emf.readTag();
				System.out.println(tag);

				if (tag instanceof EOF) {
					// stop on End Of File tag
					break;
				}
			} while (tag != null);

			System.out.println("Parsed file in: " + (System.currentTimeMillis() - start) + " ms.");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != emf) {
					emf.close();
				} else if (null != fis) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
}
