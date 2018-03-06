/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import com.SIWeb.struts.LoginAction;
import com.mx.siweb.erp.restful.entity.Bodega;
import com.mx.siweb.erp.restful.entity.BodegaList;
import com.mx.siweb.erp.restful.entity.Empresa;
import com.mx.siweb.erp.restful.entity.EmpresaList;
import com.mx.siweb.erp.restful.entity.Sucursal;
import com.mx.siweb.erp.restful.entity.SucursalList;
import com.siweb.utilerias.json.JSONObject;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Sesiones;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.apache.logging.log4j.LogManager;

/**
 * REST Web Service
 *
 * @author ZeusGalindo
 */
@Path("mobilservice")
public class MobilserviceResource {

   @Context
   private UriInfo context;
   @Context
   private HttpServletRequest servletRequest;
   @Context
   private javax.servlet.ServletContext servletContext;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(MobilserviceResource.class.getName());

   /**
    * Creates a new instance of MobilserviceResource
    */
   public MobilserviceResource() {
   }

   /**
    * Retrieves representation of an instance of
    * com.mx.siweb.erp.restful.MobilserviceResource
    *
    * @param user Es el usuario
    * @param contrasenia Es el password o contrasenia
    * @return an instance of java.lang.String
    */
   @GET
   @Produces("application/json")
   public String getXml(
      @DefaultValue("") @QueryParam("usuario") String user, @DefaultValue("") @QueryParam("contrasenia") String contrasenia) {
      //Objeto json para almacenar los objetos
      JSONObject objJson = new JSONObject();

      //Objeto para validar la seguridad
      LoginAction action = new LoginAction();
      VariableSession varSesiones = new VariableSession(servletRequest);

      try {
         //Abrimos la conexion
         Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();

         String strResultado = "OK";
         //action.setBolSoloCliente(true);
         //solo evaluamos si el password esta lleno8
         if (!user.isEmpty() && !contrasenia.isEmpty()
            && !user.contains("'") && !contrasenia.contains("'")) {
            action.setBolEsBackOffice(false);
            action.authentication_user(oConn, user, contrasenia, servletRequest);
         } else {
            strResultado = "Faltan los datos de acceso";
            action.setBolTieneAcceso(false);
         }
         //Solo si tiene acceso
         if (action.isBolTieneAcceso()) {
            varSesiones.getVars();
            Sesiones.SetSession(servletRequest, "Acceso_mobil", "1");
            objJson.put("Status", strResultado);
            objJson.put("Codigo", servletRequest.getSession().getId());
            objJson.put("Usuario", varSesiones.getStrUser());
            objJson.put("IdCliente", varSesiones.getintIdCliente());
         } else {
            varSesiones.SetVars(0, 0, 0, 0, "", "", 0, "", 0);
            //Determinamos el error de acceso
            if (action.isBolErrorLogged()) {
               strResultado = "Error el usuario ya esta logueado";
            } else if (action.isBolErrorBloqued()) {
               strResultado = "Error el usuario esta bloqueado favor de esperar";
            } else {
               strResultado = "Error el usuario no tiene permiso de acceso";
            }
            Sesiones.SetSession(servletRequest, "Acceso_mobil", "0");
            objJson.put("Status", strResultado);
            objJson.put("Codigo", "XXXXXXXXX");
         }
         oConn.close();
      } catch (Exception ex) {
         this.log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
      }
      return objJson.toString();
   }

   /**
    * Retrieves representation of an instance of
    * com.mx.siweb.erp.restful.entity.EmpresaList
    *
    * @param strCodigo Es el codigo de la sesion
    * @return an instance of java.lang.String
    */
   @GET
   @Path("/EmpresaList")
   @Produces("application/json")
   public EmpresaList getJsonEmpresaList(@DefaultValue("") @QueryParam("Codigo") String strCodigo) {
      EmpresaList lista = new EmpresaList();
      VariableSession varSesiones = new VariableSession(servletRequest);
      try {
         //Abrimos la conexión
         Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();
         //Se valida si se tiene acceso 
         EvalSesion eval = new EvalSesion();
         log.debug("Validando secion");
         if (eval.evaluaSesion(strCodigo, oConn)) {
            varSesiones.SetVars(eval.getIntIdUser(), 0, 0, 0, "", "", 0, "", 0);
            log.debug("Seccion Valida");
            varSesiones.getVars();
            //log.debug("Permiso Valido");
            String consulta = "Select vta_userempresa.EMP_ID,vta_empresas.EMP_RAZONSOCIAL FROM vta_userempresa, vta_empresas WHERE \n"
               + " vta_userempresa.EMP_ID =  vta_empresas.EMP_ID AND ID_USUARIOS = " + varSesiones.getIntNoUser();
            ResultSet rs = oConn.runQuery(consulta);
            while (rs.next()) {
               Empresa deta = new Empresa();
               deta.setId_empresa(rs.getInt("EMP_ID"));
               deta.setRazon_social(rs.getString("EMP_RAZONSOCIAL"));
               lista.getEmpresaItem().add(deta);
            }//Fin WHILE
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();
         }//Fin SESION VALIDA
         oConn.close();
      }//Fin TRY
      catch (Exception e) {
         log.error("Etiquetas resources...." + e.getMessage() + " " + e.getLocalizedMessage());
      }
      return lista;
   }

