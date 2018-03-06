/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.restful;

import com.mx.siweb.erp.restful.entity.Payment;
import com.mx.siweb.erp.restful.entity.PaymentDeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
@Path("Payment")
public class PaymentResource {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(PaymentResource.class.getName());

    /**
     * Creates a new instance of PaymentResource
     */
    public PaymentResource() {
    }

    /**
     * Retrieves representation of an instance of com.mx.siweb.erp.restful.PaymentResource
     * @param strCodigo
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public Payment getJson(@DefaultValue("") @QueryParam("Codigo") String strCodigo) {
        Payment pmt = new Payment();
        //Accedemos a la base de datos para ver si tenemos acceso
        VariableSession varSesiones = new VariableSession(servletRequest);
        try{
            //Abrimos la conexión
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();
            //Se valida si se tiene acceso 
            EvalSesion eval = new EvalSesion();
            log.debug("Validando secion");
            if (eval.evaluaSesion(strCodigo, oConn)) {
                log.debug("Sesion Valida");
                if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
                    log.debug("Permiso Valido");
                    String strActivos = "select * from ecomm_payments where PAYMENT_ACTIVE = 1;";
                    ResultSet res1 = oConn.runQuery(strActivos);
                    while(res1.next()){
                        PaymentDeta pmtDeta = new PaymentDeta();
                        pmtDeta.setDescripcion(res1.getString("PAYMENT_DESC"));
                        pmtDeta.setTitulo(res1.getString("PAYMENT_TYPE"));
                        pmtDeta.setActivo(res1.getString("PAYMENT_ACTIVE"));
                        pmt.getPaymentDeta().add(pmtDeta);
                    }
                    pmt.setCodigo(strCodigo);
                }//Fin IF evalua Permiso
            }//Fin IF evaluaSesion
        }//Fin TRY
        catch (Exception e) {
            log.error("MobilServiceLogin1" + e.getMessage() + " " + e.getLocalizedMessage());
        }
        return pmt;
    }

    
    
}
