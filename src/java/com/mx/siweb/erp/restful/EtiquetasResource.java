/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import com.mx.siweb.erp.restful.entity.Etiquetas;
import com.mx.siweb.erp.restful.entity.EtiquetasList;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.apache.logging.log4j.LogManager;

/**
 * REST Web Service
 *
 * @author ZeusSIWEB
 */
@Path("Etiquetas")
public class EtiquetasResource {

   @Context
   private UriInfo context;
   @Context
   private HttpServletRequest servletRequest;
   @Context
   private javax.servlet.ServletContext servletContext;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(EtiquetasResource.class.getName());

   /**
    * Creates a new instance of EtiquetasResource
    */
   public EtiquetasResource() {
   }

   /**
    * Retrieves representation of an instance of
    * com.mx.siweb.erp.restful.EtiquetasList
    *
    * @param strCodigo Es el codigo de la sesion
    * @return an instance of java.lang.String
    */
   @GET
   @Path("/Etiquetaslist")
   @Produces("application/json")
   public EtiquetasList getJson(@DefaultValue("") @QueryParam("Codigo") String strCodigo) {
      EtiquetasList etiquetaList = new EtiquetasList();
      VariableSession varSesiones = new VariableSession(servletRequest);

      try {
         //Abrimos la conexión
         Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();
         //Se valida si se tiene acceso 
         EvalSesion eval = new EvalSesion();
         log.debug("Validando secion");
         if (eval.evaluaSesion(strCodigo, oConn)) {
            log.debug("Secion Valida");
            varSesiones.getVars();
            //Validamos el permiso
            if (eval.evaluaPermiso(464, oConn) ) {
               log.debug("Permiso Valido");
               String consulta = "select * from vta_etiquetas";
               ResultSet rs = oConn.runQuery(consulta);
               while (rs.next()) {
                  Etiquetas deta = new Etiquetas();
                  deta.setId(rs.getInt("ET_ID"));
                  deta.setDescripcion(rs.getString("ET_DESCRIPCION"));
                  deta.setRepId(rs.getInt("REP_ID"));
                  deta.setRepIdOdc(rs.getInt("ET_REP_ODC"));
                  etiquetaList.getEtiquetasItem().add(deta);
               }//Fin WHILE
               ////rs.getStatement().close();
               rs.close();
            } //Fin evalua Permiso
         }//Fin SESION VALIDA
         oConn.close();
      }//Fin TRY
      catch (Exception e) {
         log.error("Etiquetas resources...." + e.getMessage() + " " + e.getLocalizedMessage());
      }
      return etiquetaList;
   }

