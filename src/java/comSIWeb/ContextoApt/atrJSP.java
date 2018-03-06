/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.ContextoApt;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *Inicializa los atributos generales de cada pagina
 * @author zeus
 */
public class atrJSP {

   /**
    * Definimos los atributos de expiracion y el tipo de dato a mostrar en la pagina
    * @param request Es el ojeto de peticion
    * @param response Es el objeto de respuesta
    * @param bolExpira Indica si establecemos que la pagina expira
    * @param bolEsXML  Indica si establecemos que la pagina emitira datos en XML
    */
   public static void atrJSP(HttpServletRequest request, HttpServletResponse response, boolean bolExpira, boolean bolEsXML) {
      atrJSP( request,  response,  bolExpira,  bolEsXML, false) ;
   }
   /**
    * Definimos los atributos de expiracion y el tipo de dato a mostrar en la pagina
    * @param request Es el ojeto de peticion
    * @param response Es el objeto de respuesta
    * @param bolExpira Indica si establecemos que la pagina expira
    * @param bolEsXML  Indica si establecemos que la pagina emitira datos en XML
    * @param bolJson Indica si establecemos que la pagina emitira datos en Json
    */
   public static void atrJSP(HttpServletRequest request, HttpServletResponse response, boolean bolExpira, boolean bolEsXML, boolean bolJson) {
      if (bolExpira) {
         // Set to expire far in the past.
         response.setHeader("Expires", "Sat, 6 May 1990 12:00:00 GMT");
         // Set standard HTTP/1.1 no-cache headers.
         response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
         // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
         response.addHeader("Cache-Control", "post-check=0, pre-check=0");
         // Set standard HTTP/1.0 no-cache header.
         response.setHeader("Pragma", "no-cache");
      }
      if (bolEsXML) {
         response.setContentType("text/xml;charset=UTF-8");
      } else {
         if(bolJson){
            response.setContentType("application/json; charset=UTF-8");
         }else{
            response.setContentType("text/html;charset=UTF-8");
         }         
      }
      try {
         request.setCharacterEncoding("UTF-8");
      } catch (UnsupportedEncodingException ex) {
         ex.printStackTrace();
      }
   }
}
