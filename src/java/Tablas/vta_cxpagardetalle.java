package Tablas;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase para la tabla de cuentas por pagar detalle
 *
 * @author SIWEB
 */
public class vta_cxpagardetalle extends TableMaster {

   public vta_cxpagardetalle() {
      super("vta_cxpagardetalle", "CXPD_ID", "", "");
      this.Fields.put("CXPD_ID", new Integer(0));
      this.Fields.put("CXP_ID", new Integer(0));
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("PR_ID", new Integer(0));
      this.Fields.put("CXPD_CVE", new Integer(0));
      this.Fields.put("CXPD_IMPORTE", new Double(0));
      this.Fields.put("CXPD_NOTAS", "");
      this.Fields.put("CXPD_EXEN_RET_IVA", new Integer(0));
      this.Fields.put("CXPD_PEDIADUANA", "");
      this.Fields.put("CXPD_PEDIFECHA", "");
      this.Fields.put("CXPD_PEDINUM", "");
      this.Fields.put("CXPD_DESCRIPCION", "");
      this.Fields.put("CXPD_CANTIDAD", new Integer(0));
      this.Fields.put("CXPD_TASAIVA1", new Double(0));
      this.Fields.put("CXPD_TASAIVA2", new Double(0));
      this.Fields.put("CXPD_TASAIVA3", new Double(0));
      this.Fields.put("CXPD_IMPUESTO1", new Double(0));
      this.Fields.put("CC_ID", new Integer(0));
      this.Fields.put("GT_ID", new Integer(0));
      this.Fields.put("CXPD_DESCUENTO", new Double(0));
      this.Fields.put("CXPD_DESGLOSA1", new Integer(0));
      this.Fields.put("CXPD_DESGLOSA2", new Integer(0));
      this.Fields.put("CXPD_DESGLOSA3", new Integer(0));
      this.Fields.put("CXPD_IMPUESTO2", new Double(0));
      this.Fields.put("CXPD_IMPUESTO3", new Double(0));
      this.Fields.put("CXPD_EXENTO1", new Integer(0));
      this.Fields.put("CXPD_EXENTO2", new Integer(0));
      this.Fields.put("CXPD_EXENTO3", new Integer(0));
      this.Fields.put("CXPD_COSTO", new Double(0));
      this.Fields.put("CXPD_COSTOREAL", new Double(0));
      this.Fields.put("CXPD_RET_ISR", new Integer(0));
      this.Fields.put("CXPD_RET_IVA", new Integer(0));
      this.Fields.put("MPD_ID", new Integer(0));
      this.Fields.put("MP_ID", new Integer(0));
      this.Fields.put("COMD_ID", new Integer(0));
      this.Fields.put("COM_ID", new Integer(0));
      this.Fields.put("CXPD_PRORRATEO", new Integer(0));
      this.Fields.put("CXPD_ADD_IVA", new Integer(0));
      this.Fields.put("CXPD_MINUS_IVA", new Integer(0));
      this.Fields.put("CXPD_CAN_DEV", new Integer(0));
      this.Fields.put("CXPD_SERIES_DEV", new Integer(0));
   }

   /**
    * Indica con true si se considera para el prorrateo
    *
    * @param oConn Es la conexion a las base de datos
    * @return Regresa el importe por prorratear
    */
   public boolean isProrrateable(Conexion oConn) {
      boolean bolIsProrra = false;
      boolean bolEncontro = false;
      //Consultamos todos los movimientos de entrada vinculados a esta cuenta por pagar
      String strSql = "select GT_EXENTO_PRORRATEO from vta_cgastos "
              + " where GT_ID = " + this.getFieldInt("GT_ID");
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            bolEncontro = true;
            if (rs.getInt("GT_EXENTO_PRORRATEO") == 0) {
               bolIsProrra = true;
            }
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
      }
      //Sino se encontro el centro de gastos si se prorratea
      if (!bolEncontro) {
         bolIsProrra = true;
      }
      return bolIsProrra;
   }
}
