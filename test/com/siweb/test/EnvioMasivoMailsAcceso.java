/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siweb.test;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Mail;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase se encarga del envio masivo de mails a los clientes
 *
 * @author aleph_79
 */
public class EnvioMasivoMailsAcceso {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void EnvioMasivoAccesos(Conexion oConn) {
      StringBuilder strLstMail = new StringBuilder();
      String strSqlUsuarios = "SELECT EMAIL FROM usuarios WHERE BOL_MAIL_INGRESOS=1";
      try {
         ResultSet rs = oConn.runQuery(strSqlUsuarios);

         while (rs.next()) {
            if (!rs.getString("EMAIL").equals("")) {
               strLstMail.append(rs.getString("EMAIL")).append(",");
            }
         }

         rs.close();
      } catch (SQLException ex) {
         //this.strResultLast = "ERROR:" + ex.getMessage();
         ex.fillInStackTrace();
      }
      try {
         String strSql = "select CT_ID,CT_EMAIL1,CT_EMAIL2 from vta_cliente where CT_TXTIVA='@'";
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            String strEmail1 = rs.getString("CT_EMAIL1");
            String strEmail2 = rs.getString("CT_EMAIL2");
            //validamos que hallan puesto el mail
            Mail mail = new Mail();
            if (!strEmail1.isEmpty() || !strEmail2.isEmpty()) {
               StringBuilder strLstMail2 = new StringBuilder(strLstMail.toString());

               //Validamos si el mail del cliente es valido
               boolean bolUsoCliente = false;
               if (mail.isEmail(strEmail1)) {
                  strLstMail2.append(",").append(strEmail1);
                  bolUsoCliente = true;
               }
               if (mail.isEmail(strEmail2)) {
                  strLstMail2.append(",").append(strEmail2);
                  bolUsoCliente = true;
               }
               //Solo si es valido algun mail del cliente
               if (bolUsoCliente) {
                  //Intentamos mandar el mail
                  mail.setBolDepuracion(false);
                  mail.getTemplate("MSG_ING", oConn);
                  mail.getMensaje();
                  String strSqlEmp = "SELECT * FROM vta_cliente"
                          + " where CT_ID=" + rs.getInt("CT_ID") + "";
                  try {
                     ResultSet rs3 = oConn.runQuery(strSqlEmp);
                     mail.setReplaceContent(rs3);
                     rs3.close();
                  } catch (SQLException ex) {
                     //this.strResultLast = "ERROR:" + ex.getMessage();
                     ex.fillInStackTrace();
                  }
                  mail.setDestino(strLstMail2.toString());
                  boolean bol = mail.sendMail();
                  if (bol) {
                     //strResp = "MAIL ENVIADO.";
                  } else {
                     //strResp = "FALLO EL ENVIO DEL MAIL.";
                  }
               }
            } else {
               //strResp = "ERROR: INGRESE UN MAIL";
            }

         }
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(EnvioMasivoMailsAcceso.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
   // </editor-fold>
}
