package comSIWeb.Operaciones;

import com.SIWeb.struts.SelEmpresaAction;
import comSIWeb.ContextoApt.CIP_Permiso;
import comSIWeb.ContextoApt.VariableSession;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.util.*;

/**
 *Manipula la presentacion del menu
 * @author zeus
 */
public class CIP_Menu {

   protected permisos_sistema perm;

   public CIP_Menu() {
      this.perm = new permisos_sistema();
   }

   /**
    * Dibuja el menu en html
   <h3><a href="#"><img src="images/layout/casa1.png" border="0"  alt=""><bean:message key="main.message5"/></a></h3>
   <div>
   <p class="ui-menu">
   <%if (varSesiones.getIntEsCaptura() == 0) {%><u><a href="javascript:OpnOpt('CUENTA')"><bean:message key="main.message6"/></a></u><br><%}%>
   <%if (varSesiones.getIntEsCaptura() == 0) {%><u><a href="javascript:OpnOpt('RESULTADOS')"><bean:message key="main.message10"/></a></u><br><%}%>
   <u><a href="index.jsp"><bean:message key="main.message11"/></a></u><br>
   </p>
   </div>
    * @param oConn Es la conexion a la base de datos
    * @param request Es la peticion http
    * @param response Es la respuesta http
    * @param varSesiones Contiene las variables de sesion del sistema
    * @return Regresa una cadena con el HTML generado
    */
   public String DrawMenu(Conexion oConn, HttpServletRequest request, HttpServletResponse response, VariableSession varSesiones) {
      return DrawMenu( oConn,  request,  response,  varSesiones,null);
   }
   /**
    * Dibuja el menu en html
   <h3><a href="#"><img src="images/layout/casa1.png" border="0"  alt=""><bean:message key="main.message5"/></a></h3>
   <div>
   <p class="ui-menu">
   <%if (varSesiones.getIntEsCaptura() == 0) {%><u><a href="javascript:OpnOpt('CUENTA')"><bean:message key="main.message6"/></a></u><br><%}%>
   <%if (varSesiones.getIntEsCaptura() == 0) {%><u><a href="javascript:OpnOpt('RESULTADOS')"><bean:message key="main.message10"/></a></u><br><%}%>
   <u><a href="index.jsp"><bean:message key="main.message11"/></a></u><br>
   </p>
   </div>
    * @param oConn Es la conexion a la base de datos
    * @param request Es la peticion http
    * @param response Es la respuesta http
    * @param varSesiones Contiene las variables de sesion del sistema
    * @param context
    * @return Regresa una cadena con el HTML generado
    */
   public String DrawMenu(Conexion oConn, HttpServletRequest request, HttpServletResponse response, VariableSession varSesiones,
           ServletContext context) {
      MessageResources messageResources = null;
      char chr10 = 10;
      SelEmpresaAction sel = new SelEmpresaAction();
      if (request != null) {
         messageResources = sel.getmessageResources(request);
      }
      //Obtenemos todos los permisos de este perfil
      ArrayList<Integer> lstMisPermisos = CIP_Permiso.getPermiso(varSesiones.getIntIdPerfil(), oConn);

      boolean bolINV = false;
      boolean bolCOMPRAS = false;
      boolean bolMLM = false;
      if(context != null){
         String  strModInv = context.getInitParameter("mod_Inventarios");
         if(strModInv == null)strModInv = "NO";
         String  strModCompras = context.getInitParameter("mod_Compras");
         if(strModCompras == null)strModCompras = "NO";
         String  strModMLM = context.getInitParameter("mod_MLM");
         if(strModMLM == null)strModMLM = "NO";
         if(strModInv.equals("SI"))bolINV = true;
         if(strModCompras.equals("SI"))bolCOMPRAS = true;
         if(strModMLM.equals("SI"))bolMLM = true;
      }
      //Obtenemos todos los permisos
      String strCondicion = " PS_ESMENU = 1 ";
      if(!bolINV){
         strCondicion += " AND PS_INV = 0 ";
      }
      if(!bolCOMPRAS){
         strCondicion += " AND PS_COMPRAS = 0 ";
      }
      if(!bolMLM){
         strCondicion += " AND PS_MLM = 0 ";
      }
      //Condicion para las empresas
      strCondicion += " AND if(EMP_ID = 0,1=1, EMP_ID = " + varSesiones.getIntIdEmpresa()  + ") ";
      strCondicion += " ORDER BY PS_SECCION,PS_ORDEN ";
      ArrayList<TableMaster> lst = this.perm.ObtenDatosVarios(strCondicion, oConn);
      Iterator<TableMaster> it = lst.iterator();
      String strHTML = "";
      boolean bolAbreSeccion = false;
      while (it.hasNext()) {
         TableMaster tb = it.next();
         String strMsg = "";
         String strImg = tb.getFieldString("PS_IMAGEN");
         String strLink = tb.getFieldString("PS_LINK");
         if (request != null) {
            strMsg = messageResources.getMessage(tb.getFieldString("PS_DESCRIPCION"), request);
         }
         /*Validamos si el usuario tiene permiso a esta seccion*/
         if ((lstMisPermisos.contains(tb.getFieldInt("PS_ID"))
                  && tb.getFieldInt("PS_ESPERMISO") == 1) || tb.getFieldInt("PS_ESPERMISO") == 0 || (
                  tb.getFieldInt("PS_SECCION") == 1 && tb.getFieldInt("PS_ORDEN") == 0/*LA SECCION 1 SIEMPRE SE PINTA*/)) {
            //Si es cero es un encabezado del menu
            if (tb.getFieldInt("PS_ORDEN") == 0) {
               if (bolAbreSeccion) {
                  strHTML += "</p>" + chr10;
                  strHTML += "</div>" + chr10;
               }
               bolAbreSeccion = true;
               strHTML += "<h3><a href=\"#\"><img src=\"" + strImg + "\" border=\"0\"  alt=\"\">" + strMsg + "</a></h3>" + chr10;
               strHTML += "<div>" + chr10;
               strHTML += "<p class=\"ui-menu\">" + chr10;
            } else {
               /*Validamos si el usuario tiene permiso a este menu*/
               strHTML += "<u><a href=\"" + strLink + "\">" + strMsg + "</a></u><br>" + chr10;
            }
         }

      }
      strHTML += "</div>";
      //System.out.println(strHTML);
      return strHTML;
   }
}