   /**
    * Retrieves an txt content
    *
    * @param strCodigo Es el codigo de la sesion
    * @param strCodigoSku Es el codigo sku del producto
    * @param strCantidad Es la cantidad
    * @param strEtiqueta Es el id de la etiquetax
    * @return an instance of java.lang.String
    */
   @GET
   @Path("/EtiquetasTxt")
   @Produces("application/txt")
   public String getTxt(
      @DefaultValue("") @QueryParam("Codigo") String strCodigo, 
      @DefaultValue("") @QueryParam("SKU") String strCodigoSku, 
      @DefaultValue("") @QueryParam("Cantidad") String strCantidad, 
      @DefaultValue("") @QueryParam("Etiqueta") String strEtiqueta
   ) {
      String strEtiquetaTxt = "";
      VariableSession varSesiones = new VariableSession(servletRequest);
      try {
         //Abrimos la conexión
         Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();
         //Se valida si se tiene acceso 
         EvalSesion eval = new EvalSesion();
         log.debug("Validando sesion");
         //Validamos el permiso
         if (eval.evaluaSesion(strCodigo, oConn)) {
            log.debug("Sesion Valida");
            varSesiones.getVars();
            if (eval.evaluaPermiso(466, oConn) ) {
               log.debug("strCodigoSku:" + strCodigoSku);
               log.debug("strCantidad:" + strCantidad);
               log.debug("strEtiqueta:" + strEtiqueta);
               ERP.Etiquetas etEtiqueta = new ERP.Etiquetas();
               strEtiquetaTxt = etEtiqueta.doEtiquetaProducto(strCodigoSku, Integer.valueOf(strCantidad), Integer.valueOf(strEtiqueta), oConn);
            } //Fin evalua Permiso
         }//Fin SESION VALIDA
         oConn.close();
      }//Fin TRY
      catch (Exception e) {
         log.error("Etiquetas resources...." + e.getMessage() + " " + e.getLocalizedMessage());
      }
      return strEtiquetaTxt;
   }
   /**
    * Retrieves an txt content
    *
    * @param strCodigo Es el codigo de la sesion
    * @param strEmpresa Es la empresa que solicita el movimiento
    * @param strBodega Es la bodega
    * @param strNumRecepcion Es el numero de recepcion  o entrada
    * @param strEtiqueta Es el id de la etiqueta
    * @return an instance of java.lang.String
    */
   @GET
   @Path("/EtiquetasTxtRecepcion")
   @Produces("application/txt")
   public String getTxtRecepcion(
      @DefaultValue("") @QueryParam("Codigo") String strCodigo, 
      @DefaultValue("") @QueryParam("Empresa") String strEmpresa, 
      @DefaultValue("") @QueryParam("Bodega") String strBodega, 
      @DefaultValue("") @QueryParam("NumRecepcion") String strNumRecepcion, 
      @DefaultValue("") @QueryParam("Etiqueta") String strEtiqueta
   ) {
      String strEtiquetaTxt = "";
      VariableSession varSesiones = new VariableSession(servletRequest);
      try {
         //Abrimos la conexión
         Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();
         //Se valida si se tiene acceso 
         EvalSesion eval = new EvalSesion();
         log.debug("Validando sesion");
         //Validamos el permiso
         if (eval.evaluaSesion(strCodigo, oConn)) {
            log.debug("Sesion Valida");
            varSesiones.getVars();
            if (eval.evaluaPermiso(466, oConn) ) {
               log.debug("strEmpresa:" + strEmpresa);
               log.debug("strBodega:" + strBodega);
               log.debug("strNumRecepcion:" + strNumRecepcion);
               log.debug("strEtiqueta:" + strEtiqueta);
               ERP.Etiquetas etEtiqueta = new ERP.Etiquetas();
               strEtiquetaTxt = etEtiqueta.doEtiquetaODC(Integer.valueOf(strEtiqueta), Integer.valueOf(strEmpresa),
                  Integer.valueOf(strBodega), Integer.valueOf(strNumRecepcion), oConn);
            } //Fin evalua Permiso
         }//Fin SESION VALIDA
         oConn.close();
      }//Fin TRY
      catch (Exception e) {
         log.error("Etiquetas resources...." + e.getMessage() + " " + e.getLocalizedMessage());
      }
      return strEtiquetaTxt;
   }
   /**
    * Retrieves an txt content
    *
    * @param strCodigo Es el codigo de la sesion
    * @param strEmpresa Es la empresa que solicita el movimiento
    * @param strBodega Es la bodega
    * @param strNumODC Es el numero de recepcion  o entrada
    * @param strEtiqueta Es el id de la etiqueta
    * @return an instance of java.lang.String
    */
   @GET
   @Path("/EtiquetasTxtODC")
   @Produces("application/txt")
   public String getTxtODC(
      @DefaultValue("") @QueryParam("Codigo") String strCodigo, 
      @DefaultValue("") @QueryParam("Empresa") String strEmpresa, 
      @DefaultValue("") @QueryParam("Bodega") String strBodega, 
      @DefaultValue("") @QueryParam("NumODC") String strNumODC, 
      @DefaultValue("") @QueryParam("Etiqueta") String strEtiqueta
   ) {
      String strEtiquetaTxt = "";
      VariableSession varSesiones = new VariableSession(servletRequest);
      try {
         //Abrimos la conexión
         Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();
         //Se valida si se tiene acceso 
         EvalSesion eval = new EvalSesion();
         log.debug("Validando sesion");
         //Validamos el permiso
         if (eval.evaluaSesion(strCodigo, oConn)) {
            log.debug("Sesion Valida");
            varSesiones.getVars();
            if (eval.evaluaPermiso(466, oConn) ) {
               log.debug("strNumODC:" + strNumODC);
               log.debug("strEtiqueta:" + strEtiqueta);
               ERP.Etiquetas etEtiqueta = new ERP.Etiquetas();
               strEtiquetaTxt = etEtiqueta.doEtiquetaCompra(Integer.valueOf(strNumODC), Integer.valueOf(strEtiqueta), oConn);
            } //Fin evalua Permiso
         }//Fin SESION VALIDA
         oConn.close();
      }//Fin TRY
      catch (Exception e) {
         log.error("Etiquetas resources...." + e.getMessage() + " " + e.getLocalizedMessage());
      }
      return strEtiquetaTxt;
   }
}
