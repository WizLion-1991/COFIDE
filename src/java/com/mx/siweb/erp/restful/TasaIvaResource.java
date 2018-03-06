/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;


import com.mx.siweb.erp.restful.entity.TasaIva;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.sql.ResultSet;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.apache.logging.log4j.LogManager;
import com.mx.siweb.ui.web.Site;

/**
 * REST Web Service
 *
 * @author siweb
 */
@Path("TasaIva")
public class TasaIvaResource {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ProductosEcommResource.class.getName());

    /**
     * Creates a new instance of ProductosEcommResource
     */
    public TasaIvaResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.ProductosEcommResource
     *
     * @return an instance of java.lang.String
     */
    @GET
  
    @Produces({"application/json"})
    public TasaIva getJson( 
            @DefaultValue("") @QueryParam("Codigo") String strCodigo) {

        TasaIva tasaiva = new TasaIva();

//Accedemos a la base de datos para ver si tenemos acceso
        VariableSession varSesiones = new VariableSession(servletRequest);
        varSesiones.getVars();

        String strResp = "OK";
        String strFiltro = "";
       
        
        try {
            //Abrimos la conexion
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();

            //Validamos si tenemos acceso
            EvalSesion eval = new EvalSesion();
            if (eval.evaluaSesion(strCodigo, oConn)) {
                if (eval.evaluaPermiso(1, oConn)) {
                    Site webBase = new Site(oConn);
                    strFiltro = "SELECT * FROM ivatasa WHERE iva_id = 2";

                    ResultSet rs = oConn.runQuery(strFiltro);

                    while (rs.next()) {
                       
                        tasaiva.setIvaId(rs.getInt("IVA_ID"));
                        tasaiva.setIvaTasa(rs.getDouble("IVA_TASA"));
                        tasaiva.setDescripcionTasa(rs.getString("IVA_DESCRIPCION"));
                    }
                    rs.close();
                    //ciclo para recuperar todos los productos

                } else {
                    strResp = "ERROR:ACCESO DENEGADO";
                }
            } else {
                strResp = "Error:Access Denied";
            }
            oConn.close();
        } catch (Exception ex) {
            log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
        }

        return tasaiva;
    }
    
}
