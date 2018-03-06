package Tablas;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Representa la tabla de movimientos de proveedor
 *
 * @author SIWEB
 */
public class vta_mov_prov extends TableMaster {
   
   public vta_mov_prov() {
      super("vta_mov_prov", "MP_ID", "", "");
      this.Fields.put("MP_ID", new Integer(0));
      this.Fields.put("MP_FECHA", "");
      this.Fields.put("MP_HORA", "");
      this.Fields.put("MP_FECHACREATE", "");
      this.Fields.put("PV_ID", new Integer(0));
      this.Fields.put("MP_CARGO", new Double(0));
      this.Fields.put("MP_ABONO", new Double(0));
      this.Fields.put("MP_FOLIO", "");
      this.Fields.put("MP_ANULADO", new Integer(0));
      this.Fields.put("MP_FECHAANUL", "");
      this.Fields.put("ID_USUARIOS", new Integer(0));
      this.Fields.put("ID_USUARIOSANUL", new Integer(0));
      this.Fields.put("MP_NOTAS", "");
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("MP_IMPUESTO1", new Double(0));
      this.Fields.put("MP_IMPUESTO2", new Double(0));
      this.Fields.put("MP_IMPUESTO3", new Double(0));
      this.Fields.put("MP_TASAIMPUESTO1", new Double(0));
      this.Fields.put("MP_TASAIMPUESTO2", new Double(0));
      this.Fields.put("MP_TASAIMPUESTO3", new Double(0));
      this.Fields.put("MP_MONEDA", new Integer(0));
      this.Fields.put("MP_TASAPESO", new Double(0));
      this.Fields.put("MP_ESPAGO", new Integer(0));
      this.Fields.put("CXP_ID", new Integer(0));
      this.Fields.put("MP_HORAANUL", "");
      this.Fields.put("CCJ_ID", new Integer(0));
      this.Fields.put("MP_ANTICIPO", new Integer(0));
      this.Fields.put("MPM_ID", new Integer(0));
      this.Fields.put("MP_FT", new Integer(0));
      this.Fields.put("EMP_ID", new Integer(0));
      this.Fields.put("MP_EXEC_INTER_CP", new Integer(0));
      this.Fields.put("MP_USA_ANTICIPO", new Integer(0));
      this.Fields.put("MP_ANTI_ID", new Integer(0));
      this.Fields.put("MP_ANTICIPO_ORIGINAL", new Double(0));
      this.Fields.put("MP_SALDO_ANTICIPO", new Double(0));
      this.Fields.put("NC_ID", new Integer(0));
      this.Fields.put("MP_ES_REEMBOLSO", new Integer(0));
      
   }
   
   /**
    *Regresa el objeto de cargo del anticipo
    * @param intIdUsoAnticipo Es el id del uso del anticipo
    * @param oConn Es la conexion
    * @return Regresa el objeto del cargo del uso del anticipo
    */
   public vta_mov_prov getMovCargoAnticipo(int intIdUsoAnticipo, Conexion oConn) {
      vta_mov_prov mov = null;
      int intIdMov = 0;
      String strSql = "select MP_ID from vta_mov_prov WHERE MP_USO_ANTI_ID = " + intIdUsoAnticipo;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            intIdMov = rs.getInt("MP_ID");
         }
         //if(rs.getStatement() != null )rs.getStatement().close();
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(vta_mov_prov.class.getName()).log(Level.SEVERE, null, ex);
      }
      if(intIdMov != 0){
         mov = new vta_mov_prov();
         mov.ObtenDatos(intIdMov, oConn);
      }
      return mov;
   }
}
