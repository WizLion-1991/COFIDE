/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.Operaciones;

import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Utilerias.UtilXml;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

/**
 *Esta clase lee la base de datos y genera un XML para que se puedan generar pantallas
 * @author zeus
 */
public class CIP_Form {

   private UtilXml utilXML;
   private Bitacora bitacora;
   private HttpServletRequest request;
   /**
    * Constructor del objeto que representa los formularios que se dibujan en pantalla
    */
   public CIP_Form() {
      utilXML = new UtilXml();
      bitacora = new Bitacora();
   }

   /**
    * Nos da un xml con los datos para generar pantallas por DOM
    * @param strOpt Es la abreviatura de la pantalla
    * @param strId Es el Id del registro a recuperar
    * @param oConn Es la conexion a la base de datos
    * @param varSesiones Contiene las variables de sesion
    * @return Nos regresa un XML
    */
   public String DameCamposSc(String strOpt, String strId, Conexion oConn, VariableSession varSesiones) {
      char chr10 = 10;//Salto de linea
      /*Inicializamos el XML*/
      String strRes = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>  " + chr10;
      //Obtenemos el formulario
      Formularios form = new Formularios();
      form.setVarSesiones(varSesiones);
      form.ObtenDatos(strOpt, oConn);
      strRes += "<Screen ";
      strRes += form.getFieldPar();
      strRes += ">"  + chr10;
      /*Actualizamos el contador del formulario*/
      String strUpdate  = "update formularios set frm_numaccesos = frm_numaccesos + 1 where frm_abrv = '" + strOpt + "'";
      oConn.runQueryLMD(strUpdate);
      /*Recuperamos los datos de la tabla a editar*/
      boolean bolAbrio = false;
      ResultSet rs = null;
      if (!strId.equals("") && !form.getFieldString("frm_table").equals("") && !form.getFieldString("frm_key").equals("")) {
         String strSql = "select * from " + form.getFieldString("frm_table") + " where " + form.getFieldString("frm_key") + " = '" + strId + "' ";
         try {
            rs = oConn.runQuery(strSql);
            while (rs.next()) {
               bolAbrio = true;
            }
            rs.first();
         } catch (SQLException ex) {
            ex.printStackTrace();
         }
      }
      /*Obtenemos los datos de detalle del formulario*/
      FormulariosDeta form_deta = new FormulariosDeta();
      ArrayList<TableMaster> lst = form_deta.ObtenDatosVarios(" frm_id = " + form.getFieldInt("frm_id") + " order by frmd_orden", oConn);
      Iterator it = lst.iterator();
      while (it.hasNext()) {
         FormulariosDeta formDeta = (FormulariosDeta) it.next();
         strRes += "<ctrl ";
         strRes += formDeta.getFieldPar(rs, varSesiones, oConn,this.utilXML,this.request);
      }

      /*Ponemos los menus especiales con que cuenta la pantalla*/
      FormulariosMenuopt form_Menu = new FormulariosMenuopt();
      form_Menu.setVarSesiones(varSesiones);
      ArrayList<TableMaster> lstMenu = form_Menu.ObtenDatosVarios(" frm_id = " + form.getFieldInt("frm_id") + " order by frmn_orden", oConn);
      it = lstMenu.iterator();
      while (it.hasNext()) {
         FormulariosMenuopt formMenu = (FormulariosMenuopt) it.next();
         strRes += "<Menu ";
         strRes += formMenu.getFieldPar();
         strRes += "/>";
      }
      strRes += "</Screen>";
      //Cerramos resulset solo si se abrio
      if (bolAbrio) {
         try {
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
         } catch (SQLException ex) {
            ex.printStackTrace();
         }
      }
      return strRes;
   }

   /**
    * Nos regresa el objeto request
    * @return Nos regresa el objeto Request
    */
   public HttpServletRequest getRequest() {
      return request;
   }

   /**
    * Define el objeto request
    * @param request Es un objeto Request
    */
   public void setRequest(HttpServletRequest request) {
      this.request = request;
   }


}
