/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.restful;

import com.mx.siweb.erp.especiales.cofide.Cofide_PBX;
import com.mx.siweb.erp.especiales.cofide.LlamadasPBX;
import com.mx.siweb.erp.especiales.cofide.entidades.CofideLlamada;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import org.apache.logging.log4j.LogManager;

/**
 * REST Web Service
 *
 * @author ZeusSIWEB
 */
@Path("Llamada")
public class LlamadaResource {

    @Context
    private UriInfo context;

    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(LlamadaResource.class.getName());

    /**
     * Creates a new instance of LlamadaResource
     */
    public LlamadaResource() {
    }
    Conexion oConn = new Conexion();
    Fechas fec = new Fechas();

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.especiales.cofide.restful.LlamadaResource
     *
     * @return an instance of
     * com.mx.siweb.erp.especiales.cofide.entidades.CofideLlamada
     */
    @GET
    @Produces("application/json")
    public CofideLlamada getJson() {
        CofideLlamada llamada = new CofideLlamada();
        log.debug("getJson ");
        llamada.setPhone("000099");
        llamada.setExt("000");
        llamada.setId("0000000");
        return llamada;
    }

    /**
     * PUT method for updating or creating an instance of LlamadaResource
     *
     * @param content representation for the resource
     * @return
     */
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public String putJson(CofideLlamada content) throws SQLException {
        String strOk = "No se Recibieron los datos correctamente!";
        boolean bolOk = false;
        if (content.getExt() != null && content.getId() != null && content.getPhone() != null) {
            log.debug("getPhone " + content.getPhone());
            log.debug("getExt " + content.getExt());
            log.debug("getId " + content.getId());
            strOk = "Se Recibieron los datos correctamente!";
            bolOk = true;
        }
        //Accedemos a la base de datos para ver si tenemos acceso
        VariableSession varSesiones = new VariableSession(servletRequest);

        //al obtener respuesta dle PBX hacemos un update al registro de la llamada
        try {
            oConn.open();
            String strGetId = "";
            String strCurFecha = fec.getFechaActual();
            String strSqlCall = " select * from cofide_llamada where  CL_EXT = '" + content.getExt() + "' and "
                    + "CL_DESTINO = '" + content.getPhone() + "' and "
                    + "CL_FECHA = '" + strCurFecha + "' order by CL_ID desc limit 1 ";
            ResultSet rs = oConn.runQuery(strSqlCall, true);
            while (rs.next()) {
                strGetId = rs.getString("CL_ID");
            }
            rs.close();
            log.debug(strGetId + " id de la llamada");
            if (!strGetId.equals("")) {
                String UpdateSqlCall = "update cofide_llamada set uniqueid = '" + content.getId() + "', CL_FIN_LLAMADA = '" + fec.getHoraActual() + "' where CL_ID = " + strGetId;
                oConn.runQueryLMD(UpdateSqlCall);
                //obtener datos de la llamada, duracion, si fue exitoso o no
                Cofide_PBX cofide_pbx = new Cofide_PBX(oConn);
                cofide_pbx.ObtenerDatosCall(content.getId());
                //cerrar la conexion
            } //si el id viene vacio, no hacer el proceso
            oConn.close();
        } catch (Exception e) {
            log.error(e);
        }
        //Actualizamos la llamada
        LlamadasPBX pbx = new LlamadasPBX();
        try {
            //Abrimos la conexión
            //Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();
            //Aplicamos los querys
            pbx.terminaLlamada(content, oConn);
            oConn.close();
        } catch (Exception e) {
            log.error("MobilServiceLogin1" + e.getMessage() + " " + e.getLocalizedMessage());
        }
        //return strOk + "," + bolOk;
        String input = "{\"respuesta\":\"" + bolOk + "\",\"mensaje\":\"" + strOk + "\"}";
        return input;
    }
}
