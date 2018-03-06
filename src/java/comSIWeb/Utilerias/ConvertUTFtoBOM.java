package comSIWeb.Utilerias;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Clase que le añade BOM a un archivo
 * @author zeus
 */
public class ConvertUTFtoBOM {

   /**
    * Le agrega el marcador BOM al inicio del archivo
    * @param strPathOrigen Es el path del archivo
    * @param strNomFile Es el nombre del archivo
    * @return Regresa true si todo fue exitoso
    */
   public static boolean convertUTF8toBOM(String strPathOrigen, String strNomFile) {
      boolean bolResp = true;
      final byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};//valor BOM
      FileInputStream fis = null;
      System.out.println("" + strNomFile);
      try {
         //Renombramos el archivo original
         File fileRenom = new File(strPathOrigen + System.getProperty("file.separator") + strNomFile);
         if (fileRenom.exists()) {
            File fileOld = new File(strPathOrigen + System.getProperty("file.separator") + "_" + strNomFile);
            if (fileRenom.renameTo(fileOld)) {
               //Generamos nuevo archivo
               FileOutputStream fos = new FileOutputStream(fileRenom);

               //Abrimos archivo original
               byte[] fileBArray = new byte[(int) fileOld.length()];
               fis = new FileInputStream(fileOld);
               fis.read(fileBArray);

               //Agregamos BOM
               fos.write(bom);

               //Agregamos el resto del contenido
               fos.write(fileBArray);

               //Cerramos archivos
               fis.close();
               fos.close();

               //Eliminamos el archivo origen
               fileOld.delete();
            } else {
               bolResp = false;
            }
         }
      } catch (FileNotFoundException ex) {
         Logger.getLogger(ConvertUTFtoBOM.class.getName()).log(Level.SEVERE, null, ex);
         bolResp = false;
      } catch (IOException ex) {
         Logger.getLogger(ConvertUTFtoBOM.class.getName()).log(Level.SEVERE, null, ex);
         bolResp = false;
      } finally {
      }
      return bolResp;
   }
}
