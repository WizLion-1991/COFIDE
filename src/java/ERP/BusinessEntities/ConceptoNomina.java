/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP.BusinessEntities;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Representa un concepto de nomina
 *
 * @author ZeusGalindo
 */
public class ConceptoNomina {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private int intIdConcepto;
   private int intIdCategoria;
   private int intGravado;

   public int getIntIdConcepto() {
      return intIdConcepto;
   }

   public void setIntIdConcepto(int intIdConcepto) {
      this.intIdConcepto = intIdConcepto;
   }

   public int getIntIdCategoria() {
      return intIdCategoria;
   }

   public void setIntIdCategoria(int intIdCategoria) {
      this.intIdCategoria = intIdCategoria;
   }

   public int getIntGravado() {
      return intGravado;
   }

   public void setIntGravado(int intGravado) {
      this.intGravado = intGravado;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public ConceptoNomina() {
      this.intGravado = 1;
   }
   
   
   
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    * Recupera el id del concepto de la percepcion
    *
    * @param strCodigo Es el codigo
    * @param intEmpId Es el id de empresa
    * @param oConn Es la conexion
    */
   public void getPercepcion(String strCodigo, int intEmpId, Conexion oConn) {
      String strSql = "select PERC_ID,TP_ID from rhh_percepciones where PERC_CVE = '" + strCodigo + "' ";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            this.intIdConcepto = rs.getInt("PERC_ID");
            this.intIdCategoria = rs.getInt("TP_ID");
         }
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }

   }

   /**
    * Recupera el id del concepto de la deduccion
    *
    * @param strCodigo Es el codigo
    * @param intEmpId Es el id de empresa
    * @param oConn Es la conexion
    */
   public void getDeduccion(String strCodigo, int intEmpId, Conexion oConn) {
      String strSql = "select DEDU_ID,TD_ID from rhh_deducciones where DEDU_CVE = '" + strCodigo + "' ";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            this.intIdConcepto = rs.getInt("DEDU_ID");
            this.intIdCategoria = rs.getInt("TD_ID");
         }
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }
   }
   // </editor-fold>
}
