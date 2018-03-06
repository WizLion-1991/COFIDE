/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import com.mx.siweb.erp.restful.entity.Flete;
import com.mx.siweb.erp.restful.entity.FleteDeta;
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
@Path("Flete")
public class FleteResource {

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
    public FleteResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.ProductosEcommResource
     *
     * @return an instance of java.lang.String
     */
    @GET

    @Produces({"application/json"})
    public Flete getJson(
            @DefaultValue("") @QueryParam("Codigo") String strCodigo) {

        Flete flete = new Flete();

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
                if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
                    Site webBase = new Site(oConn);
                    strFiltro = "SELECT * FROM vta_transportista";
                    ResultSet rs = oConn.runQuery(strFiltro);

                    while (rs.next()) {
                        FleteDeta fletedeta = new FleteDeta();

                        fletedeta.setTransporteId(rs.getInt("TR_ID"));
                        fletedeta.setTransportista(rs.getString("TR_TRANSPORTISTA"));
                        fletedeta.setImporte(rs.getDouble("TR_IMPORTE"));

                        flete.getLstItems().add(fletedeta);
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

        return flete;
    }

}
