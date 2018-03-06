/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.usuarios_accesos;
import comSIWeb.Utilerias.Fechas;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.apache.logging.log4j.LogManager;

/**
 * REST Web Service
 *
 * @author siwebmx5
 */
@Path("MobilserviceExit")
public class MobilserviceExit {

   @Context
   private HttpServletRequest servletRequest;
   @Context
   private javax.servlet.ServletContext servletContext;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(MobilserviceExit.class.getName());

   /**
    * Creates a new instance of MobilserviceExit
    */
   public MobilserviceExit() {
   }

   /**
    * Retrieves representation of an instance of
    * com.mx.siweb.erp.restful.MobilserviceExit
    *
    * @return an instance of java.lang.String
    */
   @GET
   @Produces("application/json")
   public String getJson(
           @DefaultValue("") @QueryParam("Codigo") String strCodigo) {
      //Objeto json para almacenar los objetos
      JSONObject objJson = new JSONObject();

      //Objeto para validar la seguridad

      VariableSession varSesiones = new VariableSession(servletRequest);
      varSesiones.getVars();
      String strUserNameLast = varSesiones.getStrUser();
      int intIdUsuario = varSesiones.getIntNoUser();
      int intIdCliente = varSesiones.getintIdCliente();
      varSesiones.SetVars(0, 0, 0, 0, "", "", 0, "", 0);

      try {
         //Abrimos la conexion
         Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();

         if (!strCodigo.isEmpty()) {
            //Limpiamos ambos objetos
            String strUpdate = "update usuarios set IS_LOGGED=0 where LASTSESSIONID =  '" + strCodigo + "'";
            oConn.runQueryLMD(strUpdate);
            strUpdate = "update vta_cliente set CT_IS_LOGGED=0 where CT_LASTSESSIONID =  '" + strCodigo + "'";
            oConn.runQueryLMD(strUpdate);
         } else {
            if (intIdCliente == 0) {
               String strUpdate = "update usuarios set IS_LOGGED=0 where id_usuarios =  " + intIdUsuario;
               oConn.runQueryLMD(strUpdate);
            } else {
               //Limpiamos cliente
               String strUpdate = "update vta_cliente set CT_IS_LOGGED=0 where CT_ID =  " + intIdCliente;
               oConn.runQueryLMD(strUpdate);
            }
         }


         //Guardamos bitacora de acceso
         Fechas fecha = new Fechas();
         java.util.Date today = (java.util.Date) Calendar.getInstance().getTime();
         usuarios_accesos bitaAcceso = new usuarios_accesos();
         bitaAcceso.setFieldInt("id_usuario", intIdUsuario);
         bitaAcceso.setFieldString("seg_nombre_user", strUserNameLast);
         bitaAcceso.setFieldString("seg_fecha", fecha.getFechaActual());
         bitaAcceso.setFieldString("seg_hora", fecha.getHoraActualHHMMSS());
         bitaAcceso.setFieldString("seg_ip_remote", servletRequest.getRemoteAddr());
         bitaAcceso.setFieldString("seg_computer_name", servletRequest.getRemoteHost());
         bitaAcceso.setFieldString("seg_session_id", servletRequest.getSession().getId());
         bitaAcceso.setFieldLong("seg_date", today.getTime());
         bitaAcceso.setFieldInt("seg_total_time", 0);
         bitaAcceso.setFieldInt("seg_tipo_ini_fin", 99);
         //Guardamos estadistica
         bitaAcceso.Agrega(oConn);
         objJson.put("Status", "OK");
         oConn.close();
      } catch (Exception ex) {
         try {
            objJson.put("Status", "Error");
         } catch (JSONException ex1) {
            this.log.error("MobilServiceLogout" + ex.getMessage() + " " + ex.getLocalizedMessage());
         }
         this.log.error("MobilServiceLogout" + ex.getMessage() + " " + ex.getLocalizedMessage());
      }
      return objJson.toString();
   }

   /**
    * PUT method for updating or creating an instance of MobilserviceExit
    *
    * @param content representation for the resource
    * @return an HTTP response with content of the updated or created resource.
    */
   @PUT
   @Consumes("application/json")
   public void putJson(String content) {
   }
}
