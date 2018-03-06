/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import com.mx.siweb.erp.restful.entity.Clientes;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Mail;
import comSIWeb.Utilerias.generateData;
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
@Path("Clientes")
public class ClientesResource {
   
   @Context
   private UriInfo context;
   @Context
   private HttpServletRequest servletRequest;
   @Context
   private javax.servlet.ServletContext servletContext;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ClientesResource.class.getName());

   /**
    * Creates a new instance of ClientesResource
    */
   public ClientesResource() {
   }

   /**
    * Retrieves representation of an instance of
    * com.mx.siweb.erp.restful.ClientesResource
    *
    * @param idCliente
    * @param strCodigo
    * @return an instance of com.mx.siweb.erp.restful.entity.Clientes
    */
   @GET
   @Produces("application/json")
   public Clientes getJson(@DefaultValue("") @QueryParam("idCliente") String idCliente, 
                           @DefaultValue("") @QueryParam("codigo") String strCodigo) {
      Clientes cliente = new Clientes();
      //Accedemos a la base de datos para ver si tenemos acceso
      VariableSession varSesiones = new VariableSession(servletRequest);
      try {
         //Abrimos la conexion
         Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();
         //Validamos si tenemos acceso
         String strResp = "OK";
         EvalSesion eval = new EvalSesion();
         if (eval.evaluaSesion(strCodigo, oConn)) {
             varSesiones.getVars();
            if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
               log.debug("Permiso Valido");
               cliente.setId(Integer.valueOf(idCliente));
               //Evaluamos sino envia el id mostramos todos
                    if (cliente.getId() == 0) {
                  cliente.setId(eval.getIntIdUser());
               }
               //anadir validacion para que solo muestre clientes que estan debajo del nodo
               
               cliente.ObtenerDatos(oConn);
               
                } else {
               strResp = "Error:Acceso denegado";
            }
            cliente.setCodigo(strCodigo);
         }         
         oConn.close();
      } catch (Exception ex) {
         log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
      }
      return cliente;
   }

   /**
    * POST method for updating or creating an instance of ClientesResource
    *
    * @param p
    * @return an HTTP response with content of the updated or created resource.
    */
    @POST
    @Consumes("application/json")
    public Response putJson(Clientes p) {
        //Accedemos a la base de datos para ver si tenemos acceso
        VariableSession varSesiones = new VariableSession(servletRequest);
        varSesiones.getVars();
        try {
            //Abrimos la conexion
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();
            //Validamos si tenemos acceso
            EvalSesion eval = new EvalSesion();
            String strResp = "OK";
            if (eval.evaluaSesion(p.getCodigo(), oConn)) {
                if (eval.evaluaPermiso(1, oConn) || eval.isBolEsCliente()) {
                    //Evaluamos sino envia el id mostramos todos
                    if (p.getId() == 0 && p.getTipoOperacion().equals("M")) {
                        p.setId(eval.getIntIdUser());
                    }
                    if (p.getIdUpline() == 0 && p.getTipoOperacion().equals("A")) {
                        p.setIdUpline(eval.getIntIdUser());

                    }//Fin IFGetUpLine
                    p.AlmacenaDatos(oConn);

                    String strEmail1 = p.getEmail1();
                    String strSqlUsuarios = "";
                    //validamos que hallan puesto el mail
                    Mail mail = new Mail();
                    if (!strEmail1.isEmpty()) {
                        String strLstMail = "";
                        //Validamos si el mail del cliente es valido
                        if (mail.isEmail(strEmail1)) {
                            strLstMail += "," + strEmail1;
                        }

                        strSqlUsuarios = "SELECT EMAIL FROM usuarios WHERE BOL_MAIL_INGRESOS=1";
                        try {
                            ResultSet rs11 = oConn.runQuery(strSqlUsuarios);

                            while (rs11.next()) {
                                if (!rs11.getString("EMAIL").equals("")) {
                                    strLstMail += "," + rs11.getString("EMAIL");
                                }
                            }

                            rs11.close();
                        } catch (SQLException ex) {
                            //this.strResultLast = "ERROR:" + ex.getMessage();
                            ex.fillInStackTrace();
                        }
                        //Intentamos mandar el mail
                        mail.setBolDepuracion(false);
                        mail.getTemplate("MSG_ING", oConn);
                        mail.getMensaje();
                        String strSqlEmp = "SELECT * FROM vta_cliente"
                           + " where CT_ID=" + p.getId() + "";
                        try {
                            ResultSet rs12 = oConn.runQuery(strSqlEmp);
                            mail.setReplaceContent(rs12);
                            rs12.close();
                        } catch (SQLException ex) {
                            //this.strResultLast = "ERROR:" + ex.getMessage();
                            ex.fillInStackTrace();
                        }
                        mail.setDestino(strLstMail);
                        boolean bol = mail.sendMail();
                        if (bol) {
                            strResp = "MAIL ENVIADO.";
                        } else {
                            strResp = "FALLO EL ENVIO DEL MAIL.";
                        }

                    } else {
                        strResp = "ERROR: INGRESE UN MAIL";
                    }
                } else {
                    strResp = "Error:Access Denied";
                }
            } else {
                strResp = "Error:Access Denied";
            }
            p.setCodigo(strResp);

            oConn.close();
        } catch (Exception ex) {
            log.error("ClientesResource" + ex.getMessage() + " " + ex.getLocalizedMessage());
        }
        return Response.ok(p).build();
    }
}
