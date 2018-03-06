/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import com.mx.siweb.erp.restful.entity.CambioPassword;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import java.sql.ResultSet;
import nl.captcha.Captcha;
import comSIWeb.Operaciones.bitacorausers;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.Mail;
import java.sql.SQLException;

/**
 * REST Web Service
 *
 * @author siweb
 */
@Path("CambioPassword")
public class CambioPasswordResource {

   @Context
   private UriInfo context;
   @Context
   private HttpServletRequest servletRequest;
   @Context
   private javax.servlet.ServletContext servletContext;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CambioPasswordResource.class.getName());

   /**
    * Creates a new instance of CambioPasswordResource
    */
   public CambioPasswordResource() {
   }

   /**
    * Retrieves representation of an instance of
    * com.mx.siweb.erp.restful.CambioPasswordResource
    *
    * @return an instance of java.lang.String
    */
   /**
    * PUT method for updating or creating an instance of CambioPasswordResource
    *
    * @param content representation for the resource
    * @return an HTTP response with content of the updated or created resource.
    */
   @POST
   @Consumes({"application/xml", "application/json"})
   public Response putJson(CambioPassword c) {

      //Accedemos a la base de datos para ver si tenemos acceso
      VariableSession varSesiones = new VariableSession(servletRequest);
      varSesiones.getVars();

      EvalSesion eval = new EvalSesion();

      try {
         //Abrimos la conexion
         Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();

         String strResp = "OK";
         servletRequest.setCharacterEncoding("UTF-8"); // Do this so we can capture non-Latin chars

         String idCliente = c.getIdCliente();
         String codigo = c.getCodigo();
         String strCaptcha = c.getCaptcha();
         String strNvoPass = c.getPassword();
         String strNvoPassConfirm = c.getPasswordConfirm();
         String strANTPass = c.getPasswordAnterior();//Password tecleado
         String strANTPassBD = "";// Password que se obtiene de la BD

         if (eval.evaluaSesion(codigo, oConn)) {
            //Accedemos a la base de datos para ver si tenemos acceso
            varSesiones.getVars();
            if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {

               boolean identificador;

               if (c.getPassword() == null) {
                  strNvoPass = "";
               }
               if (strNvoPassConfirm == null) {
                  strNvoPassConfirm = "";
               }
               if (strANTPass == null) {
                  strANTPass = "";
               }



               //Evaluar al cliente o usuario para la contraseña 
               //Si es != 0 es cliente == true
               //Si no es usuario == false

               if (eval.isBolEsCliente() ) {
                  identificador = true;
               } else {
                  identificador = false;
               }

               System.out.println(identificador + "---");
               if (identificador == true) {
                  // <editor-fold defaultstate="collapsed" desc="Cliente">
                  try {
                     System.out.println("///////////////////////////////////////Cliente");
                     //Contrasena anterior
                     String strcontr = "select ct_password from vta_cliente  where CT_ID = '" + idCliente + "';";
                     ResultSet rs1 = oConn.runQuery(strcontr, true);
                     while (rs1.next()) {
                        strANTPassBD = rs1.getString("ct_password");
                        System.out.println("//////" + strANTPass);
                        if (strANTPassBD == null) {
                           strANTPassBD = "";
                        }
                     }
                     rs1.close();
                  } catch (SQLException ex) {
                     log.debug(ex.getMessage());
                  }
                  // </editor-fold>
               } else {
                  // <editor-fold defaultstate="collapsed" desc="Usuario">
                  try {
                     //Contrasena anterior
                     System.out.println("///////////////////////////////////////Usuario");
                     String strcontr = "select password from usuarios  where id_usuarios = " + varSesiones.getintIdCliente() + ";";
                     ResultSet rs1 = oConn.runQuery(strcontr, true);
                     while (rs1.next()) {
                        strANTPassBD = rs1.getString("password");
                        System.out.println("PASSWORD viejo: " + strANTPass);
                     }
                     if (strANTPassBD == null) {
                        strANTPassBD = "";
                     }
                     rs1.close();
                  } catch (SQLException ex) {
                     log.debug(ex.getMessage());
                  }
                  // </editor-fold>
               }

               // <editor-fold defaultstate="collapsed" desc="Evalua que el password anterior coincida">
               System.out.println("PASSWORD viejo: " +strANTPass);
               System.out.println("PASSWORD viejo BD: " +strANTPassBD);
               System.out.println("CAPTCHA obtenido android: " +strCaptcha);
               if (strANTPassBD.equals(strANTPass)) {
                  Captcha captcha = (Captcha) servletRequest.getSession().getAttribute(Captcha.NAME);
                  System.out.println("captcha del servlet: " + captcha);
             //    System.out.println("captcha:" + captcha.getAnswer());
//                  if (captcha.isCorrect(strCaptcha)) {
                     // <editor-fold defaultstate="collapsed" desc="evaluamos el nuevo password">
                     if (strNvoPass.equals(strNvoPassConfirm)) {
                         System.out.println(strNvoPass);
                         System.out.println(strNvoPassConfirm);
                        //Acctualizamos el nuevo password
                        if (identificador == true) {
                           // <editor-fold defaultstate="collapsed" desc="Cliente">
                           String strUpdate = "UPDATE vta_cliente set ct_password = '" + strNvoPass + "' "
                                   + "where CT_ID = '" + varSesiones.getintIdCliente() + "'";
                           oConn.runQueryLMD(strUpdate);

                           //Bitacora de acciones
                           bitacorausers logUser = new bitacorausers();
                           Fechas fecha = new Fechas();
                           logUser.setFieldString("BTU_FECHA", fecha.getFechaActual());
                           logUser.setFieldString("BTU_HORA", fecha.getHoraActual());
                           logUser.setFieldString("BTU_NOMMOD", "PASSWORD");
                           logUser.setFieldString("BTU_NOMACTION", "CHANGE");
                           logUser.setFieldInt("BTU_IDOPER", varSesiones.getintIdCliente());
                           logUser.setFieldInt("BTU_IDUSER", varSesiones.getintIdCliente());
                           logUser.setFieldString("BTU_NOMUSER", varSesiones.getStrUser());
                           ///
                           logUser.Agrega(oConn);

                           //Buscamos el mail en la bd
                           boolean bolEncontro = false;
                           String strMail = "";
                           String strSql = "select * from usuarios  where id_usuarios='" + varSesiones.getintIdCliente() + "'";
                           ResultSet rs = oConn.runQuery(strSql, true);
                           while (rs.next()) {
                              bolEncontro = true;
                              strMail = rs.getString("EMAIL");
                           }
                           //Si encontro el mail mandamos el password
                           if (bolEncontro) {
                              //validamos que hallan puesto el mail
                              Mail mail = new Mail();
                              if (!strMail.equals("")) {
                                 if (mail.isEmail(strMail)) {
                                    //Intentamos mandar el mail
                                    mail.setBolDepuracion(false);
                                    mail.getTemplate("CHANGEPASS", oConn);
                                    mail.setReplaceContent(rs);
                                    mail.setDestino(strMail);
                                    boolean bol = mail.sendMail();
                                    if (bol) {
                                       strResp += " MAIL ENVIADO.";
                                    } else {
                                       strResp += " FALLO EL ENVIO DEL MAIL.";
                                    }
                                 } else {
                                    strResp += " NO TIENE MAIL VALIDO";
                                 }
                              } else {
                                 //strResp = "ERROR: INGRESE UN MAIL";
                              }
                           } else {
                              //strResp = "ERROR:EL MAIL DEL USUARIO NO SE ENCONTRO";
                           }
                           // </editor-fold>

                        } else {
                           // <editor-fold defaultstate="collapsed" desc="Usuario">
                           String strUpdate = "UPDATE usuarios set password = '" + strNvoPass + "' "
                                   + "where id_usuarios = '" + varSesiones.getIntNoUser() + "'";
                           oConn.runQueryLMD(strUpdate);

                           //Bitacora de acciones
                           bitacorausers logUser = new bitacorausers();
                           Fechas fecha = new Fechas();
                           logUser.setFieldString("BTU_FECHA", fecha.getFechaActual());
                           logUser.setFieldString("BTU_HORA", fecha.getHoraActual());
                           logUser.setFieldString("BTU_NOMMOD", "PASSWORD");
                           logUser.setFieldString("BTU_NOMACTION", "CHANGE");
                           logUser.setFieldInt("BTU_IDOPER", varSesiones.getIntNoUser());
                           logUser.setFieldInt("BTU_IDUSER", varSesiones.getIntNoUser());
                           logUser.setFieldString("BTU_NOMUSER", varSesiones.getStrUser());
                           logUser.Agrega(oConn);

                           //Buscamos el mail en la bd
                           boolean bolEncontro = false;
                           String strMail = "";
                           String strSql = "select * from usuarios  where id_usuarios='" + varSesiones.getIntNoUser() + "'";
                           ResultSet rs = oConn.runQuery(strSql, true);
                           while (rs.next()) {
                              bolEncontro = true;
                              strMail = rs.getString("EMAIL");
                           }
                           //Si encontro el mail mandamos el password
                           if (bolEncontro) {
                              //validamos que hallan puesto el mail
                              Mail mail = new Mail();
                              if (!strMail.equals("")) {
                                 if (mail.isEmail(strMail)) {
                                    //Intentamos mandar el mail
                                    mail.setBolDepuracion(false);
                                    mail.getTemplate("CHANGEPASS", oConn);
                                    mail.setReplaceContent(rs);
                                    mail.setDestino(strMail);
                                    boolean bol = mail.sendMail();
                                    if (bol) {
                                       strResp += "MAIL ENVIADO.";
                                    } else {
                                       strResp += "FALLO EL ENVIO DEL MAIL.";
                                    }
                                 } else {
                                    strResp += "NO TIENE UN MAIL VALIDO";
                                 }
                              } else {
                                 //strResp = "ERROR: INGRESE UN MAIL";
                              }
                           } else {
                              //strResp = "ERROR:EL MAIL DEL USUARIO NO SE ENCONTRO";
                           }
                           // </editor-fold>
                        }

                     } else {
                        strResp = "ERROR:LAS CONTRASEÑAS NUEVAS NO COINCIDEN";
                     }
                     // </editor-fold>
//                  } else {
//                     strResp = "ERROR:EL CAPTCHA NO COINCIDE";
//                  }
               } else {
                  strResp = "ERROR:EL PASSWORD ANTERIOR NO COINCIDE";
               }
               // </editor-fold>

            } else {
               strResp = "ERROR:ACCESO DENEGADO";
            }
         } else {
            strResp = "ERROR: SESION NO VALIDA";

         }
         c.setCodigo(strResp);
         oConn.close();
      } catch (Exception ex) {
         log.error("MobilService cambio password" + ex.getMessage() + " " + ex.getLocalizedMessage());
      }

      return Response.ok(c).build();
   }
}
