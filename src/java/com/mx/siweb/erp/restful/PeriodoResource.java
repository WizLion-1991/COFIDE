/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.restful;


import com.mx.siweb.erp.restful.entity.Periodo;
import com.mx.siweb.erp.restful.entity.PeriodoDeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
@Path("Periodo")
public class PeriodoResource {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(PeriodoResource.class.getName());

    /**
     * Creates a new instance of PeriodoResource
     */
    public PeriodoResource() {
    }

    /**
     * Retrieves representation of an instance of com.mx.siweb.erp.restful.PeriodoResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public Periodo getJson(@DefaultValue("") @QueryParam("Codigo") String strCodigo) {
        //TODO return proper representation object
        Periodo pr = new Periodo();

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
                    
                    String strConsulta = "select * from mlm_periodos;";
                    
                    log.debug(strConsulta);
                    ResultSet rs= oConn.runQuery(strConsulta);
                    while(rs.next()){
                        PeriodoDeta prDeta = new PeriodoDeta();
                        prDeta.setIdPeriodo(rs.getInt("MPE_ID"));
                        prDeta.setNombre(rs.getString("MPE_NOMBRE"));
                        prDeta.setFecInicial(rs.getString("MPE_FECHAINICIAL"));
                        prDeta.setFecFinal(rs.getString("MPE_FECHAFINAL"));
                        prDeta.setAbrv(rs.getString("MPE_ABRV"));
                        pr.setCodigo(strCodigo);
                        pr.getPeriodoItem().add(prDeta);
                    }//Fin WHILE
                    rs.close();
                    
                    
                }//Fin IF Evalua Permiso
            } //Fin IF Evalua Sesión
            else {
                log.error("Error:Access Denied");
                strResp = "Error:Access Denied";
            }
            pr.setCodigo(strResp);
            oConn.close();
        }//Fin TRY
        catch (Exception e) {
            log.error("MobilServiceLogin1" + e.getMessage() + " " + e.getLocalizedMessage());
        }
        return pr;

    }

    /**
     * PUT method for updating or creating an instance of PeriodoResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
