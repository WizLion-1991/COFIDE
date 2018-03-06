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

/**
 * REST Web Service
 *
 * @author siwebmx5
 */
@Path("ProductosCategorias")
public class ProductosCategorias {

    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    org.apache.logging.log4j.Logger log = null;

    /**
     * Creates a new instance of ProductosCategorias
     */
    public ProductosCategorias() {
    }

    /**
     * Retrieves representation of an instance of com.mx.siweb.erp.restful.ProductosCategorias
    * @param strOpcion Es la opcion por consultar
    * @param strEmpresa Es el id de la empresa
    * @param strSucursal Es el id de la bodega
    * @param strEcomm Indica con 1 si se filtra  solo lo del ecommerce
    * @param strCodigo Es el codigo de la sesion
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson(@DefaultValue("") @QueryParam("Opcion") String strOpcion,
                          @DefaultValue("") @QueryParam("Empresa") String strEmpresa,
                          @DefaultValue("") @QueryParam("Sucursal") String strSucursal,
                          @DefaultValue("") @QueryParam("Ecomm") String strEcomm,
                          @DefaultValue("") @QueryParam("Codigo") String strCodigo) {
        //TODO return proper representation object
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
                    String strFiltro = "Where 1 ";
                    if(!strEmpresa.equals(""))
                    {
                        strFiltro+= " AND EMP_ID = "+strEmpresa;
                    }
                    
                    if(!strSucursal.equals(""))
                    {
                        strFiltro+= " AND SC_ID = "+strSucursal;
                    }
                    if(!strEcomm.equals(""))
                    {
                        strFiltro+= " AND PR_ECOMM = "+strEcomm;
                    }
                    
                    String strConsulta = "SELECT *, "
                            + "(select p1.PP_PRECIO from vta_prodprecios p1 where p1.LP_ID = 1 and p1.PR_ID = vta_producto.PR_ID) as precio1_mn," 
                            + "(select p1.PP_PRECIO_USD from vta_prodprecios p1 where p1.LP_ID = 1 and p1.PR_ID = vta_producto.PR_ID) as precio1_usd,"
                            + "(select p3.PP_PRECIO from vta_prodprecios p3 where p3.LP_ID = 3 and p3.PR_ID = vta_producto.PR_ID) as precio3_mn," 
                            + "(select p3.PP_PRECIO_USD from vta_prodprecios p3 where p3.LP_ID = 3 and p3.PR_ID = vta_producto.PR_ID) as precio3_usd,"
                            + "(select p4.PP_PRECIO from vta_prodprecios p4 where p4.LP_ID = 4 and p4.PR_ID = vta_producto.PR_ID) as precio4_mn," 
                            + "(select p4.PP_PRECIO_USD from vta_prodprecios p4 where p4.LP_ID =4 and p4.PR_ID = vta_producto.PR_ID) as precio4_usd,"
                            + "(select p5.PP_PRECIO from vta_prodprecios p5 where p5.LP_ID = 5 and p5.PR_ID = vta_producto.PR_ID) as precio5_mn," 
                            + "(select p5.PP_PRECIO_USD from vta_prodprecios p5 where p5.LP_ID =5 and p5.PR_ID = vta_producto.PR_ID) as precio5_usd"
                            + " FROM vta_producto "+strFiltro;
                    
                    try {
                        JSONArray jsonChild = new JSONArray();
                        ResultSet rs = oConn.runQuery(strConsulta);
                         while (rs.next()) {
                             JSONObject objJsonCte = new JSONObject();                             
                             objJsonCte.put("PR_ID", rs.getString("PR_ID"));
                             objJsonCte.put("PR_CODIGO", rs.getString("PR_CODIGO"));
                             objJsonCte.put("PR_DESCRIPCION", rs.getString("PR_DESCRIPCION"));
                             objJsonCte.put("PR_DESCRIPCIONCORTA", rs.getString("PR_DESCRIPCIONCORTA"));
                             objJsonCte.put("PR_DESCRIPCIONCOMPRA", rs.getString("PR_DESCRIPCIONCOMPRA"));
                             objJsonCte.put("PR_GPO_MODI_PREC", rs.getString("PR_GPO_MODI_PREC"));
                             objJsonCte.put("PR_INVENTARIO", rs.getString("PR_INVENTARIO"));
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
                             objJsonCte.put("PR_UNIDADMEDIDA", rs.getString("PR_UNIDADMEDIDA"));
                             objJsonCte.put("PR_UNIDADMEDIDA_COMPRA", rs.getString("PR_UNIDADMEDIDA_COMPRA"));
                             objJsonCte.put("PR_NOMIMG1", rs.getString("PR_NOMIMG1"));
                             objJsonCte.put("PR_NOMIMG2", rs.getString("PR_NOMIMG2"));
                             objJsonCte.put("PR_ECOMM", rs.getString("PR_ECOMM"));
                             objJsonCte.put("PR_EXISTENCIA", rs.getString("PR_EXISTENCIA"));
                             objJsonCte.put("precio1_mn", rs.getString("precio1_mn"));
                             objJsonCte.put("precio1_usd", rs.getString("precio1_usd"));
                             objJsonCte.put("precio3_mn", rs.getString("precio3_mn"));
                             objJsonCte.put("precio3_usd", rs.getString("precio3_usd"));
                             objJsonCte.put("precio4_mn", rs.getString("precio4_mn"));
                             objJsonCte.put("precio4_usd", rs.getString("precio4_usd"));
                             objJsonCte.put("precio5_mn", rs.getString("precio5_mn"));
                             objJsonCte.put("precio5_usd", rs.getString("precio5_usd"));
                             
                             jsonChild.put(objJsonCte);
                         }
                         rs.close();                        
                        objJson.put("productosCategorias_datos", jsonChild);
                    }catch (SQLException ex) {
                        ex.fillInStackTrace();
                    }
                    
                    
                }else {
                    objJson.put("resultado", "Opcion no valida especifique");
                }
            }else {
                objJson.put("resultado", "No es valido el codigo ");
            }
        }catch (Exception ex) {
            this.log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
        }
        return objJson.toString();
    }

    /**
     * PUT method for updating or creating an instance of ProductosCategorias
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
