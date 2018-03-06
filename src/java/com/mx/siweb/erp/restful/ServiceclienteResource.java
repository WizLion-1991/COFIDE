/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import com.siweb.utilerias.json.JSONArray;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
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
 * @author ZeusGalindo
 */
@Path("servicecliente")
public class ServiceclienteResource {

   @Context
   private UriInfo context;
   @Context
   private HttpServletRequest servletRequest;
   @Context
   private javax.servlet.ServletContext servletContext;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ServiceclienteResource.class.getName());

   /**
    * Creates a new instance of ServiceclienteResource
    */
   public ServiceclienteResource() {

   }

   /**
    * Retrieves representation of an instance of
    * com.mx.siweb.erp.restful.ServiceclienteResource
    *
    * @return an instance of java.lang.String
    */
   @GET
   @Produces("application/json")
   public String getXml(@DefaultValue("") @QueryParam("Opcion") String strOpcion, @DefaultValue("") @QueryParam("Codigo") String strCodigo) {
      //Objeto json para almacenar los objetos
      JSONObject objJson = new JSONObject();
      //Evaluamos que sea una opcion valida
      if (strOpcion.isEmpty()) {
         try {
            objJson.put("resultado", "Opcion no valida especifique");
         } catch (JSONException ex) {
            log.error(ex.getMessage());
         }
         
      } else {
         //Accedemos a la base de datos para ver si tenemos acceso
         VariableSession varSesiones = new VariableSession(servletRequest);
         varSesiones.getVars();
         try {
            //Abrimos la conexion
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();

            //Validamos si tenemos acceso
            EvalSesion eval = new EvalSesion();
            if (eval.evaluaSesion(strCodigo, oConn)) {
               //Recuperamos la opcion indicada

               // <editor-fold defaultstate="collapsed" desc="Opcion 1 Enviamos los datos del cliente">
               if (strOpcion.equals("1")) {
                  //Mostramos los datos del cliente
                  String strNombre = "";
                  String strDir_Nombre = "";
                  String strDir_Telefono = "";
                  String strDir_Email = "";
                  String strDir_Calle = "";
                  String strDir_Numero = "";
                  String strDir_NumInt = "";
                  String strDir_Colonia = "";
                  String strDir_Estado = "";
                  String strDir_Municipio = "";
                  String strDir_Cp = "";
                  String strDir_Descripcion = "";
                  String strFecha_Ingreso = "";
                  String strRazonsocial = "";
                  int intDir_Id = 0;

                  //Recuperamos los datos del distribuidor a editar
                  String strSelectCliente = "select b.CDE_ID,a.CT_NOMBRE,a.CT_RAZONSOCIAL,b.CDE_NOMBRE,b.CDE_TELEFONO1,b.CDE_EMAIL,"
                          + "b.CDE_CALLE,b.CDE_NUMERO,b.CDE_NUMINT,b.CDE_COLONIA,b.CDE_ESTADO,b.CDE_MUNICIPIO,"
                          + "b.CDE_CP,b.CDE_DESCRIPCION,a.CT_FECHAREG from vta_cliente a left join vta_cliente_dir_entrega b on a.CT_ID = b.CT_ID  where a.CT_ID = " + varSesiones.getIntNoUser();
                  ResultSet rs = oConn.runQuery(strSelectCliente, true);
                  while (rs.next()) {
                     strNombre = rs.getString("CT_NOMBRE");
                     strDir_Nombre = rs.getString("CDE_NOMBRE");
                     strDir_Telefono = rs.getString("CDE_TELEFONO1");
                     strDir_Email = rs.getString("CDE_EMAIL");
                     strDir_Calle = rs.getString("CDE_CALLE");
                     strDir_Numero = rs.getString("CDE_NUMERO");
                     strDir_NumInt = rs.getString("CDE_NUMINT");
                     strDir_Colonia = rs.getString("CDE_COLONIA");
                     strDir_Estado = rs.getString("CDE_ESTADO");
                     strDir_Municipio = rs.getString("CDE_MUNICIPIO");
                     strDir_Cp = rs.getString("CDE_CP");
                     strDir_Descripcion = rs.getString("CDE_DESCRIPCION");
                     intDir_Id = rs.getInt("CDE_ID");
                     if (strDir_Nombre == null) {
                        strDir_Nombre = "";
                     }
                     if (strDir_Telefono == null) {
                        strDir_Telefono = "";
                     }
                     if (strDir_Email == null) {
                        strDir_Email = "";
                     }
                     if (strDir_Calle == null) {
                        strDir_Calle = "";
                     }
                     if (strDir_Numero == null) {
                        strDir_Numero = "";
                     }
                     if (strDir_NumInt == null) {
                        strDir_NumInt = "";
                     }
                     if (strDir_Colonia == null) {
                        strDir_Colonia = "";
                     }
                     if (strDir_Estado == null) {
                        strDir_Estado = "";
                     }
                     if (strDir_Municipio == null) {
                        strDir_Municipio = "";
                     }
                     if (strDir_Cp == null) {
                        strDir_Cp = "";
                     }
                     if (strDir_Descripcion == null) {
                        strDir_Descripcion = "";
                     }
                     strFecha_Ingreso = rs.getString("CT_FECHAREG");
                     strRazonsocial = rs.getString("CT_RAZONSOCIAL");
                  }
                  rs.close();
                  objJson.put("resultado", "OK");
                  JSONArray jsonChild = new JSONArray();
                  JSONObject objJsonCte = new JSONObject();
                  objJsonCte.put("Nombre", strNombre);
                  objJsonCte.put("Dir_Nombre", strDir_Nombre);
                  objJsonCte.put("Dir_Telefono", strDir_Telefono);
                  objJsonCte.put("Dir_Email", strDir_Email);
                  objJsonCte.put("Dir_Calle", strDir_Calle);
                  objJsonCte.put("Dir_Numero", strDir_Numero);
                  objJsonCte.put("Dir_NumInt", strDir_NumInt);
                  objJsonCte.put("Dir_Colonia", strDir_Colonia);
                  objJsonCte.put("Dir_Estado", strDir_Estado);
                  objJsonCte.put("Dir_Municipio", strDir_Municipio);
                  objJsonCte.put("Dir_Cp", strDir_Cp);
                  objJsonCte.put("Dir_Descripcion", strDir_Descripcion);
                  objJsonCte.put("Fecha_Ingreso", strFecha_Ingreso);
                  objJsonCte.put("Razonsocial", strRazonsocial);
                  objJsonCte.put("Dir_Id", intDir_Id);
                  jsonChild.put(objJsonCte);
                  objJson.put("clientes", jsonChild);

               }
               // </editor-fold>


               // <editor-fold defaultstate="collapsed" desc="Opcion 2 Enviamos los estados">
               // </editor-fold>

               // <editor-fold defaultstate="collapsed" desc="Opcion 3 Obtener las contrasenas">
               // </editor-fold>
            } else {
               objJson.put("resultado", "Opcion no valida especifique");
            }

            oConn.close();
         } catch (Exception ex) {
            this.log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
         }
      }
      return objJson.toString();
   }

   /**
    * PUT method for updating or creating an instance of ServiceclienteResource
    *
    * @param content representation for the resource
    * @return an HTTP response with content of the updated or created resource.
    */
   @PUT
   @Consumes("application/xml")
   public void putXml(String content) {
   }
}
