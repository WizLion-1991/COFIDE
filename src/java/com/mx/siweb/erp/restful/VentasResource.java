/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import com.mx.siweb.erp.restful.entity.Ventas;
import com.mx.siweb.erp.restful.entity.VentasDeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.apache.logging.log4j.LogManager;

/**
 * REST Web Service
 *
 * @author siweb
 */
@Path("Ventas")
public class VentasResource {

   @Context
   private UriInfo context;
   @Context
   private HttpServletRequest servletRequest;
   @Context
   private javax.servlet.ServletContext servletContext;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(VentasResource.class.getName());

   /*
    * Creates a new instance of VentasResource
    */
   public VentasResource() {
   }

   /**
    * Retrieves representation of an instance of
    * com.mx.siweb.erp.restful.VentasResource
    *
    * @param strCodigo
    * @return an instance of java.lang.String
    */
   @GET
   @Produces("application/json")
   public Ventas getJSON(@DefaultValue("") @QueryParam("codigo") String strCodigo) {
      Ventas venta = new Ventas();
      
      //Accedemos a la base de datos para ver si tenemos acceso
      VariableSession varSessiones = new VariableSession(servletRequest);
      try {
         //Abrimos la conexion de base de datos
         Conexion oConn = new Conexion(varSessiones.getStrUser(), servletContext);
         oConn.open();
         //Validamos el acceso
         EvalSesion eval = new EvalSesion();
         String strResp = "OK";
         log.debug("Validando sesion");
         if (eval.evaluaSesion(strCodigo, oConn)) {
            log.debug("Sesion Valida");
            varSessiones.getVars();
            if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
               log.debug("Permiso Valido");

               int CT_ID = 0;
               CT_ID = eval.getIntIdUser();
               
               String Consulta1 = "select TKT_ID,TKT_FECHA, TKT_FOLIO, TKT_IMPORTE, TKT_IMPUESTO1,"
                       + "TKT_TOTAL, TKT_SALDO, TKT_IMPORTE_PUNTOS, TKT_IMPORTE_NEGOCIO "
                       + "from vta_tickets where CT_ID = " + CT_ID + ";";
               log.debug(Consulta1);
               ResultSet rs = oConn.runQuery(Consulta1);
               while (rs.next()) {

                  VentasDeta vendeta = new VentasDeta();
                  vendeta.setFecha(rs.getString("TKT_FECHA"));
                  vendeta.setFolio(rs.getString("TKT_FOLIO"));
                  vendeta.setImporte(rs.getDouble("TKT_IMPORTE"));
                  vendeta.setImpuesto1(rs.getDouble("TKT_IMPUESTO1"));
                  vendeta.setTotal(rs.getDouble("TKT_TOTAL"));
                  vendeta.setSaldo(rs.getDouble("TKT_SALDO"));
                  vendeta.setImporte_Puntos(rs.getDouble("TKT_IMPORTE_PUNTOS"));
                  vendeta.setImporte_Negocios(rs.getDouble("TKT_IMPORTE_NEGOCIO"));
                  vendeta.setTipoDoc("TICKET");
                  vendeta.setId(rs.getInt("TKT_ID"));
                  venta.setCodigo(strCodigo);
                  venta.getVentaItem().add(vendeta);

               }
               rs.close();

               String strConsulta2 = "select FAC_ID,FAC_FECHA, FAC_FOLIO, FAC_FOLIO, FAC_IMPORTE, FAC_IMPUESTO1,"
                       + " FAC_TOTAL, FAC_SALDO, FAC_IMPORTE_PUNTOS, FAC_IMPORTE_NEGOCIO "
                       + "from vta_facturas where CT_ID = " + CT_ID + ";";
               log.debug(strConsulta2);
               ResultSet rs2 = oConn.runQuery(strConsulta2);
               while (rs2.next()) {

                  VentasDeta vendeta = new VentasDeta();
                  vendeta.setFecha(rs2.getString("FAC_FECHA"));
                  vendeta.setFolio(rs2.getString("FAC_FOLIO"));
                  vendeta.setImporte(rs2.getDouble("FAC_IMPORTE"));
                  vendeta.setImpuesto1(rs2.getDouble("FAC_IMPUESTO1"));
                  vendeta.setTotal(rs2.getDouble("FAC_TOTAL"));
                  vendeta.setSaldo(rs2.getDouble("FAC_SALDO"));
                  vendeta.setImporte_Puntos(rs2.getDouble("FAC_IMPORTE_NEGOCIO"));
                  vendeta.setTipoDoc("FACTURA_cfdi");
                  vendeta.setId(rs2.getInt("FAC_ID"));
                  venta.setCodigo(strCodigo);

                  venta.getVentaItem().add(vendeta);
               }
               rs2.close();

               venta.setCT_ID(eval.getIntIdUser());
            }
         } else {
            log.error("Error:Access Denied");
            strResp = "Error:Access Denied";
         }
         venta.setCodigo(strResp);
         oConn.close();
      } catch (Exception e) {
         log.error("MobilServiceLogin1" + e.getMessage() + " " + e.getLocalizedMessage());
      }
      return venta;
   }
}