package Tablas;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Representa los movimientos de cargo y abono de un cliente y de sus facturas
 *
 * @author zeus
 */
public class vta_mov_cte extends TableMaster {

   public vta_mov_cte() {
      super("vta_mov_cte", "MC_ID", "", "");
      this.Fields.put("MC_ID", new Integer(0));
      this.Fields.put("MC_FECHA", "");
      this.Fields.put("MC_HORA", "");
      this.Fields.put("MC_FECHACREATE", "");
      this.Fields.put("CT_ID", new Integer(0));
      this.Fields.put("MC_CARGO", new Double(0));
      this.Fields.put("MC_ABONO", new Double(0));
      this.Fields.put("MC_FOLIO", "");
      this.Fields.put("MC_ANULADO", new Integer(0));
      this.Fields.put("MC_FECHAANUL", "");
      this.Fields.put("MC_HORAANUL", "");
      this.Fields.put("ID_USUARIOS", new Integer(0));
      this.Fields.put("ID_USUARIOSANUL", new Integer(0));
      this.Fields.put("MC_NOTAS", "");
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("MC_IMPUESTO1", new Double(0));
      this.Fields.put("MC_IMPUESTO2", new Double(0));
      this.Fields.put("MC_IMPUESTO3", new Double(0));
      this.Fields.put("MC_TASAIMPUESTO1", new Double(0));
      this.Fields.put("MC_TASAIMPUESTO2", new Double(0));
      this.Fields.put("MC_TASAIMPUESTO3", new Double(0));
      this.Fields.put("MC_MONEDA", new Integer(0));
      this.Fields.put("MC_TASAPESO", new Double(0));
      this.Fields.put("MC_ESPAGO", new Integer(0));
      this.Fields.put("FAC_ID", new Integer(0));
      this.Fields.put("TKT_ID", new Integer(0));
      this.Fields.put("PD_ID", new Integer(0));
      this.Fields.put("MC_ANTICIPO", new Integer(0));
      this.Fields.put("MCM_ID", new Integer(0));
      this.Fields.put("MC_FT", new Integer(0));
      this.Fields.put("EMP_ID", new Integer(0));
      this.Fields.put("NC_ID", new Integer(0));
      this.Fields.put("MC_USA_ANTICIPO", new Integer(0));
      this.Fields.put("MC_ANTI_ID", new Integer(0));
      this.Fields.put("MC_EXEC_INTER_CP", new Integer(0));
      this.Fields.put("MC_ANTICIPO_ORIGINAL", new Double(0));
      this.Fields.put("MC_SALDO_ANTICIPO", new Double(0));
   }
   /**
    *Regresa el objeto de cargo del anticipo
    * @param intIdUsoAnticipo Es el id del uso del anticipo
    * @param oConn Es la conexion
    * @return Regresa el objeto del cargo del uso del anticipo
    */
   public vta_mov_cte getMovCargoAnticipo(int intIdUsoAnticipo, Conexion oConn) {
      vta_mov_cte mov = null;
      int intIdMov = 0;
      String strSql = "select MC_ID from vta_mov_cte WHERE MC_USO_ANTI_ID = " + intIdUsoAnticipo;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            intIdMov = rs.getInt("MC_ID");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(vta_mov_prov.class.getName()).log(Level.SEVERE, null, ex);
      }
      if(intIdMov != 0){
         mov = new vta_mov_cte();
         mov.ObtenDatos(intIdMov, oConn);
      }
      return mov;
   }
}
