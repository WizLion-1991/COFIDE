/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comSIWeb.ContextoApt;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;

/**
 *Muestra mensaje de acceso restringido
 * @author zeus
 */
public class MsgSystem {

   public void MsgDenied(String strUser,HttpServletResponse response){
      String strError = "<font color=red>ACCESO RESTRINGIDO.FAVOR DE TECLEAR NUEVAMENTE SU ACCESO</font>";
      try {
         response.sendRedirect("index.jsp?strError=" + URLEncoder.encode(strError, "UTF-8"));/*checar el encode*/
      } catch (IOException ex) {
         Logger.getLogger(MsgSystem.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