   /**
    * Retrieves representation of an instance of
    * com.mx.siweb.erp.restful.entity.SucursalList
    *
    * @param strCodigo Es el codigo de la sesion
    * @param strEmpresa Es la empresa de la obtendremos las sucursales
    * @return an instance of java.lang.String
    */
   @GET
   @Path("/SucursalList")
   @Produces("application/json")
   public SucursalList getJsonSucursalList(@DefaultValue("") @QueryParam("Codigo") String strCodigo,
      @DefaultValue("") @QueryParam("Empresa") String strEmpresa) {
      SucursalList lista = new SucursalList();
      VariableSession varSesiones = new VariableSession(servletRequest);
      try {
         //Abrimos la conexión
         Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();
         //Se valida si se tiene acceso 
         EvalSesion eval = new EvalSesion();
         log.debug("Validando secion");
         if (eval.evaluaSesion(strCodigo, oConn)) {
            varSesiones.SetVars(eval.getIntIdUser(), 0, 0, 0, "", "", 0, "", 0);
            log.debug("Seccion Valida");
            varSesiones.getVars();
            //log.debug("Permiso Valido");
            String consulta = "Select vta_sucursales_master.SM_ID,vta_sucursales_master.SM_NOMBRE,vta_sucursales_master.SM_CODIGO "
               + " FROM vta_sucursales_master,usuarios_suc_master WHERE \n"
               + "  usuarios_suc_master.id_usuarios = " + varSesiones.getIntNoUser() + " "
               + " AND vta_sucursales_master.SM_ID = usuarios_suc_master.SM_ID AND vta_sucursales_master.EMP_ID = " + strEmpresa;
            ResultSet rs = oConn.runQuery(consulta);
            while (rs.next()) {
               Sucursal deta = new Sucursal();
               deta.setId_sucursal(rs.getInt("SM_ID"));
               deta.setNombre_sucursal(rs.getString("SM_NOMBRE"));
               deta.setCodigo_sucursal(rs.getString("SM_CODIGO"));
               deta.setId_empresa(Integer.valueOf(strEmpresa));
               lista.getSucursalItem().add(deta);
            }//Fin WHILE
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();
         }//Fin SESION VALIDA
         oConn.close();
      }//Fin TRY
      catch (Exception e) {
         log.error("Etiquetas resources...." + e.getMessage() + " " + e.getLocalizedMessage());
      }
      return lista;
   }

   /**
    * Retrieves representation of an instance of
    * com.mx.siweb.erp.restful.entity.BodegaList
    *
    * @param strCodigo Es el codigo de la sesion
    * @param strEmpresa Es la empresa de la obtendremos las sucursales
    * @param strSucursal Es la sucursal de la que obtendremos las bodegas
    * @return an instance of java.lang.String
    */
   @GET
   @Path("/BodegaList")
   @Produces("application/json")
   public BodegaList getJsonBodegaList(@DefaultValue("") @QueryParam("Codigo") String strCodigo,
      @DefaultValue("") @QueryParam("Empresa") String strEmpresa,
      @DefaultValue("") @QueryParam("Sucursal") String strSucursal) {
      BodegaList lista = new BodegaList();
      VariableSession varSesiones = new VariableSession(servletRequest);
      try {
         //Abrimos la conexión
         Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();
         //Se valida si se tiene acceso 
         EvalSesion eval = new EvalSesion();
         log.debug("Validando secion");
         if (eval.evaluaSesion(strCodigo, oConn)) {
            varSesiones.SetVars(eval.getIntIdUser(), 0, 0, 0, "", "", 0, "", 0);
            log.debug("Seccion Valida");
            varSesiones.getVars();
            //log.debug("Permiso Valido");
            String consulta = "Select vta_sucursal.SC_ID,vta_sucursal.SC_CLAVE,vta_sucursal.SC_NOMBRE FROM usuarios_sucursales, vta_sucursal where " +
            "usuarios_sucursales.SC_ID = vta_sucursal.SC_ID AND usuarios_sucursales.id_usuarios = " + varSesiones.getIntNoUser() + " "
               + " AND vta_sucursal.EMP_ID = " + strEmpresa;
            if (!strSucursal.isEmpty()) {
               if (!strSucursal.equals("0")) {
                  consulta = "SELECT  vta_sucursal.SC_ID,vta_sucursal.SC_NOMBRE,vta_sucursal.SC_CLAVE "
                    + " FROM vta_sucursal_master_as,  vta_sucursal, usuarios_sucursales"
                    + " where  vta_sucursal_master_as.SC_ID =  vta_sucursal.SC_ID "
                     + " AND usuarios_sucursales.SC_ID = vta_sucursal.SC_ID AND usuarios_sucursales.id_usuarios = " + varSesiones.getIntNoUser() + " "
                    + " and vta_sucursal_master_as.SM_ID = " + strSucursal;
               }
            }
            ResultSet rs = oConn.runQuery(consulta);
            while (rs.next()) {
               Bodega deta = new Bodega();
               deta.setId_bodega(rs.getInt("SC_ID"));
               deta.setNombre_bodega(rs.getString("SC_NOMBRE"));
               deta.setCodigo_bodega(rs.getString("SC_CLAVE"));
               deta.setId_empresa(Integer.valueOf(strEmpresa));
               if (!strSucursal.isEmpty()) {
                  if (!strSucursal.equals("0")) {
                     deta.setId_sucursal(Integer.valueOf(strSucursal));
                  }
               }
               lista.getBodegaItem().add(deta);
            }//Fin WHILE
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();
         }//Fin SESION VALIDA
         oConn.close();
      }//Fin TRY
      catch (Exception e) {
         log.error("Etiquetas resources...." + e.getMessage() + " " + e.getLocalizedMessage());
      }
      return lista;
   }
}
