/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import com.siweb.utilerias.json.JSONArray;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
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
@Path("ServiceCompras")
public class ServiceComprasResourceResource {

    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final  org.apache.logging.log4j.Logger log = LogManager.getLogger(ServiceComprasResourceResource.class.getName());

    /**
     * Creates a new instance of ServiceComprasResourceResource
     */
    public ServiceComprasResourceResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.ServiceComprasResourceResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson(@DefaultValue("") @QueryParam("Opcion") String strOpcion,
            @DefaultValue("") @QueryParam("Codigo") String strCodigo,
            @DefaultValue("") @QueryParam("CodigoProd") String strCodigoProd,
            @DefaultValue("") @QueryParam("Meses") String strMeses) {
        //Objeto json para almacenar los objetos
        JSONObject objJson = new JSONObject();
        //Evaluamos que sea una opcion valida
        if (strOpcion.isEmpty()) {
            try {
                objJson.put("resultado", "Opcion no valida especifique");
            } catch (JSONException ex) {
                log.error(ex.getMessage());
            }

        } else {
            //Accedemos a la base de datos para ver si tenemos acceso
            VariableSession varSesiones = new VariableSession(servletRequest);
            varSesiones.getVars();
            try {
                //Abrimos la conexion
                Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
                oConn.open();

                //Validamos si tenemos acceso
                EvalSesion eval = new EvalSesion();
                if (eval.evaluaSesion(strCodigo, oConn)) {
                    //Recuperamos la opcion indicada

                    // <editor-fold defaultstate="collapsed" desc="Opcion 1 Enviamos los datos del cliente">
                    if (strOpcion.equals("1")) {

                        String strFiltro = "";
                        Fechas fecha = new Fechas();
                        if (strCodigoProd != null && !strCodigoProd.equals("")) {
                            strFiltro = " AND a.PR_CODIGO like '" + strCodigoProd + "%'";
                        }

                        String FechaB = fecha.addFecha(fecha.getFechaActual(), 2, Integer.valueOf(strMeses));
                        //Arreglo de nodos
                        JSONArray jsonChild = new JSONArray();
                        String strConsulta = "SELECT"
                                + " b.COM_ID,"
                                + " a.PR_CODIGO,"
                                + " a.PR_DESCRIPCION,"
                                + " sum(c.COMD_CANTIDAD) as COMD_CANTIDAD"
                                + " FROM vta_producto a, vta_compra b, vta_compradeta c\n"
                                + " WHERE b.COM_ID = c.COM_ID\n"
                                + " AND b.COM_SURTIDA = 0\n"
                                + " AND b.COM_AUTORIZA=1\n"
                                + " AND b.COM_STATUS  IN (1,2,4)\n"
                                + " AND c.COMD_CANTIDADSURTIDA < c.COMD_CANTIDAD\n"
                                + " AND a.PR_ID=c.PR_ID\n"
                                + " AND b.COM_FECHA_DISP_VTA >= '"+fecha.getFechaActual()+"'\n"
                                + " AND b.COM_FECHA_DISP_VTA <= '"+FechaB+"'\n"
                                + " group by a.PR_CODIGO,a.PR_DESCRIPCION"                                
                                + " " + strFiltro;
                        try {
                            ResultSet rs = oConn.runQuery(strConsulta);
                            while (rs.next()) {
                                JSONObject objJsonCte = new JSONObject();
                                objJsonCte.put("CODIGO", rs.getString("PR_CODIGO"));
                                objJsonCte.put("DESCRIPCION", rs.getString("PR_DESCRIPCION").replace(",", ""));
                                //objJsonCte.put("ORDEN_DE_COMPRA", rs.getString("COM_FOLIO"));
                                //objJsonCte.put("FECHA_PROMESA", fecha.FormateaDDMMAAAA(rs.getString("COM_FECHA_PROMESA"), "/"));
                                objJsonCte.put("CANTIDAD_A_LLEGAR", rs.getInt("COMD_CANTIDAD"));
                                //objJsonCte.put("FECHA_ESTIMADA", fecha.FormateaDDMMAAAA(rs.getString("COM_FECHA_DISP_VTA"), "/"));
                                /*if (rs.getString("COM_DIAS") != null) {
                                    objJsonCte.put("DIAS_PENDIENTES", rs.getString("COM_DIAS"));
                                } else {
                                    objJsonCte.put("DIAS_PENDIENTES", "");
                                }*/

                                jsonChild.put(objJsonCte);
                            }
                            rs.close();
                            objJson.put("resultado", "OK");
                            objJson.put("producto_compras", jsonChild);

                        } catch (SQLException ex) {
                            ex.fillInStackTrace();
                        }


                    } else {
                        objJson.put("resultado", "Opcion no valida especifique");
                    }
                    // </editor-fold>


                    // <editor-fold defaultstate="collapsed" desc="Opcion 2 Enviamos los estados">
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Opcion 3 Obtener las contrasenas">
                    // </editor-fold>
                } else {
                    objJson.put("resultado", "Codigo no valido");
                }

                oConn.close();
            } catch (Exception ex) {
                this.log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
            }
        }
        return objJson.toString();
    }

    /**
     * PUT method for updating or creating an instance of
     * ServiceComprasResourceResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
