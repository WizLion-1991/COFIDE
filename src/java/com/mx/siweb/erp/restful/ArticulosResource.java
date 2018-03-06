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
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
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
 * @author siwebmx5
 */
@Path("Articulos")
public class ArticulosResource {

    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    org.apache.logging.log4j.Logger log =  LogManager.getLogger(ArticulosResource.class.getName());


    /**
     * Creates a new instance of ArticulosResource
     */
    public ArticulosResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.ArticulosResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson(@DefaultValue("") @QueryParam("Opcion") String strOpcion,
            @DefaultValue("") @QueryParam("Empresa") String strEmpresa,
            @DefaultValue("") @QueryParam("Sucursal") String strSucursal,
            @DefaultValue("") @QueryParam("CodigoPR") String strCodigoPR,
            @DefaultValue("") @QueryParam("Codigo") String strCodigo) {

        JSONObject objJson = new JSONObject();

        VariableSession varSesiones = new VariableSession(servletRequest);
        varSesiones.getVars();
        try {
            //Abrimos la conexion
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();
            String strAnexo ="";
            //Validamos si tenemos acceso
            EvalSesion eval = new EvalSesion();
            if (eval.evaluaSesion(strCodigo, oConn)) {
                if (strOpcion.equals("1")) {
                    JSONArray jsonChild = new JSONArray();
                    
                    if(!strCodigoPR.equals(""))
                    {
                        strAnexo = " AND PR_CODIGO = '"+strCodigoPR+"'";
                    }
                    String strConsulta = "SELECT * "
                            + " FROM vta_producto"
                            + " WHERE SC_ID = " + strSucursal
                            + " AND EMP_ID = " + strEmpresa
                            + strAnexo;
                    
                    try {
                        ResultSet rs = oConn.runQuery(strConsulta);
                        while (rs.next()) {
                            JSONObject objJsonCte = new JSONObject();
                            objJsonCte.put("PR_ID", rs.getString("PR_ID"));
                            objJsonCte.put("PR_CODIGO", rs.getString("PR_CODIGO"));
                            objJsonCte.put("PR_DESCRIPCION", rs.getString("PR_DESCRIPCIONCORTA"));
                            objJsonCte.put("PR_MAXIMA", rs.getString("PR_MAXIMA"));
                            objJsonCte.put("PR_MINIMA", rs.getString("PR_MINIMA"));
                            objJsonCte.put("PR_RESURTIDO", rs.getString("PR_RESURTIDO"));
                            objJsonCte.put("PR_NUM_EMPAQUES", rs.getString("PR_NUM_EMPAQUES"));
                            objJsonCte.put("PR_ORDEN_MINIMA", rs.getString("PR_ORDEN_MINIMA"));
                            objJsonCte.put("PR_PORCENTAJE_SEG", rs.getString("PR_PORCENTAJE_SEG"));
                            objJsonCte.put("PR_CATEGORIA1", rs.getString("PR_CATEGORIA1"));
                            objJsonCte.put("PR_CATEGORIA2", rs.getString("PR_CATEGORIA2"));
                            objJsonCte.put("PR_CATEGORIA3", rs.getString("PR_CATEGORIA3"));
                            objJsonCte.put("PR_CATEGORIA4", rs.getString("PR_CATEGORIA4"));
                            objJsonCte.put("PR_CATEGORIA5", rs.getString("PR_CATEGORIA5"));
                            objJsonCte.put("PR_CATEGORIA6", rs.getString("PR_CATEGORIA6"));
                            objJsonCte.put("PR_CATEGORIA7", rs.getString("PR_CATEGORIA7"));
                            objJsonCte.put("PR_CATEGORIA8", rs.getString("PR_CATEGORIA8"));
                            objJsonCte.put("PR_CATEGORIA9", rs.getString("PR_CATEGORIA9"));
                            objJsonCte.put("PR_CATEGORIA10", rs.getString("PR_CATEGORIA10"));
                            objJsonCte.put("PV_ID", rs.getString("PV_ID"));                            
                            jsonChild.put(objJsonCte);
                        }
                        rs.close();
                        objJson.put("productos_datos", jsonChild);
                        objJson.put("resultado", "OK");
                    } catch (SQLException ex) {
                        ex.fillInStackTrace();
                    }
                } else {
                    objJson.put("resultado", "Opcion no valida especifique");
                }
            } else {
                objJson.put("resultado", "No es valido el codigo ");
            }

        } catch (Exception ex) {
            this.log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
        }
        //TODO return proper representation object
        return objJson.toString();
    }

    /**
     * PUT method for updating or creating an instance of ArticulosResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
