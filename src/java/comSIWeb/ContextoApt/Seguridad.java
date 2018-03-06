/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.ContextoApt;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *Esta clase valida que la URL halla sido redireccionada desde el mismo dominio
 * @author zeus
 */
public class Seguridad {

   private boolean bolDEBUG;

   /**
    * Valida que la URL que nos mando a llamar es del mismo dominio para evitar que el usuario teclee manualmente estas peticiones
    * @param request Es el objeto HttpServletRequest
    * @return Nos regresa TRUE o FALSE
    */
   public boolean ValidaURL(HttpServletRequest request) {
      boolean bolValido = false;
      String strHTTP_HOST = "";
      String strHTTP_REFERER = request.getHeader("referer");
      if (strHTTP_REFERER != null) {
         String currentFile = request.getRequestURI();
         if (request.getQueryString() != null) {
            currentFile = currentFile + '?' + request.getQueryString();
         }
         URL currentURL;
         try {
            currentURL = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), currentFile);
            strHTTP_HOST = currentURL.toString();
         } catch (MalformedURLException ex) {
            Logger.getLogger(Seguridad.class.getName()).log(Level.SEVERE, null, ex);
         }
         /*Comparamos las dos cadenas*/
         if (this.bolDEBUG) {
            System.out.println("Original strHTTP_HOST:" + strHTTP_HOST + " ");
            System.out.println("Original strHTTP_REFERER:" + strHTTP_REFERER + " ");
         }
         strHTTP_HOST = strHTTP_HOST.replace("http://", "");
         strHTTP_HOST = strHTTP_HOST.replace("https://", "");
         strHTTP_HOST = strHTTP_HOST.replace(":8080", "");
         strHTTP_HOST = strHTTP_HOST.replace(":8084", "");
         strHTTP_HOST = strHTTP_HOST.replace(":80", "");
         strHTTP_REFERER = strHTTP_REFERER.replace("http://", "");
         strHTTP_REFERER = strHTTP_REFERER.replace("https://", "");
         strHTTP_REFERER = strHTTP_REFERER.replace(":8080", "");
         strHTTP_REFERER = strHTTP_REFERER.replace(":8084", "");
         strHTTP_REFERER = strHTTP_REFERER.replace(":80", "");
         int intDiagonal = strHTTP_HOST.indexOf("/");
         int intDiagonal2 = strHTTP_REFERER.indexOf("/");
         if (this.bolDEBUG) {
            System.out.println("strHTTP_HOST:" + strHTTP_HOST + " " + strHTTP_HOST.substring(0, intDiagonal));
            System.out.println("strHTTP_REFERER:" + strHTTP_REFERER + " " + strHTTP_REFERER.substring(0, intDiagonal2));
         }
         if (strHTTP_HOST.substring(0, intDiagonal).toLowerCase().equals(strHTTP_REFERER.substring(0, intDiagonal2).toLowerCase())) {
            bolValido = true;
         }

      }
      return bolValido;
   }

   /**
    * Indicamos si esta en modo DEBUG
    * @param bolDEBUG es un boolean
    */
   public void setBolDEBUG(boolean bolDEBUG) {
      this.bolDEBUG = bolDEBUG;
   }
}
