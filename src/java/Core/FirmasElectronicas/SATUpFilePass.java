package Core.FirmasElectronicas;

import comSIWeb.Operaciones.Conexion;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *Esta clase se encarga de guardar el archivo con la llave privada y guardar de
 * manera encriptada el password
 * @author zeus
 */
public class SATUpFilePass {

   /**
    * Genera el archivo PEM en base a la llave privada
    * @param request Es la peticion web
    * @param context Es la variable context del servlet
    * @param oConn Es la conexion a la base de datos
    * @return Regresa Ok en caso de todo halla salido correcto
    * @throws Exception  es la excepcion generada
    */
   public String savePrivateKey(HttpServletRequest request, ServletContext context,
           Conexion oConn) throws Exception {
      String strResp = "OK";
      //Validamos si la peticion se genero con enctype
      if (ServletFileUpload.isMultipartContent(request)) {
         // Parse the HTTP request...
         int intEMP_ID = 0;
         String strPass = "";
         String strNoCert = "";

         //Creamos objeto para subir archivo al servidor
         ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
         List fileItemsList = servletFileUpload.parseRequest(request);
         Iterator it = fileItemsList.iterator();
         while (it.hasNext()) {
            FileItem fileItem = (FileItem) it.next();
            if (fileItem.isFormField()) {
               /* The file item contains a simple name-value pair of a form field */
               //System.out.println("form..." + fileItem.getFieldName() + " "  + fileItem.getString());
               if (fileItem.getFieldName().equals("EMP_ID")) {
                  intEMP_ID = Integer.valueOf(fileItem.getString());
               }
               if (fileItem.getFieldName().equals("PassPrivate")) {
                  strPass = fileItem.getString();
               }
               if (fileItem.getFieldName().equals("NoCert")) {
                  strNoCert = fileItem.getString();
               }
            } else {
               /* The file item contains an uploaded file */
               /* Get the name attribute value of the <input type="file"> element. */
               String fieldName = fileItem.getFieldName();

               /* Get the size of the uploaded file in bytes. */
               long fileSize = fileItem.getSize();

               /* Get the name of the uploaded file at the client-side. 
               Some browsers such as IE 6 include the whole path here (e.g. e:\files\myFile.txt),
               so you may need to extract the file name from the path.
               This information is provided by the client browser, which means you should be cautious since it may be
               a wrong value provided by a malicious user. */
               String fileName = fileItem.getName();
               //Guardamos la llave
               if (fileName.toLowerCase().endsWith(".key")) {
                  //Separardor de carpetas
                  String strSeparator = System.getProperty("file.separator");
                  //out.println("strPathAct:" + strPathAct);
                  String strPathSave = context.getInitParameter("PathPrivateKey");
                  //Cambiamos el nombre
                  fileName = "key_private" + intEMP_ID + ".key";
                  String strPathUsado = strPathSave + strSeparator + fileName;
                  //Guardamos el archivo
                  File saveTo = new File(strPathUsado);
                  fileItem.write(saveTo);
                  //Validamos si la llave es valida
                  boolean bolValido = SATXml.ValidaPrivateKey(strPathUsado, strPass);
                  //Es valido continuamos
                  if (bolValido) {
                     boolean bolShowQuery = oConn.isBolMostrarQuerys();
                     //Quitamos la muestra de querys
                     oConn.setBolMostrarQuerys(false);
                     //Ahora guardamos la contrasena encriptada
                     String strUpdate = "Update vta_empresas set EMP_PASSKEY = AES_ENCRYPT('" + strPass + "','" + getMyPass(context) + "'),"
                             + "EMP_NOMKEY='" + fileName + "',EMP_NOSERIECERT='" + strNoCert + "' "
                             + "where EMP_ID = " + intEMP_ID;
                     oConn.runQueryLMD(strUpdate);
                     oConn.setBolMostrarQuerys(bolShowQuery);
                  } else {
                     strResp = "ERROR:EL sello no pudo abrirse.";
                  }
               }
               //Guardamos los certificados
               if (fileName.toLowerCase().endsWith(".cer")) {
                  //Separardor de carpetas
                  String strSeparator = System.getProperty("file.separator");
                  //out.println("strPathAct:" + strPathAct);
                  String strPathSave = context.getInitParameter("PathPrivateKey");
                  //Cambiamos el nombre
                  fileName = "cert_file" + intEMP_ID + ".cer";
                  String strPathUsado = strPathSave + strSeparator + fileName;
                  //Guardamos el archivo
                  File saveTo = new File(strPathUsado);
                  fileItem.write(saveTo);
                  boolean bolShowQuery = oConn.isBolMostrarQuerys();
                  //Quitamos la muestra de querys
                  oConn.setBolMostrarQuerys(false);
                  //Ahora guardamos la contrasena encriptada
                  String strUpdate = "Update vta_empresas set "
                          + "EMP_NOMCERT='" + fileName + "' "
                          + "where EMP_ID = " + intEMP_ID;
                  oConn.runQueryLMD(strUpdate);
                  oConn.setBolMostrarQuerys(bolShowQuery);
               }
            }
         }
      } else {
         strResp = "ERROR:La peticion no subio un archivo";
      }
      return strResp;
   }

   /**Obtiene el password para encriptar*/
   private String getMyPass(ServletContext context) {
      String strMyPass = "";
      try {
         String strPassB64 = context.getInitParameter("SecretWord");
         Opalina opa = new Opalina();
         strMyPass = opa.DesEncripta(strPassB64, "dWAM1YhbGAeu7CTULai4eQ==");
      } catch (NoSuchAlgorithmException ex) {
         Logger.getLogger(SATUpFilePass.class.getName()).log(Level.SEVERE, null, ex);
      } catch (NoSuchPaddingException ex) {
         Logger.getLogger(SATUpFilePass.class.getName()).log(Level.SEVERE, null, ex);
      } catch (InvalidKeyException ex) {
         Logger.getLogger(SATUpFilePass.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalBlockSizeException ex) {
         Logger.getLogger(SATUpFilePass.class.getName()).log(Level.SEVERE, null, ex);
      } catch (BadPaddingException ex) {
         Logger.getLogger(SATUpFilePass.class.getName()).log(Level.SEVERE, null, ex);
      }
      return strMyPass;
   }
}
