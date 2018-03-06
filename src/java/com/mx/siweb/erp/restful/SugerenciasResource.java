/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import Tablas.vta_sugerencias;
import com.mx.siweb.erp.restful.entity.Sugerencias;
import com.mx.siweb.erp.restful.entity.SugerenciasDeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.Mail;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.logging.log4j.LogManager;

/**
 * REST Web Service
 *
 * @author ZeusGalindo
 */
@Path("Sugerencias")
public class SugerenciasResource {

   @Context
   private UriInfo context;
   @Context
   private HttpServletRequest servletRequest;
   @Context
   private javax.servlet.ServletContext servletContext;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SugerenciasResource.class.getName());

   /**
    * Creates a new instance of SugerenciasResource
    */
   public SugerenciasResource() {
   }

   /**
    * Retrieves representation of an instance of
    * com.mx.siweb.erp.restful.SugerenciasResource
    *
     * @param idSugerencia
     * @param strCodigo
    * @return an instance of com.mx.siweb.erp.restful.client.entity.Sugerencias
    */
   @GET
   @Path("/SugerenciaIndividual")
   @Produces("application/json")
   public SugerenciasDeta getJson(@DefaultValue("") @QueryParam("idSugerencia") String idSugerencia,
           @DefaultValue("") @QueryParam("Codigo") String strCodigo) {
      SugerenciasDeta sugerenciasDeta = new SugerenciasDeta();
      //Accedemos a la base de datos para ver si tenemos acceso
      VariableSession varSesiones = new VariableSession(servletRequest);

      try {
         //Abrimos la conexión
         Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();
         //Se valida si se tiene acceso 
         EvalSesion eval = new EvalSesion();
         log.debug("Validando secion");
         if (eval.evaluaSesion(strCodigo, oConn)) {
            log.debug("Sesion Valida");
            varSesiones.getVars();
            if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
                
                log.debug("Permiso Valido");
               String consulta = "select * from "
                       + "vta_sugerencias where SUG_ID = "
                       + idSugerencia + ";";
               ResultSet rs = oConn.runQuery(consulta);
               while (rs.next()) {
                  sugerenciasDeta.setsug_id(rs.getInt("SUG_ID"));
                  sugerenciasDeta.setfecha(rs.getString("SUG_FECHA"));
                  sugerenciasDeta.setDirigido(rs.getString("SUG_DIRIGIDO"));
                  sugerenciasDeta.setCorreo(rs.getString("SUG_CORREO"));
                  sugerenciasDeta.setComentario(rs.getString("SUG_COMENTARIO"));
                  sugerenciasDeta.setEstatus(rs.getString("SUG_ESTATUS"));
                  sugerenciasDeta.setCt_id(rs.getInt("CT_ID"));
                  sugerenciasDeta.setSolucion(rs.getString("SUG_SOLUCION"));
               }
            }
         }
         oConn.close();
      } catch (Exception e) {
         log.error("MobilServiceLogin1" + e.getMessage() + " " + e.getLocalizedMessage());
      }     
      return sugerenciasDeta;
   }

   /**
    * PUT method for updating or creating an instance of SugerenciasResource
    *
    * @param content representation for the resource
    * @return an HTTP response with content of the updated or created resource.
    */
   @POST
   @Consumes("application/json")
   public Response putJson(SugerenciasDeta s) throws Exception {
      //Accedemos a la base de datos para ver si tenemos acceso
      VariableSession varSessiones = new VariableSession(servletRequest);
      varSessiones.getVars();
      //Abrimos la conexion
      Conexion oConn = new Conexion(varSessiones.getStrUser(), servletContext);
      oConn.open();

      EvalSesion eval = new EvalSesion();
      if (eval.evaluaSesion(s.getCodigo(), oConn)) {
         varSessiones.getVars();
            if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
            try {
               Fechas fecha = new Fechas();
               String strFecha = fecha.getFechaActual();
               String strDirigido = s.getdirigido();
               String strCorreo = s.getCorreo();
               String strTexto = s.getComentario();
               String strEstatus = s.getEstatus();
               int CT_ID = s.getCt_id();
               String strSolucion = s.getSolucion();
               log.debug("Se asignan las variables");
               //Creamos el Objeto
               vta_sugerencias suger = new vta_sugerencias();
               suger.setFieldString("SUG_FECHA", strFecha);
               suger.setFieldString("SUG_DIRIGIDO", strDirigido);
               suger.setFieldString("SUG_CORREO", strCorreo);
               suger.setFieldString("SUG_COMENTARIO", strTexto);
               suger.setFieldString("SUG_ESTATUS", strEstatus);
               suger.setFieldInt("CT_ID", CT_ID);
               suger.setFieldString("SUG_SOLUCION", strSolucion);               

               String strResult = "";
               //Generamos una alta

               strResult = suger.Agrega(oConn);
               log.debug("Se asignan las variables" + suger);
               String strSqlUsuarios = "";
               ResultSet rs;

               if (strResult.equals("OK")) {
                  log.debug("se agrega conexion");
                  s.setsug_id(Integer.valueOf(suger.getValorKey()));
                  Mail mail = new Mail();
                  if (!strCorreo.isEmpty()) {
                     String strLstMail = "";
                     //Validamos si el mail del cliente es valido
                     log.debug("Mail valido");
                     if (mail.isEmail(strCorreo)) {
                        strLstMail += "," + strCorreo;
                     }
                     log.debug("Query email de usuario");
                     strSqlUsuarios = "SELECT EMAIL FROM usuarios WHERE BOL_MAIL_SUGERENCIAS=1";
                     try {
                        rs = oConn.runQuery(strSqlUsuarios);

                        while (rs.next()) {
                           if (!rs.getString("EMAIL").equals("")) {
                              strLstMail += "," + rs.getString("EMAIL");
                           }
                        }
                        rs.close();
                     } catch (SQLException ex) {
                        //this.strResultLast = "ERROR:" + ex.getMessage();
                        ex.fillInStackTrace();
                     }
                     //Intentamos mandar el mail
                     mail.setBolDepuracion(false);
                     mail.getTemplate("MSG_SUG", oConn);
                     mail.getMensaje();
                     mail.setDestino(strLstMail);
                     String strSqlEmp = "SELECT SUG_ID,DATE_FORMAT(CURDATE(), '%d/%m/%Y') as FECHA FROM vta_sugerencias"
                             + " where SUG_ID=" + suger.getValorKey() + "";
                     try {
                        rs = oConn.runQuery(strSqlEmp);
                        mail.setReplaceContent(rs);
                        rs.close();
                     } catch (SQLException ex) {
                        //this.strResultLast = "ERROR:" + ex.getMessage();
                        ex.fillInStackTrace();
                     }
                     mail.setDestino(strLstMail);

                     boolean bol = mail.sendMail();
                     if (bol) {
                        log.debug("Termino el registro de la sugerencia");
                        log.debug("Se ha registrado su sugerencia ");
                     } else {
                        log.error("Fallo el Envio del mail ");
                     }
                  } else {
                     log.error("Error ingrese un mail");
                  }
               } else {
                  log.error("Error al ingresar su mensaje");
               }
               s.setCodigo(strResult);
            } catch (Exception e) {
               log.error("Error al iniciar sesion");
            }
         }
         oConn.close();
      }
      return Response.ok(s).build();
   }
   
   
   @GET
   @Path("/ConsultaSugerencias")
   @Produces("application/json")
   public Sugerencias getJSON(@DefaultValue("") @QueryParam("idCliente") String strIdCliente,
                              @DefaultValue("") @QueryParam("Codigo") String strCodigo){
       Sugerencias sugerencias = new Sugerencias();
       
      //Accedemos a la base de datos para ver si tenemos acceso
      VariableSession varSessiones = new VariableSession(servletRequest);
      try{
          Conexion oConn = new Conexion(varSessiones.getStrUser(), servletContext);
          oConn.open();
          //Validamos Acceso
          EvalSesion eval = new EvalSesion();
          String strResp = "OK";
          log.debug("Validando Sesión");
          if (eval.evaluaSesion(strCodigo, oConn)) {
              log.debug("Sesion Valida");
              varSessiones.getVars();
              if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
                  log.debug("Permiso Valido");
                  int CT_ID = 0;
                  CT_ID = Integer.parseInt(strIdCliente);
                  
                  String consulta = "select * from vta_sugerencias where "
                     + "CT_ID = " + CT_ID + ";";

                  log.debug(consulta);
                  ResultSet rs = oConn.runQuery(consulta);
                  while(rs.next()){
                      SugerenciasDeta sugDeta = new SugerenciasDeta();
                      sugDeta.setsug_id(rs.getInt("SUG_ID"));
                      sugDeta.setfecha(rs.getString("SUG_FECHA"));
                      sugDeta.setDirigido(rs.getString("SUG_DIRIGIDO"));
                      sugDeta.setCorreo(rs.getString("SUG_CORREO"));
                      sugDeta.setComentario(rs.getString("SUG_COMENTARIO"));
                      sugDeta.setEstatus(rs.getString("SUG_ESTATUS"));
                      sugDeta.setSolucion(rs.getString("SUG_SOLUCION"));
                      sugDeta.setCt_id(rs.getInt("CT_ID"));
                      sugerencias.getSugerenciasItem().add(sugDeta);
                  }
                  rs.close();
                  sugerencias.setCt_id(CT_ID);
                  sugerencias.setCodigo("OK");
              }//Fin evalua Permiso
              
              
          }//Fin valida Sesión
          else {
            log.error("Error:Access Denied");
            strResp = "Error:Access Denied";
            sugerencias.setCodigo("Error:Access Denied");
         }
          oConn.close();
      }catch (Exception e) {
         log.error("MobilServiceLogin1" + e.getMessage() + " " + e.getLocalizedMessage());
      }
       return sugerencias;
   }
}