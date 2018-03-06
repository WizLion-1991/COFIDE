/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.restful;

import ERP.Monedas;
import com.SIWeb.struts.LoginAction;
import com.siweb.utilerias.json.JSONArray;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Sesiones;
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
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;

/**
 * REST Web Service
 *
 * @author siweb
 */
@Path("ObtenerParidad")
public class ObtenerParidadResource {
   
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ObtenerParidadResource.class.getName());
    
   public ObtenerParidadResource() {
   }

   /**
    * Retrieves representation of an instance of com.mx.siweb.erp.restful.ObtenerParidadResource
    * @return an instance of java.lang.String
    */
   @GET
   @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
   public String getJson(@DefaultValue("") @QueryParam("fecha") String strFecha,
          @DefaultValue("") @QueryParam("tipoConversion") String intConversion,
          @DefaultValue("") @QueryParam("monBase") String intMonBase,
          @DefaultValue("") @QueryParam("monFinal") String intMonFinal,
          @DefaultValue("") @QueryParam("Codigo") String strCodigo) {
   
        JSONObject objJson = new JSONObject();
        VariableSession varSesiones = new VariableSession(servletRequest);
        varSesiones.getVars();
        try{
           
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();
            EvalSesion eval = new EvalSesion();
            if(!strCodigo.equals("")){
            if (eval.evaluaSesion(strCodigo, oConn)) {
               Monedas mon = new Monedas(oConn);
               JSONArray jsonChild = new JSONArray();
               JSONObject jParidad= new JSONObject();
               double paridad= (mon.GetFactorConversion(strFecha,Integer.valueOf(intConversion),Integer.valueOf( intMonBase), Integer.valueOf(intMonFinal)));
               if(paridad!=0){
                  jParidad.put("Paridad",paridad );  
                  jsonChild.put(jParidad);
                  objJson.put("OK", jsonChild);
               }
               else{
                  objJson.put("ProblemaM", "La paridad es 0 o no fue encontrada en el proceso");
               }
            }
            else {
               objJson.put("ProblemaC", "No hubo conexion al evaluar el codigo");
            }
        }else{
               objJson.put("ProblemaC", "El codigo es nulo(vacio)");
            }
        }catch(JSONException  ex){
        log.debug("Errror con JSON >>>>>" + ex.getMessage());
        }
        catch(Exception px){
           log.debug("Oops! >>>>>>>>" + px.getMessage());
        }
       return objJson.toString();
   }

   /**
    * PUT method for updating or creating an instance of ObtenerParidadResource
    * @param content representation for the resource
    * @return an HTTP response with content of the updated or created resource.
    */
   @PUT
   @Consumes("application/json")
   public void putJson(String content) {
   }
}
