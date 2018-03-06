/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import com.siweb.utilerias.json.JSONArray;
import com.siweb.utilerias.json.JSONObject;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import java.sql.SQLException;
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
 * @author siwebmx5
 */
@Path("DatProveedores")
public class DatProveedoresResource {

    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(DatProveedoresResource.class.getName());

    /**
     * Creates a new instance of DatProveedoresResource
     */
    public DatProveedoresResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.DatProveedoresResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String getJson(@DefaultValue("") @QueryParam("Opcion") String strOpcion,
            @DefaultValue("") @QueryParam("Empresa") String strEmpresa,
            @DefaultValue("") @QueryParam("Sucursal") String strSucursal,
            @DefaultValue("") @QueryParam("Codigo") String strCodigo) {
        JSONObject objJson = new JSONObject();
        VariableSession varSesiones = new VariableSession(servletRequest);
        varSesiones.getVars();
        try {
            //Abrimos la conexion
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();

            //Validamos si tenemos acceso
            EvalSesion eval = new EvalSesion();
            if (eval.evaluaSesion(strCodigo, oConn)) {
                if (strOpcion.equals("1")) {
                    JSONArray jsonChild = new JSONArray();
                    String strConsulta = "SELECT * "
                            + " FROM vta_proveedor"
                            + " WHERE SC_ID = " + strSucursal
                            + " AND EMP_ID = " + strEmpresa;
                    try {
                        ResultSet rs = oConn.runQuery(strConsulta);
                        while (rs.next()) {
                            JSONObject objJsonCte = new JSONObject();
                            objJsonCte.put("ID_PROV", rs.getString("PV_ID"));
                            objJsonCte.put("NOMBRE", rs.getString("PV_RAZONSOCIAL"));
                            objJsonCte.put("DIAS_PROD", rs.getString("PV_DIAS_PROD"));
                            objJsonCte.put("DIAS_TRAN", rs.getString("PV_DIAS_TRANSITO"));
                            jsonChild.put(objJsonCte);
                        }
                        rs.close();                        
                        objJson.put("productos_datos", jsonChild);
                        //objJson.put("resultado", "OK");

                    } catch (SQLException ex) {
                        ex.fillInStackTrace();
                    }
                }
                else {
                    objJson.put("resultado", "Opcion no valida especifique");
                }
            } else {
                objJson.put("resultado", "No es valido el codigo ");
            }

            oConn.close();
        } catch (Exception ex) {
            this.log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
        }
        return objJson.toString();
    }

    /**
     * PUT method for updating or creating an instance of DatProveedoresResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
