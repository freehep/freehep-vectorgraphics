package org.freehep.util.export;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A Transferable for putting vector graphics onto the system clipboard.
 * This is particularly useful on Windows where EMF format can be used to copy
 * vector graphics onto the system clipboard, from where it can be pasted into
 * many applications, including PowerPoint and Word. VectorGraphicsTranferable
 * will fall back to using a bitmap image format if no suitable VectorGraphics
 * format is registered.
 * <p>
 * Note: This only works with JDK 1.4 or later.
 * @author tonyj
 * @version $Id: src/main/java/org/freehep/util/export/VectorGraphicsTransferable.java 5c17e2566e45 2005/11/22 08:55:30 duns $
 */
public class VectorGraphicsTransferable implements ClipboardOwner, Transferable
{
   private Component component;
   private DataFlavor imageFlavor = new DataFlavor("image/x-java-image; class=java.awt.Image", "Image");
   private Map types = new HashMap();
   private static Map defaultTypes;
   /**
    * Create a VectorGraphicsTransferable with the built-in flavor/ExportFileType associations
    * @param c The Component whose graphics will be transfered
    */
   public VectorGraphicsTransferable(Component c)
   {
      this(c,true);
   }
   /**
    * Create a VectorGraphicsTransferable.
    * @param c The Component whose graphics will be transfered
    * @param addDefaultAssociations Add the standard flavor/ExportFileType associations
    */
   public VectorGraphicsTransferable(Component c, boolean addDefaultAssociations)
   {
      this.component = c;
      if (addDefaultAssociations) addDefaultFileTypes();
   }
   public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException
   {
//      System.out.println(dataFlavor);
      if (dataFlavor.match(imageFlavor))
      {
        Image img = component.createImage(component.getWidth(),component.getHeight());
        Graphics g = img.getGraphics();
        component.print(g);
        g.dispose();
        return img;
      }
      else
      {
         ExportFileType type = (ExportFileType) types.get(dataFlavor);
         if (type != null)
         {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            type.exportToFile(out,component,component,null,component.getName());
            out.close();
            return new ByteArrayInputStream(out.toByteArray());
         }
      }
      throw new UnsupportedFlavorException(dataFlavor);
   }
   public DataFlavor[] getTransferDataFlavors()
   {
      DataFlavor[] result = new DataFlavor[types.size() + 1];
      types.keySet().toArray(result);
      result[types.size()] = imageFlavor;
      return result;
   }
   public boolean isDataFlavorSupported(DataFlavor dataFlavor)
   {
      if (dataFlavor.match(imageFlavor)) return true;
      if (types.containsKey(dataFlavor)) return true;
      return false;
   }
   public void lostOwnership(Clipboard clipboard, Transferable transferable)
   {
   }
   /**
    * Associate a DataFlavor with the ExportFileType
    */
   public void addExportFileType(DataFlavor flavor, ExportFileType type)
   {
      types.put(flavor,type);
   }
   private void addDefaultFileTypes()
   {
      if (defaultTypes == null) defaultTypes = createDefaultTypes();
      types.putAll(defaultTypes);
   }
   private static Map createDefaultTypes()
   {
      Map result = new HashMap();
      // TODO: It would make sense to check for services which implement some
      // interface, rather than having this hardwired here.
      addType(result, "ENHMETAFILE", "image/emf",
                      "Enhanced Meta File",
                      "org.freehep.graphicsio.emf.EMFExportFileType");
// DISABLED for now, MacOS X does not pick up PDF and or PNG
//      addType(result, "PDF",         "application/pdf",
//                      "Portable Document Format",
//                      "org.freehep.graphicsio.pdf.PDFExportFileType");
//      addType(result, "PNG",         "application/pdf",
//                      "Portable Network Graphics",
//                      "org.freehep.graphicsio.png.PNGExportFileType");
      return result;
   }

   private static void addType(Map result, String atom, String mimeType, String description, String className)
   {
      try
      {
         DataFlavor df = new DataFlavor(mimeType, description);
         SystemFlavorMap map =  (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();
         map.addUnencodedNativeForFlavor(df, atom);

         ClassLoader loader = Thread.currentThread().getContextClassLoader();
         Class cls = loader == null ? Class.forName(className) : loader.loadClass(className);
         ExportFileType type = (ExportFileType) cls.newInstance();

         result.put(df,type);
      }
      catch (Throwable x)
      {
         System.err.println("Unable to install flavor for mime type '"+mimeType+"' (this is expected if not using JDK 1.4)");
      }
   }
}