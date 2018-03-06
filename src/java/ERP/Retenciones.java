/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.Rhh_Ret_Retenciones;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Mail;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author siweb
 */
public class Retenciones extends ProcesoMaster implements ProcesoInterfaz {

   protected TableMaster document;
   protected String strPATHXml;
   private static final Logger log = LogManager.getLogger(Retenciones.class.getName());

   public TableMaster getDocument() {
      return document;
   }

   public void setDocument(TableMaster document) {
      this.document = document;
   }

   public Retenciones(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
   }

   @Override
   public void Init() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrx() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrxAnul() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrxRevive() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrxMod() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   public void doEnvioMasivo(int intEmp_id, String strFolioIni, String strFolioFin, String strCorreo, int intCopia, String  strPATHXml) {
      this.strResultLast = "OK";
      this.strPATHXml = strPATHXml;
      //Recuperamos valores para el envio del correo
      document = new Rhh_Ret_Retenciones();
      String strSql = "select RET_ID,EMP_ID from rhh_ret_retenciones where RET_SE_TIMBRO <> 0 "
              + "and RET_FOLIOINT >= '" + strFolioIni + "' "
              + "and RET_FOLIOINT <= '" + strFolioFin + "' "
              + "and EMP_ID =" + intEmp_id;

      ResultSet rs;
      try {
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            //Recuperamos el ID
            int intRET_ID = rs.getInt("RET_ID");
            //Buscamos el e-mail del empleado
            String strCT_EMAIL1 = "";
            String strCT_EMAIL2 = "";
            strSql = "SELECT EMP_EMAIL1,EMP_EMAIL2 FROM rhh_empleados "
                    + " where EMP_NUM=" + rs.getInt("EMP_ID") + "";

            try {
               ResultSet rs2 = oConn.runQuery(strSql, true);
               while (rs2.next()) {
                  strCT_EMAIL1 = rs2.getString("EMP_EMAIL1");
                  strCT_EMAIL2 = rs2.getString("EMP_EMAIL2");
               }
               rs2.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               log.error(ex.getMessage());
            }

            //Evaluamos si hay correo donde enviar
            if (!strCT_EMAIL1.isEmpty() || !strCT_EMAIL1.isEmpty() || (!strCorreo.isEmpty() && intCopia == 1)) {
               this.document.ObtenDatos(intRET_ID, oConn);
               //Objeto de mail
               Mail mail = new Mail();
               String strLstMail = "";
               //Validamos si el mail del cliente es valido
               if (mail.isEmail(strCT_EMAIL1)) {
                  strLstMail += "," + strCT_EMAIL1;
               }
               if (mail.isEmail(strCT_EMAIL2)) {
                  strLstMail += "," + strCT_EMAIL2;
               }
               if (mail.isEmail(strCorreo)) {
                  strLstMail += "," + strCorreo;
               }
               if (strLstMail.startsWith(",")) {
                  strLstMail = strLstMail.substring(1, strLstMail.length());
               }
               //Intentamos mandar el mail
               mail.setBolDepuracion(false);
               mail.getTemplate("MAIL_RETENCION", oConn);
               mail.getMensaje();
               mail.setReplaceContent(this.getDocument());

               //Sacamos datos de la empresa
               strSql = "SELECT * FROM vta_empresas "
                       + " where EMP_ID=" + this.document.getFieldInt("EMP_ID") + "";
               try {
                  ResultSet rs2 = oConn.runQuery(strSql);
                  mail.setReplaceContent(rs2);
                  rs2.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
                  ex.fillInStackTrace();
               }
               mail.setDestino(strLstMail);
               //Adjuntamos los archivos PDF 
               File fXml = new File(this.strPATHXml + "Retenciones_" + intRET_ID + ".xml");
               if (fXml.exists()) {
                  mail.setFichero(this.strPATHXml + "Retenciones_" + intRET_ID + ".xml");                  
               }
               String[] lstParamsName = {"folio_ini", "folio_fin", "emp_id", "sc_id"};
               String[] lstParamsValue = {this.document.getFieldString("RET_FOLIOINT"), this.document.getFieldString("RET_FOLIOINT"), this.document.getFieldInt("EMP_ID") + "", this.document.getFieldInt("SC_ID") + ""};
               String strNomFormato = "RETENCIONES";

               //Obtenemos los formatos correspondientes
               String strNomFile = this.doGeneraFormatoJasper(0, strNomFormato, "PDF", this.getDocument(), lstParamsName, lstParamsValue, this.strPATHXml);
               if (!strNomFile.isEmpty()) {
                  log.debug(strNomFile);
                  mail.setFichero(strNomFile);
               }
               log.debug("Enviamos el correo....");
               boolean bol = mail.sendMail();
               if (bol) {
                  //Se envio el recibo de Retenciones
               } else {
                  //No se envio...
               }

            }//Fin IF

         }//Fin while

      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }//Fin doEnvioMasivo

   public String doGeneraFolioImportacion() {

      int intUltimo = 0;

      intUltimo = doRegresaConsecutivoRetencion();

      String strUltimo = Integer.toString(intUltimo);

      int digitos = strUltimo.length();

      String folio = "";
      switch (digitos) {

         case 1:
            folio = "000000" + strUltimo;
            break;
         case 2:
            folio = "00000" + strUltimo;
            break;
         case 3:
            folio = "0000" + strUltimo;
            break;
         case 4:
            folio = "000" + strUltimo;
            break;
         case 5:
            folio = "00" + strUltimo;
            break;
         case 6:
            folio = "0" + strUltimo;
            break;

      }

      return folio;
   }

   public int doRegresaConsecutivoRetencion() {

      int intUltimo = 0;
      //Buscamos el consecutivo en la tabla de importacion
      String strSql = "select max(RET_CONSECUTIVO) as ultimo from rhh_ret_retenciones";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            intUltimo = rs.getInt("ultimo");

         }
         rs.close();
      } catch (SQLException ex) {
         //  java.util.logging.Logger.getLogger(Folios.class.getName()).log(Level.SEVERE, null, ex);
      }

      if (intUltimo == 0) {
         intUltimo = 1;
      } else {
         intUltimo++;
      }

      return intUltimo;
   }
}
