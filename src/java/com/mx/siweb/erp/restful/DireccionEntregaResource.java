/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import Tablas.vta_cliente_dir_entrega;
import com.mx.siweb.erp.restful.entity.DireccionEntrega;
import com.mx.siweb.erp.restful.entity.DireccionEntregaDeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.logging.log4j.LogManager;

/**
 * REST Web Service
 *
 * @author siweb
 */
@Path("DireccionEntrega")
public class DireccionEntregaResource {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(DireccionEntregaResource.class.getName());

    /**
     * Creates a new instance of DireccionEntregaResource
     */
    public DireccionEntregaResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.DireccionEntregaResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public DireccionEntrega getJson(@DefaultValue("") @QueryParam("IdCliente") String strIdCliente,
       @DefaultValue("") @QueryParam("Codigo") String strCodigo) {
        DireccionEntrega DirEntrega = new DireccionEntrega();
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
                if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
                    log.debug("Permiso Valido");
                    String strConsulta = "select * from vta_cliente_dir_entrega where CT_ID =" + strIdCliente;
                    ResultSet rs = oConn.runQuery(strConsulta);
                    while (rs.next()) {
                        DireccionEntregaDeta dEntregaDeta = new DireccionEntregaDeta();
                        dEntregaDeta.setDireccionId(rs.getInt("CDE_ID"));
                        dEntregaDeta.setAcuDir(rs.getString("CDE_ACUDIRCON"));
                        dEntregaDeta.setCalle(rs.getString("CDE_CALLE"));
                        dEntregaDeta.setColonia(rs.getString("CDE_COLONIA"));
                        dEntregaDeta.setLocalidad(rs.getString("CDE_LOCALIDAD"));
                        dEntregaDeta.setMunicipio(rs.getString("CDE_MUNICIPIO"));
                        dEntregaDeta.setEstado(rs.getString("CDE_ESTADO"));
                        dEntregaDeta.setCP(rs.getString("CDE_CP"));
                        dEntregaDeta.setNumeroExterior(rs.getString("CDE_NUMERO"));
                        dEntregaDeta.setNumeroInterior(rs.getString("CDE_NUMINT"));
                        dEntregaDeta.setTelefono1(rs.getString("CDE_TELEFONO1"));
                        dEntregaDeta.setGoogle(rs.getString("CDE_GOOGLE"));
                        dEntregaDeta.setEmp_Id(rs.getString("EMP_ID"));
                        dEntregaDeta.setDescripcion(rs.getString("CDE_DESCRIPCION"));
                        dEntregaDeta.setCt_Id(rs.getString("CT_ID"));
                        dEntregaDeta.setNombre(rs.getString("CDE_NOMBRE"));
                        dEntregaDeta.setEmail1(rs.getString("CDE_EMAIL"));
                        dEntregaDeta.setPais(rs.getString("CDE_PAIS"));
                        dEntregaDeta.setCodigo(strCodigo);
                        DirEntrega.getDireccionEntregaItem().add(dEntregaDeta);
                    }//Fin WHILE
                    rs.close();
                    DirEntrega.setCodigo(strCodigo);
                    DirEntrega.setCt_id(strIdCliente);
                }//Fin EVALUA PERMISO
            }//Fin EVALUAR SESION
            else {
                log.error("Error:Access Denied");
                strResp = "Error:Access Denied";
                DirEntrega.setCodigo("Error:Access Denied");
            }
            oConn.close();
        }//Fin TRY
        catch (Exception e) {
            log.error("MobilServiceLogin1" + e.getMessage() + " " + e.getLocalizedMessage());
        }
        return DirEntrega;
    }

    /**
     * PUT method for updating or creating an instance of
     * DireccionEntregaResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Path("/Insertar")
    @Consumes("application/json")
    public Response putJson(DireccionEntregaDeta DirEnt) throws Exception {
        //Accedemos a la base de datos para ver si tenemos acceso
        VariableSession varSessiones = new VariableSession(servletRequest);
        varSessiones.getVars();
        //Abrimos la conexion
        Conexion oConn = new Conexion(varSessiones.getStrUser(), servletContext);
        oConn.open();
        EvalSesion eval = new EvalSesion();

        if (eval.evaluaSesion(DirEnt.getCodigo(), oConn)) {
            varSessiones.getVars();
            if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {

                String strEmpId = "";
                String QueryEmp = "select EMP_ID from vta_cliente where CT_ID = " + DirEnt.getCt_Id() + ";";

                ResultSet res = oConn.runQuery(QueryEmp);
                while (res.next()) {
                    strEmpId = res.getString("EMP_ID");
                }
                res.close();

                try {
                    String strAcuDir = DirEnt.getAcuDir();
                    String strCalle = DirEnt.getCalle();
                    String strColonia = DirEnt.getColonia();
                    String strLocalidad = DirEnt.getLocalidad();
                    String strMunicipio = DirEnt.getMunicipio();
                    String strEstado = DirEnt.getEstado();
                    String strCP = DirEnt.getCP();
                    String strNumExt = DirEnt.getNumeroExterior();
                    String strNumInt = DirEnt.getNumeroInterior();
                    String strTelefono1 = DirEnt.getTelefono1();
                    String strGoogle = DirEnt.getGoogle();
                    String strDescripcion = DirEnt.getDescripcion();
                    String strCT_ID = DirEnt.getCt_Id();
                    String strNombre = DirEnt.getNombre();
                    String strEmail1 = DirEnt.getEmail1();
                    String strPais = DirEnt.getPais();

                    //Creamos el Objeto de la table vta_cliente_dir_entrega
                    vta_cliente_dir_entrega dir_entrega = new vta_cliente_dir_entrega();
                    dir_entrega.setFieldString("CDE_ACUDIRCON", strAcuDir);
                    dir_entrega.setFieldString("CDE_CALLE", strCalle);
                    dir_entrega.setFieldString("CDE_COLONIA", strColonia);
                    dir_entrega.setFieldString("CDE_LOCALIDAD", strLocalidad);
                    dir_entrega.setFieldString("CDE_MUNICIPIO", strMunicipio);
                    dir_entrega.setFieldString("CDE_ESTADO", strEstado);
                    dir_entrega.setFieldString("CDE_CP", strCP);
                    dir_entrega.setFieldString("CDE_NUMERO", strNumExt);
                    dir_entrega.setFieldString("CDE_NUMINT", strNumInt);
                    dir_entrega.setFieldString("CDE_TELEFONO1", strTelefono1);
                    dir_entrega.setFieldString("CDE_GOOGLE", strGoogle);
                    dir_entrega.setFieldString("EMP_ID", strEmpId);
                    dir_entrega.setFieldString("CDE_DESCRIPCION", strDescripcion);
                    dir_entrega.setFieldString("CT_ID", strCT_ID);
                    dir_entrega.setFieldString("CDE_NOMBRE", strNombre);
                    dir_entrega.setFieldString("CDE_EMAIL", strEmail1);
                    dir_entrega.setFieldString("CDE_PAIS", strPais);

                    String strResult = "";
                    strResult = dir_entrega.Agrega(oConn);

                    if (strResult.equals("OK")) {
                        DirEnt.setDireccionId(Integer.valueOf(dir_entrega.getValorKey()));
                    } else {
                        log.error("Error al ingresar su mensaje");
                    }
                }//FIN TRY
                catch (Exception e) {
                    log.error("Error al iniciar sesion");
                }
            }//Fin IF evalua permiso
            oConn.close();
        }//Fin if EVALUA SESION
        return Response.ok(DirEnt).build();
    }//Fin POST

    @POST
    @Path("/Modificar")
    @Consumes("application/json")
    public Response Json(DireccionEntregaDeta DirEnt) throws Exception {
        //Accedemos a la base de datos para ver si tenemos acceso
        VariableSession varSessiones = new VariableSession(servletRequest);
        varSessiones.getVars();
        //Abrimos la conexion
        Conexion oConn = new Conexion(varSessiones.getStrUser(), servletContext);
        oConn.open();

        EvalSesion eval = new EvalSesion();
        if (eval.evaluaSesion(DirEnt.getCodigo(), oConn)) {
            varSessiones.getVars();
            if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
                try {
                    int strIdDireccion = DirEnt.getDireccionId();
                    String strCalle = DirEnt.getCalle();
                    String strColonia = DirEnt.getColonia();
                    String strMunicipio = DirEnt.getMunicipio();
                    String strEstado = DirEnt.getEstado();
                    String strCP = DirEnt.getCP();
                    String strNumExt = DirEnt.getNumeroExterior();
                    String strNumInt = DirEnt.getNumeroInterior();
                    String strTelefono1 = DirEnt.getTelefono1();
                    String strDescripcion = DirEnt.getDescripcion();
                    String strNombre = DirEnt.getNombre();
                    String strEmail1 = DirEnt.getEmail1();
                    String strPais = DirEnt.getPais();


                    String strQuery = "update vta_cliente_dir_entrega set CDE_CALLE = '" + strCalle + "'" +
                       ", CDE_COLONIA ='" + strColonia + "'" +
                       ", CDE_MUNICIPIO = '" + strMunicipio + "'" +
                       ", CDE_ESTADO = '" + strEstado + "'" +
                       ", CDE_CP = '" + strCP + "'" +
                       ", CDE_NUMERO = '" + strNumExt + "'" +
                       ", CDE_NUMINT = '" + strNumInt + "'" +
                       ", CDE_TELEFONO1 = '" + strTelefono1 + "'" +
                       ", CDE_DESCRIPCION = '" + strDescripcion + "'" +
                       ", CDE_NOMBRE = '" + strNombre + "'" + 
                       ", CDE_EMAIL = '" + strEmail1 + "'" +
                       ", CDE_PAIS = '" + strPais + "'"
                       + "where CDE_ID =" + strIdDireccion + ";";
                       oConn.runQueryLMD(strQuery);
                }//Fin TRY
                catch (Exception e) {
                    log.error("Error al iniciar sesion");
                }
            }//FIN IF EVALUA PERMISO
            oConn.close();
        }//Fin IF EVALUA SESION
        return Response.ok(DirEnt).build();
    }//Fin modificar

    /**
     *
     * @param idDireccion
     * @param strCodigo
     * @return
     */
    @GET
    @Path("/DireccionIndividual")
    @Produces("application/json")
    public DireccionEntregaDeta getJson(@DefaultValue("") @QueryParam("idDireccion") String idDireccion,
       @DefaultValue("") @QueryParam("Codigo") String strCodigo,
       @DefaultValue("") @QueryParam("IdCliente") String idCliente) {

        DireccionEntregaDeta dirDeta = new DireccionEntregaDeta();

        //Accedemos a la base de datos para ver si tenemos acceso
        VariableSession varSessiones = new VariableSession(servletRequest);
        try {
            Conexion oConn = new Conexion(varSessiones.getStrUser(), servletContext);
            oConn.open();
            //Validamos Acceso
            EvalSesion eval = new EvalSesion();
            log.debug("Validando Sesión");
            if (eval.evaluaSesion(strCodigo, oConn)) {
                log.debug("Sesion Valida");
              varSessiones.getVars();
              if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
                  log.debug("Permiso Valido");
                  String strQueryEntrega = "select * from vta_cliente_dir_entrega where CDE_ID = " + idDireccion + " and CT_ID = " + idCliente + ";";
                  ResultSet res = oConn.runQuery(strQueryEntrega);
                  while(res.next()){
                      dirDeta.setDireccionId(res.getInt("CDE_ID"));
                      dirDeta.setAcuDir(res.getString("CDE_ACUDIRCON"));
                      dirDeta.setCalle(res.getString("CDE_CALLE"));
                      dirDeta.setColonia(res.getString("CDE_COLONIA"));
                      dirDeta.setLocalidad(res.getString("CDE_LOCALIDAD"));
                      dirDeta.setMunicipio(res.getString("CDE_MUNICIPIO"));
                      dirDeta.setEstado(res.getString("CDE_ESTADO"));
                      dirDeta.setCP(res.getString("CDE_CP"));
                      dirDeta.setNumeroExterior(res.getString("CDE_NUMERO"));
                      dirDeta.setNumeroInterior(res.getString("CDE_NUMINT"));
                      dirDeta.setTelefono1(res.getString("CDE_TELEFONO1"));
                      dirDeta.setDescripcion(res.getString("CDE_DESCRIPCION"));
                      dirDeta.setNombre(res.getString("CDE_NOMBRE"));
                      dirDeta.setPais(res.getString("CDE_PAIS"));
                      dirDeta.setEmail1(res.getString("CDE_EMAIL"));
                      dirDeta.setCt_Id(res.getString("CT_ID"));
                      dirDeta.setEmp_Id(res.getString("EMP_ID"));
                  }//Fin  WHILE
              }//Fin IF evalua Permiso
            }//Fin IF evalua SESION
            oConn.close();
        }//Fin TRY
        catch (Exception e) {
            log.error("MobilServiceLogin1" + e.getMessage() + " " + e.getLocalizedMessage());
        }
        return dirDeta;
    }//Fin Método GET Dirección Individual
}
