/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.ui.web;

import com.SIWeb.struts.SelEmpresaAction;
import comSIWeb.ContextoApt.CIP_Permiso;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.CIP_Menu;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.util.MessageResources;

/**
 *Nos ayuda a generar el menu superFish
 * @author aleph_79
 */
public class SuperFishMenu extends CIP_Menu {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private boolean bolMostrarIdPermiso = false;

   /**
    *Regresa si se muestra el id del permiso
    * @return
    */
   public boolean isBolMostrarIdPermiso() {
      return bolMostrarIdPermiso;
   }

   /**
    *Define si se muestra el id de los permisos
    * @param bolMostrarIdPermiso
    */
   public void setBolMostrarIdPermiso(boolean bolMostrarIdPermiso) {
      this.bolMostrarIdPermiso = bolMostrarIdPermiso;
   }
   
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public SuperFishMenu() {
      super();
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    * Dibuja el menu en html para los estilos de superfish
   
    * 
   <li class="current">
   <a href="#a">Inicio</a>
   <ul>
   <li>
   <a href="#aa">Configuracion Global</a>
   </li>
   <li>
   <a href="#aa">Cambiar contraseña</a>
   </li>
   
   </ul>
   </li>
    * 
    * @param oConn Es la conexion a la base de datos
    * @param request Es la peticion http
    * @param response Es la respuesta http
    * @param varSesiones Contiene las variables de sesion del sistema
    * @param context
    * @return Regresa una cadena con el HTML generado
    */
   @Override
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
      if (context != null) {
         String strModInv = context.getInitParameter("mod_Inventarios");
         if (strModInv == null) {
            strModInv = "NO";
         }
         String strModCompras = context.getInitParameter("mod_Compras");
         if (strModCompras == null) {
            strModCompras = "NO";
         }
         String strModMLM = context.getInitParameter("mod_MLM");
         if (strModMLM == null) {
            strModMLM = "NO";
         }
         if (strModInv.equals("SI")) {
            bolINV = true;
         }
         if (strModCompras.equals("SI")) {
            bolCOMPRAS = true;
         }
         if (strModMLM.equals("SI")) {
            bolMLM = true;
         }
      }
      //Obtenemos todos los permisos
      String strCondicion = " PS_ESMENU = 1 ";
      if (!bolINV) {
         strCondicion += " AND PS_INV = 0 ";
      }
      if (!bolCOMPRAS) {
         strCondicion += " AND PS_COMPRAS = 0 ";
      }
      if (!bolMLM) {
         strCondicion += " AND PS_MLM = 0 ";
      }
      if (oConn.getStrDriverName().contains("MySQL")) {
         strCondicion += " AND if(EMP_ID = 0,1=1, EMP_ID = " + varSesiones.getIntIdEmpresa()  + ") ";
      }else{
         if (oConn.getStrDriverName().contains("jTDS")) {
            strCondicion += " AND EMP_ID =(CASE WHEN EMP_ID = 0 THEN 0 ELSE " + varSesiones.getIntIdEmpresa()  + " END)  ";
         }else{
            //Hay que implementarlo para otros lenguajes...
         }
      }
      strCondicion += " ORDER BY PS_SECCION,PS_SUBSECCION,PS_ORDEN ";
      ArrayList<TableMaster> lst = this.perm.ObtenDatosVarios(strCondicion, oConn);
      Iterator<TableMaster> it = lst.iterator();
      String strHTML = "";
      boolean bolAbreSeccion = false;
      int intSubSeccionActual = 0;
      while (it.hasNext()) {
         TableMaster tb = it.next();
         String strMsg = "";
         String strImg = tb.getFieldString("PS_IMAGEN");
         String strLink = tb.getFieldString("PS_LINK");
         String strMsgUser = tb.getFieldString("PS_ALTERNO");
         if (!strMsgUser.isEmpty()) {
            strMsg = strMsgUser;
         } else {
            if (request != null) {
               strMsg = messageResources.getMessage(tb.getFieldString("PS_DESCRIPCION"), request);
            }
         }
         if(bolMostrarIdPermiso){
            strMsg = tb.getFieldInt("PS_ID") + ".-" + strMsg;
         }
         /*Validamos si el usuario tiene permiso a esta seccion*/
         if ((lstMisPermisos.contains(tb.getFieldInt("PS_ID"))
                 && tb.getFieldInt("PS_ESPERMISO") == 1)
                 || tb.getFieldInt("PS_ESPERMISO") == 0
                 || (tb.getFieldInt("PS_SECCION") == 1 && tb.getFieldInt("PS_ORDEN") == 0/*LA SECCION 1 SIEMPRE SE PINTA*/)) {
            //Si es cero es un encabezado del menu
            if (tb.getFieldInt("PS_ORDEN") == 0) {
               //Si la subseccion de la seccion anterior no se cerro la cerramos
               if (tb.getFieldInt("PS_SUBSECCION") != intSubSeccionActual) {
                  if (intSubSeccionActual != 0) {
                     //Cerramos la subseccion
                     strHTML += "</ul>" + chr10;
                     strHTML += "</li>" + chr10;
                     intSubSeccionActual = 0;
                  }
               }
               //Si ya esta abierta una seccion la cerramos
               if (bolAbreSeccion) {
                  strHTML += "</ul>" + chr10;
                  strHTML += "</li>" + chr10;
               }
               bolAbreSeccion = true;
               strHTML += "<li >" + chr10;
               strHTML += "<a href=\"#a\"><i class=\"" + strImg + "\"></i>&nbsp;" + strMsg + "</a>" + chr10;
               strHTML += "<ul>" + chr10;
            } else {
               boolean bolPintaMnuNormal = true;
               //Validamos si inicia una subseccion
               if (tb.getFieldInt("PS_SUBSECCION") != intSubSeccionActual) {
                  if (intSubSeccionActual != 0) {
                     //Cerramos la subseccion
                     strHTML += "</ul>" + chr10;
                     strHTML += "</li>" + chr10;
                  }

                  //Abrimos nueva subseccion solo si es diferente de cero
                  if (tb.getFieldInt("PS_SUBSECCION") != 0) {
                     //Validamos si el usuario tiene acceso a un permiso de esta subseccion
                     if (EvalAccess(varSesiones.getIntIdPerfil(), tb.getFieldInt("PS_SUBSECCION"), oConn)) {
                        strHTML += "<li class=\"current\">" + chr10;
                        strHTML += "<a href=\"#a\">" + strMsg + "</a>" + chr10;
                        strHTML += "<ul>" + chr10;
                        bolPintaMnuNormal = false;
                        intSubSeccionActual = tb.getFieldInt("PS_SUBSECCION");
                     } else {
                        bolPintaMnuNormal = false;
                        intSubSeccionActual = 0;
                     }

                  } else {
                     intSubSeccionActual = tb.getFieldInt("PS_SUBSECCION");
                  }
               }
               //Pintamos el menu
               if (bolPintaMnuNormal) {
                  strHTML += "<li>" + chr10;
                  strHTML += "<a href=\"" + strLink + "\">" + strMsg + "</a>" + chr10;
                  strHTML += "</li>" + chr10;
               }
            }
         }

      }
      return strHTML;
   }

   /**
    * Evalua si el perfil tiene acceso a la subseccion
    * @param intPerfilId Es el id del perfil
    * @param intSubseccion Es el id de la subseccion
    * @param oConn Es la conexion
    * @return Regresa treu si tenemos acceso a la subseccion
    */
   public boolean EvalAccess(int intPerfilId, int intSubseccion, Conexion oConn) {
      boolean bolAcceso = false;
      String strSql = "SELECT COUNT(perfiles_permisos.PS_ID) as cuantos FROM perfiles_permisos,permisos_sistema "
              + " where perfiles_permisos.PS_ID = permisos_sistema.PS_ID and "
              + "PF_ID = " + intPerfilId + " AND "
              + "PS_SUBSECCION = " + intSubseccion;
      ResultSet rs2;
      try {
         rs2 = oConn.runQuery(strSql, true);
         while (rs2.next()) {
            int intCuantos = rs2.getInt("cuantos");
            if (intCuantos > 0) {
               bolAcceso = true;
            }
         }
         if(rs2.getStatement() != null )rs2.getStatement().close(); rs2.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }
      return bolAcceso;
   }
   // </editor-fold>
}
