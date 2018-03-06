/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.restful;

import com.mx.siweb.erp.restful.entity.Estados;
import com.mx.siweb.erp.restful.entity.EstadosDeta;
import com.mx.siweb.erp.restful.entity.Paises;
import com.mx.siweb.erp.restful.entity.PaisesDeta;
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
@Path("Paises")
public class PaisesResource {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(PaisesResource.class.getName());

    /**
     * Creates a new instance of PaisesResource
     */
    public PaisesResource() {
    }

    /**
     * Retrieves representation of an instance of com.mx.siweb.erp.restful.PaisesResource
     * @param strCodigo
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/Paises")
    @Produces("application/json")
    public Paises getJson(@DefaultValue("") @QueryParam("Codigo") String strCodigo) {
        //TODO return proper representation object
        
        Paises ps = new Paises();
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
                if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
                    log.debug("Permiso Valido");
                    String consulta = "select * from paises";
                    ResultSet rs = oConn.runQuery(consulta);
                    while (rs.next()) {
                        PaisesDeta psDeta = new PaisesDeta();
                        psDeta.setidPais(rs.getInt("PA_ID"));
                        psDeta.setPais(rs.getString("PA_NOMBRE"));
                        ps.getPaisesItem().add(psDeta);
                    }//Fin WHILE
                    rs.close();
                    ps.setCodigo(strCodigo);
                } //Fin evalua Permiso
            }//Fin SESION VALIDA
            oConn.close();
        }//Fin TRY
        catch (Exception e) {
            log.error("MobilServiceLogin1" + e.getMessage() + " " + e.getLocalizedMessage());
        }
        return ps;
    }
    
    /**
     *
     * @param strPais
     * @return
     */
    @GET
    @Path("/Estados")
    @Produces("application/json")
    public Estados getJson(@DefaultValue("") @QueryParam("idPais") String strPais,
       @DefaultValue("") @QueryParam("Codigo") String strCodigo) {

        Estados est = new Estados();
        VariableSession varSesiones = new VariableSession(servletRequest);
        Integer idPais =0;
        try{
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
          oConn.open();
          //Validamos Acceso
          EvalSesion eval = new EvalSesion();
          String strResp = "OK";
          log.debug("Validando Sesión");
          if(eval.evaluaSesion(strCodigo, oConn)){
              log.debug("Sesion Valida");
              varSesiones.getVars();
              if(eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()){
                  log.debug("Permiso Valido");
                  String consultaEstados = "select * from estadospais where PA_ID = " + strPais + ";";
                  ResultSet rs1 = oConn.runQuery(consultaEstados);
                  while(rs1.next()){
                      EstadosDeta estDeta = new EstadosDeta();
                      estDeta.setIdEstado(rs1.getInt("ESP_ID"));
                      estDeta.setEstado(rs1.getString("ESP_NOMBRE"));
                      est.getEstadosItem().add(estDeta);
                  }//Fin WHILE2
                  rs1.close();
              }//Fin Evalua Permiso
          }//Fin Evalua Sesion
            else {
            log.error("Error:Access Denied");
            strResp = "Error:Access Denied";
         }
          est.setCodigo(strCodigo);
          oConn.close();
          
        }//Fin TRY
        catch (Exception e) {
         log.error("MobilServiceLogin1" + e.getMessage() + " " + e.getLocalizedMessage());
      }
        return est;
    }
}
