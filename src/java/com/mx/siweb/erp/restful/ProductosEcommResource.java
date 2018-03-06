/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import com.mx.siweb.erp.restful.entity.Categorias;
import com.mx.siweb.erp.restful.entity.CategoriasDeta;
import com.mx.siweb.erp.restful.entity.ProductosEcomm;
import com.mx.siweb.erp.restful.entity.ProductosEcommDeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.sql.ResultSet;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
@Path("ProductosEcomm")
public class ProductosEcommResource {

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
    public ProductosEcommResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.ProductosEcommResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/productos")
    @Produces({"application/json"})
    public ProductosEcomm getJson(
            @DefaultValue("") @QueryParam("Codigo") String strCodigo,
            @DefaultValue("") @QueryParam("IdProductos") String strIdproductos,
            @DefaultValue("") @QueryParam("PrCategoria1") String strPrCategoria1,
            @DefaultValue("") @QueryParam("PrCategoria2") String strPrCategoria2,
            @DefaultValue("") @QueryParam("PrCategoria3") String strPrCategoria3,
            @DefaultValue("") @QueryParam("PrCategoria4") String strPrCategoria4,
            @DefaultValue("") @QueryParam("PrCategoria5") String strPrCategoria5,
            @DefaultValue("") @QueryParam("PrCategoria6") String strPrCategoria6,
            @DefaultValue("") @QueryParam("PrCategoria7") String strPrCategoria7,
            @DefaultValue("") @QueryParam("PrCategoria8") String strPrCategoria8,
            @DefaultValue("") @QueryParam("PrCategoria9") String strPrCategoria9,
            @DefaultValue("") @QueryParam("PrCategoria10") String strPrCategoria10) {

        ProductosEcomm productos = new ProductosEcomm();

//Accedemos a la base de datos para ver si tenemos acceso
        VariableSession varSesiones = new VariableSession(servletRequest);
        varSesiones.getVars();

        String strResp = "OK";
        String strFiltro = "";
        int strIdproductos1 = Integer.parseInt(strIdproductos);
        int strPrCategoria1P = Integer.parseInt(strPrCategoria1);
        int strPrCategoria2P = Integer.parseInt(strPrCategoria2);
        int strPrCategoria3P = Integer.parseInt(strPrCategoria3);
        int strPrCategoria4P = Integer.parseInt(strPrCategoria4);
        int strPrCategoria5P = Integer.parseInt(strPrCategoria5);
        int strPrCategoria6P = Integer.parseInt(strPrCategoria6);
        int strPrCategoria7P = Integer.parseInt(strPrCategoria7);
        int strPrCategoria8P = Integer.parseInt(strPrCategoria8);
        int strPrCategoria9P = Integer.parseInt(strPrCategoria9);
        int strPrCategoria10P = Integer.parseInt(strPrCategoria10);
        try {
            //Abrimos la conexion
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();

            //Validamos si tenemos acceso
            EvalSesion eval = new EvalSesion();
            if (eval.evaluaSesion(strCodigo, oConn)) {

                if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
                    Site webBase = new Site(oConn);
                    strFiltro = " select *,"
                            + " (SELECT PP_PRECIO FROM vta_prodprecios  WHERE vta_prodprecios.PR_ID = vta_producto.PR_ID and lp_id = 1) as precio,"
                            + " (SELECT PP_PUNTOS FROM vta_prodprecios WHERE vta_prodprecios.PR_ID = vta_producto.PR_ID and lp_id = 1) as puntos,"
                            + " (SELECT PP_NEGOCIO FROM vta_prodprecios WHERE vta_prodprecios.PR_ID = vta_producto.PR_ID and lp_id = 1) as negocio "
                            + " from vta_producto where EMP_ID =" + webBase.getIntEMP_ID() + " AND PR_ECOMM = 1 AND SC_ID =" + webBase.getIntSC_ID();

                    if (strIdproductos1 != 0) {
                        strFiltro += " AND  vta_producto.PR_ID = " + strIdproductos1 + "";
                    }
                    if (strPrCategoria1P != 0) {
                        strFiltro += " AND  vta_producto.PR_CATEGORIA1 = " + strPrCategoria1P + "";
                    }
                    if (strPrCategoria2P != 0) {
                        strFiltro += " AND  vta_producto.PR_CATEGORIA2 = " + strPrCategoria2P + "";
                    }
                    if (strPrCategoria3P != 0) {
                        strFiltro += " AND  vta_producto.PR_CATEGORIA3 = " + strPrCategoria3P + "";
                    }
                    if (strPrCategoria4P != 0) {
                        strFiltro += " AND  vta_producto.PR_CATEGORIA4 = " + strPrCategoria4P + "";
                    }
                    if (strPrCategoria5P != 0) {
                        strFiltro += " AND  vta_producto.PR_CATEGORIA5 = " + strPrCategoria5P + "";
                    }
                    if (strPrCategoria6P != 0) {
                        strFiltro += " AND  vta_producto.PR_CATEGORIA6 = " + strPrCategoria6P + "";
                    }
                    if (strPrCategoria7P != 0) {
                        strFiltro += " AND  vta_producto.PR_CATEGORIA7 = " + strPrCategoria7P + "";
                    }
                    if (strPrCategoria8P != 0) {
                        strFiltro += " AND  vta_producto.PR_CATEGORIA8 = " + strPrCategoria8P + "";
                    }
                    if (strPrCategoria9P != 0) {
                        strFiltro += " AND  vta_producto.PR_CATEGORIA9 = " + strPrCategoria9P + "";
                    }
                    if (strPrCategoria10P != 0) {
                        strFiltro += " AND  vta_producto.PR_CATEGORIA10 = " + strPrCategoria10P + ";";
                    }

                    ResultSet rs = oConn.runQuery(strFiltro);

                    while (rs.next()) {
                        ProductosEcommDeta productosDeta = new ProductosEcommDeta();

                        productosDeta.setExistencia(rs.getInt("PR_EXISTENCIA"));
                        productosDeta.setNombreProducto(rs.getString("PR_DESCRIPCIONCORTA"));
                        productosDeta.setPrecio(rs.getDouble("precio"));
                        productosDeta.setNombreImagenPequena(rs.getString("PR_NOMIMG1"));
                        productosDeta.setNombreImagenGrande(rs.getString("PR_NOMIMG2"));
                        productosDeta.setCodigo(rs.getString("PR_CODIGO"));
                        productosDeta.setPr_id(rs.getInt("PR_ID"));
                        productosDeta.setDescripcion(rs.getString("PR_DESCRIPCION"));
                        productosDeta.setPuntos(rs.getDouble("puntos"));
                        productosDeta.setNegocio(rs.getDouble("negocio"));
                        productosDeta.setExento1(rs.getInt("PR_EXENTO1"));
                        productosDeta.setExento2(rs.getInt("PR_EXENTO2"));
                        productosDeta.setExento3(rs.getInt("PR_EXENTO3"));
                        productosDeta.setCve(rs.getString("PR_CODIGO"));
                        productosDeta.setTasaIva1(rs.getInt("PR_TASA_IVA"));
                        productosDeta.setUnidadDeMedida(rs.getInt("PR_UNIDADMEDIDA"));
                        
                        productos.getLstItems().add(productosDeta);

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

        return productos;
    }

    @GET
    @Path("/Categoria")
    @Produces("application/json")
    public Categorias getJSON(@DefaultValue("") @QueryParam("Codigo") String strCodigo,
            @DefaultValue("") @QueryParam("Categoria") String strCategoria,
            @DefaultValue("") @QueryParam("Subcategoria") String strSubcategoria) {
        Categorias categorias = new Categorias();

        //Accedemos a la base de datos para ver si tenemos acceso
        VariableSession varSessiones = new VariableSession(servletRequest);
        try {
            Conexion oConn = new Conexion(varSessiones.getStrUser(), servletContext);
            oConn.open();
            //Validamos Acceso
            EvalSesion eval = new EvalSesion();
            String strResp = "OK";
            int intCategoria = Integer.parseInt(strCategoria);
            int intSubcategoria = Integer.parseInt(strSubcategoria);
            log.debug("Validando Sesión");
            if (eval.evaluaSesion(strCodigo, oConn)) {
                log.debug("Sesion Valida");
                varSessiones.getVars();
                if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
                    log.debug("Permiso Valido");
                    if (intCategoria == 1) {
                        String consulta = "select PC_ID,PC_DESCRIPCION from vta_prodcat1 order by pc_id;";

                        log.debug(consulta);
                        ResultSet rs = oConn.runQuery(consulta);
                        while (rs.next()) {
                            CategoriasDeta categoDeta = new CategoriasDeta();
                            categoDeta.setId(rs.getInt("PC_ID"));
                            categoDeta.setDescripcion(rs.getString("PC_DESCRIPCION"));
                            log.debug(rs.getInt("PC_ID"));
                            log.debug(rs.getString("PC_DESCRIPCION"));
                            categorias.getLstItems().add(categoDeta);
                        }

                        rs.close();
                    } else {
                        String consulta1 = "SELECT  distinct vta_prodcat2.PC2_ID, \n"
                                + " vta_prodcat2.PC2_DESCRIPCION\n"
                                + "FROM vta_prodcat2 INNER JOIN vta_producto ON vta_prodcat2.PC2_ID = vta_producto.PR_CATEGORIA2\n"
                                + "WHERE vta_producto.PR_CATEGORIA1 =  " + intSubcategoria + " order by PC2_ID";

                        log.debug(consulta1);
                        ResultSet rs1 = oConn.runQuery(consulta1);
                        while (rs1.next()) {
                            CategoriasDeta categoDeta = new CategoriasDeta();
                            categoDeta.setId(rs1.getInt("PC2_ID"));
                            categoDeta.setDescripcion(rs1.getString("PC2_DESCRIPCION"));
                            categorias.getLstItems().add(categoDeta);

                        }
                    }
                }//Fin evalua Permiso

            }//Fin valida Sesión
            else {
                log.error("Error:Access Denied");
                strResp = "Error:Access Denied";
            }
            oConn.close();

        } catch (Exception e) {
            log.error("MobilServiceLogin1" + e.getMessage() + " " + e.getLocalizedMessage());
        }
        return categorias;
    }

}
