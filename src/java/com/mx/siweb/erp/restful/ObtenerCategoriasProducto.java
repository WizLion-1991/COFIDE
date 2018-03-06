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
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author siwebmx5
 */
@Path("ObtenerCategoriasProducto")
public class ObtenerCategoriasProducto {

    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    org.apache.logging.log4j.Logger log = null;

    /**
     * Creates a new instance of ObtenerCategoriasProducto
     */
    public ObtenerCategoriasProducto() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.ObtenerCategoriasProducto
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson(@DefaultValue("") @QueryParam("Opcion") String strOpcion,
            @DefaultValue("") @QueryParam("Codigo") String strCodigo) {
        //TODO return proper representation object
        JSONObject objJson = new JSONObject();

        VariableSession varSesiones = new VariableSession(servletRequest);
        varSesiones.getVars();
        try {
            //Abrimos la conexion
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();
            EvalSesion eval = new EvalSesion();
            if (eval.evaluaSesion(strCodigo, oConn)) {
                //SI SELECCIONAN PRECIOS
                if (strOpcion.equals("1")) {
                    String strConsulta = "Select * From vta_prodmodiprec";
                    try {
                        JSONArray jsonChild = new JSONArray();
                        ResultSet rs = oConn.runQuery(strConsulta);
                        while (rs.next()) {
                            JSONObject objJsonCte = new JSONObject();
                            objJsonCte.put("PR_ID", rs.getString("PMP_ID"));
                            objJsonCte.put("PR_CODIGO", rs.getString("PMP_DESCRIPCION"));

                            jsonChild.put(objJsonCte);
                        }
                        rs.close();
                        objJson.put("precios", jsonChild);
                    } catch (SQLException ex) {
                        ex.fillInStackTrace();
                    }


                }
                //SI SELECCIONAN CATEGORIAS
                if (strOpcion.equals("2")) {
                     String strConsulta = "Select * From vta_prodcat1";
                    try {
                        JSONArray jsonChild = new JSONArray();
                        ResultSet rs = oConn.runQuery(strConsulta);
                        while (rs.next()) {
                            JSONObject objJsonCte = new JSONObject();
                            objJsonCte.put("PC_ID", rs.getString("PC_ID"));
                            objJsonCte.put("PC_DESCRIPCION", rs.getString("PC_DESCRIPCION"));
                            objJsonCte.put("PC_IMAGEN", rs.getString("PC_IMAGEN"));

                            jsonChild.put(objJsonCte);
                        }
                        rs.close();
                        objJson.put("Categoria1", jsonChild);
                        
                        strConsulta = "Select * From vta_prodcat2";
                        jsonChild = new JSONArray();
                        rs = oConn.runQuery(strConsulta);
                        while (rs.next()) {
                            JSONObject objJsonCte = new JSONObject();
                            objJsonCte.put("PC2_ID", rs.getString("PC2_ID"));
                            objJsonCte.put("PC2_DESCRIPCION", rs.getString("PC2_DESCRIPCION"));

                            jsonChild.put(objJsonCte);
                        }
                        rs.close();
                        objJson.put("Categoria2", jsonChild);
                        
                        strConsulta = "Select * From vta_prodcat3";
                        jsonChild = new JSONArray();
                        rs = oConn.runQuery(strConsulta);
                        while (rs.next()) {
                            JSONObject objJsonCte = new JSONObject();
                            objJsonCte.put("PC3_ID", rs.getString("PC3_ID"));
                            objJsonCte.put("PC3_DESCRIPCION", rs.getString("PC3_DESCRIPCION"));

                            jsonChild.put(objJsonCte);
                        }
                        rs.close();
                        objJson.put("Categoria3", jsonChild);
                        
                        strConsulta = "Select * From vta_prodcat4";
                        jsonChild = new JSONArray();
                        rs = oConn.runQuery(strConsulta);
                        while (rs.next()) {
                            JSONObject objJsonCte = new JSONObject();
                            objJsonCte.put("PC4_ID", rs.getString("PC4_ID"));
                            objJsonCte.put("PC4_DESCRIPCION", rs.getString("PC4_DESCRIPCION"));

                            jsonChild.put(objJsonCte);
                        }
                        rs.close();
                        objJson.put("Categoria4", jsonChild);
                        
                        strConsulta = "Select * From vta_prodcat5";
                        jsonChild = new JSONArray();
                        rs = oConn.runQuery(strConsulta);
                        while (rs.next()) {
                            JSONObject objJsonCte = new JSONObject();
                            objJsonCte.put("PC5_ID", rs.getString("PC5_ID"));
                            objJsonCte.put("PC5_DESCRIPCION", rs.getString("PC5_DESCRIPCION"));

                            jsonChild.put(objJsonCte);
                        }
                        rs.close();
                        objJson.put("Categoria5", jsonChild);
                        
                        strConsulta = "Select * From vta_prodcat6";
                        jsonChild = new JSONArray();
                        rs = oConn.runQuery(strConsulta);
                        while (rs.next()) {
                            JSONObject objJsonCte = new JSONObject();
                            objJsonCte.put("PC6_ID", rs.getString("PC6_ID"));
                            objJsonCte.put("PC6_DESCRIPCION", rs.getString("PC6_DESCRIPCION"));

                            jsonChild.put(objJsonCte);
                        }
                        rs.close();
                        objJson.put("Categoria6", jsonChild);
                        
                        strConsulta = "Select * From vta_prodcat7";
                        jsonChild = new JSONArray();
                        rs = oConn.runQuery(strConsulta);
                        while (rs.next()) {
                            JSONObject objJsonCte = new JSONObject();
                            objJsonCte.put("PC7_ID", rs.getString("PC7_ID"));
                            objJsonCte.put("PC7_DESCRIPCION", rs.getString("PC7_DESCRIPCION"));

                            jsonChild.put(objJsonCte);
                        }
                        rs.close();
                        objJson.put("Categoria7", jsonChild);
                        
                        strConsulta = "Select * From vta_prodcat8";
                        jsonChild = new JSONArray();
                        rs = oConn.runQuery(strConsulta);
                        while (rs.next()) {
                            JSONObject objJsonCte = new JSONObject();
                            objJsonCte.put("PC8_ID", rs.getString("PC8_ID"));
                            objJsonCte.put("PC8_DESCRIPCION", rs.getString("PC8_DESCRIPCION"));

                            jsonChild.put(objJsonCte);
                        }
                        rs.close();
                        objJson.put("Categoria8", jsonChild);
                        
                        strConsulta = "Select * From vta_prodcat9";
                        jsonChild = new JSONArray();
                        rs = oConn.runQuery(strConsulta);
                        while (rs.next()) {
                            JSONObject objJsonCte = new JSONObject();
                            objJsonCte.put("PC9_ID", rs.getString("PC9_ID"));
                            objJsonCte.put("PC9_DESCRIPCION", rs.getString("PC9_DESCRIPCION"));

                            jsonChild.put(objJsonCte);
                        }
                        rs.close();
                        objJson.put("Categoria9", jsonChild);
                        
                        strConsulta = "Select * From vta_prodcat10";
                        jsonChild = new JSONArray();
                        rs = oConn.runQuery(strConsulta);
                        while (rs.next()) {
                            JSONObject objJsonCte = new JSONObject();
                            objJsonCte.put("PC10_ID", rs.getString("PC10_ID"));
                            objJsonCte.put("PC10_DESCRIPCION", rs.getString("PC10_DESCRIPCION"));
                            objJsonCte.put("PC9_ID", rs.getString("PC9_ID"));

                            jsonChild.put(objJsonCte);
                        }
                        rs.close();
                        objJson.put("Categoria10", jsonChild);
                        
                    } catch (SQLException ex) {
                        ex.fillInStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            this.log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
        }
        return objJson.toString();
    }

    /**
     * PUT method for updating or creating an instance of ObtenerCategoriasProducto
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
