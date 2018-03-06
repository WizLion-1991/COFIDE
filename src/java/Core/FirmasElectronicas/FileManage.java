package Core.FirmasElectronicas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *Clase Utilitaria para guardar los cfdi
 * @author zeus
 */
public class FileManage {

   /* Obtiene el arreglo de bytes de un Archivo
    * @param fileToRead Archivo a leer
    * @return Arreglo de bytes
    * @throws FileNotFoundException
    * @throws IOException
    * @throws Exception
    */
   public static byte[] getBytesFromFile(File fileToRead) throws FileNotFoundException,
           IOException, Exception {
      byte[] bytes = null;
      FileInputStream fis = new FileInputStream(fileToRead);
      bytes = new byte[(int) fileToRead.length()];
      fis.read(bytes);
      fis.close();
      return bytes;
   }

   /**
    * Para los archivos que contienen texto tales como XML, txt, html, etc.
    * Se puede recuperar su contenido en formato Cadena con codificación UTF8
    * con este método.
    * @param fileToRead 
    * @return contenido del archivo expresado en formato cadena UTF8
    */
   public static String getStringFromFile(File fileToRead) {
      String contentTextArchivo = null;
      try {
         contentTextArchivo = new String(getBytesFromFile(fileToRead), "UTF8");
      } catch (Exception e) {
      }
      return contentTextArchivo;
   }

   /**
    * Método para crear un objeto tipo java.io.File a partir de un arreglo de bytes
    * @param bytesFile Los bytes que representan al archivo
    * @param pathFile Ruta en donde se grabara el archivo
    * @param nameFile Nombre del archivo
    * @return Objeto tipo File creado
    * @throws FileNotFoundException
    * @throws IOException
    */
   public static File createFileFromByteArray(byte[] bytesFile, String pathFile, String nameFile)
           throws FileNotFoundException, IOException {
      File newTempFile = new File(pathFile + nameFile);
      FileOutputStream fos = new FileOutputStream(newTempFile);
      fos.write(bytesFile);
      fos.flush();
      fos.close();
      return newTempFile;
   }

   /**
    * Método para crear un objeto tipo java.io.File a partir de un arreglo de bytes
    * @param contentFile El string con el contenido del archivo
    * @param pathFile Ruta en donde se grabara el archivo
    * @param nameFile Nombre del archivo
    * @return Objeto tipo File creado
    * @throws FileNotFoundException
    * @throws IOException
    */
   public static File createFileFromString(String contentFile, String pathFile, String nameFile)
           throws FileNotFoundException, IOException {
      File newTempFile = new File(pathFile + nameFile);
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newTempFile), "UTF8"));
      bw.write(contentFile);
      bw.flush();
      bw.close();
      return newTempFile;
   }
}
