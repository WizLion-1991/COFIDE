/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.restful;

import com.mx.siweb.erp.especiales.cofide.restful.entidades.DatosFacturacion;
import com.mx.siweb.erp.especiales.cofide.restful.entidades.ListaDatosFacturacion;
import com.mx.siweb.erp.restful.DireccionEntregaResource;
import com.mx.siweb.erp.restful.EvalSesion;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;

/**
 * REST Web Service
 *
 * @author ZeusSIWEB
 */
@Path("DatosFacturacion")
public class DatosFacturacionResource {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(DireccionEntregaResource.class.getName());

    /**
     * Creates a new instance of DatosFacturacionResource
     */
    public DatosFacturacionResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.especiales.cofide.restful.DatosFacturacionResource
     *
     * @param strIdClienteCRM
     * @param strCuentaMail
     * @param strCodigo
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public ListaDatosFacturacion getJson(
            @DefaultValue("") @QueryParam("IdClienteCRM") String strIdClienteCRM,
            @DefaultValue("") @QueryParam("CuentaMail") String strCuentaMail,
            @DefaultValue("") @QueryParam("Codigo") String strCodigo) {
        ListaDatosFacturacion lista = new ListaDatosFacturacion();
        //Accedemos a la base de datos para ver si tenemos acceso
        VariableSession varSesiones = new VariableSession(servletRequest);
        try {
            //Abrimos la conexión
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();
            //Se valida si se tiene acceso 
            EvalSesion eval = new EvalSesion();
            String strResp = "OK";
            log.debug("Validando secion");
            if (eval.evaluaSesion(strCodigo, oConn)) {
                log.debug("Sesion Valida");
                if (eval.evaluaPermiso(9, oConn)) {
                    log.debug("Permiso Valido");
                    boolean bolBuscaPorCliente = false;
                    if (strIdClienteCRM != null) {
                        if (!strIdClienteCRM.isEmpty()) {
                            bolBuscaPorCliente = true;
                        }
                    }
                    //Hacemos la busqueda
                    if (bolBuscaPorCliente) {
                        buscaPorCliente(lista, strIdClienteCRM, oConn);
                    } else {
                        buscaPorCorreo(lista, strCuentaMail, oConn);
                    }

                }//Fin EVALUA PERMISO
            }//Fin EVALUAR SESION
            else {
                log.error("Error:Access Denied");
                strResp = "Error:Access Denied";
                lista.setCodigo("Error:Access Denied");
            }
            oConn.close();
        }//Fin TRY
        catch (Exception e) {
            log.error("MobilServiceLogin1" + e.getMessage() + " " + e.getLocalizedMessage());
        }
        return lista;
    }

    private void buscaPorCliente(ListaDatosFacturacion lista, String strIdCliente, Conexion oConn) {
//        String strSql = "select vta_cliente_facturacion.* from vta_cliente_facturacion, vta_cliente where vta_cliente_facturacion.CT_ID =  vta_cliente.CT_ID AND vta_cliente.CT_ID = " + strIdCliente;
        String strSql = "select DISTINCT DFA_RAZONSOCIAL, DFA_RFC, DFA_CALLE, DFA_NUMERO, DFA_NUMINT, "
                + "DFA_COLONIA, DFA_MUNICIPIO, DFA_ESTADO, DFA_CP, vta_cliente_facturacion.CT_ID, DFA_TELEFONO, DFA_EMAIL, DFA_EMAI2,DFA_LOCALIDAD, DFA_PAIS "
                + "from vta_cliente_facturacion, vta_cliente "
                + "where vta_cliente_facturacion.CT_ID =  vta_cliente.CT_ID "
                + "AND vta_cliente.CT_ID = " + strIdCliente;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                DatosFacturacion datos = new DatosFacturacion();

                datos.setRazonsocial(rs.getString("DFA_RAZONSOCIAL"));
                datos.setRfc(rs.getString("DFA_RFC"));
                datos.setCalle(rs.getString("DFA_CALLE"));
                datos.setNumeroExterior(rs.getString("DFA_NUMERO"));
                datos.setNumeroInterior(rs.getString("DFA_NUMINT"));
                datos.setColonia(rs.getString("DFA_COLONIA"));
                datos.setLocalidad(rs.getString("DFA_LOCALIDAD"));
                datos.setMunicipio(rs.getString("DFA_MUNICIPIO"));
                datos.setEstado(rs.getString("DFA_ESTADO"));
                datos.setCp(rs.getString("DFA_CP"));
                datos.setDatos_Id(rs.getInt("CT_ID"));
                datos.setTelefono1(rs.getString("DFA_TELEFONO"));
                datos.setEmail1(rs.getString("DFA_EMAIL"));
                datos.setPais(rs.getString("DFA_PAIS"));

                lista.getListadoFacturacion().add(datos);
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }

    private void buscaPorCorreo(ListaDatosFacturacion lista, String strCuentaCorreo, Conexion oConn) {
        String strSql = "select vta_cliente_facturacion.* from vta_cliente_facturacion, vta_cliente "
                + " where vta_cliente_facturacion.CT_ID =  vta_cliente.CT_ID AND (vta_cliente.CT_EMAIL1 = '" + strCuentaCorreo + "'"
                + " OR vta_cliente.CT_EMAIL2 = '" + strCuentaCorreo + "')";
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                DatosFacturacion datos = new DatosFacturacion();
                datos.setRazonsocial(rs.getString("DFA_RAZONSOCIAL"));
                datos.setRfc(rs.getString("DFA_RFC"));
                datos.setCalle(rs.getString("DFA_CALLE"));
                datos.setNumeroExterior(rs.getString("DFA_NUMERO"));
                datos.setNumeroInterior(rs.getString("DFA_NUMINT"));
                datos.setColonia(rs.getString("DFA_COLONIA"));
                datos.setLocalidad(rs.getString("DFA_LOCALIDAD"));
                datos.setMunicipio(rs.getString("DFA_MUNICIPIO"));
                datos.setEstado(rs.getString("DFA_ESTADO"));
                datos.setCp(rs.getString("DFA_CP"));
                datos.setDatos_Id(rs.getInt("CT_ID"));
                datos.setTelefono1(rs.getString("DFA_TELEFONO"));
                datos.setEmail1(rs.getString("DFA_EMAIL"));
                datos.setPais(rs.getString("DFA_PAIS"));
                lista.getListadoFacturacion().add(datos);
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * PUT method for updating or creating an instance of
     * DatosFacturacionResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
