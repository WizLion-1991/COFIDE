/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.Utilerias;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;

/**
 * Clase que administra las variables de sesion
 *
 * @author zeus
 */
public class Sesiones {

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Sesiones.class.getName());

   /**
    * Esta funcion identifica las variable de sesion usuario
    *
    * @param request Enviamos el objeto request para recupera las variables de
    * sesion
    * @param strNomVar Es el nombre de la variable sesion que deseamos recuperar
    * @return Regresa el valor de la variable de sesion
    */
   public static String gerVarSession(HttpServletRequest request, String strNomVar) {

      // Obtenemos el objeto sesion
      HttpSession session = request.getSession(true);
      String strVarSesion = "";

      strVarSesion = (String) session.getAttribute(strNomVar);

      // Validamos si no esta definido la variable de session
      if (strVarSesion == null || strVarSesion.equals("")) {
         session.setAttribute(strNomVar, "0");
         strVarSesion = (String) session.getAttribute(strNomVar);
      }

      return strVarSesion;
   }

   /**
    * Limpiamos una variable de sesion
    *
    * @param request Es el objeto request del Servlet
    * @param strNomVar Es el nombre de la variable sesion que deseamos limpiar
    */
   public static void CleanSession(HttpServletRequest request, String strNomVar) {

      // Obtenemos el objeto sesion
      HttpSession session = request.getSession(true);
      String strVarSesion;

      strVarSesion = (String) session.getAttribute(strNomVar);

      // Validamos si no esta definido el usuario para crear la variable de sesion
      if (strVarSesion == null) {
         session.setAttribute(strNomVar, "0");
      } else {
         session.removeAttribute(strNomVar);
         session.setAttribute(strNomVar, "0");
      }
   }

   /**
    * Definimos el valor de una variable de sesion
    *
    * @param request Es el objeto request del Servlet
    * @param strNomVar Es el nombre de la variable de sesion
    * @param strValor Es el valor de la variable sesion
    */
   public static void SetSession(HttpServletRequest request, String strNomVar, String strValor) {

      if (request != null) {
         // Obtenemos el objeto sesion
         HttpSession session = request.getSession(true);
         String strVarSesion;

         strVarSesion = (String) session.getAttribute(strNomVar);

         // Validamos si no esta definido el usuario para crear la variable de sesion
         if (strVarSesion == null) {
            session.setAttribute(strNomVar, strValor);
         } else {
            session.removeAttribute(strNomVar);
            session.setAttribute(strNomVar, strValor);
         }
      } else {
         log.error("!!!!No esta definida el objeto request----!!!!!!");
      }

   }
}
